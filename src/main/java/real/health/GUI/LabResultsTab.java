package real.health.GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import real.health.Patient.Patient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LabResultsTab {
    public JComponent createLabResultsTab() {
        JPanel labResultsPanel = new JPanel(new BorderLayout());

        // Create table to display lab results
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Test");
        model.addColumn("Result");
        model.addColumn("Date");
        model.addColumn("Test Interpretation");
        model.addColumn("Date of Report");
        model.addColumn("Signature");
        model.addColumn("Comments");
        JTable table = new JTable(model);
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table);
        labResultsPanel.add(scrollPane, BorderLayout.CENTER);



        // Hardcode example lab results
        model.addRow(new Object[] { "Complete Blood Count", "Normal", "02/01/2023",
                "No abnormalities detected", "02/05/2023", "Dr. Smith", "" });
        model.addRow(new Object[] { "Liver Function Test", "Elevated", "02/01/2023",
                "Further testing recommended", "02/05/2023", "Dr. Johnson", "Consult with liver specialist" });

        // Test Information
        JPanel testInformationPanel = new JPanel();
        testInformationPanel.setLayout(new BoxLayout(testInformationPanel, BoxLayout.Y_AXIS));
        JLabel testDateLabel = new JLabel("Test Date:");
        JTextField testDateField = new JTextField();
        testDateField.setEditable(false);
        JLabel testTypeLabel = new JLabel("Test Type:");
        JTextField testTypeField = new JTextField();
        testTypeField.setEditable(false);
        JLabel orderingProviderLabel = new JLabel("Ordering Provider:");
        JTextField orderingProviderField = new JTextField();
        orderingProviderField.setEditable(false);
        testInformationPanel.add(testDateLabel);
        testInformationPanel.add(testDateField);
        testInformationPanel.add(testTypeLabel);
        testInformationPanel.add(testTypeField);
        testInformationPanel.add(orderingProviderLabel);
        testInformationPanel.add(orderingProviderField);
        labResultsPanel.add(testInformationPanel, BorderLayout.WEST);

        // Download/Print Options
        JPanel downloadPrintPanel = new JPanel();
        JButton downloadButton = new JButton("Download");
        JButton printButton = new JButton("Print");
        downloadPrintPanel.add(downloadButton);
        downloadPrintPanel.add(printButton);
        labResultsPanel.add(downloadPrintPanel, BorderLayout.SOUTH);

        return labResultsPanel;
    }
}
