package real.health.PatientLogin;
import real.health.Patient.*;
import real.health.GUI.*;
import real.health.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import real.health.Patient.*;
import real.health.PatientLogin.*;
import real.health.SQL.*;
import real.health.GUI.*;
import java.sql.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import javax.swing.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class showLoginScreen {
    public static void showLoginScreen() {
        JFrame frame = new JFrame("Login Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        JLabel usernameLabel = new JLabel("Username:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        panel.add(usernameLabel, constraints);

        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30));
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(usernameField, constraints);

        JLabel passwordLabel = new JLabel("Password:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(passwordLabel, constraints);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30));
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        panel.add(passwordField, constraints);

        JButton loginButton = new JButton("Login");
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        panel.add(loginButton, constraints);

        JButton cancelButton = new JButton("Cancel");
        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        panel.add(cancelButton, constraints);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();

                try {
                    // Load the MySQL JDBC driver
                    HealthConn newConnection = new HealthConn();
                    Connection con = newConnection.connect();

                    // Create a prepared statement to query the database for the user's login
                    // credentials
                    String sql = "SELECT id, password FROM userpass WHERE user = ?";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.setString(1, username);
                    ResultSet rs = statement.executeQuery();

                    if (rs.next()) {
                        String userPassword = rs.getString("password");
                        String id = rs.getString("id");
                        // Compare the password entered by the user with the password stored in the
                        // database
                        if (userPassword.equals(new String(password))) {
                            // Authentication succeeded
                            LoadingScreen loadingScreen = new LoadingScreen(frame, () -> {
                                // Perform loading process here
                                // Call loadingScreen.setProgress(progress) to update progress as it occurs
                                try {

                                    patientInformationSystem.patientInformationSystem(id);

                                } catch (ClassNotFoundException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }
                                frame.dispose();
                            });
                            loadingScreen.showLoadingScreen();
                        } else {
                            // Authentication failed
                            JOptionPane.showMessageDialog(panel, "Invalid username or password.", "Login Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // Authentication failed
                        JOptionPane.showMessageDialog(panel, "Invalid username or password.", "Login Error",
                                JOptionPane.ERROR_MESSAGE);
                    }

                    // Close the connection and release any resources used by the prepared statement
                    // and result set
                    rs.close();
                    statement.close();
                    con.close();
                } catch (ClassNotFoundException ex) {
                    System.out.println("Error: unable to load MySQL JDBC driver");
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    System.out.println("Error: unable to connect to MySQL database");
                    ex.printStackTrace();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                App.homeScreen();
                frame.dispose();
            }
        });

        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
