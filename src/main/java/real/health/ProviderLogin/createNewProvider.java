package real.health.ProviderLogin;

import real.health.API.AddressFill;
import real.health.GUI.UserRole;
import real.health.SQL.*;
import real.health.UTIL.FieldFormat;
import real.health.UTIL.ValidFields;

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
        JFormattedTextField phoneField = FieldFormat.createPhoneNumber(true);

        JLabel practiceNameLabel = new JLabel("Practice Name:");
        JTextField practiceNameField = new JTextField();
        JLabel addressLabel = new JLabel("Address:");
        AddressFill mailingFill = new AddressFill(20, null);
        final String[] selectedAddress = new String[1];

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
        panel1.add(mailingFill);
        panel1.add(licenseLabel);
        panel1.add(licenseField);
        panel1.add(specialtiesLabel);
        panel1.add(specialtiesField);
        panel1.add(insuranceLabel);
        panel1.add(insuranceField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String fullname = nameField.getText();
                String[] firstLast = ValidFields.formatName(fullname);
                String first = firstLast[0];
                String last = firstLast[1];
                if (!ValidFields.isValidName(first)) {
                    JOptionPane.showMessageDialog(dialog, "Invalid First Name. Please enter a valid first name.", "Name Validation Error", JOptionPane.ERROR_MESSAGE);
                }
                else if (!ValidFields.isValidName(last)) {
                    JOptionPane.showMessageDialog(dialog, "Invalid Last Name. Please enter a valid last name.", "Name Validation Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    String name = nameField.getText();
                    String email = emailField.getText();
                    String phone = phoneField.getText();
                    String practiceName = practiceNameField.getText();
                    selectedAddress[0] = mailingFill.getSelectedAddress();
                    String license = licenseField.getText();
                    String specialties = specialtiesField.getText();
                    String insurance = insuranceField.getText();

                    // Create the provider account panel
                    JPanel providerAccountPanel = createProviderPanel.createProviderAccountPanel(name, email, phone,
                            practiceName,
                            selectedAddress[0], license, specialties, insurance);

                    // Display the provider account panel
                    dialog.getContentPane().removeAll();
                    dialog.add(providerAccountPanel, BorderLayout.CENTER);
                    dialog.revalidate();
                }
                
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                providerHomeScreen.homeScreen(UserRole.PROVIDER);
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
