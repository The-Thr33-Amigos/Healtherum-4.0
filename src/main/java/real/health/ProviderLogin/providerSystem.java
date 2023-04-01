package real.health.ProviderLogin;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.io.IOException;

import real.health.GUI.UserRole;
import real.health.Patient.Patient;
import real.health.PatientLogin.patientInformationSystem;
import real.health.SQL.*;
import java.awt.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class providerSystem {

    public static class User {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static void initialize() {
        // Initialize the home screen
        JFrame homeScreen = new JFrame("Provider Home");
        homeScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homeScreen.setSize(800, 600);
        homeScreen.setLocationRelativeTo(null);

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
                        List<User> users = searchPatients(searchText);

                        if (!users.isEmpty()) {
                            // Display a window with the search results
                            displaySearchResults(users);
                        } else {
                            JOptionPane.showMessageDialog(panel, "No patients found with the given search criteria.",
                                    "Search Result", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        // Handle database errors
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(panel, "An error occurred while searching for patients.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (ClassNotFoundException ex) {
                        // Handle ClassNotFoundException
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(panel, "An error occurred while connecting to the database.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

    }

    private static List<User> searchPatients(String searchText) throws SQLException, ClassNotFoundException {
        List<User> users = new ArrayList<>();

        // Use the HealthConn class to establish a connection
        HealthConn newConnection = new HealthConn();
        Connection connection = newConnection.connect();

        // Prepare a patient search query
        String query = "SELECT * FROM basic WHERE name LIKE ? OR bdate LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            String searchPattern = "%" + searchText + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);

            // Execute the query and process the results
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setName(resultSet.getString("name"));
                    users.add(user);
                }
            }
        }

        // Close the connection
        connection.close();

        return users;
    }

    private static void displaySearchResults(List<User> users) {
        // Create a new JFrame for displaying the search results
        JFrame searchResultsFrame = new JFrame("Search Results");
        searchResultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        searchResultsFrame.setSize(400, 300);
        searchResultsFrame.setLocationRelativeTo(null);

        // Create a table model for the search results
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Name");
        tableModel.addColumn("Birthdate");
        tableModel.addColumn("Phone Number");
        tableModel.addColumn("Address");

        for (User user : users) {
            tableModel.addRow(new Object[] { user.getName() });
        }

        // Create a table for displaying the search results
        JTable resultsTable = new JTable(tableModel);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add the search results table to the search results JFrame
        searchResultsFrame.add(new JScrollPane(resultsTable), BorderLayout.CENTER);

        // Create a "View" button for viewing the selected patient's info
        JButton viewButton = new JButton("View");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = resultsTable.getSelectedRow();
                if (selectedRow != -1) {
                    User selectedUser = users.get(selectedRow);
                    viewPatientInfo(selectedUser, UserRole.PROVIDER);
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchResultsFrame.dispose();
            }
        });

        // Add the "View" button to the search results JFrame
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewButton);
        buttonPanel.add(cancelButton);
        searchResultsFrame.add(buttonPanel, BorderLayout.SOUTH);

        // Display the search results JFrame
        searchResultsFrame.setVisible(true);
    }

    private static void viewPatientInfo(User user, UserRole role) {
        try {
            // Show the loading screen here
            JFrame loadingFrame = new JFrame("Loading...");
            loadingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            loadingFrame.setSize(400, 100);
            JPanel panel = new JPanel(new BorderLayout());
            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setValue(0);
            progressBar.setStringPainted(true);
            panel.add(progressBar, BorderLayout.CENTER);
            loadingFrame.add(panel);
            loadingFrame.setLocationRelativeTo(null);
            loadingFrame.setVisible(true);

            // Create a SwingWorker object to execute patientInformationSystem on a separate
            // thread
            SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
                JFrame patientInfoFrame;

                @Override
                protected Void doInBackground() throws Exception {
                    int progress = 0;
                    while (progress < 100) {
                        progressBar.setValue(progressBar.getValue());
                        // Increment the progress bar every 5ms
                        Thread.sleep(5);
                        progress++;
                        progressBar.setValue(progress);
                        if (progress == 50) {
                            patientInfoFrame = (JFrame) patientInformationSystem
                                    .patientInformationSystem(String.valueOf(user.getId()), progressBar, role);
                            patientInfoFrame.setTitle("Patient Info - " + user.getName());
                            patientInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            patientInfoFrame.setSize(1000, 600);
                            patientInfoFrame.setVisible(false);
                        } else {
                            progressBar.setValue(progressBar.getValue());
                        }
                    }
                    return null;
                }

                @Override
                protected void done() {
                    // Once patientInformationSystem has finished loading, dispose of the loading
                    // screen and show the patient's medical records JFrame
                    loadingFrame.dispose();
                    patientInfoFrame.setVisible(true);
                }
            };

            // Schedule the SwingWorker to be executed on the Event Dispatch Thread
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    worker.execute();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while opening the Patient Information System.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void createNavigationButtons(JPanel panel) {
        // Add quick navigation buttons for prescription management, referral
        // management, messaging, and analytics/reporting
    }
}
