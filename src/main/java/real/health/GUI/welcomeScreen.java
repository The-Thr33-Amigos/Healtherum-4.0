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
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and style welcome message
        JPanel welcomePanel = new JPanel();
        JLabel welcomeLabel = new JLabel("Welcome to Healtherum");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomePanel.add(welcomeLabel);

        // Create and style button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 20, 20));
        JButton patientButton = new JButton("Patient");
        JButton providerButton = new JButton("Provider");
        patientButton.setFont(new Font("Arial", Font.BOLD, 18));
        providerButton.setFont(new Font("Arial", Font.BOLD, 18));
        buttonPanel.add(patientButton);
        buttonPanel.add(providerButton);

        // Add panels to frame
        frame.setLayout(new BorderLayout(20, 20));
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
