package real.health.ProviderLogin;

import real.health.SQL.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class createNewProvider {
    public static void newProviderRegistration() {
        JDialog dialog = new JDialog();
        dialog.setTitle("New Provider Registration");
        dialog.setSize(500, 500);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);

        JPanel panel1 = new JPanel(new GridLayout(0, 2));
        panel1.setPreferredSize(new Dimension(400, 300));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel phoneLabel = new JLabel("Phone Number:");
        JTextField phoneField = new JTextField();
        JLabel practiceNameLabel = new JLabel("Practice Name:");
        JTextField practiceNameField = new JTextField();
        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField();
        JLabel licenseLabel = new JLabel("Medical License Number:");
        JTextField licenseField = new JTextField();
        JLabel specialtiesLabel = new JLabel("Specialties:");
        JTextField specialtiesField = new JTextField();
        JLabel insuranceLabel = new JLabel("Insurance Providers Accepted:");
        JTextField insuranceField = new JTextField();

        panel1.add(nameLabel);
        panel1.add(nameField);
        panel1.add(emailLabel);
        panel1.add(emailField);
        panel1.add(phoneLabel);
        panel1.add(phoneField);
        panel1.add(practiceNameLabel);
        panel1.add(practiceNameField);
        panel1.add(addressLabel);
        panel1.add(addressField);
        panel1.add(licenseLabel);
        panel1.add(licenseField);
        panel1.add(specialtiesLabel);
        panel1.add(specialtiesField);
        panel1.add(insuranceLabel);
        panel1.add(insuranceField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String practiceName = practiceNameField.getText();
                String address = addressField.getText();
                String license = licenseField.getText();
                String specialties = specialtiesField.getText();
                String insurance = insuranceField.getText();

                // Create the provider account panel
                JPanel providerAccountPanel = createProviderPanel.createProviderAccountPanel(name, email, phone,
                        practiceName,
                        address, license, specialties, insurance);

                // Display the provider account panel
                dialog.getContentPane().removeAll();
                dialog.add(providerAccountPanel, BorderLayout.CENTER);
                dialog.revalidate();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                providerHomeScreen.homeScreen();
                dialog.dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel1, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
