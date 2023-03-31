package real.health.GUI;

import javax.swing.*;
import java.awt.event.*;
import real.health.PatientLogin.*;
import real.health.ProviderLogin.*;;

public class welcomeScreen {
    private JFrame frame;

    public welcomeScreen() {
        // Set frame properties
        frame = new JFrame("Welcome to Healtherum");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create panel and add buttons
        JPanel panel = new JPanel();
        JButton patientButton = new JButton("Patient");
        JButton providerButton = new JButton("Provider");
        panel.add(patientButton);
        panel.add(providerButton);

        // Add panel to frame
        frame.add(panel);

        // Add action listeners to buttons
        patientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Launch patient screen
                patientHomeScreen.homeScreen();
                // Close welcome screen
                frame.dispose();
            }
        });

        providerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Launch provider screen
                providerHomeScreen.homeScreen();
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
