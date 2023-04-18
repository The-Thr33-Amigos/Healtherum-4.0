package real.health.GUI;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;
import com.toedter.calendar.*;

import real.health.SQL.HealthConn;

import org.jdatepicker.impl.*;
import org.jdatepicker.util.*;
import org.jdatepicker.*;

public class AppointmentsTab {
    public JComponent createAppointmentsTab(String id, UserRole user) {
        JPanel appointmentsPanel = new JPanel(new BorderLayout());
        JTable appointmentsTable = new JTable();

        // Populate the table with existing data from SQL database
        try {
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();

            String sql = "SELECT appointment_date, appointment_time, appointment_type, provider, status FROM appointments WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            String[] columnNames = { "Appointment Date", "Appointment Time", "Appointment Type", "Provider", "Status" };

            DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            while (resultSet.next()) {
                model.addRow(new Object[] { resultSet.getString("appointment_date"),
                        resultSet.getString("appointment_time"), resultSet.getString("appointment_type"),
                        resultSet.getString("provider"), resultSet.getString("status") });
            }

            appointmentsTable.setModel(model);

            statement.close();
            con.close();

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error: unable to connect to MySQL database");
            ex.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        appointmentsPanel.add(scrollPane, BorderLayout.CENTER);

        // Add appointment button
        JButton addAppointmentButton = new JButton("Add Appointment");
        addAppointmentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<String> providerNames = getAllProviderNames();
                addAppointment(appointmentsTable, id, user);
            }
        });

        // Button panel
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(addAppointmentButton, BorderLayout.WEST);
        appointmentsPanel.add(buttonPanel, BorderLayout.SOUTH);

        JButton rescheduleButton = new JButton("Reschedule");
        rescheduleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rescheduleAppointment(appointmentsTable, id);
            }
        });

        buttonPanel.add(rescheduleButton, BorderLayout.EAST);
        if (user == UserRole.PROVIDER) {
            // Add provider buttons
            JButton acceptButton = new JButton("Accept");
            JButton declineButton = new JButton("Decline");

            // Add action listeners for buttons
            acceptButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    updateAppointmentStatus(appointmentsTable, id, "ACCEPTED");
                }
            });

            declineButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    updateAppointmentStatus(appointmentsTable, id, "DECLINED");
                }
            });

            buttonPanel.add(acceptButton, BorderLayout.CENTER);
            buttonPanel.add(declineButton, BorderLayout.WEST);

        } else {
            // Add appointment button for patients and doctors
            buttonPanel.add(addAppointmentButton, BorderLayout.WEST);
        }

        // Create a JCalendar for date selection
        JCalendar calendar = new JCalendar();

        return appointmentsPanel;
    }

    private List<String> getAllProviderNames() {
        List<String> providerNames = new ArrayList<>();

        try {
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();

            String sql = "SELECT name FROM provider";
            PreparedStatement statement = con.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                providerNames.add(resultSet.getString("name"));
            }

            resultSet.close();
            statement.close();
            con.close();

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error: unable to connect to MySQL database");
            ex.printStackTrace();
        }

        return providerNames;
    }

    /**
     * Add a new appointment to the appointments table.
     * Check if there is no existing appointment for the same date and time.
     *
     * @param user
     */

    private void addAppointment(JTable appointmentsTable, String id, UserRole user) {
        JFrame addAppointmentFrame = new JFrame("Add Appointment");
        addAppointmentFrame.setSize(400, 200);
        addAppointmentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addAppointmentFrame.setLocationRelativeTo(null);

        GridBagLayout layout = new GridBagLayout();
        addAppointmentFrame.setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.HORIZONTAL;

        JLabel dateLabel = new JLabel("Appointment Date:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        layout.setConstraints(dateLabel, constraints);
        addAppointmentFrame.add(dateLabel);

        JTextField dateField = new JTextField();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        layout.setConstraints(dateField, constraints);
        addAppointmentFrame.add(dateField);

        JButton datePickerButton = new JButton("Select Date");
        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        layout.setConstraints(datePickerButton, constraints);
        addAppointmentFrame.add(datePickerButton);

        // Create a JCalendar for date selection
        JCalendar calendar = new JCalendar();
        UtilDateModel model = new UtilDateModel();
        Properties properties = new Properties();
        JDatePickerImpl datePicker = new JDatePickerImpl(new JDatePanelImpl(model, properties),
                new DateLabelFormatter());

        datePickerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, datePicker, "Select Date",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    Date selectedDate = (Date) datePicker.getModel().getValue();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(selectedDate);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = dateFormat.format(calendar.getTime());
                    dateField.setText(formattedDate);
                }
            }

        });

        JLabel timeLabel = new JLabel("Appointment Time:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        layout.setConstraints(timeLabel, constraints);
        addAppointmentFrame.add(timeLabel);

        String[] timeSlots = { "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00" };
        JComboBox<String> timeComboBox = new JComboBox<>(timeSlots);
        timeComboBox.setRenderer(new TimeSlotRenderer(appointmentsTable));
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        layout.setConstraints(timeComboBox, constraints);
        addAppointmentFrame.add(timeComboBox);

        String[] typeList = { "Annual Checkup", "Surgery", "Illness", "Follow-Up Visit", "Patient Evaluation" };
        JLabel typeLabel = new JLabel("Appointment Type:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        layout.setConstraints(typeLabel, constraints);
        addAppointmentFrame.add(typeLabel);

        JComboBox<String> typeCombo = new JComboBox<String>(typeList);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 3;
        layout.setConstraints(typeCombo, constraints);
        addAppointmentFrame.add(typeCombo);

        // Add Provider label and provider JComboBox
        JLabel providerLabel = new JLabel("Provider:");
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        layout.setConstraints(providerLabel, constraints);
        addAppointmentFrame.add(providerLabel);

        List<String> providerNames = getAllProviderNames();
        JComboBox<String> providerComboBox = new JComboBox<>(providerNames.toArray(new String[0]));
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 3;
        layout.setConstraints(providerComboBox, constraints);
        addAppointmentFrame.add(providerComboBox);

        // TODO:q
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton saveButton = new JButton("Save");
        constraints.gridx = 2;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        layout.setConstraints(saveButton, constraints);
        buttonPanel.add(saveButton);

        if (user == UserRole.PATIENT) {
            saveButton.setText("Submit");
        }
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String[] newAppointment = { dateField.getText(), (String) timeComboBox.getSelectedItem(),
                        (String) typeCombo.getSelectedItem(), (String) providerComboBox.getSelectedItem() };

                // Check if there is no existing appointment for the same date and time
                boolean isDuplicate = false;
                for (int i = 0; i < appointmentsTable.getRowCount(); i++) {
                    String date = (String) appointmentsTable.getValueAt(i, 0);
                    String time = (String) appointmentsTable.getValueAt(i, 1);
                    if (date.equals(newAppointment[0]) && time.equals(newAppointment[1])) {
                        isDuplicate = true;
                        break;
                    }
                }
                if (!isDuplicate) {
                    // Add the new appointment to the table
                    DefaultTableModel model = (DefaultTableModel) appointmentsTable.getModel();
                    model.addRow(newAppointment);

                    // Add the new appointment to the SQL database
                    try {
                        HealthConn newConnection = new HealthConn();
                        Connection con = newConnection.connect();

                        String sql = "INSERT INTO appointments (id, appointment_date, appointment_time, appointment_type, provider, status) VALUES (?, ?, ?, ?, ?, ?)";
                        PreparedStatement statement = con.prepareStatement(sql);
                        statement.setString(1, id);
                        statement.setString(2, newAppointment[0]);
                        statement.setString(3, newAppointment[1]);
                        statement.setString(4, newAppointment[2]);
                        statement.setString(5, newAppointment[3]);

                        if (user == UserRole.PATIENT) {
                            statement.setString(6, "PENDING");
                        } else {
                            statement.setString(6, newAppointment[4]);
                        }

                        int rowsInserted = statement.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println("A new appointment was inserted successfully!");
                        }

                        statement.close();
                        con.close();

                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println("Error: unable to connect to MySQL database");
                        ex.printStackTrace();
                    }

                    addAppointmentFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(addAppointmentFrame,
                            "There is already an appointment at the same date and time.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        constraints.gridx = 3;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        layout.setConstraints(cancelButton, constraints);
        buttonPanel.add(cancelButton);

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addAppointmentFrame.dispose();
            }
        });

        addAppointmentFrame.add(buttonPanel);

        addAppointmentFrame.setVisible(true);

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

                        statement.close();
                        con.close();

                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println("Error: unable to connect to MySQL database");
                        ex.printStackTrace();
                    }
                    rescheduleFrame.dispose();
                }
            });

            rescheduleFrame.setVisible(true);
        }
    }

    // New method to update the appointment status in the database
    private void updateAppointmentStatus(JTable appointmentsTable, String id, String newStatus) {
        int selectedRow = appointmentsTable.getSelectedRow();
        if (selectedRow >= 0) {
            String appointmentDate = (String) appointmentsTable.getValueAt(selectedRow, 0);
            String appointmentTime = (String) appointmentsTable.getValueAt(selectedRow, 1);

            try {
                HealthConn newConnection = new HealthConn();
                Connection con = newConnection.connect();

                String sql = "UPDATE appointments SET status = ? WHERE id = ? AND appointment_date = ? AND appointment_time = ?";
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setString(1, newStatus);
                statement.setString(2, id);
                statement.setString(3, appointmentDate);
                statement.setString(4, appointmentTime);

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Appointment status updated successfully!");
                    appointmentsTable.setValueAt(newStatus, selectedRow, 4);
                }

                statement.close();
                con.close();

            } catch (ClassNotFoundException | SQLException ex) {
                System.out.println("Error: unable to connect to MySQL database");
                ex.printStackTrace();
            }
        }
    }

    class TimeSlotRenderer extends DefaultListCellRenderer {
        private JTable appointmentsTable;

        public TimeSlotRenderer(JTable appointmentsTable) {
            this.appointmentsTable = appointmentsTable;
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            String selectedDate = null;
            Container parent = SwingUtilities.getAncestorOfClass(JFrame.class, list);
            if (parent != null) {
                for (Component comp : parent.getComponents()) {
                    if (comp instanceof JTextField) {
                        selectedDate = ((JTextField) comp).getText();
                        break;
                    }
                }
            }

            if (selectedDate != null && !selectedDate.isEmpty()) {
                for (int i = 0; i < appointmentsTable.getRowCount(); i++) {
                    String date = (String) appointmentsTable.getValueAt(i, 0);
                    String time = (String) appointmentsTable.getValueAt(i, 1);
                    if (date.equals(selectedDate) && time.equals(value)) {
                        c.setEnabled(false);
                        c.setBackground(Color.LIGHT_GRAY);
                        break;
                    } else {
                        c.setEnabled(true);
                        c.setBackground(Color.WHITE);
                    }
                }
            }
            return c;
        }

    }

    class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private String datePattern = "yyyy-MM-dd";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }

            return "";
        }
    }

}