package real.health.GUI.MedicalHistoryTabs;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import java.awt.event.*;
import java.io.IOException;
import real.health.API.FDAPI;
import real.health.API.readCSV;
import real.health.SQL.*;
import java.awt.*;
import real.health.GUI.UserRole;

public class createMedicationsTab {
    private UserRole userRole;

    public JComponent createMedicationsTab(String id, UserRole userRole) {
        this.userRole = userRole;
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
            DateFormat normal = new SimpleDateFormat("MM/dd/yyyy");
            // Create a table model and populate it with the retrieved data
            DefaultTableModel tableModel = new DefaultTableModel(
                    new Object[] { "Medication", "Dosage", "Frequency", "Date Prescribed" },
                    0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            while (result.next()) {
                java.sql.Date sqlDate = result.getDate(4);
                java.util.Date utilDate = new java.util.Date(sqlDate.getTime());
                String formattedDate = normal.format(utilDate);
                tableModel.addRow(new Object[] { result.getString(1), result.getString(2), result.getString(3),
                        formattedDate });
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

                readCSV rc = new readCSV();
                List<String> drugList = rc.readDrugNames("output.csv");
                Collections.sort(drugList);

                JLabel nameLabel = new JLabel("Name:");
                JComboBox<String> drugCombo = new JComboBox<>(drugList.toArray(new String[0]));
                drugCombo.setEditable(true);
                drugCombo.setSelectedItem(null);
                AutoCompleteDecorator.decorate(drugCombo);

                addMedicationFrame.add(nameLabel);
                addMedicationFrame.add(drugCombo);

                JComboBox<String> doseCombo = new JComboBox<>();
                JLabel doseLabel = new JLabel("Dose:");
                drugCombo.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        String selectedDrug = (String) drugCombo.getSelectedItem();
                        List<String> newDoses;

                        if (selectedDrug != null && !selectedDrug.isEmpty() && drugList.contains(selectedDrug)) {
                            FDAPI newFDA = new FDAPI();
                            try {
                                newDoses = newFDA.getDrugDosages(selectedDrug.toString());

                            } catch (IOException e1) {
                                e1.printStackTrace();
                                newDoses = Arrays.asList("0.5 mg", "1 mg", "5 mg", "10 mg");
                            }
                        } else {
                            newDoses = Arrays.asList("0.5 mg", "1 mg", "5 mg", "10 mg");
                        }
                        doseCombo.setModel(new DefaultComboBoxModel<>(newDoses.toArray(new String[0])));
                        doseCombo.setSelectedItem(null);
                        doseCombo.repaint();
                    }
                });

                addMedicationFrame.add(doseLabel);
                addMedicationFrame.add(doseCombo);

                JLabel frequencyLabel = new JLabel("Frequency:");
                String[] freq = { "Daily", "Twice A Day", "Three Times a Day", "When Needed" };
                JComboBox<String> freqCombo = new JComboBox<>(freq);
                freqCombo.setSelectedItem(null);
                addMedicationFrame.add(frequencyLabel);
                addMedicationFrame.add(freqCombo);

                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                DateFormatter dateFormatter = new DateFormatter(dateFormat);
                dateFormatter.setAllowsInvalid(false);
                dateFormatter.setOverwriteMode(true);

                JFormattedTextField dateField = new JFormattedTextField();
                dateField.setFormatterFactory(new DefaultFormatterFactory(dateFormatter));
                dateField.setValue(new java.util.Date());

                JLabel datePrescribedLabel = new JLabel("Date Prescribed:");

                addMedicationFrame.add(datePrescribedLabel);
                addMedicationFrame.add(dateField);

                // Add a button for submitting the form
                JButton submitButton = new JButton("Submit");
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Get the values from the form fields
                        String medicationName = (String) drugCombo.getSelectedItem();
                        String dose = (String) doseCombo.getSelectedItem();
                        String frequency = (String) freqCombo.getSelectedItem();
                        String datePrescribed = dateField.getText();

                        DateFormat toSQL = new SimpleDateFormat("yyyy-MM-dd");
                        DateFormat normal = new SimpleDateFormat("MM/dd/yyyy");

                        String formDate;
                        java.sql.Date sqlDate = null;
                        try {
                            java.util.Date prescribe = normal.parse(datePrescribed);
                            formDate = toSQL.format(prescribe);
                            sqlDate = new java.sql.Date(prescribe.getTime());
                        } catch (ParseException e1) {
                            formDate = (String) datePrescribed;
                            e1.printStackTrace();
                        }

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
                            statement.setDate(5, sqlDate);
                            statement.executeUpdate();

                            // Refresh the medications table to show the newly added medication
                            DefaultTableModel tableModel = (DefaultTableModel) medicationsTable.getModel();
                            tableModel.addRow(new Object[] { medicationName, dose, frequency, datePrescribed });

                            // Clean up resources
                            statement.close();
                            con.close();
                            // Close the add medication frame
                            addMedicationFrame.dispose();
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
                        addMedicationFrame.dispose();
                    }
                });

                addMedicationFrame.add(cancelButton);
                addMedicationFrame.add(submitButton);

                // Display the add medication frame
                addMedicationFrame.setVisible(true);
            }
        });

        // Create the delete button and add an ActionListener
        // to delete the selected medications
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row in the table
                int selectedRow = medicationsTable.getSelectedRow();

                // If no row is selected, display an error message
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(medicationsTable, "Please select a row to delete.");
                    return;
                }
                // Display a confirmation dialog
                int confirmation = JOptionPane.showConfirmDialog(medicationsTable,
                        "Are you sure you want to delete the selected row?", "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION);

                // If the user confirms the deletion, proceed
                if (confirmation == JOptionPane.YES_OPTION) {

                    // Get the primary key or unique identifier of the record from the selected row
                    // Assuming the first column of the table contains the primary key
                    String primaryKey = medicationsTable.getValueAt(selectedRow, 0).toString();

                    // Execute an SQL DELETE statement to delete
                    // the corresponding record from the database
                    try {
                        HealthConn newConnection = new HealthConn();
                        Connection con = newConnection.connect();
                        String sql = "DELETE FROM medications WHERE id = ? AND medication = ? AND dose = ? AND frequency = ? AND datePrescribed = ?";
                        PreparedStatement statement = con.prepareStatement(sql);
                        statement.setString(1, id);
                        statement.setString(2, medicationsTable.getValueAt(selectedRow, 0).toString());
                        statement.setString(3, medicationsTable.getValueAt(selectedRow, 1).toString());
                        statement.setString(4, medicationsTable.getValueAt(selectedRow, 2).toString());
                        statement.setString(5, medicationsTable.getValueAt(selectedRow, 3).toString());

                        statement.executeUpdate();

                        // Close the statement and connection
                        statement.close();
                        con.close();

                        // Remove the selected row from the table
                        DefaultTableModel model = (DefaultTableModel) medicationsTable.getModel();
                        model.removeRow(selectedRow);

                    } catch (ClassNotFoundException | SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(medicationsTable, "An error occurred while deleting the record.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Create the medications tab panel and add
        // the medications table and add button panel
        JPanel medicationsTabPanel = new JPanel(new BorderLayout());
        medicationsTabPanel.add(new JScrollPane(medicationsTable), BorderLayout.CENTER);

        // Create a panel for the add button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JPanel addDeletePanel = new JPanel(new GridLayout(1, 2, 10, 10));

        if (userRole == UserRole.PROVIDER) {
            addDeletePanel.add(addButton);
            addDeletePanel.add(deleteButton);
            buttonPanel.add(addDeletePanel);
            medicationsTabPanel.add(buttonPanel, BorderLayout.PAGE_END);
        }

        return medicationsTabPanel;
    }
}
