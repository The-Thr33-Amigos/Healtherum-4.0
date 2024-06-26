package real.health.PatientLogin;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;

import real.health.GUI.UserRole;
import real.health.SQL.*;
import java.awt.*;

public class patientHomeScreen {

    
    public static void homeScreen(UserRole patient) {
        JFrame frame = new JFrame("Healthereum - Patient Home");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel welcomeLabel = new JLabel("Welcome to Patient Portal");
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(welcomeLabel, constraints);

        JLabel infoLabel = new JLabel(
                "<html><center>Welcome to your personal health portal.<br>We're committed to providing secure, user-friendly access to your medical information.</center></html>");
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(infoLabel, constraints);

        JButton existingUserButton = new JButton("Existing User");
        existingUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoginScreen.showLoginScreen(UserRole.PATIENT);
                frame.dispose();
            }
        });

        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(existingUserButton, constraints);

        JButton newUserButton = new JButton("New User");
        newUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createNewUser.createNewUser();
                frame.dispose();
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(newUserButton, constraints);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
