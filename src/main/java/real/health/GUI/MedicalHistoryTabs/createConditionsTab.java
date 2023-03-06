package real.health.GUI.MedicalHistoryTabs;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import real.health.SQL.*;
import java.awt.*;

public class createConditionsTab {
    public JComponent createConditionsTab(String id) {
        JTable conditionsTable = new JTable();
        // populate the table with the patient's current medical conditions
        try {
            // Load the MySQL JDBC driver
            // Create a connection to the database
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();

            // Create a SQL statement to retrieve the patient's current medical conditions
            String sql = "SELECT condition, status FROM conditions WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();

            // Create a table model and populate it with the retrieved data
            DefaultTableModel tableModel = new DefaultTableModel(
                    new Object[] { "Condition", "Status" },
                    0);
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

                            // Create a SQL statement to insert the new medical condition
                            String sql = "INSERT INTO conditions (id, condition, status) VALUES (?, ?, ?)";
                            PreparedStatement statement = con.prepareStatement(sql);
                            statement.setString(1, id);
                            statement.setString(2, condition);
                            statement.setString(3, status);
                            statement.executeUpdate();

                            // Refresh the medical conditions table to show the newly added medical
                            // condition
                            DefaultTableModel tableModel = (DefaultTableModel) conditionsTable.getModel();
                            tableModel.addRow(new Object[] { condition, status });
                            // Clean up resources
                            statement.close();
                            con.close();
                        } catch (ClassNotFoundException ex) {
                            System.out.println("Error: unable to load MySQL JDBC driver");
                            ex.printStackTrace();
                        } catch (SQLException ex) {
                            System.out.println("Error: unable to connect to MySQL database");
                            ex.printStackTrace();
                        }

                        // Close the add medical condition frame
                        addConditionFrame.dispose();
                    }
                });
                addConditionFrame.add(submitButton);

                // Display the add medical condition frame
                addConditionFrame.setVisible(true);
            }
        });

        // Create a panel for the add button
        JPanel addButtonPanel = new JPanel();
        addButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        addButtonPanel.add(addButton);

        // Create the medical conditions tab panel and add the medical conditions table
        // and add button
        // panel
        JPanel conditionsTabPanel = new JPanel(new BorderLayout());
        conditionsTabPanel.add(new JScrollPane(conditionsTable), BorderLayout.CENTER);
        conditionsTabPanel.add(addButtonPanel, BorderLayout.SOUTH);

        return conditionsTabPanel;
    }
}
