package real.health.GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;
import java.beans.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import com.toedter.calendar.*;

import real.health.SQL.*;

import org.jdatepicker.impl.*;
import org.jdatepicker.util.*;
import org.jdatepicker.*;

public class AppointmentsTab {
    public JComponent createAppointmentsTab(String id) {
        JPanel appointmentsPanel = new JPanel(new BorderLayout());
        JTable appointmentsTable = new JTable();

        // TODO: Populate the table with existing data from SQL database

        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        appointmentsPanel.add(scrollPane, BorderLayout.CENTER);

        // Add appointment button
        JButton addAppointmentButton = new JButton("Add Appointment");
        addAppointmentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addAppointment(appointmentsTable);
            }
        });

        // Button panel
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(addAppointmentButton, BorderLayout.WEST);
        appointmentsPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Populate the table with the patient's family history
        try {
            // Load the MySQL JDBC driver
            // Create a connection to the database
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();

            // Create a SQL statement to retrieve the patient's family history
            String sql = "SELECT adate, atime, atype FROM sched WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();

            // Create a table model and populate it with the retrieved data
            DefaultTableModel tableModel = new DefaultTableModel(new Object[] {  "Appointment Date", "Appointment Time", "Appointment Type" }, 0);
            while (result.next()) {
                tableModel.addRow(new Object[] { result.getString(1), result.getString(2), result.getString(3) });
            }
            appointmentsTable.setModel(tableModel);

            // Clean up resources
            result.close();
            statement.close();
            con.close();

        } catch (ClassNotFoundException ex) {
            System.out.println("Error: unable to load MySQL JDBC driver");
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.out.println("Error: unable to connect to MySQL database");
            ex.printStackTrace();
        }

        return appointmentsPanel;
    }

    /**
     * Add a new appointment to the appointments table.
     * Check if there is no existing appointment for the same date and time.
     */

    private void addAppointment(JTable appointmentsTable) {
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

        datePickerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UtilDateModel model = new UtilDateModel();
                Properties properties = new Properties();
                JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
                JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

                int result = JOptionPane.showConfirmDialog(addAppointmentFrame, datePicker, "Select Date",
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
                    try {

                        String appointmentTime = newAppointment[1];
                        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                        Date date = timeFormat.parse(newAppointment[1]);
                        String formattedTime = outputFormat.format(date);
                        // Insert the new appointment into the SQL database
                        // Load the MySQL JDBC driver
                        HealthConn newConnection = new HealthConn();
                        Connection conn = newConnection.connect();
                        // Create a SQL statement to insert the new family member into the database
                        String sql = "INSERT INTO sched (adate, atime, atype) VALUES (?, ?, ?)";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setString(1, newAppointment[0]);
                        stmt.setString(2, formattedTime);
                        stmt.setString(3, newAppointment[2]);
                        stmt.executeUpdate();
                        conn.close();

                        // Add the new appointment to the table
                        DefaultTableModel model = (DefaultTableModel) appointmentsTable.getModel();
                        model.addRow(newAppointment);
                        addAppointmentFrame.dispose();
                    } catch (SQLException | ClassNotFoundException | ParseException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(addAppointmentFrame,
                                "Failed to save the appointment. Please try again later.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
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