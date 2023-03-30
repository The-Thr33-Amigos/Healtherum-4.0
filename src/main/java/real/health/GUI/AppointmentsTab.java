package real.health.GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.*;
import java.util.*;
import java.beans.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import com.toedter.calendar.*;

import real.health.SQL.HealthConn;

import org.jdatepicker.impl.*;
import org.jdatepicker.util.*;
import org.jdatepicker.*;

public class AppointmentsTab {
    public JComponent createAppointmentsTab(String id) {
        JPanel appointmentsPanel = new JPanel(new BorderLayout());
        JTable appointmentsTable = new JTable();

        // Populate the table with existing data from SQL database
        try {
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();

            String sql = "SELECT appointment_date, appointment_time, appointment_type FROM appointments WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            DefaultTableModel model = new DefaultTableModel(
                    new Object[][] {},
                    new String[] { "Appointment Date", "Appointment Time", "Appointment Type" });

            while (resultSet.next()) {
                model.addRow(new Object[] { resultSet.getString("appointment_date"),
                        resultSet.getString("appointment_time"), resultSet.getString("appointment_type") });
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
                addAppointment(appointmentsTable, id);
            }
        });

        // Button panel
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(addAppointmentButton, BorderLayout.WEST);
        appointmentsPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Hardcoded yearly checkup appointment
        Object[][] data = {
                { "2023-01-01", "12:00 PM", "Yearly Checkup" }
        };
        String[] columnNames = { "Appointment Date", "Appointment Time", "Appointment Type" };
        appointmentsTable.setModel(new DefaultTableModel(data, columnNames));
        appointmentsTable.setEnabled(false);

        // Create a JCalendar for date selection
        JCalendar calendar = new JCalendar();

        return appointmentsPanel;
    }

    /**
     * Add a new appointment to the appointments table.
     * Check if there is no existing appointment for the same date and time.
     */

    private void addAppointment(JTable appointmentsTable, String id) {
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

        JButton saveButton = new JButton("Save");
        constraints.gridx = 2;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        layout.setConstraints(saveButton, constraints);
        addAppointmentFrame.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String[] newAppointment = { dateField.getText(), (String) timeComboBox.getSelectedItem(),
                        (String) typeCombo.getSelectedItem() };

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

                        String sql = "INSERT INTO appointments (id, appointment_date, appointment_time, appointment_type) VALUES (?, ?, ?, ?)";
                        PreparedStatement statement = con.prepareStatement(sql);
                        statement.setString(1, id);
                        statement.setString(2, newAppointment[0]);
                        statement.setString(3, newAppointment[1]);
                        statement.setString(4, newAppointment[2]);

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
        addAppointmentFrame.add(cancelButton);

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addAppointmentFrame.dispose();
            }
        });

        addAppointmentFrame.setVisible(true);

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