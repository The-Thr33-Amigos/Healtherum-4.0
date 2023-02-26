package real.health.GUI;

import java.sql.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.*;
import real.health.Patient.*;
import real.health.SQL.HealthConn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MedicalHistoryTab {
    public JComponent createMedicalHistoryTab(String id) {
        JTabbedPane medicalHistoryTabs = new JTabbedPane();
        medicalHistoryTabs.addTab("Summary", createSummaryTab());
        medicalHistoryTabs.addTab("Allergies", createAllergiesTab(id));
        medicalHistoryTabs.addTab("Medications", createMedicationsTab(id));

        medicalHistoryTabs.addChangeListener((ChangeEvent e) -> {
            int index = medicalHistoryTabs.getSelectedIndex();
            if (index == 0) { // Summary tab
                medicalHistoryTabs.setSelectedIndex(0); // Navigate to the summary subtab
            } else if (index == 1) { // Allergies tab
                medicalHistoryTabs.setSelectedIndex(1); // Navigate to the allergies subtab
            } else if (index == 2) { // Medications tab
                medicalHistoryTabs.setSelectedIndex(2); // Navigate to the medications subtab
            }
        });

        JPanel medicalHistoryPanel = new JPanel();
        medicalHistoryPanel.setLayout(new BorderLayout());
        medicalHistoryPanel.add(medicalHistoryTabs, BorderLayout.CENTER);

        return medicalHistoryPanel;
    }

    private JComponent createMedicationsTab(String id) {
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
            DefaultTableModel tableModel = new DefaultTableModel(new Object[] { "Medication", "Dosage", "Frequency" },
                    0);
            while (result.next()) {
                tableModel.addRow(new Object[] { result.getString(1), result.getString(2), result.getString(3) });
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
            // Create the add button and add an ActionListener to upload the new medication to the SQL server
            JButton addButton = new JButton("Add");
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Create the form for entering the new medication details
                    JFrame addMedicationFrame = new JFrame("Add Medication");
                    addMedicationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    addMedicationFrame.setSize(400, 200);
                    addMedicationFrame.setLayout(new GridLayout(5, 2, 10, 10));

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
    }});

        // Create a panel for the add button
        JPanel addButtonPanel = new JPanel();
        addButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        addButtonPanel.add(addButton);

        // Create the medications tab panel and add the medications table and add button panel
        JPanel medicationsTabPanel = new JPanel(new BorderLayout());
        medicationsTabPanel.add(new JScrollPane(medicationsTable), BorderLayout.CENTER);
        medicationsTabPanel.add(addButtonPanel, BorderLayout.SOUTH);


        return medicationsTabPanel;
    }

    private JComponent createSummaryTab() {

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 10, 10);

        JLabel allergiesLabel = new JLabel("Allergies:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(allergiesLabel, constraints);

        JTextField allergiesField = new JTextField();
        allergiesField.setPreferredSize(new Dimension(200, 30));
        allergiesField.setEditable(false);
        constraints.gridx = 3;
        constraints.gridy = 0;
        panel.add(allergiesField, constraints);

        JLabel medicationsLabel = new JLabel("Medications:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(medicationsLabel, constraints);

        JTextField medicationsField = new JTextField();
        medicationsField.setPreferredSize(new Dimension(200, 30));
        medicationsField.setEditable(false);
        constraints.gridx = 3;
        constraints.gridy = 1;
        panel.add(medicationsField, constraints);

        JLabel conditionsLabel = new JLabel("Conditions:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(conditionsLabel, constraints);

        JTextField conditionsField = new JTextField();
        conditionsField.setPreferredSize(new Dimension(200, 30));
        conditionsField.setEditable(false);
        constraints.gridx = 3;
        constraints.gridy = 2;
        panel.add(conditionsField, constraints);

        JLabel surgeriesLabel = new JLabel("Surgeries:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(surgeriesLabel, constraints);

        JTextField surgeriesField = new JTextField();
        surgeriesField.setPreferredSize(new Dimension(200, 30));
        surgeriesField.setEditable(false);
        constraints.gridx = 3;
        constraints.gridy = 3;
        panel.add(surgeriesField, constraints);

        JLabel chronicConditionsLabel = new JLabel("Chronic Conditions:");
        constraints.gridx = 0;
        constraints.gridy = 4;
        panel.add(chronicConditionsLabel, constraints);

        JTextField chronicConditionsField = new JTextField();
        chronicConditionsField.setPreferredSize(new Dimension(200, 30));
        chronicConditionsField.setEditable(false);
        constraints.gridx = 3;
        constraints.gridy = 4;
        panel.add(chronicConditionsField, constraints);

        JLabel familyMedicalHistoryLabel = new JLabel("Family Medical History:");
        constraints.gridx = 0;
        constraints.gridy = 5;
        panel.add(familyMedicalHistoryLabel, constraints);

        JTextField familyMedicalHistoryField = new JTextField();
        familyMedicalHistoryField.setPreferredSize(new Dimension(200, 30));
        familyMedicalHistoryField.setEditable(false);
        constraints.gridx = 3;
        constraints.gridy = 5;
        panel.add(familyMedicalHistoryField, constraints);

        JLabel vaccinationHistoryLabel = new JLabel("Vaccination History:");
        constraints.gridx = 0;
        constraints.gridy = 6;
        panel.add(vaccinationHistoryLabel, constraints);

        JTextField vaccinationHistoryField = new JTextField();
        vaccinationHistoryField.setPreferredSize(new Dimension(200, 30));
        vaccinationHistoryField.setEditable(false);
        constraints.gridx = 3;
        constraints.gridy = 6;
        panel.add(vaccinationHistoryField, constraints);

        JLabel lifestyleFactorsLabel = new JLabel("Lifestyle Factors:");
        constraints.gridx = 0;
        constraints.gridy = 7;
        panel.add(lifestyleFactorsLabel, constraints);

        JTextField lifestyleFactorsField = new JTextField();
        lifestyleFactorsField.setPreferredSize(new Dimension(200, 30));
        lifestyleFactorsField.setEditable(false);
        constraints.gridx = 3;
        constraints.gridy = 7;
        panel.add(lifestyleFactorsField, constraints);

        JLabel sexualHistoryLabel = new JLabel("Sexual History:");
        constraints.gridx = 0;
        constraints.gridy = 8;
        panel.add(sexualHistoryLabel, constraints);

        JTextField sexualHistoryField = new JTextField();
        sexualHistoryField.setPreferredSize(new Dimension(200, 30));
        sexualHistoryField.setEditable(false);
        constraints.gridx = 3;
        constraints.gridy = 8;
        panel.add(sexualHistoryField, constraints);

        JLabel mentalHealthHistoryLabel = new JLabel("Mental Health History:");
        constraints.gridx = 0;
        constraints.gridy = 9;
        panel.add(mentalHealthHistoryLabel, constraints);

        JTextField mentalHealthHistoryField = new JTextField();
        mentalHealthHistoryField.setPreferredSize(new Dimension(200, 30));
        mentalHealthHistoryField.setEditable(false);
        constraints.gridx = 3;
        constraints.gridy = 9;
        panel.add(mentalHealthHistoryField, constraints);

        JLabel developmentalMilestonesLabel = new JLabel("Developmental Milestones:");
        constraints.gridx = 0;
        constraints.gridy = 10;
        panel.add(developmentalMilestonesLabel, constraints);

        JTextField developmentalMilestonesField = new JTextField();
        developmentalMilestonesField.setPreferredSize(new Dimension(200, 30));
        developmentalMilestonesField.setEditable(false);
        constraints.gridx = 3;
        constraints.gridy = 10;
        panel.add(developmentalMilestonesField, constraints);

        // Hardcoded example
        allergiesField.setText("Peanuts");
        medicationsField.setText("Ibuprofen");
        conditionsField.setText("Asthma");
        surgeriesField.setText("Appendix removal");
        chronicConditionsField.setText("None");
        familyMedicalHistoryField.setText("Diabetes in mother");
        vaccinationHistoryField.setText("Up to date");
        lifestyleFactorsField.setText("Non-smoker, occasional drinker");
        sexualHistoryField.setText("Not applicable");
        mentalHealthHistoryField.setText("None");
        developmentalMilestonesField.setText("Normal");

        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BorderLayout());
        summaryPanel.add(panel, BorderLayout.SOUTH);

        return summaryPanel;

    }

    private JComponent createAllergiesTab(String id) {

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
            DefaultTableModel tableModel = new DefaultTableModel(new Object[] { "Name", "Type", "Reaction", "Severity" },
                    0);
            while (result.next()) {
                tableModel.addRow(new Object[] { result.getString(1), result.getString(2), result.getString(3) });
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
            // Create the add button and add an ActionListener to upload the new medication to the SQL server
            JButton addButton = new JButton("Add");
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Create the form for entering the new medication details
                    JFrame addAllergiesFrame = new JFrame("Add Allergy");
                    addAllergiesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    addAllergiesFrame.setSize(400, 200);
                    addAllergiesFrame.setLayout(new GridLayout(5, 2, 10, 10));

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
                        } catch (ClassNotFoundException ex) {
                            System.out.println("Error: unable to load MySQL JDBC driver");
                            ex.printStackTrace();
                        } catch (SQLException ex) {
                            System.out.println("Error: unable to connect to MySQL database");
                            ex.printStackTrace();
                        }

                        // Close the add medication frame
                        addAllergiesFrame.dispose();
                    }
                });
                addAllergiesFrame.add(submitButton);


            // Display the add medication frame
            addAllergiesFrame.setVisible(true);
    }});

        // Create a panel for the add button
        JPanel addButtonPanel = new JPanel();
        addButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        addButtonPanel.add(addButton);

        // Create the medications tab panel and add the medications table and add button panel
        JPanel allergyTabPanel = new JPanel(new BorderLayout());
        allergyTabPanel.add(new JScrollPane(allergiesTable), BorderLayout.CENTER);
        allergyTabPanel.add(addButtonPanel, BorderLayout.SOUTH);

        return allergyTabPanel;
    }

}
