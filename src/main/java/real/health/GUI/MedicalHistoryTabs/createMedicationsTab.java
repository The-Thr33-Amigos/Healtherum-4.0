package real.health.GUI.MedicalHistoryTabs;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import real.health.SQL.*;
import java.awt.*;

public class createMedicationsTab {
    public JComponent createMedicationsTab(String id) {
        JTable medicationsTable = new JTable();
        // populate the table with the patient's current medications
        try {
            // Load the MySQL JDBC driver
            // Create a connection to the database
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();

            // Create a SQL statement to retrieve the patient's current medications
            String sql = "SELECT medication, dose, frequency, datePrescribed FROM medications WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();

            // Create a table model and populate it with the retrieved data
            DefaultTableModel tableModel = new DefaultTableModel(
                    new Object[] { "Medication", "Dosage", "Frequency", "Date Prescribed" },
                    0);
            while (result.next()) {
                tableModel.addRow(new Object[] { result.getString(1), result.getString(2), result.getString(3),
                        result.getString(4) });
            }
            medicationsTable.setModel(tableModel);

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
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create the form for entering the new medication details
                JFrame addMedicationFrame = new JFrame("Add Medication");
                addMedicationFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                addMedicationFrame.setSize(400, 200);
                addMedicationFrame.setLayout(new GridLayout(5, 2, 10, 10));
                addMedicationFrame.setLocationRelativeTo(null);

                // Add form components for entering the medication details
                JLabel nameLabel = new JLabel("Name:");
                JTextField nameField = new JTextField();
                addMedicationFrame.add(nameLabel);
                addMedicationFrame.add(nameField);

                JLabel doseLabel = new JLabel("Dose:");
                JTextField doseField = new JTextField();
                addMedicationFrame.add(doseLabel);
                addMedicationFrame.add(doseField);

                JLabel frequencyLabel = new JLabel("Frequency:");
                JTextField frequencyField = new JTextField();
                addMedicationFrame.add(frequencyLabel);
                addMedicationFrame.add(frequencyField);

                JLabel datePrescribedLabel = new JLabel("Date Prescribed:");
                JTextField datePrescribedField = new JTextField();
                addMedicationFrame.add(datePrescribedLabel);
                addMedicationFrame.add(datePrescribedField);

                // Add a button for submitting the form
                JButton submitButton = new JButton("Submit");
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Get the values from the form fields
                        String medicationName = nameField.getText();
                        String dose = doseField.getText();
                        String frequency = frequencyField.getText();
                        String datePrescribed = datePrescribedField.getText();

                        // Upload the new medication to the SQL server
                        try {
                            // Load the MySQL JDBC driver
                            HealthConn newConnection = new HealthConn();
                            Connection con = newConnection.connect();

                            // Create a SQL statement to insert the new medication
                            String sql = "INSERT INTO medications (id, medication, dose, frequency, datePrescribed) VALUES (?, ?, ?, ?, ?)";
                            PreparedStatement statement = con.prepareStatement(sql);
                            statement.setString(1, id);
                            statement.setString(2, medicationName);
                            statement.setString(3, dose);
                            statement.setString(4, frequency);
                            statement.setString(5, datePrescribed);
                            statement.executeUpdate();

                            // Refresh the medications table to show the newly added medication
                            DefaultTableModel tableModel = (DefaultTableModel) medicationsTable.getModel();
                            tableModel.addRow(new Object[] { medicationName, dose, frequency, datePrescribed });

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

                        // Close the add medication frame
                        addMedicationFrame.dispose();
                    }
                });
                addMedicationFrame.add(submitButton);

                // Display the add medication frame
                addMedicationFrame.setVisible(true);
            }
        });

        // Create a panel for the add button
        JPanel addButtonPanel = new JPanel();
        addButtonPanel.setLayout(new BorderLayout());
        addButtonPanel.add(addButton);

        // Create the medications tab panel and add the medications table and add button
        // panel
        JPanel medicationsTabPanel = new JPanel(new BorderLayout());
        medicationsTabPanel.add(new JScrollPane(medicationsTable), BorderLayout.CENTER);
        medicationsTabPanel.add(addButtonPanel, BorderLayout.PAGE_END);

        return medicationsTabPanel;
    }
}
