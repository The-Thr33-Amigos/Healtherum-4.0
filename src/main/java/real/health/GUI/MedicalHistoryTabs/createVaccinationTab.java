package real.health.GUI.MedicalHistoryTabs;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import real.health.SQL.*;
import java.awt.*;

public class createVaccinationTab {
    public JComponent createVaccinationTab(String id) {
        JTable vaccinationTable = new JTable();
        // populate the table with the patient's vaccination history
        try {
            // Load the MySQL JDBC driver
            // Create a connection to the database
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();

            // Create a SQL statement to retrieve the patient's vaccination history
            String sql = "SELECT vaccine, dateAdministered, locationAdministered, administeringProvider FROM vaccinations WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();

            // Create a table model and populate it with the retrieved data
            DefaultTableModel tableModel = new DefaultTableModel(
                    new Object[] { "Vaccine", "Date Administered", "Location Administered", "Administering Provider" },
                    0);
            while (result.next()) {
                tableModel.addRow(new Object[] { result.getString(1), result.getString(2), result.getString(3),
                        result.getString(4) });
            }
            vaccinationTable.setModel(tableModel);

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

        // Create the add button and add an ActionListener to upload the new vaccination
        // to the SQL server
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create the form for entering the new vaccination details
                JFrame addVaccinationFrame = new JFrame("Add Vaccination");
                addVaccinationFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                addVaccinationFrame.setSize(400, 200);
                addVaccinationFrame.setLayout(new GridLayout(5, 2, 10, 10));
                addVaccinationFrame.setLocationRelativeTo(null);

                // Add form components for entering the vaccination details
                JLabel vaccineLabel = new JLabel("Vaccine:");
                JTextField vaccineField = new JTextField();
                addVaccinationFrame.add(vaccineLabel);
                addVaccinationFrame.add(vaccineField);

                JLabel dateAdministeredLabel = new JLabel("Date Administered:");
                JTextField dateAdministeredField = new JTextField();
                addVaccinationFrame.add(dateAdministeredLabel);
                addVaccinationFrame.add(dateAdministeredField);

                JLabel locationAdministeredLabel = new JLabel("Location Administered:");
                JTextField locationAdministeredField = new JTextField();
                addVaccinationFrame.add(locationAdministeredLabel);
                addVaccinationFrame.add(locationAdministeredField);

                JLabel administeringProviderLabel = new JLabel("Administering Provider:");
                JTextField administeringProviderField = new JTextField();
                addVaccinationFrame.add(administeringProviderLabel);
                addVaccinationFrame.add(administeringProviderField);

                // Add a button for submitting the form
                JButton submitButton = new JButton("Submit");
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Get the values from the form fields
                        String vaccineName = vaccineField.getText();
                        String dateAdministered = dateAdministeredField.getText();
                        String locationAdministered = locationAdministeredField.getText();
                        String administeringProvider = administeringProviderField.getText();

                        // Upload the new vaccination record to the SQL server
                        try {
                            // Load the MySQL JDBC driver
                            HealthConn newConnection = new HealthConn();
                            Connection con = newConnection.connect();

                            // Create a SQL statement to insert the new vaccination record into the database
                            String sql = "INSERT INTO vaccinations (id, vaccine, dateAdministered, locationAdministered, administeringProvider) VALUES (?, ?, ?, ?, ?)";
                            PreparedStatement statement = con.prepareStatement(sql);
                            statement.setString(1, id);
                            statement.setString(2, vaccineName);
                            statement.setString(3, dateAdministered);
                            statement.setString(4, locationAdministered);
                            statement.setString(5, administeringProvider);
                            statement.executeUpdate();

                            // Refresh the vaccination table to show the newly added record
                            DefaultTableModel tableModel = (DefaultTableModel) vaccinationTable.getModel();
                            tableModel.addRow(new Object[] { vaccineName, dateAdministered, locationAdministered,
                                    administeringProvider });

                            // Clean up resources
                            statement.close();
                            con.close();
                            addVaccinationFrame.dispose();
                        } catch (ClassNotFoundException ex) {
                            System.out.println("Error: unable to load MySQL JDBC driver");
                            ex.printStackTrace();
                        } catch (SQLException ex) {
                            System.out.println("Error: unable to connect to MySQL database");
                            ex.printStackTrace();
                        }
                    }
                });
                addVaccinationFrame.add(new JPanel());
                addVaccinationFrame.add(submitButton);

                // Display the form for entering the new vaccination details
                addVaccinationFrame.setVisible(true);
            }
        });
        // Create the delete button and add an ActionListener to delete the selected
        // vaccination
        // from the SQL server and the table
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the index of the selected row in the table
                int selectedRow = vaccinationTable.getSelectedRow();

                // If a row is selected, delete the corresponding vaccination record from the
                // SQL server and the table
                if (selectedRow != -1) {
                    try {
                        // Load the MySQL JDBC driver
                        HealthConn newConnection = new HealthConn();
                        Connection con = newConnection.connect();

                        // Get the values from the selected row in the table
                        String vaccineName = (String) vaccinationTable.getValueAt(selectedRow, 0);
                        String dateAdministered = (String) vaccinationTable.getValueAt(selectedRow, 1);
                        String locationAdministered = (String) vaccinationTable.getValueAt(selectedRow, 2);
                        String administeringProvider = (String) vaccinationTable.getValueAt(selectedRow, 3);

                        // Create a SQL statement to delete the selected vaccination record from the
                        // database
                        String sql = "DELETE FROM vaccinations WHERE id = ? AND vaccine = ? AND dateAdministered = ? AND locationAdministered = ? AND administeringProvider = ?";
                        PreparedStatement statement = con.prepareStatement(sql);
                        statement.setString(1, id);
                        statement.setString(2, vaccineName);
                        statement.setString(3, dateAdministered);
                        statement.setString(4, locationAdministered);
                        statement.setString(5, administeringProvider);
                        statement.executeUpdate();

                        // Remove the selected row from the table
                        DefaultTableModel tableModel = (DefaultTableModel) vaccinationTable.getModel();
                        tableModel.removeRow(selectedRow);

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
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row to delete");
                }
            }
        });

        // Add the table and buttons to the panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(vaccinationTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;

    }

}
