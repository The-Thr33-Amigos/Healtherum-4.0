package real.health.GUI.MedicalHistoryTabs;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import real.health.SQL.*;
import java.awt.*;
import real.health.GUI.UserRole;

public class createMilestonesTab {
    private UserRole userRole;

    public JComponent createMilestonesTab(String id, UserRole userRole) {
        this.userRole = userRole;
        JTable milestonesTable = new JTable();
        // populate the table with the patient's developmental milestones
        try {
            // Load the MySQL JDBC driver
            // Create a connection to the database
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();

            // Create a SQL statement to retrieve the patient's developmental milestones
            String sql = "SELECT milestone, dateAchieved FROM milestones WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();

            // Create a table model and populate it with the retrieved data
            DefaultTableModel tableModel = new DefaultTableModel(
                    new Object[] { "Milestone", "Date Achieved" },
                    0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            while (result.next()) {
                tableModel.addRow(new Object[] { result.getString(1), result.getString(2) });
            }
            milestonesTable.setModel(tableModel);

            // Clean up resources
            result.close();
            statement.close();
            con.close();

        } catch (ClassNotFoundException ex) {
            System.out.println("Error: unable to load MySQL JDBC driver");
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.out.println("Error: unable to connect to MySQL database");
            ex.printStackTrace();
        }

        // Create the add button and add an ActionListener to upload the new milestone
        // to the SQL server
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create the form for entering the new milestone details
                JFrame addMilestoneFrame = new JFrame("Add Developmental Milestone");
                addMilestoneFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                addMilestoneFrame.setSize(400, 150);
                addMilestoneFrame.setLayout(new GridLayout(3, 2, 10, 10));
                addMilestoneFrame.setLocationRelativeTo(null);

                // Add form components for entering the milestone details
                JLabel milestoneLabel = new JLabel("Milestone:");
                JTextField milestoneField = new JTextField();
                addMilestoneFrame.add(milestoneLabel);
                addMilestoneFrame.add(milestoneField);

                JLabel dateAchievedLabel = new JLabel("Date Achieved:");
                JTextField dateAchievedField = new JTextField();
                addMilestoneFrame.add(dateAchievedLabel);
                addMilestoneFrame.add(dateAchievedField);

                // Add a button for submitting the form
                JButton submitButton = new JButton("Submit");
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Get the values from the form fields
                        String milestone = milestoneField.getText();
                        String dateAchieved = dateAchievedField.getText();

                        // Upload the new milestone to the SQL server
                        try {
                            // Load the MySQL JDBC driver
                            HealthConn newConnection = new HealthConn();
                            Connection con = newConnection.connect();

                            // Create a SQL statement to insert the new milestone
                            String sql = "INSERT INTO milestones (id, milestone, dateAchieved) VALUES (?, ?, ?)";
                            PreparedStatement statement = con.prepareStatement(sql);
                            statement.setString(1, id);
                            statement.setString(2, milestone);
                            statement.setString(3, dateAchieved);
                            statement.executeUpdate();

                            // Refresh the milestones table to show the newly added milestone
                            DefaultTableModel tableModel = (DefaultTableModel) milestonesTable.getModel();
                            tableModel.addRow(new Object[] { milestone, dateAchieved });

                            // Clean up resources
                            statement.close();
                            con.close();
                            // Close the add milestone frame
                            addMilestoneFrame.dispose();
                        } catch (ClassNotFoundException ex) {
                            System.out.println("Error: unable to load MySQL JDBC driver");
                            ex.printStackTrace();
                        } catch (SQLException ex) {
                            System.out.println("Error: unable to connect to MySQL database");
                            ex.printStackTrace();
                        }
                    }
                });
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addMilestoneFrame.dispose();
                    }
                });
                addMilestoneFrame.add(cancelButton);
                addMilestoneFrame.add(submitButton);
                // Display the add milestone frame
                addMilestoneFrame.setVisible(true);
            }
        });

        // Create the delete button and add an ActionListener
        // to delete the selected milestones
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row in the table
                int selectedRow = milestonesTable.getSelectedRow();

                // If no row is selected, display an error message
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(milestonesTable, "Please select a row to delete.");
                    return;
                }

                // Display a confirmation dialog
                int confirmation = JOptionPane.showConfirmDialog(milestonesTable,
                        "Are you sure you want to delete the selected row?", "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION);

                // If the user confirms the deletion, proceed
                if (confirmation == JOptionPane.YES_OPTION) {
                    // Get the primary key or unique identifier of the record from the selected row
                    // Assuming the first column of the table contains the primary key
                    String primaryKey = milestonesTable.getValueAt(selectedRow, 0).toString();

                    // Execute an SQL DELETE statement to delete
                    // the corresponding record from the database
                    try {
                        HealthConn newConnection = new HealthConn();
                        Connection con = newConnection.connect();
                        String sql = "DELETE FROM milestones WHERE id = ? AND milestone = ? AND dateAchieved = ?";
                        PreparedStatement statement = con.prepareStatement(sql);
                        statement.setString(1, id);
                        statement.setString(2, milestonesTable.getValueAt(selectedRow, 0).toString());
                        statement.setString(3, milestonesTable.getValueAt(selectedRow, 1).toString());
                        statement.executeUpdate();

                        // Close the statement and connection
                        statement.close();
                        con.close();

                        // Remove the selected row from the table
                        DefaultTableModel model = (DefaultTableModel) milestonesTable.getModel();
                        model.removeRow(selectedRow);

                    } catch (ClassNotFoundException | SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(milestonesTable, "An error occurred while deleting the record.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Create the milestones tab panel and add
        // the milestones table and add button panel
        JPanel milestonesTabPanel = new JPanel();
        milestonesTabPanel.setLayout(new BorderLayout());
        milestonesTabPanel.add(new JScrollPane(milestonesTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JPanel addDeletePanel = new JPanel(new GridLayout(1, 2, 10, 10));

        if (userRole == UserRole.PROVIDER) {
            addDeletePanel.add(addButton);
            addDeletePanel.add(deleteButton);
            buttonPanel.add(addDeletePanel);
            milestonesTabPanel.add(buttonPanel, BorderLayout.PAGE_END);
        }

        return milestonesTabPanel;
    }
}