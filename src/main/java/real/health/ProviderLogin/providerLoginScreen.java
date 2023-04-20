package real.health.ProviderLogin;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

import real.health.GUI.UserRole;
import real.health.SQL.*;


public class providerLoginScreen {
    public static void loginScreen() {
        JFrame frame = new JFrame("Provider Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Provider Login");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(titleLabel, constraints);

        JLabel usernameLabel = new JLabel("Username:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(usernameLabel, constraints);

        JTextField usernameField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(usernameField, constraints);

        JLabel passwordLabel = new JLabel("Password:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(passwordLabel, constraints);

        JPasswordField passwordField = new JPasswordField(20);
        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(passwordField, constraints);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Connect to SQL database
                try {
                    // Load the MySQL JDBC driver
                    HealthConn newConnection = new HealthConn();
                    Connection con = newConnection.connect();

                    // Create a prepared statement to query the database for the user's login
                    // credentials
                    String sql = "SELECT id, password FROM provideruserpass WHERE user = ?";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.setString(1, username);
                    ResultSet rs = statement.executeQuery();


                    if (rs.next()) {
                        // Login successful
                        JOptionPane.showMessageDialog(frame, "Login successful.");
                        providerSystem.initialize(sql);
                        frame.dispose();
                    } else {
                        // Login failed
                        JOptionPane.showMessageDialog(frame, "Invalid username or password.");
                    }
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        panel.add(loginButton, constraints);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                providerHomeScreen.homeScreen(UserRole.PROVIDER);
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(cancelButton, constraints);

        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

