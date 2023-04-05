package real.health.PatientLogin;


import real.health.Blockchain.*;
import real.health.GUI.UserRole;
import real.health.Patient.UserPass;
import real.health.*;
import javax.swing.*;
import java.sql.*;
import real.health.SQL.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class createAccountPanel {
    public static JPanel createAccountPanel(String firstName, String lastName, String bdate, String email, String phone, String gender,
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
                boolean validPassword = validatePassword.validatePassword(password, confirmPassword);

                if (validPassword) {
                    // Use SQL
                    try {

                        Blockchain patientBlockchain = new Blockchain();

                        HealthConn newConnection = new HealthConn();
                        Connection con = newConnection.connect();


                        String id = UserPass.generateUniqueId();

                        // Add the patient's ID to the blockchain
                        patientBlockchain.addBlock(id);

                        // Print the contents of the blockchain
                        patientBlockchain.printChain();
                        // Create a SQL statement to insert the user's information
                        String sql = "INSERT INTO basic (id, firstName, lastName, email, phone, bdate, gender, race, mailing) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement statement = con.prepareStatement(sql);
                        statement.setString(1, id);
                        statement.setString(2, firstName);
                        statement.setString(3, lastName);
                        statement.setString(4, email);
                        statement.setString(5, phone);
                        statement.setString(6, bdate);
                        statement.setString(7, gender);
                        statement.setString(8, race);
                        statement.setString(9, mailing);
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
                        patientHomeScreen.homeScreen(UserRole.PATIENT);
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
                createNewUser.createNewUser();
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
