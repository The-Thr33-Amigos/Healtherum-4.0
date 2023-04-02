package real.health.GUI;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.sql.Connection;
import java.time.Year;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import real.health.GUI.UserRole;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import java.sql.Blob;

import real.health.SQL.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InsuranceTab {
    private static Map<Integer, BufferedImage> insuranceCards = new HashMap<>();
    private UserRole userRole;

    public JComponent createInsuranceTab(String id, UserRole userRole) {
        this.userRole = userRole;
        // Create the main panel with BorderLayout
        JPanel panel = new JPanel(new BorderLayout());

        // Create the insurance card upload panel on the right using BorderLayout
        JPanel uploadPanel = new JPanel(new BorderLayout());
        JButton uploadButton = new JButton("Upload Insurance Card");
        uploadButton.setPreferredSize(new Dimension(200, 50));
        if (userRole == UserRole.PATIENT) {
            uploadPanel.add(uploadButton, BorderLayout.NORTH);
        }

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
        editButton.setVisible(true);

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
        // Load the insurance information from the SQL database
        try {
            // Create a connection to the database
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();
            String sql = "SELECT provider_name, policy_number, group_number, holder_name, holder_dob, holder_ssn, insurance_card FROM insurance WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            DefaultTableModel tableModel = new DefaultTableModel(
                    new Object[] { "providertr_name", "policy_number", "group_number", "holder_name", "holder_dob",
                            "holder_ssn", "insurance_card" },
                    0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }

                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    if (columnIndex == 6) {
                        return Object.class;
                    } else {
                        return super.getColumnClass(columnIndex);
                    }
                }
            };
            tableModel.setRowCount(0);

            while (rs.next()) {
                // Get the Blob object from the ResultSet
                Blob blob = rs.getBlob("insurance_card");

                // If the Blob is not null, create an ImageIcon from its data
                if (blob != null) {
                    try (InputStream in = blob.getBinaryStream()) {
                        ImageIcon icon = new ImageIcon(ImageIO.read(in), "View Image");

                        // set the ImageIcon on a JLabel or other component in your GUI
                        insuranceCards.put(tableModel.getRowCount(), (BufferedImage) icon.getImage());
                        tableModel.addRow(new Object[] {
                                rs.getString(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getString(5),
                                rs.getString(6),
                                icon
                        });
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    tableModel.addRow(new Object[] {
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getString(6),
                            "No Image Available"
                    });
                }
            }

            table.setModel(tableModel);
            // Clean up resources
            rs.close();
            statement.close();
            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        // Add the button panel to the SOUTH position of the main panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        if (userRole == UserRole.PATIENT) {
            buttonPanel.add(newButton);
            buttonPanel.add(deleteButton);
            buttonPanel.add(editButton);
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }

        // Add a mouse listener to the table to handle clicks on table rows
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = table.getSelectedRow();
                    if (insuranceCards.containsKey(selectedRow)) {
                        JFrame imageFrame = new JFrame("Insurance Card");
                        imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        imageFrame.setLocationRelativeTo(null);

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
            frame.setPreferredSize(new Dimension(500, 300));
            frame.setLocationRelativeTo(null);
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
                // Save the new insurance information to the SQL database
                try {
                    HealthConn newConnection = new HealthConn();
                    Connection con = newConnection.connect();
                    String sql = "INSERT INTO insurance (id, provider_name, policy_number, group_number, holder_name, holder_dob, holder_ssn) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.setString(1, id);
                    statement.setString(2, provider);
                    statement.setString(3, policyNumber);
                    statement.setString(4, groupNumber);
                    statement.setString(5, policyHolderName);
                    statement.setString(6, policyHolderDOB);
                    statement.setString(7, policyHolderSSN);
                    statement.executeUpdate();

                    statement.close();
                    con.close();
                } catch (ClassNotFoundException | SQLException ez) {
                    ez.printStackTrace();
                }
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

            // Get the primary key or unique identifier of the record from the selected row
            // Assuming the first column of the table contains the primary key
            String primaryKey = table.getValueAt(selectedRow, 0).toString();

            // Execute an SQL DELETE statement to delete the corresponding record from the
            // database
            try {
                HealthConn newConnection = new HealthConn();
                Connection con = newConnection.connect();
                String sql = "DELETE FROM insurance WHERE provider_name = ?"; // Replace 'id' with the actual primary
                                                                              // key column
                // name
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setString(1, primaryKey);
                statement.executeUpdate();

                // Close the statement and connection
                statement.close();
                con.close();

                // Remove the selected row from the table
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.removeRow(selectedRow);

            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, "An error occurred while deleting the record.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
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

            // Get the values of the selected row from the table model
            String providerName = (String) table.getValueAt(selectedRow, 0);
            String policyNumber = (String) table.getValueAt(selectedRow, 1);
            String groupNumber = (String) table.getValueAt(selectedRow, 2);
            String holderName = (String) table.getValueAt(selectedRow, 3);
            String holderDOB = (String) table.getValueAt(selectedRow, 4);
            String holderSSN = (String) table.getValueAt(selectedRow, 5);
            BufferedImage insuranceCard = insuranceCards.get(selectedRow);

            // Create a new JFrame for the edit window
            JFrame editFrame = new JFrame("Edit Insurance Information");
            editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            editFrame.setPreferredSize(new Dimension(500, 300));
            editFrame.setLocationRelativeTo(null);


            // Create a JPanel for the edit window fields
            JPanel editPanel = new JPanel(new GridLayout(7, 2, 10, 10));

            // Add the fields to the edit panel
            editPanel.add(new JLabel("Provider Name:"));
            JTextField providerNameField = new JTextField(providerName);
            editPanel.add(providerNameField);

            editPanel.add(new JLabel("Policy Number:"));
            JTextField policyNumberField = new JTextField(policyNumber);
            editPanel.add(policyNumberField);

            editPanel.add(new JLabel("Group Number:"));
            JTextField groupNumberField = new JTextField(groupNumber);
            editPanel.add(groupNumberField);

            editPanel.add(new JLabel("Policy Holder Name:"));
            JTextField holderNameField = new JTextField(holderName);
            editPanel.add(holderNameField);

            editPanel.add(new JLabel("Policy Holder DOB:"));
            JTextField holderDOBField = new JTextField(holderDOB);
            editPanel.add(holderDOBField);

            editPanel.add(new JLabel("Policy Holder SSN:"));
            JTextField holderSSNField = new JTextField(holderSSN);
            editPanel.add(holderSSNField);

            // Add the edit panel to the edit frame
            editFrame.add(editPanel, BorderLayout.CENTER);

            // Create a JPanel for the submit button
            JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

            // Add the submit button to the submit panel
            JButton submitButton = new JButton("Submit");
            submitButton.addActionListener(e1 -> {
                // Get the new values from the edit window fields
                String newProviderName = providerNameField.getText();
                String newPolicyNumber = policyNumberField.getText();
                String newGroupNumber = groupNumberField.getText();
                String newHolderName = holderNameField.getText();
                String newHolderDOB = holderDOBField.getText();
                String newHolderSSN = holderSSNField.getText();

                // Update the selected row in the table model
                table.setValueAt(newProviderName, selectedRow, 0);
                table.setValueAt(newPolicyNumber, selectedRow, 1);
                table.setValueAt(newGroupNumber, selectedRow, 2);
                table.setValueAt(newHolderName, selectedRow, 3);
                table.setValueAt(newHolderDOB, selectedRow, 4);
                table.setValueAt(newHolderSSN, selectedRow, 5);

                // Update the insurance card in the insuranceCards map
                insuranceCards.put(selectedRow, insuranceCard);

                // Close the edit window
                editFrame.dispose();

                // Show the "Edit" and hide the "Submit" button
                editButton.setVisible(true);
                submitButton.setVisible(false);

                // Disable editing of the table
                table.setEnabled(false);
            });
            submitPanel.add(submitButton);
            editFrame.add(submitPanel, BorderLayout.SOUTH);

            // Show the edit frame
            editFrame.pack();
            editFrame.setVisible(true);

            // Hide the "Edit" and show the "Submit" button
            editButton.setVisible(false);
            submitButton.setVisible(true);

            // Enable editing of the table
            table.setEnabled(true);
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
                                table.getModel().setValueAt("View Image", i, table.getColumnCount() - 1);

                                // Save the insurance card image to the SQL database
                                try {
                                    HealthConn newConnection = new HealthConn();
                                    Connection con = newConnection.connect();
                                    String sql = "UPDATE insurance SET insurance_card = ? WHERE id = ? AND provider_name = ?";
                                    PreparedStatement statement = con.prepareStatement(sql);
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    ImageIO.write(insuranceCard, "jpg", baos);
                                    byte[] insuranceCardBytes = baos.toByteArray();

                                    statement.setBytes(1, insuranceCardBytes);
                                    statement.setString(2, id);
                                    statement.setString(3, selectedOption);
                                    statement.executeUpdate();

                                    statement.close();
                                    con.close();
                                } catch (ClassNotFoundException ex) {
                                    System.out.println("Error: unable to load MySQL JDBC driver");
                                    ex.printStackTrace();
                                } catch (SQLException ex) {
                                    System.out.println("Error: unable to connect to MySQL database");
                                    ex.printStackTrace();
                                } catch (IOException ex) {
                                    System.out.println("Error: unable to write insurance card image to byte array");
                                    ex.printStackTrace();
                                }
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