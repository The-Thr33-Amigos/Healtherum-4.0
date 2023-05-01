package real.health.ProviderLogin;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import real.health.ProviderLogin.*;
import real.health.GUI.UserRole;
import real.health.PatientLogin.*;
import real.health.SQL.*;
import java.awt.*;

public class providerHomeScreen {
    public static void homeScreen(UserRole provider) {
        JFrame frame = new JFrame("Healthereum - Provider Home");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel welcomeLabel = new JLabel("Welcome to Provider Portal");
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(welcomeLabel, constraints);

        JLabel infoLabel = new JLabel(
                "<html><center>Welcome to your professional health portal.<br>We're dedicated to providing secure, user-friendly tools for efficient patient care.</center></html>");
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(infoLabel, constraints);

        JButton existingUserButton = new JButton("Existing User");
        existingUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                providerLoginScreen.loginScreen();
                frame.dispose();
            }
        });

        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(existingUserButton, constraints);

        JButton newUserButton = new JButton("New User");
        newUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createNewProvider.newProviderRegistration();
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
