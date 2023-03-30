package real.health.GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import real.health.SQL.*;
import real.health.Patient.BloodTest;
import real.health.Patient.Patient;
import real.health.Patient.BloodItem;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import real.health.SQL.HealthConn;

public class InsuranceTab {
    public static JComponent createInsuranceTab(String id) {
        JPanel panel = new JPanel(new BorderLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        // Create the table to display insurance information
        DefaultTableModel tableModel = new DefaultTableModel(
                new String[] { "Provider Name", "Policy Number", "Group Number" }, 0);
        JTable table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(800, 500));
        table.setFillsViewportHeight(true);

        // Fetch insurance information from the database
        try {
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();

            String sql = "SELECT provider_name, policy_number, group_number FROM insurance WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                tableModel.addRow(new Object[] { result.getString(1), result.getString(2), result.getString(3) });
            }

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

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create the top panel with upload button
        JPanel topPanel = new JPanel(new GridBagLayout());
        panel.add(topPanel, BorderLayout.NORTH);

        JButton uploadButton = new JButton("Upload Insurance Card");

        c.anchor = GridBagConstraints.EAST;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 3;
        topPanel.add(uploadButton, c);

        // Create the bottom panel with buttons
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        panel.add(bottomPanel, BorderLayout.SOUTH);

        JButton addButton = new JButton("Add");
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        bottomPanel.add(addButton, c);
        JButton deleteButton = new JButton("Delete");
        c.gridx = 1;
        bottomPanel.add(deleteButton, c);
        JButton editButton = new JButton("Edit");
        c.gridx = 2;
        bottomPanel.add(editButton, c);

        // Add action listener for the "Add" button
        addButton.addActionListener(e -> {
            JFrame addFrame = createAddInsuranceFrame(tableModel, id);
            addFrame.setVisible(true);
            JButton submitButton = new JButton("Submit");
            // Add JLabel and JTextField for Provider
            c.gridx = 0;
            c.gridy = 0;
            addFrame.add(new JLabel("Provider:"), c);

            c.gridx = 1;
            c.gridy = 0;
            addFrame.add(providerField, c);

            // Add JLabel and JTextField for Policy Number
            c.gridx = 0;
            c.gridy = 1;
            addFrame.add(new JLabel("Policy Number:"), c);

            c.gridx = 1;
            c.gridy = 1;
            addFrame.add(policyNumberField, c);

            // Add JLabel and JTextField for Group Number
            c.gridx = 0;
            c.gridy = 2;
            addFrame.add(new JLabel("Group Number:"), c);

            c.gridx = 1;
            c.gridy = 2;
            addFrame.add(groupNumberField, c);

            // Add submit button
            c.gridx = 0;
            c.gridy = 3;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.CENTER;
            addFrame.add(submitButton, c);

            // Add back button
            c.gridx = 0;
            c.gridy = 4;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.CENTER;
            //addFrame.add(backButton, c);

            submitButton.addActionListener(e1 -> {
                // Get the values from the form fields
                String provider = providerField.getText();
                String policyNumber = policyNumberField.getText();
                String groupNumber = groupNumberField.getText();
                // Upload the new insurance record to the SQL server
                try {
                    HealthConn newConnection = new HealthConn();
                    Connection con = newConnection.connect();

                    String sql = "INSERT INTO insurance (id, provider_name, policy_number, group_number) VALUES (?, ?, ?, ?)";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.setString(1, id);
                    statement.setString(2, provider);
                    statement.setString(3, policyNumber);
                    statement.setString(4, groupNumber);
                    statement.executeUpdate();

                    // Refresh the insurance table to show the newly added record
                    tableModel.addRow(new Object[] { provider, policyNumber, groupNumber });

                    // Clean up resources
                    statement.close();
                    con.close();
                    addFrame.dispose();
                } catch (ClassNotFoundException ex) {
                    System.out.println("Error: unable to load MySQL JDBC driver");
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    System.out.println("Error: unable to connect to MySQL database");
                    ex.printStackTrace();
                }
            });
        });

        // Add action listener for the "Delete" button
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                tableModel.removeRow(selectedRow);
            }
        });

        // Add action listener for the "Edit" button
        editButton.addActionListener(e -> {
            int editSelectedRow = table.getSelectedRow();
            if (editSelectedRow != -1) {
                String oldProvider = tableModel.getValueAt(editSelectedRow, 0).toString();
                String oldPolicyNumber = tableModel.getValueAt(editSelectedRow, 1).toString();
                String oldGroupNumber = tableModel.getValueAt(editSelectedRow, 2).toString();

                // Create a dialog to input the new insurance information
                JTextField providerField = new JTextField(20);
                JTextField policyNumberField = new JTextField(20);
                JTextField groupNumberField = new JTextField(20);
                JPanel inputPanel = new JPanel(new GridLayout(0, 1));
                inputPanel.add(new JLabel("New Provider:"));
                inputPanel.add(providerField);
                inputPanel.add(new JLabel("New Policy Number:"));
                inputPanel.add(policyNumberField);
                inputPanel.add(new JLabel("New Group Number:"));
                inputPanel.add(groupNumberField);

                int result = JOptionPane.showConfirmDialog(null, inputPanel,
                        "Update Insurance Information", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    String newProvider = providerField.getText();
                    String newPolicyNumber = policyNumberField.getText();
                    String newGroupNumber = groupNumberField.getText();

                    // Use the new insurance information in the PreparedStatement
                    try {
                        HealthConn newConnection = new HealthConn();
                        Connection con = newConnection.connect();

                        String sql = "UPDATE insurance SET provider_name = ?, policy_number = ?, group_number = ? WHERE id = ? AND provider_name = ? AND policy_number = ? AND group_number = ?";
                        PreparedStatement statement = con.prepareStatement(sql);
                        statement.setString(1, newProvider);
                        statement.setString(2, newPolicyNumber);
                        statement.setString(3, newGroupNumber);
                        statement.setString(4, id);
                        statement.setString(5, oldProvider);
                        statement.setString(6, oldPolicyNumber);
                        statement.setString(7, oldGroupNumber);

                        int updateResult = statement.executeUpdate();

                        if (updateResult > 0) {
                            tableModel.setValueAt(newProvider, editSelectedRow, 0);
                            tableModel.setValueAt(newPolicyNumber, editSelectedRow, 1);
                            tableModel.setValueAt(newGroupNumber, editSelectedRow, 2);
                        }

                        statement.close();
                        con.close();
                    } catch (ClassNotFoundException ex) {
                        System.out.println("Error: unable to load MySQL JDBC driver");
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        System.out.println("Error: unable to connect to MySQL database");
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Add action listener to the upload button
        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(new JFrame());
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
            }
        });

        return panel;
    }

    private static JFrame createAddInsuranceFrame(DefaultTableModel tableModel, String id) {
        JFrame addFrame = new JFrame("Add Insurance Information");
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addFrame.setSize(400, 300);
        addFrame.setLocationRelativeTo(null);

        // Define the fields within the method
        JTextField providerField = new JTextField(20);
        JTextField policyNumberField = new JTextField(20);
        JTextField groupNumberField = new JTextField(20);

        JPanel formPanel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        JButton submitButton = new JButton("Submit");
        // Add action listeners for the submit and back buttons
        submitButton.addActionListener(e -> {
            String provider = providerField.getText();
            String policyNumber = policyNumberField.getText();
            String groupNumber = groupNumberField.getText();
            // Upload the new insurance record to the SQL server
            try {
                HealthConn newConnection = new HealthConn();
                Connection con = newConnection.connect();

                String sql = "INSERT INTO insurance (id, provider_name, policy_number, group_number) VALUES (?, ?, ?, ?)";
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setString(1, id);
                statement.setString(2, provider);
                statement.setString(3, policyNumber);
                statement.setString(4, groupNumber);
                statement.executeUpdate();

                // Refresh the insurance table to show the newly added record
                tableModel.addRow(new Object[] { provider, policyNumber, groupNumber });

                // Clean up resources
                statement.close();
                con.close();
                addFrame.dispose();
            } catch (ClassNotFoundException ex) {
                System.out.println("Error: unable to load MySQL JDBC driver");
                ex.printStackTrace();
            } catch (SQLException ex) {
                System.out.println("Error: unable to connect to MySQL database");
                ex.printStackTrace();
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            addFrame.dispose();
        });

        // Add JLabel and JTextField for Provider
        c.gridx = 0;
        c.gridy = 0;
        formPanel.add(new JLabel("Provider:"), c);

        c.gridx = 1;
        c.gridy = 0;
        formPanel.add(providerField, c);

        // Add JLabel and JTextField for Policy Number
        c.gridx = 0;
        c.gridy = 1;
        formPanel.add(new JLabel("Policy Number:"), c);

        c.gridx = 1;
        c.gridy = 1;
        formPanel.add(policyNumberField, c);

        // Add JLabel and JTextField for Group Number
        c.gridx = 0;
        c.gridy = 2;
        formPanel.add(new JLabel("Group Number:"), c);

        c.gridx = 1;
        c.gridy = 2;
        formPanel.add(groupNumberField, c);

        // Add submit button
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        formPanel.add(submitButton, c);

        // Add back button
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        formPanel.add(backButton, c);

        // Finally, add the formPanel to the addFrame
        addFrame.add(formPanel);
        return addFrame;
    }

    private static JFrame createEditInsuranceFrame(DefaultTableModel tableModel, int selectedRow) {
        JFrame editFrame = new JFrame("Edit Insurance Information");
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFrame.setSize(400, 300);
        editFrame.setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridBagLayout());
        editFrame.add(formPanel);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        // Add the insurance form with labels and text fields
        JLabel providerLabel = new JLabel("Provider Name:");
        JTextField providerField = new JTextField(tableModel.getValueAt(selectedRow, 0).toString(), 20);
        JLabel policyNumberLabel = new JLabel("Policy Number:");
        JTextField policyNumberField = new JTextField(tableModel.getValueAt(selectedRow, 1).toString(), 20);
        JLabel groupNumberLabel = new JLabel("Group Number:");
        JTextField groupNumberField = new JTextField(tableModel.getValueAt(selectedRow, 2).toString(), 20);

        c.gridx = 0;
        c.gridy = 0;
        formPanel.add(providerLabel, c);
        c.gridx = 1;
        formPanel.add(providerField, c);
        c.gridx = 0;
        c.gridy = 1;
        formPanel.add(policyNumberLabel, c);
        c.gridx = 1;
        formPanel.add(policyNumberField, c);
        c.gridx = 0;
        c.gridy = 2;
        formPanel.add(groupNumberLabel, c);
        c.gridx = 1;
        formPanel.add(groupNumberField, c);

        // Add the update and back buttons
        JButton updateButton = new JButton("Update");
        JButton backButton = new JButton("Back");
        c.gridx = 1;
        c.gridy = 3;
        formPanel.add(updateButton, c);
        c.gridx = 0;
        formPanel.add(backButton, c);

        // Add action listeners for the update and back buttons
        updateButton.addActionListener(e -> {
            String provider = providerField.getText();
            String policyNumber = policyNumberField.getText();
            String groupNumber = groupNumberField.getText();

            if (!provider.isEmpty() && !policyNumber.isEmpty() && !groupNumber.isEmpty()) {
                tableModel.setValueAt(provider, selectedRow, 0);
                tableModel.setValueAt(policyNumber, selectedRow, 1);
                tableModel.setValueAt(groupNumber, selectedRow, 2);
                JOptionPane.showMessageDialog(editFrame, "Insurance information updated successfully!");
                editFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(editFrame, "Please fill in all fields.");
            }
        });

        backButton.addActionListener(e -> {
            editFrame.dispose();
        });

        return editFrame;
    }

}
