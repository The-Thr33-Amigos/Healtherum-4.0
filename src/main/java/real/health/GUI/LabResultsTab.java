package real.health.GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import com.formdev.flatlaf.FlatLightLaf;
import real.health.Patient.BloodTest;
import real.health.Patient.BloodItem;
import real.health.SQL.*;
import real.health.UTIL.CustomBooleanRenderer;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class LabResultsTab {

    private UserRole userRole;

    public LabResultsTab(UserRole userRole) {
        this.userRole = userRole;
    }

    public HashMap<Integer, BloodTest> bloodTestMap = new HashMap<>();
    public HashMap<Integer, Integer> uniqueMap = new HashMap<>();

    public ArrayList<Object> getColumnValues(JTable table, int columnIndex) {
        ArrayList<Object> columnValues = new ArrayList<>();
        for (int row = 0; row < table.getRowCount(); row++) {
            columnValues.add(table.getValueAt(row, columnIndex));
        }
        return columnValues;
    }

    

    // TODO add race as a parameter to be based on patient race
    public JComponent createLabResultsTab(String id) throws IOException {

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Failed");
        }

        JPanel labResultsPanel = new JPanel(new BorderLayout());

        // Create table to display lab results
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Test");
        model.addColumn("Result");
        model.addColumn("Date");
        model.addColumn("Test Interpretation");
        model.addColumn("Date of Report");
        model.addColumn("Signature");
        model.addColumn("Comments");
        JTable table = new JTable(model);
        

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    System.out.println("Double click");
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        BloodTest selectedBT = bloodTestMap.get(row);
                        JFrame bloodFrame = new JFrame(selectedBT.testName + " " + selectedBT.testDate);
                        bloodFrame.setSize(800, 600);
                        DefaultTableModel bloodTestModel = new DefaultTableModel() {
                            @Override
                            public boolean isCellEditable(int row, int column) {
                                return false;
                            }
                        };

                        bloodTestModel.addColumn("Name");
                        bloodTestModel.addColumn("Result");
                        bloodTestModel.addColumn("Lower Bound");
                        bloodTestModel.addColumn("Upper Bound");
                        bloodTestModel.addColumn("Units");
                        bloodTestModel.addColumn("Flag");

                        HashMap<String, BloodItem> newBloodMap = selectedBT.getMap();

                        for (HashMap.Entry<String, BloodItem> entry : newBloodMap.entrySet()) {
                            BloodItem current = entry.getValue();
                            bloodTestModel.addRow(new Object[] { current.getTestName(), current.getResult(),
                                    current.getLow(), current.getHigh(), current.getUnits(), current.getFlag() });
                        }

                        JTable bloodTable = new JTable(bloodTestModel);
                        int lastColumnIndex = bloodTable.getColumnCount() - 1;
                        bloodTable.getColumnModel().getColumn(lastColumnIndex).setCellRenderer(new CustomBooleanRenderer());
                        
                        JScrollPane scrollPane = new JScrollPane(bloodTable);

                        bloodFrame.add(scrollPane, BorderLayout.CENTER);
                        bloodFrame.setLocationRelativeTo(null);
                        bloodFrame.setVisible(true);

                    }
                }
            }
        });

        table.setEnabled(true);
        
        JScrollPane scrollPane = new JScrollPane(table);
        labResultsPanel.add(scrollPane, BorderLayout.CENTER);

        JButton newButton2 = new JButton("New Test");
        JButton nextButton2 = new JButton("Next");
        JButton cancelButton = new JButton("Cancel");
        JButton deleteButton = new JButton("Delete");
        nextButton2.setPreferredSize(new Dimension(200, nextButton2.getPreferredSize().height));
        cancelButton.setPreferredSize(new Dimension(200, cancelButton.getPreferredSize().height));

        JFrame newFrame = new JFrame("Test Information");
        JPanel nextButtonPanel = new JPanel();
        nextButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        nextButtonPanel.add(cancelButton);
        nextButtonPanel.add(nextButton2);

        String[] testName = { "Generic Blood Panel", "Liver Panel", "Kidney Panel"};
        JComboBox<String> nameCombo = new JComboBox<String>(testName);
        JLabel tNameLabel = new JLabel("Test Name:");

        String[] result = { "Normal", "Elevated", "Severe" };
        JComboBox<String> resultCombo = new JComboBox<String>(result);
        JLabel resultLabel = new JLabel("Result:");

        String[] interp = { "No Action", "Further Testing", "See Specialist" };
        JComboBox<String> interpCombo = new JComboBox<String>(interp);
        JLabel interpLabel = new JLabel("Interpretation:");

        JLabel sigLabel = new JLabel("Signature:");
        JTextField sigField = new JTextField(20);

        JLabel commentLabel = new JLabel("Comments:");
        JTextField commentField = new JTextField(20);
        newFrame.setSize(600, 400);
        newFrame.setLayout(new GridLayout(6, 2, 5, 5));
        newFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        newFrame.setLocationRelativeTo(null);

        JLabel emptyLabel = new JLabel();
        newFrame.add(tNameLabel);
        newFrame.add(nameCombo);
        // newFrame.add(resultLabel);
        // newFrame.add(resultCombo);
        newFrame.add(interpLabel);
        newFrame.add(interpCombo);
        newFrame.add(sigLabel);
        newFrame.add(sigField);
        newFrame.add(commentLabel);
        newFrame.add(commentField);
        newFrame.add(emptyLabel);
        newFrame.add(nextButtonPanel);

        newButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // newFrame.pack();
                newFrame.setVisible(true);

            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newFrame.dispose();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();

                // If no row is selected, display an error message
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(table, "Please select a test to delete.");
                    return;
                }

                // Display a confirmation dialog
                int confirmation = JOptionPane.showConfirmDialog(table, "Are you sure you want to delete the selected test?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                if (confirmation == JOptionPane.YES_OPTION) {
                    int unique = uniqueMap.get(selectedRow);
                    try {
                        HealthConn newConnection = new HealthConn();
                        Connection conn = newConnection.connect();
                        String sql = "DELETE FROM bloodtest WHERE `unique` = ?";
                        PreparedStatement statement = conn.prepareStatement(sql);
                        statement.setInt(1, unique);

                        statement.executeUpdate();

                        statement.close();
                        conn.close();

                        bloodTestMap.remove(selectedRow);
                        model.removeRow(selectedRow);
                        uniqueMap.remove(selectedRow);
                    }
                    catch (SQLException sqlError) {
                        sqlError.printStackTrace();
                    }
                    catch (ClassNotFoundException cle) {
                        cle.printStackTrace();
                    }
                }
            }
        });

        nextButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String testName = (String) nameCombo.getSelectedItem();
                BloodTest newBloodTest = new BloodTest("Female", "White", testName);
                LocalDate currentDate = LocalDate.now();
                LocalDate oldDate = currentDate.minusDays(4);

                String curr = currentDate.toString();
                String old = oldDate.toString();

                newBloodTest.testDate = old;
                newBloodTest.resultDate = curr;

                newBloodTest.testName = testName;

                // String resultSelect = (String) resultCombo.getSelectedItem();
                int resultSelect = newBloodTest.flagSeverity();
                newBloodTest.resultIndicator = result[resultSelect];

                String interpSelect = (String) interpCombo.getSelectedItem();
                newBloodTest.testInterp = interpSelect;

                String sigSelect = sigField.getText();
                newBloodTest.signature = sigSelect;

                String comSelect = commentField.getText();
                newBloodTest.comment = comSelect;

                nameCombo.setSelectedIndex(0);
                resultCombo.setSelectedIndex(0);
                interpCombo.setSelectedIndex(0);
                sigField.setText("");
                commentField.setText("");

                JFrame bloodFrame = new JFrame(testName + " " + newBloodTest.testDate);
                bloodFrame.setSize(800, 600);

                DefaultTableModel model = new DefaultTableModel() {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return column == 1;
                    }
                };

                model.addColumn("Name");
                model.addColumn("Result");
                model.addColumn("Lower Bound");
                model.addColumn("Upper Bound");
                model.addColumn("Units");
                model.addColumn("Flag");

                HashMap<String, BloodItem> newBloodMap = newBloodTest.getMap();

                for (HashMap.Entry<String, BloodItem> entry : newBloodMap.entrySet()) {
                    BloodItem current = entry.getValue();
                    model.addRow(new Object[] { current.getTestName(), current.getResult(), current.getLow(),
                            current.getHigh(), current.getUnits(), current.getFlag() });
                }

                JTable bloodTable = new JTable(model);

                int lastColumnIndex = bloodTable.getColumnCount() - 1;
                bloodTable.getColumnModel().getColumn(lastColumnIndex).setCellRenderer(new CustomBooleanRenderer());
                JScrollPane scrollPane = new JScrollPane(bloodTable);

                JButton saveButton = new JButton("Save");
                JButton cancelButton = new JButton("Save");

                JPanel saveButtonPanel = new JPanel();
                saveButtonPanel.setLayout(new BorderLayout());

                saveButtonPanel.add(cancelButton);
                saveButtonPanel.add(saveButton);

                bloodFrame.add(scrollPane, BorderLayout.CENTER);
                bloodFrame.add(saveButtonPanel, BorderLayout.SOUTH);
                bloodFrame.setLocationRelativeTo(null);
                bloodFrame.setVisible(true);

                // Add an action listener to the "Save" button
                saveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        newFrame.dispose();
                        // Add the new test to the table on the first screen
                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        int rowIndex = table.getRowCount();
                        bloodTestMap.put(rowIndex, newBloodTest);

                        try {
                            BTest convert = new BTest(id);
                            String json = convert.bloodToJSON(newBloodTest);

                            HealthConn newConnection = new HealthConn();
                            Connection conn = newConnection.connect();
                            Statement firstStatement = conn.createStatement();
                            ResultSet result = firstStatement.executeQuery("SHOW TABLE STATUS LIKE 'bloodtest'");
                            if (result.next()) {
                                int nextAutoInc = result.getInt("Auto_increment");
                                String sql = "INSERT INTO bloodtest (id, test, `unique`) VALUES (?, ?, ?)";

                                PreparedStatement statement = conn.prepareStatement(sql);
                                statement.setString(1, id);
                                statement.setString(2, json);
                                statement.setInt(3, nextAutoInc);
                                statement.executeUpdate();

                                statement.close();
                                conn.close();
                            }

                        } catch (ClassNotFoundException c) {
                            // TODO: Error handling
                            System.err.println("ClassNotFoundException");
                            c.printStackTrace();
                        } catch (SQLException ex) {
                            // TODO: Error handling
                            System.err.println("SQLException");
                            ex.printStackTrace();
                        }

                        model.addRow(new Object[] { newBloodTest.testName, newBloodTest.resultIndicator,
                                newBloodTest.testDate,
                                newBloodTest.testInterp, newBloodTest.resultDate, newBloodTest.signature,
                                newBloodTest.comment });

                        // Clear the test information fields
                        nameCombo.setSelectedIndex(0);
                        resultCombo.setSelectedIndex(0);
                        interpCombo.setSelectedIndex(0);
                        sigField.setText("");
                        commentField.setText("");

                        // Show the first screen
                        labResultsPanel.setVisible(false);

                        bloodFrame.dispose();

                    }

                });
            }
        });

        // Add button panel to the bottom right of labResultsPanel

        try {
            HealthConn newConnection = new HealthConn();
            Connection conn = newConnection.connect();
            String sql = "SELECT test, `unique` FROM bloodtest WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result2 = statement.executeQuery();

            BTest newTest = new BTest(id);
            int count = 0;
            while (result2.next()) {
                BloodTest newBlood;
                newBlood = newTest.jsonToBT(result2.getString(1));
                int unique = result2.getInt(2);
                newTest.addToList(newBlood);
                model.addRow(new Object[] { newBlood.testName, newBlood.resultIndicator, newBlood.testDate,
                        newBlood.testInterp, newBlood.resultDate, newBlood.signature, newBlood.comment });
                bloodTestMap.put(count, newBlood);
                uniqueMap.put(count, unique);
                count++;
            }

            table.setModel(model);

            // Clean up resources
            result2.close();
            statement.close();
            conn.close();

        } catch (ClassNotFoundException c) {
            // TODO: Error handling
            c.printStackTrace();
        } catch (SQLException e) {
            // TODO: Error handling
            e.printStackTrace();
        }

        // Test Information
        JPanel testInformationPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 5, 10, 5);
        JPanel statusPanel = new JPanel();
        JPanel statusPanel1 = new JPanel();
        JPanel statusPanel2 = new JPanel();

        JLabel testDateLabel = new JLabel();
        JLabel accuracyLabel = new JLabel();
        JLabel statusLabel = new JLabel();

        JButton predBtn = new JButton();
        JLabel testTypeLabel = new JLabel();
        JLabel statusLabel1 = new JLabel();
        JLabel accuracyLabel1 = new JLabel();

        JButton predBtn1 = new JButton();

        JButton predBtn2 = new JButton();
        JLabel orderingProviderLabel = new JLabel();
        JLabel statusLabel2 = new JLabel();
        JLabel accuracyLabel2 = new JLabel();

        if (userRole == UserRole.PROVIDER) {
            int optInStatus;
            try {
                optInStatus = checkOptInStatus(id);
                if (optInStatus == 1) {
                    // Add prediction buttons only if the patient has opted in to use ML

                    testDateLabel.setText("Heart Disease");
                    constraints.gridx = 0;
                    constraints.gridy = 1;
                    testInformationPanel.add(testDateLabel, constraints);

                    constraints.gridx = 0;
                    constraints.gridy = 2;
                    statusPanel.add(statusLabel);
                    testInformationPanel.add(statusPanel, constraints);

                    constraints.gridx = 0;
                    constraints.gridy = 3;
                    testInformationPanel.add(accuracyLabel, constraints);

                    predBtn.setText("Predict");
                    constraints.gridx = 0;
                    constraints.gridy = 4;
                    testInformationPanel.add(predBtn, constraints);

                    testTypeLabel.setText("Kidney Disease");
                    constraints.gridx = 0;
                    constraints.gridy = 5;
                    testInformationPanel.add(testTypeLabel, constraints);

                    constraints.gridx = 0;
                    constraints.gridy = 6;
                    statusPanel1.add(statusLabel1, constraints);
                    testInformationPanel.add(statusPanel1, constraints);

                    constraints.gridx = 0;
                    constraints.gridy = 7;
                    testInformationPanel.add(accuracyLabel1, constraints);

                    predBtn1.setText("Predict");
                    constraints.gridx = 0;
                    constraints.gridy = 8;
                    testInformationPanel.add(predBtn1, constraints);

                    orderingProviderLabel.setText("Diabetes");
                    constraints.gridx = 0;
                    constraints.gridy = 9;
                    testInformationPanel.add(orderingProviderLabel, constraints);

                    constraints.gridx = 0;
                    constraints.gridy = 10;
                    statusPanel2.add(statusLabel2, constraints);
                    testInformationPanel.add(statusPanel2, constraints);

                    constraints.gridx = 0;
                    constraints.gridy = 11;
                    testInformationPanel.add(accuracyLabel2, constraints);

                    predBtn2.setText("Predict");
                    constraints.gridx = 0;
                    constraints.gridy = 12;
                    testInformationPanel.add(predBtn2, constraints);

                    predBtn.setEnabled(true);
                    predBtn1.setEnabled(true);
                    predBtn2.setEnabled(true);
                }
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
        JLabel optInLabel = new JLabel();

        JButton acceptButton = new JButton();
        JButton declineButton = new JButton();

        if (userRole == UserRole.PATIENT) {
            int optInStatus = 0;

            try {
                optInStatus = checkOptInStatus(id);
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            if (optInStatus != 2 && optInStatus != 1) {
                optInLabel.setText("Machine Learning?");
                acceptButton.setText("Accept");
                declineButton.setText("Decline");

                constraints.gridx = 0;
                constraints.gridy = 14;
                testInformationPanel.add(optInLabel, constraints);
            }
        }

        if (userRole == UserRole.PATIENT) {
            int optInStatus = 0;
            try {
                optInStatus = checkOptInStatus(id);
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            try {
                optInStatus = checkOptInStatus(id);
                System.out.println(optInStatus);
                System.out.println(optInStatus);

                if (optInStatus == 0) {
                    System.out.println(optInStatus);
                    constraints.gridx = 0;
                    constraints.gridy = 15;
                    testInformationPanel.add(acceptButton, constraints);

                    constraints.gridx = 1;
                    constraints.gridy = 15;
                    testInformationPanel.add(declineButton, constraints);
                    predBtn.setEnabled(false);
                    predBtn1.setEnabled(false);
                    predBtn2.setEnabled(false);
                }
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        labResultsPanel.add(testInformationPanel, BorderLayout.WEST);
        // Initially disable predict buttons

        // Action listener for accept button
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a new window with a JTextField for initials and a "Confirm" button
                JFrame initialsFrame = new JFrame("Enter Initials");
                initialsFrame.setSize(300, 150);
                initialsFrame.setLocationRelativeTo(null);
                initialsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                initialsFrame.setLayout(new FlowLayout());

                JLabel initialsLabel = new JLabel("Enter your initials:");
                JTextField initialsField = new JTextField(5);
                JButton confirmButton = new JButton("Confirm");

                initialsFrame.add(initialsLabel);
                initialsFrame.add(initialsField);
                initialsFrame.add(confirmButton);
                initialsFrame.setVisible(true);

                // Add action listener for "Confirm" button
                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Check if the entered initials match the user's name
                        String enteredInitials = initialsField.getText().toUpperCase();
                        // Fetch the user's first and last name from the SQL database using the user ID
                        try {
                            // Load the MySQL JDBC driver
                            HealthConn newConnection = new HealthConn();
                            Connection con = newConnection.connect();
                            String sql = "SELECT firstName, lastName FROM basic WHERE id = ?";
                            PreparedStatement statement = con.prepareStatement(sql);
                            statement.setString(1, id);
                            ResultSet result = statement.executeQuery();

                            if (result.next()) {
                                // Get the user's first and last name
                                String firstName = result.getString("firstName");
                                String lastName = result.getString("lastName");
                                System.out.println(firstName.substring(0, 1));
                                System.out.println(lastName.substring(0, 1));
                                // Get the first letters of the user's first and last name
                                String userNameInitials = (firstName.substring(0, 1) + lastName.substring(0, 1))
                                        .toUpperCase();

                                System.out.println(userNameInitials);

                                // Check if the entered initials match the user's name initials
                                if (enteredInitials.equals(userNameInitials)) {
                                    // Enable the "Predict" buttons
                                    predBtn.setEnabled(true);
                                    predBtn1.setEnabled(true);
                                    predBtn2.setEnabled(true);

                                    // Update the machine learning opt-in status in the SQL database
                                    sql = "INSERT INTO machinelearning (id, status) VALUES (?, ?) ON DUPLICATE KEY UPDATE status=?";
                                    statement = con.prepareStatement(sql);
                                    statement.setString(1, id);
                                    statement.setInt(2, 1);
                                    statement.setInt(3, 1);
                                    statement.executeUpdate();

                                    // Close the initials window
                                    initialsFrame.dispose();

                                    // Remove the accept and decline buttons and update the panel
                                    acceptButton.setVisible(false);
                                    declineButton.setVisible(false);
                                    optInLabel.setVisible(false);
                                    testInformationPanel.revalidate();
                                    testInformationPanel.repaint();
                                } else {
                                    // Show a message if the entered initials don't match
                                    JOptionPane.showMessageDialog(initialsFrame,
                                            "Entered initials do not match the user's name.", "Error",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
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
                    }
                });

            }
        });

        // Action listener for decline button
        declineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Your opt-out logic goes here
                // Show a confirmation dialog when the user declines
                int confirmDecline = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to decline Machine Learning?",
                        "Confirm Decline",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirmDecline == JOptionPane.YES_OPTION) {
                    // Disable the "Predict" buttons
                    predBtn.setEnabled(false);
                    predBtn1.setEnabled(false);
                    predBtn2.setEnabled(false);

                    // Update the machine learning opt-in status in the SQL database
                    try {
                        // Load the MySQL JDBC driver
                        HealthConn newConnection = new HealthConn();
                        Connection con = newConnection.connect();
                        String sql = "INSERT INTO machinelearning (id, status) VALUES (?, ?) ON DUPLICATE KEY UPDATE status=?";
                        PreparedStatement statement = con.prepareStatement(sql);
                        statement.setString(1, id);
                        statement.setInt(2, 2); // Set status to 2 when declined
                        statement.setInt(3, 2); // Set status to 2 when declined
                        statement.executeUpdate();

                        // Clean up resources
                        statement.close();
                        con.close();
                    } catch (ClassNotFoundException ex) {
                        System.out.println("Error: unable to load MySQL JDBC driver");
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        System.out.println("Error: unable to connect to MySQL database");
                        ex.printStackTrace();
                    }
                    // Remove the accept and decline buttons and update the panel
                    acceptButton.setVisible(false);
                    declineButton.setVisible(false);
                    optInLabel.setVisible(false);
                    testInformationPanel.revalidate();
                    testInformationPanel.repaint();

                }
            }
        });

        predBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    heartDisease newHD = new heartDisease(id);
                    ArrayList<String> predic = newHD.predict();
                    String acc = predic.get(0);
                    acc = acc.substring(2, 4) + "%";
                    accuracyLabel.setText("Accuracy: " + acc);

                    String pred = predic.get(1);

                    if ("0".equals(pred)) {
                        statusLabel.setText("Absent");
                        statusPanel.setBackground(Color.GREEN);
                        statusPanel.add(statusLabel);
                    } else if ("1".equals(pred)) {
                        statusLabel.setText("Presence Detected");
                        statusPanel.setBackground(Color.RED);
                        statusPanel.add(statusLabel);
                    } else {
                        statusLabel.setText(pred);
                        statusPanel.setBackground(Color.GRAY);
                        statusPanel.add(statusLabel);
                    }
                    System.out.println(pred);

                    predBtn.setVisible(false);
                    predBtn.setEnabled(false);

                } catch (IOException ie) {
                    ie.printStackTrace();
                }

            }
        });

        predBtn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    kidneyDisease newKD = new kidneyDisease();
                    ArrayList<String> predic = newKD.kPredict();
                    String acc = predic.get(0);
                    acc = acc.substring(2, 4) + "%";
                    accuracyLabel1.setText("Accuracy: " + acc);

                    String pred1 = predic.get(1);

                    if ("0".equals(pred1)) {
                        statusLabel1.setText("Absence");
                        statusPanel1.setBackground(Color.GREEN);
                        statusPanel1.add(statusLabel1);
                    } else if ("1".equals(pred1)) {
                        statusLabel1.setText("Presence Detected");
                        statusPanel1.setBackground(Color.RED);
                        statusPanel1.add(statusLabel1);
                    } else {
                        statusLabel1.setText(pred1);
                        statusPanel1.setBackground(Color.GRAY);
                        statusPanel1.add(statusLabel1);
                    }
                    System.out.println(pred1);

                    predBtn1.setVisible(false);
                    predBtn1.setEnabled(false);

                } catch (IOException ie) {
                    // TODO: Error handling
                    ie.printStackTrace();
                }

            }
        });

        predBtn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    diabetes newDB = new diabetes();
                    ArrayList<String> predic = newDB.kPredict();
                    String acc = predic.get(0);
                    acc = acc.substring(2, 4) + "%";
                    accuracyLabel2.setText("Accuracy: " + acc);

                    String pred2 = predic.get(1);

                    if ("0".equals(pred2)) {
                        statusLabel2.setText("Absence");
                        statusPanel2.setBackground(Color.GREEN);
                        statusPanel2.add(statusLabel2);
                    } else if ("1".equals(pred2)) {
                        statusLabel2.setText("Presence Detected");
                        statusPanel2.setBackground(Color.RED);
                        statusPanel2.add(statusLabel2);
                    } else {
                        statusLabel2.setText(pred2);
                        statusPanel2.setBackground(Color.GRAY);
                        statusPanel2.add(statusLabel2);
                    }
                    System.out.println(pred2);

                    predBtn2.setVisible(false);
                    predBtn2.setEnabled(false);

                } catch (IOException ie) {
                    // TODO: Error handling
                    ie.printStackTrace();
                }

            }
        });


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));        
        JPanel addDeletePanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        // Download/Print Options
        JPanel downloadPrintPanel = new JPanel();
        JButton downloadButton = new JButton("Download");
        JButton printButton = new JButton("Print");
        if (userRole == UserRole.PATIENT) {
            downloadPrintPanel.add(downloadButton);
        }

        downloadPrintPanel.add(printButton);
        if (userRole == UserRole.PROVIDER) {
            downloadPrintPanel.add(newButton2);
            downloadPrintPanel.add(deleteButton);
        }

        labResultsPanel.add(downloadPrintPanel, BorderLayout.SOUTH);

        return labResultsPanel;
    }

    private int checkOptInStatus(String userId) throws SQLException {
        int optInStatus = 0;

        try {
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();
            String sql = "SELECT status FROM machinelearning WHERE id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                optInStatus = rs.getInt("status");
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Error: unable to load MySQL JDBC driver");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error: unable to connect to MySQL database");
            e.printStackTrace();
        }

        return optInStatus;
    }

}
