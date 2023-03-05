package real.health.PatientLogin;

import real.health.Patient.*;
import real.health.GUI.*;
import real.health.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import real.health.Patient.*;
import real.health.PatientLogin.*;
import real.health.SQL.*;
import real.health.GUI.*;
import java.sql.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import javax.swing.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;

public class patientInformationSystem {
    private static JTabbedPane tabs;

    static void patientInformationSystem(String id) throws ClassNotFoundException {
        JFrame patient = new JFrame("Patient Information System");
        patient.setSize(1000, 600);
        patient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        patient.setLayout(new BorderLayout());
        patient.setLocationRelativeTo(null);
        tabs = new JTabbedPane();
        patient.add(tabs, BorderLayout.CENTER);

        // Create a new instance of the PatientInformation class
        PatientInformation PatientInformation = new PatientInformation();
        tabs.addTab("Patient Information", PatientInformation.createPatientInformationTab(id));

        // Create a new instance of the MedicalHistoryTab class
        MedicalHistoryTab medicalHistoryTab = new MedicalHistoryTab();
        tabs.addTab("Medical History", medicalHistoryTab.createMedicalHistoryTab(id));

        // Create a new instance of the VitalsTab class
        VitalsTab VitalsTab = new VitalsTab();
        tabs.addTab("Vital Signs", VitalsTab.createVitalSignsTab(id));

        // Create a new instance of the LabResultsTab class
        LabResultsTab LabResultsTab = new LabResultsTab();
        tabs.addTab("Lab Results", LabResultsTab.createLabResultsTab(id));

        // Create a new instance of the AppointmentsTab class
        AppointmentsTab AppointmentsTab = new AppointmentsTab();
        tabs.addTab("Appointments", AppointmentsTab.createAppointmentsTab());

        // Create a new instance of the NotesTab class
        NotesTab NotesTab = new NotesTab();
        tabs.addTab("Notes and Progress", NotesTab.createNotesAndProgressTab());

        patient.setVisible(true);

    }
}
