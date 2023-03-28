package real.health.GUI.MedicalHistoryTabs;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import real.health.SQL.*;
import java.awt.*;

public class createMilestonesTab {
    public JComponent createMilestonesTab(String id) {
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
                    0);
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

        // Create the milestones tab panel and add the milestones table and add button
        // panel
        JPanel milestonesTabPanel = new JPanel();
        milestonesTabPanel.setLayout(new BorderLayout());
        milestonesTabPanel.add(new JScrollPane(milestonesTable), BorderLayout.CENTER);

        // Create a panel for the add button
        JPanel addButtonPanel = new JPanel();
        addButtonPanel.setLayout(new BorderLayout());
        addButtonPanel.add(addButton);

        milestonesTabPanel.add(addButtonPanel, BorderLayout.PAGE_END);

        return milestonesTabPanel;
    }
}