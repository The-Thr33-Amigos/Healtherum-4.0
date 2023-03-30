package real.health.PatientLogin;

import real.health.*;
import javax.swing.*;
import java.sql.*;
import real.health.SQL.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.util.List;

public class showLoginScreen extends patientInformationSystem {
    public static void showLoginScreen() {
        JFrame frame = new JFrame("Login Screen");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        frame.setLocationRelativeTo(null);

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
                            // Authentication succeeded, locks text fields
                            usernameField.setEditable(false);
                            passwordField.setEditable(false);
                            cancelButton.setEnabled(false);
                            loginButton.setEnabled(false);
                            // Show the loading screen here
                            JFrame loadingFrame = new JFrame("Loading...");
                            loadingFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                            loadingFrame.setSize(400, 100);
                            JPanel panel = new JPanel(new BorderLayout());
                            JProgressBar progressBar = new JProgressBar(0, 100);
                            progressBar.setValue(0);
                            progressBar.setStringPainted(true);
                            panel.add(progressBar, BorderLayout.CENTER);
                            loadingFrame.add(panel);
                            loadingFrame.setLocationRelativeTo(null);
                            loadingFrame.setVisible(true);
                            frame.setVisible(false);

                            // Create a SwingWorker object to execute patientInformationSystem on a separate
                            // thread
                            SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
                                JFrame patientFrame;

                                @Override
                                protected Void doInBackground() throws Exception {
                                    int progress = 0;
                                    while (progress < 100) {
                                        // Increment the progress bar every 5ms
                                        Thread.sleep(5);
                                        progress++;
                                        publish(progress);
                                        if (progress == 50) {
                                            patientFrame = (JFrame) patientInformationSystem
                                                    .patientInformationSystem(id, progressBar);
                                            patientFrame.setVisible(false);
                                        }
                                    }
                                    return null;
                                }

                                @Override
                                protected void process(List<Integer> chunks) {
                                    // Update the progress bar with the latest published progress value
                                    progressBar.setValue(chunks.get(chunks.size() - 1));
                                }

                                @Override
                                protected void done() {
                                    // Once patientInformationSystem has finished loading, dispose of the loading
                                    // screen
                                    frame.dispose();
                                    loadingFrame.dispose();
                                    patientFrame.setVisible(true);
                                }
                            };

                            // Schedule the SwingWorker to be executed on the Event Dispatch Thread
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    worker.execute();
                                }
                            });
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
