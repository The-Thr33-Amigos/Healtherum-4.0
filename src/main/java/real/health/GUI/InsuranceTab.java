package real.health.GUI;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InsuranceTab {
    private static Map<Integer, BufferedImage> insuranceCards = new HashMap<>();

    public static JComponent createInsuranceTab(String id) {
        // Create the main panel with BorderLayout
        JPanel panel = new JPanel(new BorderLayout());

        // Create the insurance card upload panel on the right using BorderLayout
        JPanel uploadPanel = new JPanel(new BorderLayout());
        JButton uploadButton = new JButton("Upload Insurance Card");
        uploadButton.setPreferredSize(new Dimension(200, 30));
        uploadPanel.add(uploadButton, BorderLayout.CENTER);

        // Create a separate panel for each insurance company using GridLayout
        JPanel insurancePanel = new JPanel(new GridLayout(0, 1));

        // Create a JScrollPane to hold the insurance panel
        JScrollPane scrollPane = new JScrollPane(insurancePanel);

        // Add the scroll pane and upload panel to the main panel
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(uploadPanel, BorderLayout.EAST);

        // Add the "New" button
        JButton newButton = new JButton("New");

        // Add the "Delete" button
        JButton deleteButton = new JButton("Delete");

        // Add the "Edit" button
        JButton editButton = new JButton("Edit");
        editButton.setVisible(false);

        // Add the "Submit" button
        JButton submitButton = new JButton("Submit");
        submitButton.setVisible(false);

        // Create the insurance table
        JTable table = new JTable(new Object[][] {
                { "Provider 1", "Policy Number 1", "Group Number 1", "Policy Holder 1", "DOB 1", "SSN 1", null },
                { "Provider 2", "Policy Number 2", "Group Number 2", "Policy Holder 2", "DOB 2", "SSN 2", null },
                { "Provider 3", "Policy Number 3", "Group Number 3", "Policy Holder 3", "DOB 3", "SSN 3", null }
        },
                new Object[] { "Provider Name", "Policy Number", "Group Number", "Policy Holder Name",
                        "Policy Holder DOB", "Policy Holder SSN", "Insurance Card" }) {
            // Override isCellEditable to always return false, preventing editing of the
            // table
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        // Add the table to the panel
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Add the button panel to the SOUTH position of the main panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.add(newButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(submitButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add a mouse listener to the table to handle clicks on table rows
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = table.getSelectedRow();
                    if (insuranceCards.containsKey(selectedRow)) {
                        JFrame imageFrame = new JFrame("Insurance Card");
                        imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                        JLabel imageLabel = new JLabel(new ImageIcon(insuranceCards.get(selectedRow)));
                        JScrollPane imageScrollPane = new JScrollPane(imageLabel);
                        imageFrame.add(imageScrollPane);

                        imageFrame.pack();
                        imageFrame.setVisible(true);
                    }
                }
            }
        });
        // Action listener for "New" button
        newButton.addActionListener(e -> {
            JFrame frame = new JFrame("New Insurance Entry");
            JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            JLabel providerLabel = new JLabel("Provider Name:");
            JTextField providerField = new JTextField();
            JLabel policyNumberLabel = new JLabel("Policy Number:");
            JTextField policyNumberField = new JTextField();
            JLabel groupNumberLabel = new JLabel("Group Number:");
            JTextField groupNumberField = new JTextField();
            JLabel policyHolderNameLabel = new JLabel("Policy Holder Name:");
            JTextField policyHolderNameField = new JTextField();
            JLabel policyHolderDOBLabel = new JLabel("Policy Holder DOB:");
            JTextField policyHolderDOBField = new JTextField();
            JLabel policyHolderSSNLabel = new JLabel("Policy Holder SSN:");
            JTextField policyHolderSSNField = new JTextField();

            formPanel.add(providerLabel);
            formPanel.add(providerField);
            formPanel.add(policyNumberLabel);
            formPanel.add(policyNumberField);
            formPanel.add(groupNumberLabel);
            formPanel.add(groupNumberField);
            formPanel.add(policyHolderNameLabel);
            formPanel.add(policyHolderNameField);
            formPanel.add(policyHolderDOBLabel);
            formPanel.add(policyHolderDOBField);
            formPanel.add(policyHolderSSNLabel);
            formPanel.add(policyHolderSSNField);

            // Add the "Save" button
            JButton saveButton = new JButton("Save");
            formPanel.add(saveButton);

            // Action listener for "Save" button in the new entry window
            saveButton.addActionListener(saveEvent -> {
                // Get the new insurance information
                String provider = providerField.getText();
                String policyNumber = policyNumberField.getText();
                String groupNumber = groupNumberField.getText();
                String policyHolderName = policyHolderNameField.getText();
                String policyHolderDOB = policyHolderDOBField.getText();
                String policyHolderSSN = policyHolderSSNField.getText();

                // Add the new row to the table
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.addRow(new Object[] { provider, policyNumber, groupNumber, policyHolderName, policyHolderDOB,
                        policyHolderSSN });

                // Close the new entry window
                frame.dispose();
            });

            // Show the new entry window
            frame.add(formPanel);
            frame.pack();
            frame.setVisible(true);
        });

        // Action listener for "Delete" button
        deleteButton.addActionListener(e -> {
            // Get the selected row in the table
            int selectedRow = table.getSelectedRow();

            // If no row is selected, display an error message
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panel, "Please select a row to delete.");
                return;
            }

            // Remove the selected row from the table
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.removeRow(selectedRow);
        });

        // Action listener for "Edit" button
        editButton.addActionListener(e -> {
            // Get the selected row in the table
            int selectedRow = table.getSelectedRow();

            // If no row is selected, display an error message
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panel, "Please select a row to edit.");
                return;
            }

            // Enable editing of the selected row
            table.setEnabled(true);
            editButton.setVisible(false);
            submitButton.setVisible(true);
        });

        // Action listener for "Submit" button
        submitButton.addActionListener(e -> {
            // Disable editing of the table
            table.setEnabled(false);
            editButton.setVisible(true);
            submitButton.setVisible(false);
        });

        // Action listener for "Upload Insurance Card" button
        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif", "bmp"));
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    BufferedImage insuranceCard = ImageIO.read(file);

                    // Prompt the user to select the entry for the insurance card
                    String[] options = new String[table.getModel().getRowCount()];
                    for (int i = 0; i < table.getModel().getRowCount(); i++) {
                        options[i] = table.getModel().getValueAt(i, 0).toString();
                    }
                    String selectedOption = (String) JOptionPane.showInputDialog(
                            null,
                            "Select the insurance provider for the uploaded card:",
                            "Select Insurance Provider",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);

                    if (selectedOption != null) {
                        for (int i = 0; i < table.getModel().getRowCount(); i++) {
                            if (table.getModel().getValueAt(i, 0).equals(selectedOption)) {
                                insuranceCards.put(i, insuranceCard);
                                table.getModel().setValueAt("View Card", i, table.getColumnCount() - 1);
                                break;
                            }
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        return panel;
    }
}