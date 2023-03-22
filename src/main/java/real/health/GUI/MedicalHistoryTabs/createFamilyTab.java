package real.health.GUI.MedicalHistoryTabs;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import real.health.SQL.*;
import java.awt.*;

public class createFamilyTab {
    public JComponent createFamilyTab(String id) {
        // Create a JTable to display the patient's family history
        JTable familyTable = new JTable();

        // Populate the table with the patient's family history
        try {
            // Load the MySQL JDBC driver
            // Create a connection to the database
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();

            // Create a SQL statement to retrieve the patient's family history
            String sql = "SELECT relationship, health_condition FROM family_history WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();

            // Create a table model and populate it with the retrieved data
            DefaultTableModel tableModel = new DefaultTableModel(new Object[] { "Relationship", "Condition" }, 0);
            while (result.next()) {
                tableModel.addRow(new Object[] { result.getString(1), result.getString(2) });
            }
            familyTable.setModel(tableModel);

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

        // Create the add button and add an ActionListener to upload the new family
        // member
        // to the SQL server
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create the form for entering the new family member's details
                JFrame addFamilyMemberFrame = new JFrame("Add Family Member");
                addFamilyMemberFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                addFamilyMemberFrame.setSize(400, 200);
                addFamilyMemberFrame.setLayout(new GridLayout(3, 2, 10, 10));
                addFamilyMemberFrame.setLocationRelativeTo(null);

                // Add form components for entering the family member's details
                JLabel relationshipLabel = new JLabel("Relationship:");
                JTextField relationshipField = new JTextField();
                addFamilyMemberFrame.add(relationshipLabel);
                addFamilyMemberFrame.add(relationshipField);

                JLabel conditionLabel = new JLabel("Condition:");
                JTextField conditionField = new JTextField();
                addFamilyMemberFrame.add(conditionLabel);
                addFamilyMemberFrame.add(conditionField);

                // Add a button for submitting the form
                JButton submitButton = new JButton("Submit");
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Get the values from the form fields
                        String relationship = relationshipField.getText();
                        String condition = conditionField.getText();

                        // Upload the new family member to the SQL server
                        try {
                            // Load the MySQL JDBC driver
                            HealthConn newConnection = new HealthConn();
                            Connection con = newConnection.connect();

                            // Create a SQL statement to insert the new family member into the database
                            String sql = "INSERT INTO family_history (id, relationship, health_condition) VALUES (?, ?, ?)";
                            
                            PreparedStatement statement = con.prepareStatement(sql);
                            statement.setString(1, id);
                            statement.setString(2, relationship);
                            statement.setString(3, condition);
                            statement.executeUpdate();

                            // Refresh the family history table to show the newly added family member
                            DefaultTableModel tableModel = (DefaultTableModel) familyTable.getModel();
                            tableModel.addRow(new Object[] { relationship, condition });

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

                        // Close the add family member form
                        addFamilyMemberFrame.dispose();
                    }
                });
                addFamilyMemberFrame.add(submitButton);

                // Display the add family member form
                addFamilyMemberFrame.setVisible(true);
            }
        });

        // Create a JPanel to hold the family history table and add button
        JPanel familyPanel = new JPanel();
        familyPanel.setLayout(new BorderLayout());
        familyPanel.add(new JScrollPane(familyTable), BorderLayout.CENTER);
        familyPanel.add(addButton, BorderLayout.PAGE_END);

        return familyPanel;

    }
}
