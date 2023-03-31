package real.health.PatientLogin;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import real.health.SQL.*;
import java.awt.*;

public class patientHomeScreen {
    public static void homeScreen() {
        JFrame frame = new JFrame("Healthereum");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel welcomeLabel = new JLabel("Welcome to Healthereum");
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(welcomeLabel, constraints);

        JLabel infoLabel = new JLabel(
                "<html><center>We're a healthcare technology company dedicated to<br>providing secure, user-friendly solutions for patients and providers.</center></html>");
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(infoLabel, constraints);

        JButton existingUserButton = new JButton("Existing User");
        existingUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoginScreen.showLoginScreen();
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
