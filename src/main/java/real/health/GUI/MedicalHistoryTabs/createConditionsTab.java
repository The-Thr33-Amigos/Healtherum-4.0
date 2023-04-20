package real.health.GUI.MedicalHistoryTabs;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;

import real.health.GUI.UserRole;
import real.health.SQL.*;
import java.awt.*;

public class createConditionsTab {
    private UserRole userRole;
    public JComponent createConditionsTab(String id, UserRole userRole) {
        this.userRole = userRole;
        JTable conditionsTable = new JTable();
        // populate the table with the patient's current medical conditions
        try {
            // Load the MySQL JDBC driver
            // Create a connection to the database
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();

            // Create a SQL statement to retrieve the patient's current medical conditions
            String sql = "SELECT medical_condition, status FROM conditions WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();

            // Create a table model and populate it with the retrieved data
            DefaultTableModel tableModel = new DefaultTableModel(
                    new Object[] { "Condition", "Status" },
                    0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            while (result.next()) {
                tableModel.addRow(new Object[] { result.getString(1), result.getString(2) });
            }
            conditionsTable.setModel(tableModel);

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

        // Create the add button and add an ActionListener to upload the new medical
        // condition
        // to the SQL server
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create the form for entering the new medical condition details
                JFrame addConditionFrame = new JFrame("Add Medical Condition");
                addConditionFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                addConditionFrame.setSize(400, 150);
                addConditionFrame.setLayout(new GridLayout(3, 2, 10, 10));
                addConditionFrame.setLocationRelativeTo(null);

                // Add form components for entering the medical condition details
                JLabel conditionLabel = new JLabel("Condition:");
                JTextField conditionField = new JTextField();
                addConditionFrame.add(conditionLabel);
                addConditionFrame.add(conditionField);

                JLabel statusLabel = new JLabel("Status:");
                JTextField statusField = new JTextField();
                addConditionFrame.add(statusLabel);
                addConditionFrame.add(statusField);

                // Add a button for submitting the form
                JButton submitButton = new JButton("Submit");
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Get the values from the form fields
                        String condition = conditionField.getText();
                        String status = statusField.getText();

                        // Upload the new medical condition to the SQL server
                        try {
                            // Load the MySQL JDBC driver
                            HealthConn newConnection = new HealthConn();
                            Connection con = newConnection.connect();

                            // Create a SQL statement to insert the new medication
                            String sql = "INSERT INTO conditions (id, medical_condition, status) VALUES (?, ?, ?)";
                            PreparedStatement statement = con.prepareStatement(sql);
                            statement.setString(1, id);
                            statement.setString(2, condition);
                            statement.setString(3, status);
                            statement.executeUpdate();

                            // Refresh the medications table to show the newly added medication
                            DefaultTableModel tableModel = (DefaultTableModel) conditionsTable.getModel();
                            tableModel.addRow(new Object[] { condition, status });

                            // Clean up resources
                            statement.close();
                            con.close();
                            // Close the add medical condition frame
                            addConditionFrame.dispose();
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
                        addConditionFrame.dispose();
                    }
                });
                addConditionFrame.add(cancelButton);
                addConditionFrame.add(submitButton);

                // Display the add medical condition frame
                addConditionFrame.setVisible(true);
            }
        });

        // Create the delete button and add an ActionListener
        // to delete the selected condition
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row in the table
                int selectedRow = conditionsTable.getSelectedRow();

                // If no row is selected, display an error message
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(conditionsTable, "Please select a row to delete.");
                    return;
                }

                // Display a confirmation dialog
                int confirmation = JOptionPane.showConfirmDialog(conditionsTable, "Are you sure you want to delete the selected row?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                // If the user confirms the deletion, proceed
                if (confirmation == JOptionPane.YES_OPTION) {
                    
                    // Get the primary key or unique identifier of the record from the selected row
                    // Assuming the first column of the table contains the primary key
                    String primaryKey = conditionsTable.getValueAt(selectedRow, 0).toString();

                    // Execute an SQL DELETE statement to delete
                    // the corresponding record from the database
                    try {
                        HealthConn newConnection = new HealthConn();
                        Connection con = newConnection.connect();
                        String sql = "DELETE FROM conditions WHERE id = ? AND medical_condition = ? AND status = ?";
                        PreparedStatement statement = con.prepareStatement(sql);
                        statement.setString(1, id); 
                        statement.setString(2, conditionsTable.getValueAt(selectedRow, 0).toString());
                        statement.setString(3, conditionsTable.getValueAt(selectedRow, 1).toString());
                        statement.executeUpdate();

                        // Close the statement and connection
                        statement.close();
                        con.close();

                        // Remove the selected row from the table
                        DefaultTableModel model = (DefaultTableModel) conditionsTable.getModel();
                        model.removeRow(selectedRow);

                    } catch (ClassNotFoundException | SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(conditionsTable, "An error occurred while deleting the record.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Create the medical conditions tab panel and add the medical conditions table
        // and add button panel
        JPanel conditionsTabPanel = new JPanel(new BorderLayout());
        conditionsTabPanel.add(new JScrollPane(conditionsTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));        
        // Create a panel for the add button
        JPanel addDeletePanel = new JPanel(new GridLayout(1, 2, 10, 10));

        if (userRole == UserRole.PROVIDER) {
            addDeletePanel.add(addButton);
            addDeletePanel.add(deleteButton);
            buttonPanel.add(addDeletePanel);
            conditionsTabPanel.add(buttonPanel, BorderLayout.PAGE_END);
        }

        return conditionsTabPanel;
    }
}
