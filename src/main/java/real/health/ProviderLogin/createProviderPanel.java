
package real.health.ProviderLogin;

import real.health.PatientLogin.validatePassword;
import real.health.SQL.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Scanner;

public class createProviderPanel {
    public static JPanel createProviderAccountPanel(String name, String email, String phone, String practiceName,
            String address, String license, String specialties, String insurance) {

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

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String user = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                // Check that the password meets the criteria
                boolean validPassword = validatePassword.validatePassword(password, confirmPassword);

                if (validPassword) {
                    // Use SQL to insert new provider information
                    try {
                        HealthConn newConnection = new HealthConn();
                        Connection con = newConnection.connect();

                        Scanner scan = new Scanner(System.in);
                        System.out.print("Enter id: ");

                        String id = scan.nextLine();

                        // Check if email and license are unique
                        String sql = "SELECT * FROM provider WHERE email = ? OR license = ?";
                        PreparedStatement checkStatement = con.prepareStatement(sql);
                        checkStatement.setString(1, email);
                        checkStatement.setString(2, license);
                        ResultSet resultSet = checkStatement.executeQuery();

                        if (resultSet.next()) {
                            JOptionPane.showMessageDialog(panel2,
                                    "Email or medical license number is already registered.");
                        } else {
                            // Insert provider information into database
                            String providerSql = "INSERT INTO provider (id, name, email, phone, practiceName, address, license, specialties, insurance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                            PreparedStatement statement = con.prepareStatement(providerSql);
                            statement.setString(1, id);
                            statement.setString(2, name);
                            statement.setString(3, email);
                            statement.setString(4, phone);
                            statement.setString(5, practiceName);
                            statement.setString(6, address);
                            statement.setString(7, license);
                            statement.setString(8, specialties);
                            statement.setString(9, insurance);
                            statement.executeUpdate();

                            // Insert the provider's login information into the database
                            sql = "INSERT INTO provideruserpass (id, user, password) VALUES (?, ?, ?)";
                            statement = con.prepareStatement(sql);
                            statement.setString(1, id);
                            statement.setString(2, user);
                            statement.setString(3, password);
                            statement.executeUpdate();

                            // Close the connection and dispose of the dialog
                            statement.close();
                            con.close();
                            scan.close();
                            JDialog dialog = (JDialog) SwingUtilities.getWindowAncestor(panel2);
                            dialog.dispose();
                            providerHomeScreen.homeScreen();
                        }
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
                createNewProvider.newProviderRegistration();
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
}