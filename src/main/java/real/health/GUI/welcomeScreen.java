package real.health.GUI;

import javax.swing.*;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import java.awt.event.*;
import real.health.PatientLogin.*;
import real.health.ProviderLogin.*;

public class welcomeScreen {
    private JFrame frame;

    public welcomeScreen() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Failed");
        }
        // Set frame properties
        frame = new JFrame("Healtherum");
        frame.setSize(500, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Create and style welcome message
        JPanel welcomePanel = new JPanel();
        JLabel welcomeLabel = new JLabel("Healtherum");
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 28));
        welcomePanel.add(welcomeLabel);

        // Create and style button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(-40, 5, 5, 5);

        JButton patientButton = new JButton("Patient");
        JButton providerButton = new JButton("Provider");
        patientButton.setFont(new Font("Serif", Font.PLAIN, 18));
        providerButton.setFont(new Font("Serif", Font.PLAIN, 18));
        patientButton.setPreferredSize(new Dimension(150, 50));
        providerButton.setPreferredSize(new Dimension(150, 50));

        // Add buttons to button panel
        constraints.gridx = 0;
        constraints.gridy = 0;
        buttonPanel.add(patientButton, constraints);
        constraints.gridx = 1;
        buttonPanel.add(providerButton, constraints);

        // Add panels to frame
        frame.setLayout(new BorderLayout(20, 10));
        frame.add(welcomePanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);

        // Add action listeners to buttons
        patientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Launch patient screen
                patientHomeScreen.homeScreen(UserRole.PATIENT);
                // Close welcome screen
                frame.dispose();
            }
        });

        providerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Launch provider screen
                providerHomeScreen.homeScreen(UserRole.PROVIDER);
                // Close welcome screen
                frame.dispose();
            }
        });

        // Center frame on screen
        frame.setLocationRelativeTo(null);
    }

    public void setVisible(boolean b) {
        frame.setVisible(true);
    }
}
