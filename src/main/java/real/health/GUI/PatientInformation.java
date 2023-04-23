package real.health.GUI;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import real.health.SQL.HealthConn;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;

public class PatientInformation {

    private UserRole userRole;

    public JComponent createPatientInformationTab(String id, UserRole userRole) throws ClassNotFoundException {
        this.userRole = userRole;
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        // Create text fields to display patient information
        JTextField nameField = new JTextField();
        nameField.setEditable(false);
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(nameField, constraints);

        JTextField dobField = new JTextField();
        dobField.setEditable(false);
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(dobField, constraints);

        JTextField genderField = new JTextField();
        genderField.setEditable(false);
        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(genderField, constraints);

        JTextField ethnicityField = new JTextField();
        ethnicityField.setEditable(false);
        constraints.gridx = 1;
        constraints.gridy = 3;
        panel.add(ethnicityField, constraints);

        JTextField phoneNumberField = new JTextField();
        phoneNumberField.setEditable(false);
        constraints.gridx = 1;
        constraints.gridy = 4;
        panel.add(phoneNumberField, constraints);

        JTextField emailAddressField = new JTextField();
        emailAddressField.setEditable(false);
        constraints.gridx = 1;
        constraints.gridy = 5;
        panel.add(emailAddressField, constraints);

        JTextField mailingAddressField = new JTextField();
        mailingAddressField.setEditable(false);
        constraints.gridx = 1;
        constraints.gridy = 6;
        panel.add(mailingAddressField, constraints);

        // Fetch the patient information from the SQL database using the patient ID
        try {
            // Load the MySQL JDBC driver
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();
            String sql = "SELECT firstName, lastName, email, phone, bdate, gender, race, mailing FROM basic WHERE id = ?";
            System.out.println(id);
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                // Populate the text fields with the patient information
                String firstName = result.getString("firstName");
                String lastName = result.getString("lastName");
                String fullName = firstName + " " + lastName;
                nameField.setText(fullName);
                dobField.setText(result.getString("bdate"));
                genderField.setText(result.getString("gender"));
                ethnicityField.setText(result.getString("race"));
                phoneNumberField.setText(result.getString("phone"));
                emailAddressField.setText(result.getString("email"));
                mailingAddressField.setText(result.getString("mailing"));
            }

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

        JLabel driversLicenseLabel = new JLabel("Driver's License:");
        constraints.gridx = 4;
        constraints.gridy = 0;
        panel.add(driversLicenseLabel, constraints);

        JButton driversLicenseButton = new JButton("Upload Driver's License");
        constraints.gridx = 4;
        constraints.gridy = 1;
        if (userRole == UserRole.PATIENT) {
            panel.add(driversLicenseButton, constraints);
        }

        // Action listener for "Upload Driver's License" button
        driversLicenseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    BufferedImage driversLicense = ImageIO.read(file);
                    // Save the image to the database or file system here
                } catch (IOException ex) {
                    // TODO: Error handling
                    ex.printStackTrace();
                }
            }
        });

        JButton editButton = new JButton("Edit");
        constraints.gridx = 2;
        constraints.gridy = 7;
        if (userRole == UserRole.PATIENT) {
            panel.add(editButton, constraints);
        }

        JButton submitButton = new JButton("Submit");
        constraints.gridx = 2;
        constraints.gridy = 7;
        panel.add(submitButton, constraints);
        submitButton.setVisible(false);

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mailingAddressField.setEditable(true);
                emailAddressField.setEditable(true);
                phoneNumberField.setEditable(true);
                editButton.setVisible(false);
                submitButton.setVisible(true);
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Save the updated patient information to the SQL database
                try {
                    // Load the MySQL JDBC driver
                    HealthConn newConnection = new HealthConn();
                    Connection con = newConnection.connect();

                    // Create a SQL statement to insert the user's information
                    String sql = "UPDATE basic SET email=?, phone=?, mailing=? WHERE id=?";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.setString(2, emailAddressField.getText());
                    statement.setString(3, phoneNumberField.getText());
                    statement.setString(4, mailingAddressField.getText());
                    statement.setString(5, id); // id is the patient's unique identifier
                    statement.executeUpdate();

                    // Clean up resources
                    statement.close();
                    con.close();
                } catch (SQLException ex) {
                    System.out.println("SQL Exception: " + ex.getMessage());
                } catch (ClassNotFoundException e1) {
                    // TODO: Error handling
                    e1.printStackTrace();
                }

                // Disable the text fields and show the edit button
                nameField.setEditable(false);
                emailAddressField.setEditable(false);
                phoneNumberField.setEditable(false);
                mailingAddressField.setEditable(false);
                editButton.setVisible(true);
                submitButton.setVisible(false);
            }
        });

        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new GridLayout(7, 2));
        summaryPanel.add(new JLabel("Name:"));
        summaryPanel.add(nameField);
        summaryPanel.add(new JLabel("Date of Birth:"));
        summaryPanel.add(dobField);
        summaryPanel.add(new JLabel("Biological Sex:"));
        summaryPanel.add(genderField);
        summaryPanel.add(new JLabel("Race:"));
        summaryPanel.add(ethnicityField);
        summaryPanel.add(new JLabel("Phone Number:"));
        summaryPanel.add(phoneNumberField);
        summaryPanel.add(new JLabel("Address:"));
        summaryPanel.add(mailingAddressField);
        summaryPanel.add(new JLabel("Email Address:"));
        summaryPanel.add(emailAddressField);

        // Add the summary panel to the main panel
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 4; // set grid width to 2
        constraints.gridheight = 6; // set grid height to 6
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(summaryPanel), constraints);

        return panel;
    }
}
