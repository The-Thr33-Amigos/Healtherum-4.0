package real.health.PatientLogin;

import real.health.GUI.*;
import real.health.Patient.Notes;
import real.health.PatientLogin.*;
import real.health.ProviderLogin.providerHomeScreen;

import javax.swing.*;
import real.health.GUI.UserRole;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import java.io.IOException;

public class patientInformationSystem {
    public static Component patientInformationSystem(String id, JProgressBar progressBar, UserRole role) throws ClassNotFoundException, IOException {
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
        tabs.addTab("Patient Information", PatientInformation.createPatientInformationTab(id, role));
        progressBar.setValue(progressBar.getValue() + 5);

        // Create a new instance of the MedicalHistoryTab class
        InsuranceTab InsuranceTab = new InsuranceTab();
        tabs.addTab("Insurance", InsuranceTab.createInsuranceTab(id));
        progressBar.setValue(progressBar.getValue() + 5);

        // Create a new instance of the MedicalHistoryTab class
        MedicalHistoryTab medicalHistoryTab = new MedicalHistoryTab();
        tabs.addTab("Medical History", medicalHistoryTab.createMedicalHistoryTab(id, role));
        progressBar.setValue(progressBar.getValue() + 5);

        // Create a new instance of the VitalsTab class
        VitalsTab VitalsTab = new VitalsTab(role);
        tabs.addTab("Vital Signs", VitalsTab.createVitalSignsTab(id));
        progressBar.setValue(progressBar.getValue() + 5);

        // Create a new instance of the LabResultsTab class
        LabResultsTab LabResultsTab = new LabResultsTab(role);
        tabs.addTab("Lab Results", LabResultsTab.createLabResultsTab(id));
        progressBar.setValue(progressBar.getValue() + 5);

        // Create a new instance of the AppointmentsTab class
        AppointmentsTab AppointmentsTab = new AppointmentsTab();
        tabs.addTab("Appointments", AppointmentsTab.createAppointmentsTab(id));
        progressBar.setValue(progressBar.getValue() + 5);

        // Create a new instance of the NotesTab class
        NotesTab NotesTab = new NotesTab();
        tabs.addTab("Notes and Progress", NotesTab.createNotesTab(id));

        patient.setVisible(false);
        return patient;
    }
}