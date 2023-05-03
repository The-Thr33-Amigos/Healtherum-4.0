
package real.health.ProviderLogin.providerSystemFiles;

import javax.swing.*;
import javax.swing.table.*;

import real.health.PatientLogin.createAccountPanel.TimeSlotRenderer;
import real.health.ProviderLogin.DateLabelFormatter;
import real.health.ProviderLogin.providerSystem;
import real.health.ProviderLogin.providerSystem.Appointment;
import real.health.ProviderLogin.providerSystem.User;
import real.health.SQL.HealthConn;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;
import org.jdatepicker.impl.*;

import com.toedter.calendar.JCalendar;

public class PendingAppointmentsFrame extends JFrame {
    private JTable pendingAppointmentsTable;
    private List<Appointment> pendingAppointments;
    private String providerName;

    public class TimeSlotRenderer extends DefaultListCellRenderer {
        private JTable appointmentsTable;
    
        public TimeSlotRenderer(JTable appointmentsTable) {
            this.appointmentsTable = appointmentsTable;
        }
    
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            // Add any custom rendering logic here
    
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }

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
        List<Appointment> allAppointments = providerSystem.getAppointments(id, providerName, "PENDING");
        try {
            pendingAppointments = filterPendingAppointments(allAppointments);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (Appointment appointment : pendingAppointments) {
            User patient = providerSystem.getUserById(appointment.getPatientId());
            String patientName = patient.getFirstName() + " " + patient.getLastName();
            tableModel.addRow(
                    new Object[] { appointment.getTime(), patientName, appointment.getDate(), appointment.getType(),
                            appointment.getStatus() });
        }

        pendingAppointmentsTable = new JTable(tableModel);
        JScrollPane appointmentsScrollPane = new JScrollPane(pendingAppointmentsTable);
        appointmentsScrollPane.setPreferredSize(new Dimension(600, 300));
        panel.add(appointmentsScrollPane);

        // Add action buttons for Accept, Decline, and Reschedule
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton acceptButton = new JButton("Accept");
        JButton declineButton = new JButton("Decline");
        JButton rescheduleButton = new JButton("Reschedule");

        buttonsPanel.add(acceptButton);
        buttonsPanel.add(declineButton);
        buttonsPanel.add(rescheduleButton);

        panel.add(buttonsPanel);

        add(panel, BorderLayout.CENTER);

        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = pendingAppointmentsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String patientId = pendingAppointments.get(selectedRow).getPatientId();
                    String appointmentDate = (String) pendingAppointmentsTable.getValueAt(selectedRow, 2);
                    String appointmentTime = (String) pendingAppointmentsTable.getValueAt(selectedRow, 0);
                    updateAppointmentStatus(patientId, "ACCEPTED", appointmentDate, appointmentTime);
                    ((DefaultTableModel) pendingAppointmentsTable.getModel()).removeRow(selectedRow);
                }
            }
        });

        declineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = pendingAppointmentsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String patientId = pendingAppointments.get(selectedRow).getPatientId();
                    String appointmentDate = (String) pendingAppointmentsTable.getValueAt(selectedRow, 1);
                    String appointmentTime = (String) pendingAppointmentsTable.getValueAt(selectedRow, 0);
                    updateAppointmentStatus(patientId, "DECLINED", appointmentDate, appointmentTime);
                    ((DefaultTableModel) pendingAppointmentsTable.getModel()).removeRow(selectedRow);
                }
            }
        });


        rescheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = pendingAppointmentsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String patientId = pendingAppointments.get(selectedRow).getPatientId();
                    rescheduleAppointment(pendingAppointmentsTable, patientId);
                }
            }
        });
        
    }
    
    // Add reschedule functionality
    private void rescheduleAppointment(JTable appointmentsTable, String id) {
        int selectedRow = appointmentsTable.getSelectedRow();
        if (selectedRow >= 0) {
            String appointmentDate = (String) appointmentsTable.getValueAt(selectedRow, 0);
            String appointmentTime = (String) appointmentsTable.getValueAt(selectedRow, 1);

            JFrame rescheduleFrame = new JFrame("Reschedule Appointment");
            rescheduleFrame.setSize(400, 200);
            rescheduleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            rescheduleFrame.setLocationRelativeTo(null);

            GridBagLayout layout = new GridBagLayout();
            rescheduleFrame.setLayout(layout);
            GridBagConstraints constraints = new GridBagConstraints();

            constraints.fill = GridBagConstraints.HORIZONTAL;

            JLabel newDateLabel = new JLabel("New Appointment Date:");
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = 1;
            layout.setConstraints(newDateLabel, constraints);
            rescheduleFrame.add(newDateLabel);

            JTextField newDateField = new JTextField();
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.gridwidth = 2;
            layout.setConstraints(newDateField, constraints);
            rescheduleFrame.add(newDateField);

            JButton newDatePickerButton = new JButton("Select New Date");
            constraints.gridx = 3;
            constraints.gridy = 0;
            constraints.gridwidth = 1;
            layout.setConstraints(newDatePickerButton, constraints);
            rescheduleFrame.add(newDatePickerButton);

            // Create a JCalendar for date selection
            JCalendar calendar = new JCalendar();
            UtilDateModel model = new UtilDateModel();
            Properties properties = new Properties();
            JDatePickerImpl datePicker = new JDatePickerImpl(new JDatePanelImpl(model, properties),
                    new DateLabelFormatter());

            newDatePickerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int result = JOptionPane.showConfirmDialog(null, datePicker, "Select New Date",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        Date selectedDate = (Date) datePicker.getModel().getValue();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(selectedDate);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String formattedDate = dateFormat.format(calendar.getTime());
                        newDateField.setText(formattedDate);
                    }
                }
            });

            JLabel newTimeLabel = new JLabel("New Appointment Time:");
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.gridwidth = 1;
            layout.setConstraints(newTimeLabel, constraints);
            rescheduleFrame.add(newTimeLabel);

            String[] timeSlots = { "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00" };
            JComboBox<String> newTimeComboBox = new JComboBox<>(timeSlots);
            newTimeComboBox.setRenderer(new TimeSlotRenderer(appointmentsTable));
            constraints.gridx = 1;
            constraints.gridy = 1;
            constraints.gridwidth = 3;
            layout.setConstraints(newTimeComboBox, constraints);
            rescheduleFrame.add(newTimeComboBox);
            JButton saveButton = new JButton("Save");
            constraints.gridx = 2;
            constraints.gridy = 2;
            constraints.gridwidth = 1;
            layout.setConstraints(saveButton, constraints);
            rescheduleFrame.add(saveButton);
    
            JButton cancelButton = new JButton("Cancel");
            constraints.gridx = 1;
            constraints.gridy = 2;
            constraints.gridwidth = 1;
            layout.setConstraints(cancelButton, constraints);
            rescheduleFrame.add(cancelButton);
    
            saveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Update the appointment date and time in the database
                    try {
                        HealthConn newConnection = new HealthConn();
                        Connection con = newConnection.connect();
    
                        String sql = "UPDATE appointments SET appointment_date = ?, appointment_time = ? WHERE id = ? AND appointment_date = ? AND appointment_time = ?";
                        PreparedStatement statement = con.prepareStatement(sql);
                        statement.setString(1, newDateField.getText());
                        statement.setString(2, (String) newTimeComboBox.getSelectedItem());
                        statement.setString(3, id);
                        statement.setString(4, appointmentDate);
                        statement.setString(5, appointmentTime);
                        int rowsUpdated = statement.executeUpdate();
                        if (rowsUpdated > 0) {
                            System.out.println("Appointment rescheduled successfully!");
                            appointmentsTable.setValueAt(newDateField.getText(), selectedRow, 0);
                            appointmentsTable.setValueAt((String) newTimeComboBox.getSelectedItem(), selectedRow, 1);
                        }
                        ((DefaultTableModel) pendingAppointmentsTable.getModel()).removeRow(selectedRow);
                        statement.close();
                        con.close();
    
                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println("Error: unable to connect to MySQL database");
                        ex.printStackTrace();
                    }
                    rescheduleFrame.dispose();
                }
            });
    
            cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    rescheduleFrame.dispose();
                }
            });

            rescheduleFrame.setVisible(true);
        }
    }
    
    private void updateAppointmentStatus(String patientId, String status, String date, String time) {
        try {
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();
    
            String sql = "UPDATE appointments SET status = ? WHERE id = ? AND appointment_date = ? AND appointment_time = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            System.out.println("Status: " + status);
            System.out.println("Date: " + date);
            System.out.println("Time: " + time);
            statement.setString(1, status);
            statement.setString(2, patientId);
            statement.setString(3, date);
            statement.setString(4, time);
            int rowsUpdated = statement.executeUpdate();
            
            if (rowsUpdated > 0) {
                System.out.println("Appointment status updated successfully!");
            }
    
            statement.close();
            con.close();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error: unable to connect to MySQL database");
            ex.printStackTrace();
        }
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