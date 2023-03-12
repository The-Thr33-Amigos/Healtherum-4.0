package real.health.GUI.MedicalHistoryTabs;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import real.health.SQL.*;
import java.awt.*;

public class createSummaryTab {
    public JComponent createSummaryTab(String id) {

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
}
