package real.health.PatientLogin;

import javax.swing.*;

import real.health.GUI.*;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import java.io.IOException;

public class patientInformationSystem {
    public static Component patientInformationSystem(String id, JProgressBar progressBar, UserRole role)
            throws ClassNotFoundException, IOException {
        JTabbedPane tabs;
        JFrame patient = new JFrame("Patient Information System");
        patient.setSize(1000, 600);
        patient.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        patient.setLayout(new BorderLayout());
        patient.setLocationRelativeTo(null);
        tabs = new JTabbedPane();
        patient.add(tabs, BorderLayout.CENTER);

        try {
            PatientInformation PatientInformation = new PatientInformation();
            tabs.addTab("Patient Information", PatientInformation.createPatientInformationTab(id, role));
        } catch (Exception e) {
            tabs.addTab("Patient Information", createErrorTab());
        }
        progressBar.setValue(progressBar.getValue() + 5);

        try {
            InsuranceTab InsuranceTab = new InsuranceTab();
            tabs.addTab("Insurance", InsuranceTab.createInsuranceTab(id, role));
        } catch (Exception e) {
            tabs.addTab("Insurance", createErrorTab());
        }
        progressBar.setValue(progressBar.getValue() + 5);

        try {
            MedicalHistoryTab medicalHistoryTab = new MedicalHistoryTab();
            tabs.addTab("Medical History", medicalHistoryTab.createMedicalHistoryTab(id, role));
        } catch (Exception e) {
            tabs.addTab("Medical History", createErrorTab());
        }
        progressBar.setValue(progressBar.getValue() + 5);

        try {
            VitalsTab VitalsTab = new VitalsTab(role);
            tabs.addTab("Vital Signs", VitalsTab.createVitalSignsTab(id));
        } catch (Exception e) {
            tabs.addTab("Vital Signs", createErrorTab());
        }
        progressBar.setValue(progressBar.getValue() + 5);

        try {
            LabResultsTab LabResultsTab = new LabResultsTab(role);
            tabs.addTab("Lab Results", LabResultsTab.createLabResultsTab(id));
        } catch (Exception e) {
            tabs.addTab("Lab Results", createErrorTab());
        }
        progressBar.setValue(progressBar.getValue() + 5);

        try {
            AppointmentsTab AppointmentsTab = new AppointmentsTab();
            tabs.addTab("Appointments", AppointmentsTab.createAppointmentsTab(id, role));
        } catch (Exception e) {
            tabs.addTab("Appointments", createErrorTab());
        }
        progressBar.setValue(progressBar.getValue() + 5);

        try {
            NotesTab NotesTab = new NotesTab();
            tabs.addTab("Notes and Progress", NotesTab.createNotesTab(id));
        } catch (Exception e) {
            tabs.addTab("Notes and Progress", createErrorTab());
        }

        patient.setVisible(false);
        return patient;
    }

    private static JPanel createErrorTab() {
        JPanel errorPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;

        JLabel errorMessage = new JLabel("Error: No information found.");
        errorMessage.setFont(new Font(errorMessage.getFont().getName(), Font.PLAIN, 18));
        errorPanel.add(errorMessage, constraints);
        return errorPanel;
    }
}
