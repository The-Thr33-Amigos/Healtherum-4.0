package real.health.GUI.MedicalHistoryTabs;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import real.health.SQL.*;
import java.awt.*;

public class createMentalTab {
    public JComponent createMentalTab(String id) {
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
                    0);
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

        // Create the add button and add an ActionListener to upload the new mental health issue
        // to the SQL server
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
                JTextField issueField = new JTextField();
                addMentalFrame.add(issueLabel);
                addMentalFrame.add(issueField);

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
                        String issue = issueField.getText();
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
                        } catch (ClassNotFoundException ex) {
                            System.out.println("Error: unable to load MySQL JDBC driver");
                            ex.printStackTrace();
                        } catch (SQLException ex) {
                            System.out.println("Error: unable to connect to MySQL database");
                            ex.printStackTrace();
                        }

                        // Close the add mental health issue frame
                        addMentalFrame.dispose();
                    }
                });
                addMentalFrame.add(submitButton);

                // Display the add mental health issue frame
                addMentalFrame.setVisible(true);
            }
        });

        // Create a panel for the add button
        JPanel addButtonPanel = new JPanel();
        addButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        addButtonPanel.add(addButton);

        // Create the mental health tab panel and add the mental health table and add button
        // panel
        JPanel mentalTabPanel = new JPanel(new BorderLayout());
        mentalTabPanel.add(new JScrollPane(mentalTable), BorderLayout.CENTER);
        mentalTabPanel.add(addButtonPanel, BorderLayout.SOUTH);

        return mentalTabPanel;
    }

}
