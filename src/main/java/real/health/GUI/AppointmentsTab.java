package real.health.GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class AppointmentsTab {
    public JComponent createAppointmentsTab() {
        JPanel appointmentsPanel = new JPanel(new BorderLayout());
        JTable appointmentsTable = new JTable();

        // TODO: Populate the table with data from your data model
        // You'll need to write code to load the data from a database or other source
        // into the table's data model

        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        appointmentsPanel.add(scrollPane, BorderLayout.CENTER);

        // Hardcoded yearly checkup appointment
        Object[][] data = {
                { "January 1st, 2023", "12:00 PM", "Yearly Checkup" }
        };

        String[] columnNames = { "Appointment Date", "Appointment Time", "Appointment Type" };
        appointmentsTable.setModel(new DefaultTableModel(data, columnNames));
        appointmentsTable.setEnabled(false);

        return appointmentsPanel;
    }
}
