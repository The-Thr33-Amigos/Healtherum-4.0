package real.health.GUI.MedicalHistoryTabs;

import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import real.health.SQL.*;
import java.awt.*;

public class createSummaryTab {
    public JComponent createSummaryTab(String id) {

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(14, 5, 5, 10);

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

        JLabel vaccinationHistoryLabel = new JLabel("Vaccinations:");
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

        // Call the fetchPatientData method and populate the fields
        try {
            Map<String, String> patientData = fetchPatientData(id);

            allergiesField.setText(patientData.get("allergies"));
            medicationsField.setText(patientData.get("medications"));
            conditionsField.setText(patientData.get("conditions"));
            surgeriesField.setText(patientData.get("surgeries"));
            chronicConditionsField.setText(patientData.get("chronicConditions"));
            familyMedicalHistoryField.setText(patientData.get("familyMedicalHistory"));
            vaccinationHistoryField.setText(patientData.get("vaccinationHistory"));
            lifestyleFactorsField.setText(patientData.get("lifestyleFactors"));
            sexualHistoryField.setText(patientData.get("sexualHistory"));
            mentalHealthHistoryField.setText(patientData.get("mentalHealthHistory"));
            developmentalMilestonesField.setText(patientData.get("developmentalMilestones"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BorderLayout());
        summaryPanel.add(panel, BorderLayout.SOUTH);

        return summaryPanel;

    }

    // Method to fetch the patient data from the database
    private Map<String, String> fetchPatientData(String id) throws ClassNotFoundException, SQLException {
        Map<String, String> patientData = new HashMap<>();

        HealthConn newConnection = new HealthConn();
        Connection con = newConnection.connect();

        // Fetch the patient's data using SQL queries
        // Replace the queries with the appropriate ones for your database structure

        // Fetch allergies
        String allergiesSql = "SELECT name FROM allergies WHERE id = ?";
        PreparedStatement allergiesStatement = con.prepareStatement(allergiesSql);
        allergiesStatement.setString(1, id);
        ResultSet allergiesResult = allergiesStatement.executeQuery();
        StringBuilder allergies = new StringBuilder();

        while (allergiesResult.next()) {
            if (allergies.length() > 0) {
                allergies.append(", ");
            }
            allergies.append(allergiesResult.getString(1));
        }
        patientData.put("allergies", allergies.toString());

        // Add other queries and put the data into the patientData map
        // Fetch chronic_condition
        String chronicSql = "SELECT chronic_condition FROM chronic_conditions WHERE id = ?";
        PreparedStatement chronicStatement = con.prepareStatement(chronicSql);
        chronicStatement.setString(1, id);
        ResultSet chronicResult = chronicStatement.executeQuery();
        StringBuilder chronic = new StringBuilder();

        while (chronicResult.next()) {
            if (chronic.length() > 0) {
                chronic.append(", ");
            }
            chronic.append(chronicResult.getString(1));
        }
        patientData.put("chronicConditions", chronic.toString());

        // Add other queries and put the data into the patientData map
        // Fetch conditions
        String conditionsSql = "SELECT medical_condition FROM conditions WHERE id = ?";
        PreparedStatement conditionsStatement = con.prepareStatement(conditionsSql);
        conditionsStatement.setString(1, id);
        ResultSet conditionsResult = conditionsStatement.executeQuery();
        StringBuilder conditions = new StringBuilder();

        while (conditionsResult.next()) {
            if (conditions.length() > 0) {
                conditions.append(", ");
            }
            conditions.append(conditionsResult.getString(1));
        }
        patientData.put("conditions", conditions.toString());
        
        // Add other queries and put the data into the patientData map
        // Fetch family
        String familySql = "SELECT health_condition FROM family_history WHERE id = ?";
        PreparedStatement familyStatement = con.prepareStatement(familySql);
        familyStatement.setString(1, id);
        ResultSet familyResult = familyStatement.executeQuery();
        StringBuilder family = new StringBuilder();

        while (familyResult.next()) {
            if (family.length() > 0) {
                family.append(", ");
            }
            family.append(familyResult.getString(1));
        }
        patientData.put("familyMedicalHistory", family.toString());

        // Add other queries and put the data into the patientData map
        // Fetch lifestyle
        String lifestyleSql = "SELECT factor FROM lifestyle WHERE id = ?";
        PreparedStatement lifestyleStatement = con.prepareStatement(lifestyleSql);
        lifestyleStatement.setString(1, id);
        ResultSet lifestyleResult = lifestyleStatement.executeQuery();
        StringBuilder lifestyle = new StringBuilder();

        while (lifestyleResult.next()) {
            if (lifestyle.length() > 0) {
                lifestyle.append(", ");
            }
            lifestyle.append(lifestyleResult.getString(1));
        }
        patientData.put("lifestyleFactors", lifestyle.toString());

        // Add other queries and put the data into the patientData map
        // Fetch medications
        String medicationsSql = "SELECT medication FROM medications WHERE id = ?";
        PreparedStatement medicationsStatement = con.prepareStatement(medicationsSql);
        medicationsStatement.setString(1, id);
        ResultSet medicationsResult = medicationsStatement.executeQuery();
        StringBuilder medications = new StringBuilder();

        while (medicationsResult.next()) {
            if (medications.length() > 0) {
                medications.append(", ");
            }
            medications.append(medicationsResult.getString(1));
        }
        patientData.put("medications", medications.toString());

        // Add other queries and put the data into the patientData map
        // Fetch mental
        String mentalSql = "SELECT issue FROM mental_health WHERE id = ?";
        PreparedStatement mentalStatement = con.prepareStatement(mentalSql);
        mentalStatement.setString(1, id);
        ResultSet mentalResult = mentalStatement.executeQuery();
        StringBuilder mental = new StringBuilder();

        while (mentalResult.next()) {
            if (mental.length() > 0) {
                mental.append(", ");
            }
            mental.append(mentalResult.getString(1));
        }
        patientData.put("mentalHealthHistory", mental.toString());

        // Add other queries and put the data into the patientData map
        // Fetch milestones
        String milestonesSql = "SELECT milestone FROM milestones WHERE id = ?";
        PreparedStatement milestonesStatement = con.prepareStatement(milestonesSql);
        milestonesStatement.setString(1, id);
        ResultSet milestonesResult = milestonesStatement.executeQuery();
        StringBuilder milestones = new StringBuilder();

        while (milestonesResult.next()) {
            if (milestones.length() > 0) {
                milestones.append(", ");
            }
            milestones.append(milestonesResult.getString(1));
        }
        patientData.put("developmentalMilestones", milestones.toString());

        // Fetch sexual
        String sexualSql = "SELECT sexual_activity FROM sexual_history WHERE id = ?";
        PreparedStatement sexualStatement = con.prepareStatement(sexualSql);
        sexualStatement.setString(1, id);
        ResultSet sexualResult = sexualStatement.executeQuery();
        StringBuilder sexual = new StringBuilder();

        while (sexualResult.next()) {
            if (sexual.length() > 0) {
                sexual.append(", ");
            }
            sexual.append(sexualResult.getString(1));
        }
        patientData.put("sexualHistory", sexual.toString());

        // Fetch surgeries
        String surgeriesSql = "SELECT surgery_procedure FROM surgeries WHERE id = ?";
        PreparedStatement surgeriesStatement = con.prepareStatement(surgeriesSql);
        surgeriesStatement.setString(1, id);
        ResultSet surgeriesResult = surgeriesStatement.executeQuery();
        StringBuilder surgeries = new StringBuilder();

        while (surgeriesResult.next()) {
            if (surgeries.length() > 0) {
                surgeries.append(", ");
            }
            surgeries.append(surgeriesResult.getString(1));
        }
        patientData.put("surgeries", surgeries.toString());

        // Fetch vaccinations
        String vaccinationsSql = "SELECT vaccine FROM vaccinations WHERE id = ?";
        PreparedStatement vaccinationsStatement = con.prepareStatement(vaccinationsSql);
        vaccinationsStatement.setString(1, id);
        ResultSet vaccinationsResult = vaccinationsStatement.executeQuery();
        StringBuilder vaccinations = new StringBuilder();

        while (vaccinationsResult.next()) {
            if (vaccinations.length() > 0) {
                vaccinations.append(", ");
            }
            vaccinations.append(vaccinationsResult.getString(1));
        }
        patientData.put("vaccinationHistory", vaccinations.toString());

        // Close the resources
        allergiesResult.close();
        allergiesStatement.close();
        chronicResult.close();
        chronicStatement.close();
        conditionsResult.close();
        conditionsStatement.close();
        familyResult.close();
        familyStatement.close();
        lifestyleResult.close();
        lifestyleStatement.close();
        medicationsResult.close();
        medicationsStatement.close();
        mentalResult.close();
        mentalStatement.close();
        milestonesResult.close();
        milestonesStatement.close();
        sexualResult.close();
        sexualStatement.close();
        surgeriesResult.close();
        surgeriesStatement.close();
        vaccinationsResult.close();
        vaccinationsStatement.close();
        // Close other resources
        // ...
        con.close();

        return patientData;
    }
}