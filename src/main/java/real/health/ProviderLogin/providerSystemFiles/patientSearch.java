package real.health.ProviderLogin.providerSystemFiles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.*;

import real.health.GUI.UserRole;
import real.health.ProviderLogin.providerSystem;
import real.health.ProviderLogin.providerSystem.User;
import real.health.SQL.HealthConn;

public class patientSearch {
    
    public static void createPatientSearch(JPanel panel) {
        // Create a new panel with FlowLayout for the search components
        JPanel searchComponentsPanel = new JPanel(new FlowLayout());
        panel.add(searchComponentsPanel, BorderLayout.NORTH);

        // Create a search label
        JLabel searchLabel = new JLabel("Search Patient:");
        searchComponentsPanel.add(searchLabel);

        // Create a search text field
        JTextField searchField = new JTextField(20);
        searchComponentsPanel.add(searchField);

        // Create a search button
        JButton searchButton = new JButton("Search");
        searchComponentsPanel.add(searchButton);

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

    // Prepare a patient search query
    private static List<User> searchPatients(String searchText) throws SQLException, ClassNotFoundException {
        List<User> users = new ArrayList<>();

        // Use the HealthConn class to establish a connection
        HealthConn newConnection = new HealthConn();
        Connection connection = newConnection.connect();

        // Prepare a patient search query
        String query = "SELECT id, firstName, lastName, bdate, phone, mailing FROM basic WHERE firstName LIKE ? OR lastName LIKE ? OR bdate LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            String searchPattern = "%" + searchText + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);

            // Execute the query and process the results
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getString("id"));
                    user.setFirstName(resultSet.getString("firstName"));
                    user.setLastName(resultSet.getString("lastName"));
                    user.setBdate(resultSet.getString("bdate"));
                    user.setPhone(resultSet.getString("phone"));
                    user.setAddress(resultSet.getString("mailing"));
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
        // Create a table model for the search results
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableModel.addColumn("First Name");
        tableModel.addColumn("Last Name");
        tableModel.addColumn("Birthdate");
        tableModel.addColumn("Phone Number");
        tableModel.addColumn("Address");

        for (User user : users) {
            tableModel.addRow(new Object[] { user.getFirstName(), user.getLastName(), user.getBdate(), user.getPhone(),
                    user.getAddress() });
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
                    providerSystem.viewPatientInfo(selectedUser, UserRole.PROVIDER);
                    searchResultsFrame.dispose();
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
}
