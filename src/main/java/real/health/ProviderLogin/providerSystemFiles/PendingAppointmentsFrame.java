package real.health.ProviderLogin.providerSystemFiles;

import javax.swing.*;
import javax.swing.table.*;

import real.health.ProviderLogin.providerSystem;
import real.health.ProviderLogin.providerSystem.Appointment;
import real.health.ProviderLogin.providerSystem.User;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;

public class PendingAppointmentsFrame extends JFrame {
    private JTable pendingAppointmentsTable;
    private String providerName;

    public PendingAppointmentsFrame(String id, String providerName) {
        this.providerName = providerName;
        setTitle("Pending Appointments");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        createPendingAppointmentsTable(id);
    }

    private void createPendingAppointmentsTable(String id) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel pendingAppointmentsLabel = new JLabel("Pending Appointments");
        pendingAppointmentsLabel.setFont(new Font("Serif", Font.BOLD, 18));
        panel.add(pendingAppointmentsLabel);

        // Create a table model for the pending appointments list
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableModel.addColumn("Time");
        tableModel.addColumn("Patient");
        tableModel.addColumn("Date");
        tableModel.addColumn("Reason");
        tableModel.addColumn("Status");

        // Fetches pending appointments from the database
        List<Appointment> allAppointments = providerSystem.getAppointments(id, providerName);
        List<Appointment> pendingAppointments = new ArrayList<>();
        try {
            pendingAppointments = filterPendingAppointments(allAppointments);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (Appointment appointment : pendingAppointments) {
            User patient = providerSystem.getUserById(appointment.getPatientId());
            String patientName = patient.getFirstName() + " " + patient.getLastName();
            tableModel.addRow(new Object[] { appointment.getTime(), appointment.getDate(), patientName, appointment.getType(),
                    appointment.getStatus() });
        }

        pendingAppointmentsTable = new JTable(tableModel);
        JScrollPane appointmentsScrollPane = new JScrollPane(pendingAppointmentsTable);
        appointmentsScrollPane.setPreferredSize(new Dimension(600, 300));
        panel.add(appointmentsScrollPane);

        // Add action buttons for Accept, Decline, and Reschedule
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton acceptButton = new JButton("Accept");
        JButton declineButton = new JButton("Decline");
        JButton rescheduleButton = new JButton("Reschedule");

        buttonsPanel.add(acceptButton);
        buttonsPanel.add(declineButton);
        buttonsPanel.add(rescheduleButton);

        panel.add(buttonsPanel);

        add(panel, BorderLayout.CENTER);

        // Add action listeners for buttons
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the accept logic here
            }
        });

        declineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the decline logic here
            }
        });

        rescheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the reschedule logic here
            }
        });
    }

    // Add getAppointments, getUserById, and filterPendingAppointments methods here.
    // You can use the same code as you have in the main class for getAppointments
    // and getUserById.
    // For filterPendingAppointments, you can create a new method that filters out
    // appointments based on their status:
    private List<Appointment> filterPendingAppointments(List<Appointment> allAppointments) throws ParseException {
        List<Appointment> filteredAppointments = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        String currentDateString = sdf.format(currentDate);

        System.out.println("Current date: " + currentDateString);
        System.out.println("All appointments: " + allAppointments);

        for (Appointment appointment : allAppointments) {
            Date appointmentDate = sdf.parse(appointment.getDate());
            if (appointment.getStatus().equalsIgnoreCase("PENDING") && 
                appointmentDate.compareTo(sdf.parse(currentDateString)) >= 0) {
                filteredAppointments.add(appointment);
            }
        }
    
        return filteredAppointments;
    }

}