package real.health.GUI.MedicalHistoryTabs;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import real.health.SQL.*;
import java.awt.*;

public class createAllergiesTab {

    public JComponent createAllergiesTab(String id) {
        JTable allergiesTable = new JTable();
        // populate the table with the patient's current medications
        try {
            // Load the MySQL JDBC driver
            // Create a connection to the database
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();

            // Create a SQL statement to retrieve the patient's current medications
            String sql = "SELECT name, type, reaction, severity FROM allergies WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();

            // Create a table model and populate it with the retrieved data
            DefaultTableModel tableModel = new DefaultTableModel(
                    new Object[] { "Name", "Type", "Reaction", "Severity" },
                    0);
            while (result.next()) {
                tableModel.addRow(new Object[] { result.getString(1), result.getString(2), result.getString(3), result.getString(4) });
            }
            allergiesTable.setModel(tableModel);

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

        // Create the add button and add an ActionListener to upload the new medication
        // to the SQL server
        // Create the add button and add an ActionListener to upload the new medication
        // to the SQL server
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create the form for entering the new medication details
                JFrame addAllergiesFrame = new JFrame("Add Allergy");
                addAllergiesFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                addAllergiesFrame.setSize(400, 200);
                addAllergiesFrame.setLayout(new GridLayout(5, 2, 10, 10));
                addAllergiesFrame.setLocationRelativeTo(null);

                // Add form components for entering the medication details
                JLabel nameLabel = new JLabel("Name:");
                JTextField nameField = new JTextField();
                addAllergiesFrame.add(nameLabel);
                addAllergiesFrame.add(nameField);

                JLabel typeLabel = new JLabel("Type:");
                JTextField typeField = new JTextField();
                addAllergiesFrame.add(typeLabel);
                addAllergiesFrame.add(typeField);

                JLabel reactionLabel = new JLabel("Reaction:");
                JTextField reactionField = new JTextField();
                addAllergiesFrame.add(reactionLabel);
                addAllergiesFrame.add(reactionField);

                JLabel severityLabel = new JLabel("Severity:");
                JTextField severityField = new JTextField();
                addAllergiesFrame.add(severityLabel);
                addAllergiesFrame.add(severityField);

                // Add a button for submitting the form
                JButton submitButton = new JButton("Submit");
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Get the values from the form fields
                        String allergyName = nameField.getText();
                        String type = typeField.getText();
                        String reaction = reactionField.getText();
                        String severity = severityField.getText();

                        // Upload the new medication to the SQL server
                        try {
                            // Load the MySQL JDBC driver
                            HealthConn newConnection = new HealthConn();
                            Connection con = newConnection.connect();

                            // Create a SQL statement to insert the new medication
                            String sql = "INSERT INTO allergies (id, name, type, reaction, severity) VALUES (?, ?, ?, ?, ?)";
                            PreparedStatement statement = con.prepareStatement(sql);
                            statement.setString(1, id);
                            statement.setString(2, allergyName);
                            statement.setString(3, type);
                            statement.setString(4, reaction);
                            statement.setString(5, severity);
                            statement.executeUpdate();

                            // Refresh the medications table to show the newly added medication
                            DefaultTableModel tableModel = (DefaultTableModel) allergiesTable.getModel();
                            tableModel.addRow(new Object[] { allergyName, type, reaction, severity });

                            // Clean up resources
                            statement.close();
                            con.close();
                            // Close the add medication frame
                            addAllergiesFrame.dispose();
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
                        addAllergiesFrame.dispose();
                    }
                });
                addAllergiesFrame.add(cancelButton);
                addAllergiesFrame.add(submitButton);
                // Display the add medication frame
                addAllergiesFrame.setVisible(true);
            }
        });

        // Create a panel for the add button
        JPanel addButtonPanel = new JPanel();
        addButtonPanel.setLayout(new BorderLayout());
        addButtonPanel.add(addButton);

        // Create the medications tab panel and add the medications table and add button
        // panel
        JPanel allergyTabPanel = new JPanel(new BorderLayout());
        allergyTabPanel.add(new JScrollPane(allergiesTable), BorderLayout.CENTER);
        allergyTabPanel.add(addButtonPanel, BorderLayout.PAGE_END);

        return allergyTabPanel;
    }
}
