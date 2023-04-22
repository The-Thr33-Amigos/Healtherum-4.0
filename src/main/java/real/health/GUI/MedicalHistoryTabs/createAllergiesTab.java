package real.health.GUI.MedicalHistoryTabs;

import java.sql.*;
import java.util.Collections;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;

import real.health.GUI.UserRole;
import real.health.SQL.*;
import java.awt.*;

public class createAllergiesTab {
    private UserRole userRole;

    public JComponent createAllergiesTab(String id, UserRole userRole) {
        this.userRole = userRole;
        JTable allergiesTable = new JTable();
        // populate the table with the patient's current medications
        try {
            // Create a connection to the database
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();

            // Create a SQL statement to retrieve the patient's current medications
            String sql = "SELECT name, type, reaction, severity FROM allergies WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();

            // Create a table model and populate it with the retrieved data
            DefaultTableModel tableModel = new DefaultTableModel(
                    new Object[] { "Name", "Type", "Reaction", "Severity" },
                    0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            while (result.next()) {
                tableModel.addRow(new Object[] { result.getString(1), result.getString(2), result.getString(3),
                        result.getString(4) });
            }
            allergiesTable.setModel(tableModel);

            // Clean up resources
            result.close();
            statement.close();
            con.close();

        } catch (ClassNotFoundException ex) {
            System.out.println("Error: unable to load MySQL JDBC driver");
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.out.println("Error: unable to connect to MySQL database");
            ex.printStackTrace();
        }

        // Create the add button and add an ActionListener
        // to upload the new medication to the SQL server
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create the form for entering the new medication details
                JFrame addAllergiesFrame = new JFrame("Add Allergy");
                addAllergiesFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                addAllergiesFrame.setSize(400, 200);
                addAllergiesFrame.setLayout(new GridLayout(5, 2, 10, 10));
                addAllergiesFrame.setLocationRelativeTo(null);

                // NOTE POSSIBLE XGAME MODE ON THESE
                String[] foodBorne = {"Crustacean Shellfish", "Eggs", "Fish", "Milk", "Peanuts", "Soybeans", "Tree Nuts", "Wheat"};
                String[] drugAllergens = {"Anti Inflammatory", "Anti seizure", "Antibiotics", "Antibody therapy", "Aspirin", "Chemotherapy drugs", "Hiv Drugs", "Insulin", "Muscle Relaxers", "Sufla"};
                String[] misc = {"Chemicals", "Dust mites", "Insect stings and bites", "Latex", "Mold", "Pet dander", "Pollen"};
                String[] typeNames = {"FoodBorne", "Drug", "Miscellaneous"};

                // Add form components for entering the medication details
                

                JLabel typeLabel = new JLabel("Type:");
                JComboBox<String> typeBox = new JComboBox<>(typeNames);
                addAllergiesFrame.add(typeLabel);
                addAllergiesFrame.add(typeBox);
                typeBox.setSelectedItem(null);

                JLabel nameLabel = new JLabel("Name:");
                JComboBox<String> nameBox = new JComboBox<>();
                addAllergiesFrame.add(nameLabel);
                addAllergiesFrame.add(nameBox);

                JLabel reactionLabel = new JLabel("Reaction:");
                
                String[] react = {"Hives", "Rashes", "Swelling", "Coughing", "Wheezing", "Shortness of Breath", "Anaphylaxis"};
                JComboBox<String> reactions = new JComboBox<>(react);
                reactions.setSelectedItem(null);

                addAllergiesFrame.add(reactionLabel);
                addAllergiesFrame.add(reactions);


                JLabel severityLabel = new JLabel("Severity:");
                String[] severityLevels = {"Mild", "Moderate", "Severe"};
                JComboBox<String> sevBox = new JComboBox<>();
                addAllergiesFrame.add(severityLabel);
                addAllergiesFrame.add(sevBox);

                typeBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String[] selection = {};
                        
                        String selectedType = (String) typeBox.getSelectedItem();
                        if (selectedType.equals("FoodBorne")) {
                            selection = foodBorne;
                        }
                        else if (selectedType.equals("Drug")) {
                            selection = drugAllergens;
                        }
                        else if (selectedType.equals("Miscellaneous")) {
                            selection = misc;
                        }
                        else {
                            String[] temp = {"No Selection"};
                            selection = temp;
                        }
                        
                        
                        nameBox.setModel(new DefaultComboBoxModel<>(selection));
                        nameBox.setSelectedItem(null);
                        nameBox.repaint();
                    }
                });

                reactions.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String reactSelect = (String) reactions.getSelectedItem();
                        String[] selection = {};
                        if (reactSelect.equals("Anaphylaxis")) {
                            String[] onlySevere = {"Severe"};
                            selection = onlySevere;
                        }
                        else {
                            selection  = severityLevels;
                        }
                        
                        
                        sevBox.setModel(new DefaultComboBoxModel<>(selection));
                        sevBox.setSelectedItem(null);
                        sevBox.repaint();
                    }
                });

                // Add a button for submitting the form
                JButton submitButton = new JButton("Submit");
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Get the values from the form fields
                        String allergyName = (String) nameBox.getSelectedItem();
                        String type = (String) typeBox.getSelectedItem();
                        String reaction = (String) reactions.getSelectedItem();
                        String severity = (String) sevBox.getSelectedItem();

                        // Upload the new medication to the SQL server
                        try {
                            // Load the MySQL JDBC driver
                            HealthConn newConnection = new HealthConn();
                            Connection con = newConnection.connect();

                            // Create a SQL statement to insert the new medication
                            String sql = "INSERT INTO allergies (id, name, type, reaction, severity) VALUES (?, ?, ?, ?, ?)";
                            PreparedStatement statement = con.prepareStatement(sql);
                            statement.setString(1, id);
                            statement.setString(2, allergyName);
                            statement.setString(3, type);
                            statement.setString(4, reaction);
                            statement.setString(5, severity);
                            statement.executeUpdate();

                            // Refresh the medications table to show the newly added medication
                            DefaultTableModel tableModel = (DefaultTableModel) allergiesTable.getModel();
                            tableModel.addRow(new Object[] { allergyName, type, reaction, severity });

                            // Clean up resources
                            statement.close();
                            con.close();
                            // Close the add medication frame
                            addAllergiesFrame.dispose();
                        } catch (ClassNotFoundException ex) {
                            System.out.println("Error: unable to load MySQL JDBC driver");
                            ex.printStackTrace();
                        } catch (SQLException ex) {
                            System.out.println("Error: unable to connect to MySQL database");
                            ex.printStackTrace();
                        }
                    }
                });

                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addAllergiesFrame.dispose();
                    }
                });

                addAllergiesFrame.add(cancelButton);
                addAllergiesFrame.add(submitButton);
                // Display the add medication frame
                addAllergiesFrame.setVisible(true);
            }
        });

        // Create the delete button and add an ActionListener
        // to delete the selected allergy
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row in the table
                int selectedRow = allergiesTable.getSelectedRow();

                // If no row is selected, display an error message
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(allergiesTable, "Please select a row to delete.");
                    return;
                }

                // Display a confirmation dialog
                int confirmation = JOptionPane.showConfirmDialog(allergiesTable, "Are you sure you want to delete the selected row?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                // If the user confirms the deletion, proceed
                if (confirmation == JOptionPane.YES_OPTION) {


                    // Get the primary key or unique identifier of the record from the selected row
                    // Assuming the first column of the table contains the primary key
                    String primaryKey = allergiesTable.getValueAt(selectedRow, 0).toString();

                    // Execute an SQL DELETE statement to delete
                    // the corresponding record from the database
                    try {
                        HealthConn newConnection = new HealthConn();
                        Connection con = newConnection.connect();
                        String sql = "DELETE FROM allergies WHERE id = ? AND name = ? AND type = ? AND reaction = ? AND severity = ?";
                        PreparedStatement statement = con.prepareStatement(sql);
                        statement.setString(1, id);
                        statement.setString(2, allergiesTable.getValueAt(selectedRow, 0).toString());
                        statement.setString(3, allergiesTable.getValueAt(selectedRow, 1).toString());
                        statement.setString(4, allergiesTable.getValueAt(selectedRow, 2).toString());
                        statement.setString(5, allergiesTable.getValueAt(selectedRow, 3).toString());
                        statement.executeUpdate();

                        // Close the statement and connection
                        statement.close();
                        con.close();

                        // Remove the selected row from the table
                        DefaultTableModel model = (DefaultTableModel) allergiesTable.getModel();
                        model.removeRow(selectedRow);

                    } catch (ClassNotFoundException | SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(allergiesTable, "An error occurred while deleting the record.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Create the medications tab panel and add the
        // medications table and add button
        JPanel allergyTabPanel = new JPanel(new BorderLayout());
        allergyTabPanel.add(new JScrollPane(allergiesTable), BorderLayout.CENTER);

        // Create a panel for the add button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JPanel addDeletePanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        // TODO: ADD COMMENT
        if (userRole == UserRole.PROVIDER) {
            addDeletePanel.add(addButton);
            addDeletePanel.add(deleteButton);
            buttonPanel.add(addDeletePanel);
            allergyTabPanel.add(buttonPanel, BorderLayout.PAGE_END);
        }

        return allergyTabPanel;
    }
}
