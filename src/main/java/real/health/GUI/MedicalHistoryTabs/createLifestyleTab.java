package real.health.GUI.MedicalHistoryTabs;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import real.health.SQL.*;
import java.awt.*;

public class createLifestyleTab {
    public JComponent createLifestyleTab(String id) {
        // Create the lifestyle factors table
        JTable lifestyleTable = new JTable();
        try {
            // Load the MySQL JDBC driver
            // Create a connection to the database
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();

            // Create a SQL statement to retrieve the patient's lifestyle factors
            String sql = "SELECT factor, status FROM lifestyle WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();

            // Create a table model and populate it with the retrieved data
            DefaultTableModel tableModel = new DefaultTableModel(
                    new Object[] { "Factor", "Status" },
                    0);
            while (result.next()) {
                tableModel.addRow(new Object[] { result.getString(1), result.getString(2) });
            }
            lifestyleTable.setModel(tableModel);

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

        // Create the add button and add an ActionListener to upload the new lifestyle
        // factor to the SQL server
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create the form for entering the new lifestyle factor details
                JFrame addFactorFrame = new JFrame("Add Lifestyle Factor");
                addFactorFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                addFactorFrame.setSize(400, 200);
                addFactorFrame.setLayout(new GridLayout(3, 2, 10, 10));
                addFactorFrame.setLocationRelativeTo(null);
                // Add form components for entering the lifestyle factor details
                JLabel factorLabel = new JLabel("Factor:");
                JTextField factorField = new JTextField();
                addFactorFrame.add(factorLabel);
                addFactorFrame.add(factorField);

                JLabel statusLabel = new JLabel("Status:");
                JTextField statusField = new JTextField();
                addFactorFrame.add(statusLabel);
                addFactorFrame.add(statusField);

                // Add a button for submitting the form
                JButton submitButton = new JButton("Submit");
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Get the values from the form fields
                        String factor = factorField.getText();
                        String status = statusField.getText();

                        // Upload the new lifestyle factor to the SQL server
                        try {
                            // Load the MySQL JDBC driver
                            HealthConn newConnection = new HealthConn();
                            Connection con = newConnection.connect();

                            // Create a SQL statement to insert the new lifestyle factor
                            String sql = "INSERT INTO lifestyle (id, factor, status) VALUES (?, ?, ?)";
                            PreparedStatement statement = con.prepareStatement(sql);
                            statement.setString(1, id);
                            statement.setString(2, factor);
                            statement.setString(3, status);
                            statement.executeUpdate();

                            // Refresh the lifestyle factors table to show the newly added factor
                            DefaultTableModel tableModel = (DefaultTableModel) lifestyleTable.getModel();
                            tableModel.addRow(new Object[] { factor, status });

                            // Clean up resources
                            statement.close();
                            con.close();
                            // Close the add lifestyle factor frame
                            addFactorFrame.dispose();
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
                        addFactorFrame.dispose();
                    }
                });
                addFactorFrame.add(cancelButton);
                addFactorFrame.add(submitButton);

                // Display the add lifestyle factor frame
                addFactorFrame.setVisible(true);
            }
        });

        // Create a panel for the add button
        JPanel addButtonPanel = new JPanel();
        addButtonPanel.setLayout(new BorderLayout());
        addButtonPanel.add(addButton);

        // Create the lifestyle factors tab panel and add the lifestyle factors table
        // and add button
        // panel
        JPanel lifestyleTabPanel = new JPanel(new BorderLayout());
        lifestyleTabPanel.add(new JScrollPane(lifestyleTable), BorderLayout.CENTER);
        lifestyleTabPanel.add(addButtonPanel, BorderLayout.PAGE_END);

        return lifestyleTabPanel;
    }

}
