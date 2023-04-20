package real.health.GUI.MedicalHistoryTabs;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import real.health.SQL.*;
import java.awt.*;
import real.health.GUI.UserRole;

public class createMentalTab {
    private UserRole userRole;
    public JComponent createMentalTab(String id, UserRole userRole) {
        this.userRole = userRole;
        JTable mentalTable = new JTable();
        // populate the table with the patient's mental health history
        try {
            // Load the MySQL JDBC driver
            // Create a connection to the database
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();

            // Create a SQL statement to retrieve the patient's mental health history
            String sql = "SELECT issue, status, dateDiagnosed FROM mental_health WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();

            // Create a table model and populate it with the retrieved data
            DefaultTableModel tableModel = new DefaultTableModel(
                    new Object[] { "Issue", "Status", "Date Diagnosed" },
                    0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            while (result.next()) {
                tableModel.addRow(new Object[] { result.getString(1), result.getString(2), result.getString(3) });
            }
            mentalTable.setModel(tableModel);

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

        // Create the add button and add an ActionListener to upload the new mental
        // health issue to the SQL server
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create the form for entering the new mental health issue details
                JFrame addMentalFrame = new JFrame("Add Mental Health Issue");
                addMentalFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                addMentalFrame.setSize(400, 200);
                addMentalFrame.setLayout(new GridLayout(4, 2, 10, 10));
                addMentalFrame.setLocationRelativeTo(null);

                // Add form components for entering the mental health issue details
                JLabel issueLabel = new JLabel("Issue:");
                String[] issueList = {"Autism Spectrum Disorder", "ADHD",  "Scizophrenia", "Schizoaffective Disorder", "Bipolar Disorder I or II", "Major Depressive Disorder", "Anxiety Disorders", "Obsessive Compulsive Disorder", "PTSD", "Disociative Disorders"};
                // JTextField issueField = new JTextField();
                JComboBox<String> issueCombo = new JComboBox<>(issueList);
                addMentalFrame.add(issueLabel);
                addMentalFrame.add(issueCombo);

                JLabel statusLabel = new JLabel("Status:");
                JTextField statusField = new JTextField();
                addMentalFrame.add(statusLabel);
                addMentalFrame.add(statusField);

                JLabel dateDiagnosedLabel = new JLabel("Date Diagnosed:");
                JTextField dateDiagnosedField = new JTextField();
                addMentalFrame.add(dateDiagnosedLabel);
                addMentalFrame.add(dateDiagnosedField);

                // Add a button for submitting the form
                JButton submitButton = new JButton("Submit");
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Get the values from the form fields
                        String issue = (String) issueCombo.getSelectedItem();
                        String status = statusField.getText();
                        String dateDiagnosed = dateDiagnosedField.getText();

                        // Upload the new mental health issue to the SQL server
                        try {
                            // Load the MySQL JDBC driver
                            HealthConn newConnection = new HealthConn();
                            Connection con = newConnection.connect();

                            // Create a SQL statement to insert the new mental health issue
                            String sql = "INSERT INTO mental_health (id, issue, status, dateDiagnosed) VALUES (?, ?, ?, ?)";
                            PreparedStatement statement = con.prepareStatement(sql);
                            statement.setString(1, id);
                            statement.setString(2, issue);
                            statement.setString(3, status);
                            statement.setString(4, dateDiagnosed);
                            statement.executeUpdate();

                            // Refresh the mental health table to show the newly added mental health issue
                            DefaultTableModel tableModel = (DefaultTableModel) mentalTable.getModel();
                            tableModel.addRow(new Object[] { issue, status, dateDiagnosed });

                            // Clean up resources
                            statement.close();
                            con.close();
                            // Close the add mental health issue frame
                            addMentalFrame.dispose();
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
                        addMentalFrame.dispose();
                    }
                });
                addMentalFrame.add(cancelButton);
                addMentalFrame.add(submitButton);

                // Display the add mental health issue frame
                addMentalFrame.setVisible(true);
            }
        });

        // Create the delete button and add an ActionListener
        // to delete the selected mental illness
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row in the table
                int selectedRow = mentalTable.getSelectedRow();

                // If no row is selected, display an error message
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(mentalTable, "Please select a row to delete.");
                    return;
                }
                // Display a confirmation dialog
                int confirmation = JOptionPane.showConfirmDialog(mentalTable, "Are you sure you want to delete the selected row?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                // If the user confirms the deletion, proceed
                if (confirmation == JOptionPane.YES_OPTION) {

                    // Get the primary key or unique identifier of the record from the selected row
                    // Assuming the first column of the table contains the primary key
                    String primaryKey = mentalTable.getValueAt(selectedRow, 0).toString();

                    // Execute an SQL DELETE statement to delete
                    // the corresponding record from the database
                    try {
                        HealthConn newConnection = new HealthConn();
                        Connection con = newConnection.connect();
                        String sql = "DELETE FROM mental_health WHERE id = ? AND issue = ? AND status = ? AND dateDiagnosed = ?";
                        PreparedStatement statement = con.prepareStatement(sql);
                        statement.setString(1, id);
                        statement.setString(2, mentalTable.getValueAt(selectedRow, 0).toString());
                        statement.setString(3, mentalTable.getValueAt(selectedRow, 1).toString());
                        statement.setString(4, mentalTable.getValueAt(selectedRow, 2).toString());

                        statement.executeUpdate();

                        // Close the statement and connection
                        statement.close();
                        con.close();

                        // Remove the selected row from the table
                        DefaultTableModel model = (DefaultTableModel) mentalTable.getModel();
                        model.removeRow(selectedRow);

                    } catch (ClassNotFoundException | SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(mentalTable, "An error occurred while deleting the record.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Create the mental health tab panel and add the mental health table and add
        // button panel
        JPanel mentalTabPanel = new JPanel(new BorderLayout());
        mentalTabPanel.add(new JScrollPane(mentalTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));        
        JPanel addDeletePanel = new JPanel(new GridLayout(1, 2, 10, 10));

        if (userRole == UserRole.PROVIDER) {
            addDeletePanel.add(addButton);
            addDeletePanel.add(deleteButton);
            buttonPanel.add(addDeletePanel);
            mentalTabPanel.add(buttonPanel, BorderLayout.PAGE_END);
        }

        return mentalTabPanel;
    }

}
