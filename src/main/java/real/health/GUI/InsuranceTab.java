package real.health.GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import real.health.SQL.*;
import real.health.Patient.BloodTest;
import real.health.Patient.Patient;
import real.health.Patient.BloodItem;
import real.health.SQL.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import real.health.SQL.HealthConn;

public class InsuranceTab {

    private static boolean DEBUG = false;

    public static JComponent createInsuranceTab(String id) {
        System.out.println("Inside createInsuranceTab method");
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        // Create the table to display insurance information
        InsuranceTableModel model = new InsuranceTableModel(id);
        JTable table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(500, 120));
        table.setFillsViewportHeight(true);

        // Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);

        // Add the "New" button
        JButton newButton = new JButton("New");
        panel.add(newButton);

        // Add the "Delete" button
        JButton deleteButton = new JButton("Delete");
        panel.add(deleteButton);

        // Add the "Edit" button
        JButton editButton = new JButton("Edit");
        editButton.setVisible(false);
        panel.add(editButton);

        // Add the "Submit" button
        JButton submitButton = new JButton("Submit");
        submitButton.setVisible(false);
        panel.add(submitButton);

        // Add the "Upload Insurance Card" button
        JButton uploadButton = new JButton("Upload Insurance Card");
        panel.add(uploadButton);

        // Add the new insurance info form
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JLabel providerLabel = new JLabel("Provider Name:");
        JTextField providerField = new JTextField();
        JLabel policyNumberLabel = new JLabel("Policy Number:");
        JTextField policyNumberField = new JTextField();
        JLabel groupNumberLabel = new JLabel("Group Number:");
        JTextField groupNumberField = new JTextField();
        JLabel policyHolderNameLabel = new JLabel("Policy Holder Name:");
        JTextField policyHolderNameField = new JTextField();
        JLabel policyHolderDOBLabel = new JLabel("Policy Holder DOB:");
        JTextField policyHolderDOBField = new JTextField();
        JLabel policyHolderSSNLabel = new JLabel("Policy Holder SSN:");
        JTextField policyHolderSSNField = new JTextField();

        formPanel.add(providerLabel);
        formPanel.add(providerField);
        formPanel.add(policyNumberLabel);
        formPanel.add(policyNumberField);
        formPanel.add(groupNumberLabel);
        formPanel.add(groupNumberField);
        formPanel.add(policyHolderNameLabel);
        formPanel.add(policyHolderNameField);
        formPanel.add(policyHolderDOBLabel);
        formPanel.add(policyHolderDOBField);
        formPanel.add(policyHolderSSNLabel);
        formPanel.add(policyHolderSSNField);

        panel.add(formPanel);
        formPanel.setVisible(false);

        // Action listener for "New" button
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.setEnabled(false);
                editButton.setVisible(false);
                submitButton.setVisible(false);
                formPanel.setVisible(true);
            }
        });

        // Action listener for "Delete" button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = table.getSelectedRows();
                List<Integer> insuranceIds = new ArrayList<>();
                for (int selectedRow : selectedRows) {
                    String insuranceId = (String) model.getValueAt(selectedRow, 0);
                    insuranceIds.add(Integer.parseInt(insuranceId));
                }
                model.deleteInsurance(insuranceIds);
                table.clearSelection();
            }
        });

        // Action listener for "Edit" button
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.setEnabled(true);
                editButton.setVisible(false);
                submitButton.setVisible(true);
            }
        });

        // Action listener for "Submit" button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Save the new insurance information
                String provider = providerField.getText();
                String policyNumber = policyNumberField.getText();
                String groupNumber = groupNumberField.getText();
                String policyHolderName = policyHolderNameField.getText();
                String policyHolderDOB = policyHolderDOBField.getText();
                String policyHolderSSN = policyHolderSSNField.getText();
                table.setEnabled(false);
                editButton.setVisible(true);
                submitButton.setVisible(false);
                formPanel.setVisible(false);
            }
        });

        // Action listener for "Upload Insurance Card" button
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        BufferedImage insuranceCard = ImageIO.read(file);
                        // Save the image to the database or file system here
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        return panel;

    }

    public static class InsuranceTableModel extends AbstractTableModel {
        private String[] columnNames = { "Insurance ID", "Provider Name", "Policy Number", "Group Number",
                "Policy Holder Name", "Policy Holder DOB", "Policy Holder SSN" };
        private List<Object[]> data = new ArrayList<>();
        private String id;
        private HealthConn conn;
        private String insuranceProvider;
        private String policyNumber;
        private String groupNumber;

        public InsuranceTableModel(String id) {
            this.id = id;
            this.conn = new HealthConn();
            loadData(id);
        }

        public void createNewInsurance() {
            // Create new insurance data with empty fields
            Object[] newData = new Object[] {
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    ""
            };
            data.add(newData);
            fireTableRowsInserted(data.size() - 1, data.size() - 1);
        }

        public void loadData(String id) {
            // Create a connection to the database
            try {
                HealthConn newConnection = new HealthConn();
                Connection con = newConnection.connect();

                // Create a SQL statement to retrieve insurance data
                String sql = "SELECT * FROM insurance WHERE id = ?";
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setString(1, this.id);
                ResultSet result = statement.executeQuery();

                // Update insurance object with retrieved data
                if (result.next()) {
                    this.insuranceProvider = result.getString("provider");
                    this.policyNumber = result.getString("policy_number");
                    this.groupNumber = result.getString("group_number");
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

        public void deleteInsurance(List<Integer> insuranceIds) {
            try {
                HealthConn connection = new HealthConn();
                Connection con = connection.connect();
                for (Integer insuranceId : insuranceIds) {
                    PreparedStatement preparedStatement = con
                            .prepareStatement("DELETE FROM insurance WHERE insurance_id = ?");
                    preparedStatement.setInt(1, insuranceId);
                    preparedStatement.executeUpdate();
                }
                loadData();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        public void submitChanges() {
            try {
                HealthConn connection = new HealthConn();
                Connection con = connection.connect();
                for (Object[] rowData : data) {
                    if (rowData[0].equals("")) {
                        // This is a new insurance record
                        PreparedStatement preparedStatement = con.prepareStatement(
                                "INSERT INTO insurance (patient_id, provider_name, policy_number, group_number, policy_holder_name, policy_holder_dob, policy_holder_ssn) VALUES (?, ?, ?, ?, ?, ?, ?)");
                        preparedStatement.setString(1, id);
                        preparedStatement.setString(2, (String) rowData[1]);
                        preparedStatement.setString(3, (String) rowData[2]);
                        preparedStatement.setString(4, (String) rowData[3]);
                        preparedStatement.setString(5, (String) rowData[4]);
                        preparedStatement.setDate(6, (java.sql.Date) rowData[5]);
                        preparedStatement.setString(7, (String) rowData[6]);
                        preparedStatement.executeUpdate();
                    }
                }
                loadData(id);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        public void loadData() {
            // Create a connection to the database
            try {
                HealthConn connection = new HealthConn();
                Connection con = connection.connect();

                // Create a SQL statement to retrieve insurance data
                String sql = "SELECT * FROM insurance WHERE patient_id = ?";
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setString(1, this.id);
                ResultSet result = statement.executeQuery();

                // Clear the current data list
                data.clear();

                // Update insurance object with retrieved data
                while (result.next()) {
                    Object[] newData = new Object[] {
                            result.getInt("insurance_id"),
                            result.getString("provider_name"),
                            result.getString("policy_number"),
                            result.getString("group_number"),
                            result.getString("policy_holder_name"),
                            result.getDate("policy_holder_dob"),
                            result.getString("policy_holder_ssn")
                    };
                    data.add(newData);
                }

                // Clean up resources
                result.close();
                statement.close();
                con.close();

                fireTableDataChanged();

            } catch (ClassNotFoundException ex) {
                System.out.println("Error: unable to load MySQL JDBC driver");
                ex.printStackTrace();
            } catch (SQLException ex) {
                System.out.println("Error: unable to connect to MySQL database");
                ex.printStackTrace();
            }
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            return data.get(row)[col];
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell. If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            // Note that the field name column is not editable
            if (col == 0) {
                return false;
            } else {
                return true;
            }
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            if (DEBUG) {
                System.out.println("Setting value at " + row + "," + col
                        + " to " + value
                        + " (an instance of "
                        + value.getClass() + ")");
            }

            data.get(row)[col] = value;
            fireTableCellUpdated(row, col);

            if (DEBUG) {
                System.out.println("New value of data:");
                printDebugData();
            }
        }

        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();

            for (int i = 0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j = 0; j < numCols; j++) {
                    System.out.print("  " + getValueAt(i, j));
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }

    }

}
