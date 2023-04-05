package real.health.PatientLogin;

import real.health.PatientLogin.*;
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
        JLabel fnameLabel = new JLabel("First Name:");
        JTextField nameField = new JTextField();
        JLabel lnameLabel = new JLabel("Last Name");
        JTextField lnameField = new JTextField();
        JLabel dobLabel = new JLabel("Date of Birth (MM/DD/YYYY):");
        JTextField dobField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel phoneLabel = new JLabel("Phone Number:");
        JTextField phoneField = new JTextField();

        String[] bioSexOptions = { "Male", "Female" };
        JComboBox<String> bioCombo = new JComboBox<>(bioSexOptions);
        bioCombo.setSelectedItem(null);
        JLabel bioLabel = new JLabel("Biological Sex:");
        

        JLabel mailingLabel = new JLabel("Mailing Address:");
        
        AddressFill mailingFill = new AddressFill(20, null);
        String selectedAddress = mailingFill.getSelectedAddress();
        
        

        String[] raceNames = { "African American / Black", "Alaska Native", "Asian American / Asian", "Middle Eastern", "Native American / Indigenous", "Native Hawaiin / Other Pacific Islander", "Multiracial", "European American / White", "Other race or ethincity"};
        JComboBox<String> raceCombo = new JComboBox<>(raceNames);
        raceCombo.setSelectedItem(null);
        JLabel raceLabel = new JLabel("Race/Ethnicity:");

        String[] genderIdentity = {"Male", "Female", "Non-Binary", "Other", "Prefer Not to Say"};
        JComboBox<String> genderCombo = new JComboBox<>(genderIdentity);
        genderCombo.setSelectedItem(null);
        JLabel genderLabel = new JLabel("Gender Identity:");

        panel1.add(fnameLabel);
        panel1.add(nameField);
        panel1.add(lnameLabel);
        panel1.add(lnameField);
        panel1.add(dobLabel);
        panel1.add(dobField);
        panel1.add(emailLabel);
        panel1.add(emailField);
        panel1.add(phoneLabel);
        panel1.add(phoneField);
        panel1.add(bioLabel);
        panel1.add(bioCombo);
        panel1.add(genderLabel);
        panel1.add(genderCombo);
        panel1.add(mailingLabel);
        panel1.add(mailingFill);
        panel1.add(raceLabel);
        panel1.add(raceCombo);
        

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedAddress = mailingFill.getSelectedAddress();
                if (selectedAddress == null) {
                    System.out.print("NO VALUE");
                } else {
                    System.out.println(selectedAddress);
                }
                dialog.getContentPane().removeAll();
                dialog.add(createAccountPanel.createAccountPanel(nameField.getText(),   lnameField.getText(), dobField.getText(),
                        emailField.getText(),
                        phoneField.getText(), (String) bioCombo.getSelectedItem(), selectedAddress,
                        (String) raceCombo.getSelectedItem(), (String) genderCombo.getSelectedItem()));
                dialog.pack();
                dialog.revalidate();
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
