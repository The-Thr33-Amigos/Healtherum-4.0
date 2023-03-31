package real.health.PatientLogin;

import real.health.PatientLogin.*;
import real.health.*;
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
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
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
        JTextField mailingField = new JTextField();

        String[] raceNames = { "Black", "White", "American Indian or Alaska Native", "Asian",
                "Native Hawaiian or Other Pacific Islander" };
        JComboBox<String> raceCombo = new JComboBox<>(raceNames);
        raceCombo.setSelectedItem(null);
        JLabel raceLabel = new JLabel("Race:");

        panel1.add(nameLabel);
        panel1.add(nameField);
        panel1.add(dobLabel);
        panel1.add(dobField);
        panel1.add(emailLabel);
        panel1.add(emailField);
        panel1.add(phoneLabel);
        panel1.add(phoneField);
        panel1.add(bioLabel);
        panel1.add(bioCombo);
        panel1.add(mailingLabel);
        panel1.add(mailingField);
        panel1.add(raceLabel);
        panel1.add(raceCombo);

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.getContentPane().removeAll();
                dialog.add(createAccountPanel.createAccountPanel(nameField.getText(), dobField.getText(),
                        emailField.getText(),
                        phoneField.getText(), (String) bioCombo.getSelectedItem(), mailingField.getText(),
                        (String) raceCombo.getSelectedItem()));
                dialog.pack();
                dialog.revalidate();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                patientHomeScreen.homeScreen();
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
