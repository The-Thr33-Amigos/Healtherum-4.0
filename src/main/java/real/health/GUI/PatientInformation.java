package real.health.GUI;

import java.awt.*;
import javax.swing.*;

import real.health.Patient.Patient;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;

public class PatientInformation {
    public JComponent createPatientInformationTab(String id) {
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
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://35.161.231.206:3306/patient", "Tyler",
                "Penis@Penis.com");
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM basic WHERE id = ?")) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Populate the text fields with the patient information
                nameField.setText(rs.getString("name"));
                dobField.setText(rs.getString("bdate"));
                genderField.setText(rs.getString("bio"));
                ethnicityField.setText(rs.getString("race"));
                phoneNumberField.setText(rs.getString("phone"));
                emailAddressField.setText(rs.getString("email"));
                mailingAddressField.setText(rs.getString("mailing"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JLabel pictureLabel = new JLabel("Insurance Card:");
        constraints.gridx = 4;
        constraints.gridy = 3;
        panel.add(pictureLabel, constraints);

        JButton pictureButton = new JButton("Upload Insurance Card");
        constraints.gridx = 4;
        constraints.gridy = 4;
        panel.add(pictureButton, constraints);

        JLabel driversLicenseLabel = new JLabel("Driver's License:");
        constraints.gridx = 4;
        constraints.gridy = 0;
        panel.add(driversLicenseLabel, constraints);

        JButton driversLicenseButton = new JButton("Upload Driver's License");
        constraints.gridx = 4;
        constraints.gridy = 1;
        panel.add(driversLicenseButton, constraints);

        JButton editButton = new JButton("Edit");
        constraints.gridx = 2;
        constraints.gridy = 7;
        panel.add(editButton, constraints);

        JButton submitButton = new JButton("Submit");
        constraints.gridx = 2;
        constraints.gridy = 7;
        panel.add(submitButton, constraints);
        submitButton.setVisible(false);

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameField.setEditable(true);
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
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    // Create a connection to the database
                    String url = "jdbc:mysql://35.161.231.206:3306/patient";
                    String dbusername = "Hunter";
                    String dbpass = "H@mmer2525";
                    Connection con = DriverManager.getConnection(url, dbusername, dbpass);

                    // Create a SQL statement to insert the user's information
                    String sql = "UPDATE basic SET name=?, email=?, phone=?, mailing=? WHERE id=?";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.setString(1, nameField.getText());
                    statement.setString(2, emailAddressField.getText());
                    statement.setString(3, phoneNumberField.getText());
                    statement.setString(4, mailingAddressField.getText());
                    statement.setString(5, id); // id is the patient's unique identifier
                    statement.executeUpdate();
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println("SQL Exception: " + ex.getMessage());
                } catch (ClassNotFoundException e1) {
                    // TODO Auto-generated catch block
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
        summaryPanel.add(new JLabel("Gender:"));
        summaryPanel.add(genderField);
        summaryPanel.add(new JLabel("Ethnicity:"));
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
