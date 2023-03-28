/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package real.health;

import real.health.Patient.*;
import real.health.PatientLogin.createNewUser;
import real.health.PatientLogin.showLoginScreen;
import real.health.SQL.HealthConn;
import real.health.GUI.*;
import java.sql.*;
import java.sql.ResultSet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import java.util.Scanner;

public class App extends JFrame {
    public App() {
        homeScreen();
    }

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
    public class IdGenerator {
        public String generateUniqueId() {
            return "987654321"; // replace this with your desired ID
            // return UUID.randomUUID().toString();
        }
    }
    // TODO: Fix window popping up in upper left corner, replace current window
    // instead

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }
}
