package real.health.GUI.MedicalHistoryTabs;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import java.awt.event.*;
import real.health.SQL.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

import real.health.API.readCSV;
import real.health.GUI.UserRole;

public class createSurgeriesTab {
    private UserRole userRole;
    public JComponent createSurgeriesTab(String id, UserRole userRole) {
        this.userRole = userRole;
        JTable surgeriesTable = new JTable();
        // populate the table with the patient's surgery history
        try {
            // Load the MySQL JDBC driver
            // Create a connection to the database
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();

            // Create a SQL statement to retrieve the patient's surgery history
            String sql = "SELECT surgery_procedure, surgeon, location, date FROM surgeries WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();

            // Create a table model and populate it with the retrieved data
            DefaultTableModel tableModel = new DefaultTableModel(
                    new Object[] { "Procedure", "Surgeon", "Location", "Date" },
                    0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            while (result.next()) {
                tableModel.addRow(new Object[] { result.getString(1), result.getString(2), result.getString(3),
                        result.getString(4) });
            }
            surgeriesTable.setModel(tableModel);

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

        // Create the add button and add an ActionListener to upload the new surgery
        // to the SQL server
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create the form for entering the new surgery details
                JFrame addSurgeryFrame = new JFrame("Add Surgery");
                addSurgeryFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                addSurgeryFrame.setSize(400, 200);
                addSurgeryFrame.setLayout(new GridLayout(5, 2, 10, 10));
                addSurgeryFrame.setLocationRelativeTo(null);

                // Add form components for entering the surgery details
                JLabel dateLabel = new JLabel("Date:");
                JTextField dateField = new JTextField();
                addSurgeryFrame.add(dateLabel);
                addSurgeryFrame.add(dateField);

                readCSV rc = new readCSV();
                List<String> surgList = rc.readDrugNames("surg_proc.csv");
                Collections.sort(surgList);

                JLabel procedureLabel = new JLabel("Procedure:");
                JComboBox<String> proCombo = new JComboBox<>(surgList.toArray(new String[0]));
                proCombo.setEditable(true);
                proCombo.setSelectedItem(null);
                AutoCompleteDecorator.decorate(proCombo);
                addSurgeryFrame.add(procedureLabel);
                addSurgeryFrame.add(proCombo);


                JLabel surgeonLabel = new JLabel("Surgeon:");
                JTextField surgeonField = new JTextField();
                addSurgeryFrame.add(surgeonLabel);
                addSurgeryFrame.add(surgeonField);

                JLabel locationLabel = new JLabel("Location:");
                JTextField locationField = new JTextField();
                addSurgeryFrame.add(locationLabel);
                addSurgeryFrame.add(locationField);

                // Add a button for submitting the form
                JButton submitButton = new JButton("Submit");
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Get the values from the form fields
                        String date = dateField.getText();
                        String procedure = (String) proCombo.getSelectedItem();
                        String surgeon = surgeonField.getText();
                        String location = locationField.getText();

                        // Upload the new surgery record to the SQL server
                        try {
                            // Load the MySQL JDBC driver
                            HealthConn newConnection = new HealthConn();
                            Connection con = newConnection.connect();

                            // Create a SQL statement to insert the new surgery record into the database
                            String sql = "INSERT INTO surgeries (id, date, surgery_procedure, surgeon, location) VALUES (?, ?, ?, ?, ?)";
                            PreparedStatement statement = con.prepareStatement(sql);
                            statement.setString(1, id);
                            statement.setString(2, date);
                            statement.setString(3, procedure);
                            statement.setString(4, surgeon);
                            statement.setString(5, location);
                            statement.executeUpdate();

                            // Refresh the surgeries table to show the newly added record
                            DefaultTableModel tableModel = (DefaultTableModel) surgeriesTable.getModel();
                            tableModel.addRow(new Object[] { procedure, surgeon, location, date });

                            // Clean up resources
                            statement.close();
                            con.close();
                            addSurgeryFrame.dispose();
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
                        addSurgeryFrame.dispose();
                    }
                });
                addSurgeryFrame.add(cancelButton);
                addSurgeryFrame.add(submitButton);

                // Display the form for entering the new surgery details
                addSurgeryFrame.setVisible(true);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        if (userRole == UserRole.PROVIDER) {
            buttonPanel.add(addButton);
        }
        
        // Add the table and buttons to the panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(surgeriesTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.PAGE_END);

        return panel;
    }
}
