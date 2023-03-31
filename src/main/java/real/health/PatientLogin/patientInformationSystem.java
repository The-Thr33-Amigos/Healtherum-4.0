package real.health.PatientLogin;

import real.health.GUI.*;
import real.health.PatientLogin.*;
import javax.swing.*;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import java.io.IOException;

public class patientInformationSystem {
    static Component patientInformationSystem(String id, JProgressBar progressBar) throws ClassNotFoundException, IOException {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Failed");
        }
        JTabbedPane tabs;
        JFrame patient = new JFrame("Patient Information System");
        patient.setSize(1000, 600);
        patient.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        patient.setLayout(new BorderLayout());
        patient.setLocationRelativeTo(null);
        tabs = new JTabbedPane();
        patient.add(tabs, BorderLayout.CENTER);

        // Create a new instance of the PatientInformation class
        PatientInformation PatientInformation = new PatientInformation();
        tabs.addTab("Patient Information", PatientInformation.createPatientInformationTab(id));
        progressBar.setValue(progressBar.getValue() + 5);

        // Create a new instance of the MedicalHistoryTab class
        InsuranceTab InsuranceTab = new InsuranceTab();
        tabs.addTab("Insurance", InsuranceTab.createInsuranceTab(id));
        progressBar.setValue(progressBar.getValue() + 5);

        // Create a new instance of the MedicalHistoryTab class
        MedicalHistoryTab medicalHistoryTab = new MedicalHistoryTab();
        tabs.addTab("Medical History", medicalHistoryTab.createMedicalHistoryTab(id));
        progressBar.setValue(progressBar.getValue() + 5);

        // Create a new instance of the VitalsTab class
        VitalsTab VitalsTab = new VitalsTab();
        tabs.addTab("Vital Signs", VitalsTab.createVitalSignsTab(id));
        progressBar.setValue(progressBar.getValue() + 5);

        // Create a new instance of the LabResultsTab class
        // LabResultsTab LabResultsTab = new LabResultsTab();
        // tabs.addTab("Lab Results", LabResultsTab.createLabResultsTab(id));
        // progressBar.setValue(progressBar.getValue() + 5);

        // Create a new instance of the AppointmentsTab class
        AppointmentsTab AppointmentsTab = new AppointmentsTab();
        tabs.addTab("Appointments", AppointmentsTab.createAppointmentsTab(id));
        progressBar.setValue(progressBar.getValue() + 5);

        // Create a new instance of the NotesTab class
        NotesTab NotesTab = new NotesTab();
        tabs.addTab("Notes and Progress", NotesTab.createNotesAndProgressTab());

        patient.setVisible(false);
        return patient;
    }
}