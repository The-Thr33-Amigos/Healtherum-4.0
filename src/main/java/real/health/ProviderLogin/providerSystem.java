package real.health.ProviderLogin;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;

import real.health.Patient.Patient;
import real.health.SQL.*;
import java.awt.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class providerSystem {
    public static void main(String[] args) {
        // Initialize the home screen
        JFrame homeScreen = new JFrame("Provider Home");
        homeScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homeScreen.setSize(800, 600);

        // Create a panel for the home screen
        JPanel panel = new JPanel();
        homeScreen.add(panel);

        // Add components to the home screen panel
        createAppointmentsList(panel);
        createCalendar(panel);
        createPatientSearch(panel);
        createNavigationButtons(panel);

        // Display the home screen
        homeScreen.setVisible(true);
    }

    private static void createAppointmentsList(JPanel panel) {
        // Add components for the daily appointments list
    }

    private static void createCalendar(JPanel panel) {
        // Add components for the calendar view
    }

    private static void createPatientSearch(JPanel panel) {
        // Create a search label
        JLabel searchLabel = new JLabel("Search Patient:");
        panel.add(searchLabel);

        // Create a search text field
        JTextField searchField = new JTextField(20);
        panel.add(searchField);

        // Create a search button
        JButton searchButton = new JButton("Search");
        panel.add(searchButton);

        // Set up action listener for the search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
                if (!searchText.isEmpty()) {
                    try {
                        // Search for the patient in the database
                        List<Patient> patients = searchPatients(searchText);

                        if (!patients.isEmpty()) {
                            // Display a window with the search results
                            displaySearchResults(patients);
                        } else {
                            JOptionPane.showMessageDialog(panel, "No patients found with the given search criteria.",
                                    "Search Result", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        // Handle database errors
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(panel, "An error occurred while searching for patients.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    // ...

    private static List<Patient> searchPatients(String searchText) throws SQLException {
        List<Patient> patients = new ArrayList<>();

        // Replace these with your own database connection details
        String url = "jdbc:mysql://localhost:3306/your_database";
        String user = "your_username";
        String password = "your_password";

        // Connect to the database
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Prepare a patient search query
            String query = "SELECT * FROM patients WHERE name LIKE ? OR date_of_birth LIKE ? OR other_identifying_info LIKE ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                String searchPattern = "%" + searchText + "%";
                statement.setString(1, searchPattern);
                statement.setString(2, searchPattern);
                statement.setString(3, searchPattern);

                // Execute the query and process the results
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Patient patient = new Patient();
                        patients.add(patient);
                    }
                }
            }
        }

        return patients;
    }

    private static void displaySearchResults(List<Patient> patients) {
        // Implement a window to display the search results and allow the user to select
        // a patient
        // Once a patient is selected, display the patient's information in a new window
    }

    private static void createNavigationButtons(JPanel panel) {
        // Add quick navigation buttons for prescription management, referral
        // management, messaging, and analytics/reporting
    }
}
