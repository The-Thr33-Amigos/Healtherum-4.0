package real.health.GUI.MedicalHistoryTabs;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import real.health.SQL.*;
import java.awt.*;
import real.health.GUI.UserRole;

public class createSexualTab {
    private UserRole userRole;
    public JComponent createSexualTab(String id, UserRole userRole) {
        this.userRole = userRole;
        JTable sexualTable = new JTable();
        // populate the table with the patient's sexual history
        try {
            // Load the MySQL JDBC driver
            // Create a connection to the database
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();

            // Create a SQL statement to retrieve the patient's sexual history
            String sql = "SELECT sexual_activity, partners, last_activity, protection FROM sexual_history WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();

            // Create a table model and populate it with the retrieved data
            DefaultTableModel tableModel = new DefaultTableModel(
                    new Object[] { "Sexual Activity", "Partners", "Last Activity", "Protection" },
                    0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            while (result.next()) {
                tableModel.addRow(new Object[] { result.getString(1), result.getInt(2), result.getString(3),
                        result.getString(4) });
            }
            sexualTable.setModel(tableModel);

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

        // Create the add button and add an ActionListener to upload the new sexual
        // history to the SQL server
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create the form for entering the new sexual history details
                JFrame addSexualFrame = new JFrame("Add Sexual History");
                addSexualFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                addSexualFrame.setSize(400, 200);
                addSexualFrame.setLayout(new GridLayout(5, 2, 10, 10));
                addSexualFrame.setLocationRelativeTo(null);

                // Add form components for entering the sexual history details
                JLabel activityLabel = new JLabel("Activity:");
                JTextField activityField = new JTextField();
                addSexualFrame.add(activityLabel);
                addSexualFrame.add(activityField);

                JLabel partnersLabel = new JLabel("Partners:");
                JSpinner partnersSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
                addSexualFrame.add(partnersLabel);
                addSexualFrame.add(partnersSpinner);

                JLabel lastActivityLabel = new JLabel("Last Activity:");
                JTextField lastActivityField = new JTextField();
                addSexualFrame.add(lastActivityLabel);
                addSexualFrame.add(lastActivityField);

                JLabel protectionLabel = new JLabel("Protection:");
                JTextField protectionField = new JTextField();
                addSexualFrame.add(protectionLabel);
                addSexualFrame.add(protectionField);

                // Add a button for submitting the form
                JButton submitButton = new JButton("Submit");
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Get the values from the form fields
                        String sexualActivity = activityField.getText();
                        int partners = (int) partnersSpinner.getValue();
                        String lastActivity = lastActivityField.getText();
                        String protection = protectionField.getText();

                        // Upload the new sexual history to the SQL server
                        try {
                            // Load the MySQL JDBC driver
                            HealthConn newConnection = new HealthConn();
                            Connection con = newConnection.connect();

                            // Create a SQL statement to insert the new sexual history
                            String sql = "INSERT INTO sexual_history (id, sexual_activity, partners, last_activity, protection) VALUES (?, ?, ?, ?, ?)";
                            PreparedStatement statement = con.prepareStatement(sql);
                            statement.setString(1, id);
                            statement.setString(2, sexualActivity);
                            statement.setInt(3, partners);
                            statement.setString(4, lastActivity);
                            statement.setString(5, protection);
                            statement.executeUpdate();

                            // Refresh the sexual history table to show the newly added history
                            DefaultTableModel tableModel = (DefaultTableModel) sexualTable.getModel();
                            tableModel.addRow(new Object[] { sexualActivity, partners, lastActivity, protection });

                            // Clean up resources
                            statement.close();
                            con.close();
                            // Close the add sexual history frame
                            addSexualFrame.dispose();
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
                        addSexualFrame.dispose();
                    }
                });

                addSexualFrame.add(cancelButton);
                addSexualFrame.add(submitButton);
                // Display the add sexual history frame
                addSexualFrame.setVisible(true);
            }
        });

        // Create a panel for the add button
        JPanel addButtonPanel = new JPanel();
        addButtonPanel.setLayout(new BorderLayout());
        if (userRole == UserRole.PROVIDER) {
            addButtonPanel.add(addButton);
        }
        
        // Create the sexual history tab panel and add the sexual history table and add
        // button panel
        JPanel sexualTabPanel = new JPanel();
        sexualTabPanel.setLayout(new BorderLayout());
        sexualTabPanel.add(new JScrollPane(sexualTable), BorderLayout.CENTER);
        sexualTabPanel.add(addButtonPanel, BorderLayout.PAGE_END);

        return sexualTabPanel;
    }

}
