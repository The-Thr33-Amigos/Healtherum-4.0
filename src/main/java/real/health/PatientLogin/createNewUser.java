package real.health.PatientLogin;

import real.health.PatientLogin.*;
import real.health.UTIL.FieldFormat;
import real.health.UTIL.ValidFields;
import real.health.*;
import real.health.API.AddressFill;
import real.health.API.AutoCompleteListener;
import real.health.GUI.UserRole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class createNewUser {
    public static void createNewUser() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Create New User");
        dialog.setSize(500, 500);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);

        // Panel for basic user information
        JPanel panel1 = new JPanel(new GridLayout(0, 2));
        
        JLabel firstNameLabel = new JLabel("First Name:");
        JTextField firstNameField = new JTextField();

        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastNameField = new JTextField();

        JLabel dobLabel = new JLabel("Date of Birth (MM/DD/YYYY):");
        JFormattedTextField dobField = FieldFormat.createPhoneNumber(false);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JLabel phoneLabel = new JLabel("Phone Number:");
        JFormattedTextField phoneField = FieldFormat.createPhoneNumber(true);

        String[] bioSexOptions = { "Male", "Female", "Intersex"};
        JComboBox<String> bioCombo = new JComboBox<>(bioSexOptions);
        bioCombo.setSelectedItem(null);
        JLabel bioLabel = new JLabel("Biological Sex:");
        

        JLabel mailingLabel = new JLabel("Mailing Address:");
        
        AddressFill mailingFill = new AddressFill(20, null);
        final String[] selectedAddress = new String[1];
        

        String[] raceNames = {"African American / Black", "Alaska Native", "Asian American / Asian", "Middle Eastern", "Native American / Indigenous", "Native Hawaiin / Other Pacific Islander", "Multiracial", "European American / White", "Other race or ethincity"};
        JComboBox<String> raceCombo = new JComboBox<>(raceNames);
        raceCombo.setSelectedItem(null);
        JLabel raceLabel = new JLabel("Race/Ethnicity:");

        panel1.add(firstNameLabel);
        panel1.add(firstNameField);
        panel1.add(lastNameLabel);
        panel1.add(lastNameField);
        panel1.add(dobLabel);
        panel1.add(dobField);
        panel1.add(emailLabel);
        panel1.add(emailField);
        panel1.add(phoneLabel);
        panel1.add(phoneField);
        panel1.add(bioLabel);
        panel1.add(bioCombo);
        panel1.add(mailingLabel);
        panel1.add(mailingFill);
        panel1.add(raceLabel);
        panel1.add(raceCombo);


        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // email, and name validation
                String email = emailField.getText();
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String date = dobField.getText();

                ValidFields emailCheck = new ValidFields();
                if (!emailCheck.isValidEmail(email)) {
                    JOptionPane.showMessageDialog(dialog, "Invalid email. Please enter a valid email.", "Email Validation Error", JOptionPane.ERROR_MESSAGE);
                } else if (!emailCheck.isValidName(firstName)) {
                    JOptionPane.showMessageDialog(dialog, "Invalid First Name. Please enter a valid first name.", "Name Validation Error", JOptionPane.ERROR_MESSAGE);
                } else if (!emailCheck.isValidName(lastName)) {
                    JOptionPane.showMessageDialog(dialog, "Invalid Last Name. Please enter a valid last name.", "Name Validation Error", JOptionPane.ERROR_MESSAGE); 
                } else if (!emailCheck.isValidDate(date)) {
                    JOptionPane.showMessageDialog(dialog, "Invalid Date. Please enter a valid date.", "Name Validation Error", JOptionPane.ERROR_MESSAGE);
                } else {

                    selectedAddress[0] = mailingFill.getSelectedAddress(); // Add this line to get the selected address
                                                                           // when the Next button is clicked
                    dialog.getContentPane().removeAll();
                    dialog.add(createAccountPanel.createAccountPanel(firstNameField.getText(), lastNameField.getText(),
                            dobField.getText(),
                            emailField.getText(),
                            phoneField.getText(), (String) bioCombo.getSelectedItem(), selectedAddress[0],
                            (String) raceCombo.getSelectedItem()));
                    dialog.pack();
                    dialog.revalidate();
                }
                
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                patientHomeScreen.homeScreen(UserRole.PATIENT);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(nextButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel1, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
