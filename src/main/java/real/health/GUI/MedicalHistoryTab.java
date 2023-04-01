package real.health.GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import real.health.GUI.MedicalHistoryTabs.*;


public class MedicalHistoryTab {
    private UserRole userRole;

    public JComponent createMedicalHistoryTab(String id, UserRole role) {
        this.userRole = userRole;
        UIManager.put("TabbedPane.tabInsets", new Insets(12, 10, 10, 10));
        JTabbedPane medicalHistoryTabs = new JTabbedPane(SwingConstants.LEFT);
        Font font = new Font("Arial", Font.PLAIN, 14); // change the font family and size as desired
        medicalHistoryTabs.setFont(font);

        // Create a new instance of the Summary class
        createSummaryTab createSummaryTab = new createSummaryTab();
        medicalHistoryTabs.addTab("Summary", createSummaryTab.createSummaryTab(id));

        // Create a new instance of the Allergies class
        createAllergiesTab createAllergiesTab = new createAllergiesTab();
        medicalHistoryTabs.addTab("Allergies", createAllergiesTab.createAllergiesTab(id, role));

        // Create a new instance of the Medications class
        createMedicationsTab createMedicationsTab = new createMedicationsTab();
        medicalHistoryTabs.addTab("Medications", createMedicationsTab.createMedicationsTab(id, role));

        // Create a new instance of the Conditions class
        createConditionsTab createConditionsTab = new createConditionsTab();
        medicalHistoryTabs.addTab("Conditions", createConditionsTab.createConditionsTab(id, role));

        // Create a new instance of the Surgeries class
        createSurgeriesTab createSurgeriesTab = new createSurgeriesTab();
        medicalHistoryTabs.addTab("Surgeries", createSurgeriesTab.createSurgeriesTab(id, role));

        // Create a new instance of the createChronicTab class
        createChronicTab createChronicTab = new createChronicTab();
        medicalHistoryTabs.addTab("Chronic Conditions", createChronicTab.createChronicTab(id, role));

        // Create a new instance of the createFamilyTab class
        createFamilyTab createFamilyTab = new createFamilyTab();
        medicalHistoryTabs.addTab("Family History", createFamilyTab.createFamilyTab(id, role));

        // Create a new instance of the createVaccinationTab class
        createVaccinationTab createVaccinationTab = new createVaccinationTab();
        medicalHistoryTabs.addTab("Vaccination History", createVaccinationTab.createVaccinationTab(id, role));

        // Create a new instance of the createLifestyleTab class
        createLifestyleTab createLifestyleTab = new createLifestyleTab();
        medicalHistoryTabs.addTab("Lifestyle Factors", createLifestyleTab.createLifestyleTab(id, role));

        // Create a new instance of the createSexualTab class
        createSexualTab createSexualTab = new createSexualTab();
        medicalHistoryTabs.addTab("Sexual History", createSexualTab.createSexualTab(id, role));

        // Create a new instance of the createMentalTab class
        createMentalTab createMentalTab = new createMentalTab();
        medicalHistoryTabs.addTab("Mental Health History", createMentalTab.createMentalTab(id, role));

        // Create a new instance of the createLifestyleTab class
        createMilestonesTab createMilestonesTab = new createMilestonesTab();
        medicalHistoryTabs.addTab("Developmental Milestones", createMilestonesTab.createMilestonesTab(id, role));

        medicalHistoryTabs.addChangeListener((ChangeEvent e) -> {
            int index = medicalHistoryTabs.getSelectedIndex();
            if (index == 0) { // Summary tab
                medicalHistoryTabs.setSelectedIndex(0); // Navigate to the summary subtab
            } else if (index == 1) { // Allergies tab
                medicalHistoryTabs.setSelectedIndex(1); // Navigate to the allergies subtab
            } else if (index == 2) { // Medications tab
                medicalHistoryTabs.setSelectedIndex(2); // Navigate to the medications subtab
            } else if (index == 3) { // Conditions tab
                medicalHistoryTabs.setSelectedIndex(3); // Navigate to the Conditions subtab
            } else if (index == 4) { // Surgeries tab
                medicalHistoryTabs.setSelectedIndex(4); // Navigate to the Surgeries subtab
            } else if (index == 5) { // Chronic Conditions tab
                medicalHistoryTabs.setSelectedIndex(5); // Navigate to the Chronic Conditions subtab
            } else if (index == 6) { // Family Medical History tab
                medicalHistoryTabs.setSelectedIndex(6); // Navigate to the Family Medical History subtab
            } else if (index == 7) { // Vaccination History tab
                medicalHistoryTabs.setSelectedIndex(7); // Navigate to the Vaccination History subtab
            } else if (index == 8) { // Lifestyle Factors History tab
                medicalHistoryTabs.setSelectedIndex(8); // Navigate to the Lifestyle Factors History subtab
            } else if (index == 9) { // Sexual History tab
                medicalHistoryTabs.setSelectedIndex(9); // Navigate to the Sexual History subtab
            } else if (index == 10) { // Mental Health History tab
                medicalHistoryTabs.setSelectedIndex(10); // Navigate to the Mental Health History subtab
            } else if (index == 11) { // Developmental Milestones tab
                medicalHistoryTabs.setSelectedIndex(11); // Navigate to the Developmental Milestones subtab
            }
        });

        JPanel medicalHistoryPanel = new JPanel();
        medicalHistoryPanel.setLayout(new BorderLayout());
        medicalHistoryPanel.add(medicalHistoryTabs, BorderLayout.CENTER);
        GridLayout layout = new GridLayout(1, 1, 10, 55);
        medicalHistoryPanel.setLayout(layout);

        return medicalHistoryPanel;
    }
}
