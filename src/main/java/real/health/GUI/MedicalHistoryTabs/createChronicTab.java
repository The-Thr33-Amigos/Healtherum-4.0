package real.health.GUI.MedicalHistoryTabs;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;

import real.health.GUI.UserRole;
import real.health.SQL.*;
import java.awt.*;

public class createChronicTab {
    private UserRole userRole;

    public JComponent createChronicTab(String id, UserRole userRole) {
        this.userRole = userRole;
        JTable chronicTable = new JTable();
        // populate the table with the patient's chronic condition history
        try {
            // Load the MySQL JDBC driver
            // Create a connection to the database
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();

            // Create a SQL statement to retrieve the patient's chronic condition history
            String sql = "SELECT chronic_condition, treatment, diagnosisDate FROM chronic_conditions WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();

            // Create a table model and populate it with the retrieved data
            DefaultTableModel tableModel = new DefaultTableModel(
                    new Object[] { "Condition", "Treatment", "Diagnosis Date" },
                    0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            while (result.next()) {
                tableModel.addRow(new Object[] { result.getString(1), result.getString(2), result.getString(3) });
            }
            chronicTable.setModel(tableModel);

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

        // Create the add button and add an ActionListener to upload the new chronic
        // condition
        // to the SQL server
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create the form for entering the new chronic condition details
                JFrame addChronicFrame = new JFrame("Add Chronic Condition");
                addChronicFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                addChronicFrame.setSize(400, 200);
                addChronicFrame.setLayout(new GridLayout(4, 2, 10, 10));
                addChronicFrame.setLocationRelativeTo(null);

                // Add form components for entering the chronic condition details
                JLabel conditionLabel = new JLabel("Condition:");
                JTextField conditionField = new JTextField();
                addChronicFrame.add(conditionLabel);
                addChronicFrame.add(conditionField);

                JLabel diagnosisDateLabel = new JLabel("Diagnosis Date:");
                JTextField diagnosisDateField = new JTextField();
                addChronicFrame.add(diagnosisDateLabel);
                addChronicFrame.add(diagnosisDateField);

                JLabel treatmentLabel = new JLabel("Treatment:");
                JTextField treatmentField = new JTextField();
                addChronicFrame.add(treatmentLabel);
                addChronicFrame.add(treatmentField);

                // Add a button for submitting the form
                JButton submitButton = new JButton("Submit");
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Get the values from the form fields
                        String condition = conditionField.getText();
                        String diagnosisDate = diagnosisDateField.getText();
                        String treatment = treatmentField.getText();

                        // Upload the new chronic condition record to the SQL server
                        try {
                            // Load the MySQL JDBC driver
                            HealthConn newConnection = new HealthConn();
                            Connection con = newConnection.connect();

                            // Create a SQL statement to insert the new chronic condition record into the
                            // database
                            String sql = "INSERT INTO chronic_conditions (id, chronic_condition, diagnosisDate, treatment) VALUES (?, ?, ?, ?)";
                            PreparedStatement statement = con.prepareStatement(sql);
                            statement.setString(1, id);
                            statement.setString(2, condition);
                            statement.setString(3, diagnosisDate);
                            statement.setString(4, treatment);
                            statement.executeUpdate();

                            // Refresh the chronic condition table to show the newly added record
                            DefaultTableModel tableModel = (DefaultTableModel) chronicTable.getModel();
                            tableModel.addRow(new Object[] { condition, diagnosisDate, treatment });

                            // Clean up resources
                            statement.close();
                            con.close();
                            addChronicFrame.dispose();
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
                        addChronicFrame.dispose();
                    }
                });
                addChronicFrame.add(cancelButton);
                addChronicFrame.add(submitButton);
                // Display the form for entering the new chronic condition details
                addChronicFrame.setVisible(true);
            }
        });

        // Create the delete button and add an ActionListener
        // to delete the selected chronic condition
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row in the table
                int selectedRow = chronicTable.getSelectedRow();

                // If no row is selected, display an error message
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(chronicTable, "Please select a row to delete.");
                    return;
                }
                // Display a confirmation dialog
                int confirmation = JOptionPane.showConfirmDialog(chronicTable,
                        "Are you sure you want to delete the selected row?", "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION);

                // If the user confirms the deletion, proceed
                if (confirmation == JOptionPane.YES_OPTION) {

                    // Get the primary key or unique identifier of the record from the selected row
                    // Assuming the first column of the table contains the primary key
                    String primaryKey = chronicTable.getValueAt(selectedRow, 0).toString();

                    // Execute an SQL DELETE statement to delete
                    // the corresponding record from the database
                    try {
                        HealthConn newConnection = new HealthConn();
                        Connection con = newConnection.connect();
                        String sql = "DELETE FROM chronic_conditions WHERE id = ? AND chronic_condition = ? AND diagnosisDate = ? AND treatment = ?";
                        PreparedStatement statement = con.prepareStatement(sql);
                        statement.setString(1, id);
                        statement.setString(2, chronicTable.getValueAt(selectedRow, 0).toString());
                        statement.setString(3, chronicTable.getValueAt(selectedRow, 1).toString());
                        statement.setString(4, chronicTable.getValueAt(selectedRow, 2).toString());
                        statement.executeUpdate();

                        // Close the statement and connection
                        statement.close();
                        con.close();

                        // Remove the selected row from the table
                        DefaultTableModel model = (DefaultTableModel) chronicTable.getModel();
                        model.removeRow(selectedRow);

                    } catch (ClassNotFoundException | SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(chronicTable, "An error occurred while deleting the record.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Create the scroll pane and add the chronic condition table and buttons to it
        JScrollPane scrollPane = new JScrollPane(chronicTable);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JPanel addDeletePanel = new JPanel(new GridLayout(1, 2, 10, 10));

        if (userRole == UserRole.PROVIDER) {
            addDeletePanel.add(addButton);
            addDeletePanel.add(deleteButton);
            buttonPanel.add(addDeletePanel);
            panel.add(buttonPanel, BorderLayout.PAGE_END);
        }

        return panel;
    }

}
