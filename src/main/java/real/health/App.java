/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package real.health;

import real.health.Patient.*;
import real.health.SQL.HealthConn;
import real.health.GUI.*;

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
    private JTabbedPane tabs;

    public App() {
        homeScreen();
    }

    private void homeScreen() {
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
                showLoginScreen();
                frame.dispose();
            }
        });

        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(existingUserButton, constraints);

        JButton newUserButton = new JButton("New User");
        newUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createNewUser();
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

    protected void createNewUser() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Create New User");
        dialog.setSize(400, 400);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);

        // Panel for basic user information
        JPanel panel1 = new JPanel(new GridLayout(0, 2));
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel dobLabel = new JLabel("Date of Birth (MM/DD/YYYY):");
        JTextField dobField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel phoneLabel = new JLabel("Phone Number:");
        JTextField phoneField = new JTextField();
        JLabel bioLabel = new JLabel("Biological Sex:");
        JTextField bioField = new JTextField();
        JLabel mailingLabel = new JLabel("Mailing Address:");
        JTextField mailingField = new JTextField();
        JLabel raceLabel = new JLabel("Race:");
        JTextField raceField = new JTextField();

        panel1.add(nameLabel);
        panel1.add(nameField);
        panel1.add(dobLabel);
        panel1.add(dobField);
        panel1.add(emailLabel);
        panel1.add(emailField);
        panel1.add(phoneLabel);
        panel1.add(phoneField);
        panel1.add(bioLabel);
        panel1.add(bioField);
        panel1.add(mailingLabel);
        panel1.add(mailingField);
        panel1.add(raceLabel);
        panel1.add(raceField);

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.getContentPane().removeAll();
                dialog.add(createAccountPanel(nameField.getText(), dobField.getText(),
                        emailField.getText(),
                        phoneField.getText(), bioField.getText(), mailingField.getText(), raceField.getText()));
                dialog.pack();
                dialog.revalidate();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                homeScreen();

            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(nextButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel1, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public JPanel createAccountPanel(String name, String bdate, String email, String phone, String bio,
            String mailing, String race) {
        JPanel panel2 = new JPanel(new GridLayout(3, 2));
        panel2.setPreferredSize(new Dimension(300, 100));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);


        JLabel usernameLabel = new JLabel("Username:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        panel2.add(usernameLabel, constraints);

        JTextField usernameField = new JTextField();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel2.add(usernameField, constraints);

        JLabel passwordLabel = new JLabel("Password:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel2.add(passwordLabel, constraints);

        JPasswordField passwordField = new JPasswordField();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel2.add(passwordField, constraints);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        panel2.add(confirmPasswordLabel, constraints);

        JPasswordField confirmPasswordField = new JPasswordField();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        panel2.add(confirmPasswordField, constraints);

        JButton loginButton = new JButton("Create");
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        panel2.add(loginButton, constraints);

        JButton cancelButton = new JButton("Cancel");
        constraints.gridx = 2;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        panel2.add(cancelButton, constraints);


        panel2.add(usernameLabel);
        panel2.add(usernameField);
        panel2.add(passwordLabel);
        panel2.add(passwordField);
        panel2.add(confirmPasswordLabel);
        panel2.add(confirmPasswordField);
        panel2.add(loginButton);
        panel2.add(cancelButton);
        // TODO: One Call per file for sql
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String user = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                // Check that the password meets the criteria
                boolean validPassword = validatePassword(password, confirmPassword);

                if (validPassword) {
                    // Use SQL
                    try {
                        // Load the MySQL JDBC driver
                        HealthConn newConnection = new HealthConn();
                        Connection con = newConnection.connect();

                        // UserPass newUser = new UserPass(user, password);
                        // String hash = newUser.hashGen();

                        Scanner scan = new Scanner(System.in);
                        System.out.print("Enter id: ");

                        String id = scan.nextLine();

                        // Create a SQL statement to insert the user's information
                        String sql = "INSERT INTO basic (id, name, email, phone, bdate, bio, race, mailing) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement statement = con.prepareStatement(sql);
                        statement.setString(1, id);
                        statement.setString(2, name);
                        statement.setString(3, email);
                        statement.setString(4, phone);
                        statement.setString(5, bdate);
                        statement.setString(6, bio);
                        statement.setString(7, race);
                        statement.setString(8, mailing);
                        statement.executeUpdate();

                        // Insert the user's login information into the database
                        sql = "INSERT INTO userpass (id, user, password) VALUES (?, ?, ?)";
                        statement = con.prepareStatement(sql);
                        statement.setString(1, id);
                        statement.setString(2, user);
                        statement.setString(3, password);
                        statement.executeUpdate();
                        System.out.println("Sentttttt");
                        // Close the connection and dispose of the dialog
                        statement.close();
                        con.close();
                        JDialog dialog = (JDialog) SwingUtilities.getWindowAncestor(panel2);
                        dialog.dispose();
                        homeScreen();
                    } catch (ClassNotFoundException ex) {
                        System.out.println("Error: unable to load MySQL JDBC driver");
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        System.out.println("Error: unable to connect to MySQL database");
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(panel2, "Password does not meet criteria.");
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = (JDialog) SwingUtilities.getWindowAncestor(panel2);
                createNewUser();
                dialog.dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(panel2, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    public class IdGenerator {
        public String generateUniqueId() {
            return "987654321"; // replace this with your desired ID
            // return UUID.randomUUID().toString();
        }
    }

    private boolean validatePassword(String password, String confirmPassword) {
        // Check if the two passwords match
        if (!password.equals(confirmPassword)) {
            return false;
        }
        return true;
    }

    private void showLoginScreen() {
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
                    String sql = "SELECT id, password FROM userpass WHERE user=?";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.setString(1, username);
                    ResultSet rs = statement.executeQuery();

                    if (rs.next()) {
                        String userPassword = rs.getString("password");
                        String id = rs.getString("id");
                        // Compare the password entered by the user with the password stored in the
                        // database
                        if (userPassword.equals(new String(password))) {
                            // Authentication succeeArraysded
                            frame.dispose();
                            patientInformationSystem(id);
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
                homeScreen();
                frame.dispose();
            }
        });

        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void patientInformationSystem(String id) throws ClassNotFoundException {
        setTitle("Patient Information System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        tabs = new JTabbedPane();
        add(tabs, BorderLayout.CENTER);

        // Create a new instance of the PatientInformation class
        PatientInformation PatientInformation = new PatientInformation();
        tabs.addTab("Patient Information", PatientInformation.createPatientInformationTab(id));

        // Create a new instance of the MedicalHistoryTab class
        MedicalHistoryTab medicalHistoryTab = new MedicalHistoryTab();
        tabs.addTab("Medical History", medicalHistoryTab.createMedicalHistoryTab(id));

        // Create a new instance of the VitalsTab class
        VitalsTab VitalsTab = new VitalsTab();
        tabs.addTab("Vital Signs", VitalsTab.createVitalSignsTab(id));

        // Create a new instance of the NotesTab class
        NotesTab NotesTab = new NotesTab();
        tabs.addTab("Notes and Progress", NotesTab.createNotesAndProgressTab());

        // Create a new instance of the AppointmentsTab class
        AppointmentsTab AppointmentsTab = new AppointmentsTab();
        tabs.addTab("Appointments", AppointmentsTab.createAppointmentsTab());

        // Create a new instance of the LabResultsTab class
        LabResultsTab LabResultsTab = new LabResultsTab();
        tabs.addTab("Lab Results", LabResultsTab.createLabResultsTab(id));

        setVisible(true);

    }

    // TODO: Fix window popping up in upper left corner, replace current window instead

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App());

    }
}
