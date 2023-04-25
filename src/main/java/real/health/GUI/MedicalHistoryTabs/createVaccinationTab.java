package real.health.GUI.MedicalHistoryTabs;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.*;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import java.awt.event.*;
import real.health.SQL.*;
import real.health.UTIL.Vaxxes;

import java.awt.*;
import real.health.GUI.UserRole;

public class createVaccinationTab {
    private UserRole userRole;

    public JComponent createVaccinationTab(String id, UserRole userRole) {
        this.userRole = userRole;
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
                // JTextField vaccineField = new JTextField();
                Vaxxes newVax = new Vaxxes(id);
                ArrayList<String> relativeVax = newVax.vaxToList();
                JComboBox vaccines = new JComboBox<>(relativeVax.toArray(new String[0]));
                vaccines.setEditable(true);
                vaccines.setSelectedItem(null);
                AutoCompleteDecorator.decorate(vaccines);

                addVaccinationFrame.add(vaccineLabel);
                addVaccinationFrame.add(vaccines);

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
                        String vaccineName = (String) vaccines.getSelectedItem();
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
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addVaccinationFrame.dispose();
                    }
                });
                addVaccinationFrame.add(cancelButton);
                addVaccinationFrame.add(submitButton);

                // Display the form for entering the new vaccination details
                addVaccinationFrame.setVisible(true);
            }
        });

        // Create the delete button and add an ActionListener
        // to delete the selected vaccination
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row in the table
                int selectedRow = vaccinationTable.getSelectedRow();

                // If no row is selected, display an error message
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(vaccinationTable, "Please select a row to delete.");
                    return;
                }

                // Display a confirmation dialog
                int confirmation = JOptionPane.showConfirmDialog(vaccinationTable,
                        "Are you sure you want to delete the selected row?", "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION);

                // If the user confirms the deletion, proceed
                if (confirmation == JOptionPane.YES_OPTION) {
                    // Get the primary key or unique identifier of the record from the selected row
                    // Assuming the first column of the table contains the primary key
                    String primaryKey = vaccinationTable.getValueAt(selectedRow, 0).toString();

                    // Execute an SQL DELETE statement to delete
                    // the corresponding record from the database
                    try {
                        HealthConn newConnection = new HealthConn();
                        Connection con = newConnection.connect();
                        String sql = "DELETE FROM vaccinations WHERE id = ? AND vaccine = ? AND dateAdministered = ? AND locationAdministered = ? AND administeringProvider = ?";
                        PreparedStatement statement = con.prepareStatement(sql);
                        statement.setString(1, id);
                        statement.setString(2, vaccinationTable.getValueAt(selectedRow, 0).toString());
                        statement.setString(3, vaccinationTable.getValueAt(selectedRow, 1).toString());
                        statement.setString(4, vaccinationTable.getValueAt(selectedRow, 2).toString());
                        statement.setString(5, vaccinationTable.getValueAt(selectedRow, 3).toString());

                        statement.executeUpdate();

                        // Close the statement and connection
                        statement.close();
                        con.close();

                        // Remove the selected row from the table
                        DefaultTableModel model = (DefaultTableModel) vaccinationTable.getModel();
                        model.removeRow(selectedRow);

                    } catch (ClassNotFoundException | SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(vaccinationTable, "An error occurred while deleting the record.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Add the table and buttons to the panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(vaccinationTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JPanel addDeletePanel = new JPanel(new GridLayout(1, 2, 10, 10));

        if (userRole == UserRole.PROVIDER) {
            addDeletePanel.add(addButton);
            addDeletePanel.add(deleteButton);
            buttonPanel.add(addDeletePanel);
            panel.add(buttonPanel, BorderLayout.PAGE_END);
        }

        return panel;
    }
}
