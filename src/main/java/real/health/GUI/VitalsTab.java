package real.health.GUI;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import real.health.SQL.HealthConn;

public class VitalsTab {

    private UserRole userRole;

    public VitalsTab(UserRole userRole) {
        this.userRole = userRole;
    }

    public JComponent createVitalSignsTab(String id) {
        JTable vitalSigns = new JTable();
        // populate the table with the patient's current medications
        try {
            // Load the MySQL JDBC driver
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();
            // Create a SQL statement to retrieve the patient's vital sign readings
            String sql = "SELECT weight, height, sysbp, diabp, hr, oxygen FROM vitals WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();

            DefaultTableModel model = new DefaultTableModel(
                    new Object[] { "Weight", "Height", "Systemic Blood Pressure", "Diastolic Blood Pressure",
                            "Heart Rate", "Oxygen" },
                    0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            // Populate the table model with the retrieved data
            while (result.next()) {
                model.addRow(new Object[] { result.getDouble(1), result.getInt(2),
                        result.getInt(3), result.getInt(4), result.getInt(5), result.getDouble(6) });
            }

            vitalSigns.setModel(model);

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

        vitalSigns.addMouseListener(new MouseAdapter() {
            private Timer timer;

            @Override
            public void mousePressed(MouseEvent e) {
                timer = new Timer(500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent c) {
                        // Get the row that was clicked
                        int row = vitalSigns.rowAtPoint(e.getPoint());

                        Object[] values = new Object[vitalSigns.getColumnCount()];
                        for (int i = 0; i < values.length; i++) {
                            values[i] = vitalSigns.getValueAt(row, i);
                        }

                        int confirm = JOptionPane.showConfirmDialog(null,
                                "Are you sure you want to delete this vital sign?", "Confirm Deletion",
                                JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            try {
                                HealthConn newConnection = new HealthConn();
                                Connection con = newConnection.connect();
                                String sql = "DELETE FROM vitals WHERE id = ? AND weight = ? AND height = ? AND sysbp = ? AND diabp = ? AND hr = ? AND oxygen = ?";
                                PreparedStatement statement = con.prepareStatement(sql);
                                statement.setString(1, id);
                                statement.setObject(2, values[0]);
                                statement.setObject(3, values[1]);
                                statement.setObject(4, values[2]);
                                statement.setObject(5, values[3]);
                                statement.setObject(6, values[4]);
                                statement.setObject(7, values[5]);
                                statement.executeUpdate();

                                DefaultTableModel tableModel = (DefaultTableModel) vitalSigns.getModel();
                                tableModel.removeRow(row);

                                statement.close();
                                con.close();

                            } catch (ClassNotFoundException ex) {
                                ex.printStackTrace();
                            } catch (SQLException s) {
                                s.printStackTrace();
                            }
                        }
                    }
                });

                timer.setRepeats(false);
                timer.start();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (timer != null) {
                    timer.stop();
                }
            }
        });

        // Create the add button and add an ActionListener to upload the new medication
        // to the SQL server
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create the form for entering the new medication details
                JFrame addVitalFrame = new JFrame("Add New Vital");
                addVitalFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                addVitalFrame.setSize(400, 200);
                addVitalFrame.setLayout(new GridLayout(7, 2, 5, 5));
                addVitalFrame.setLocationRelativeTo(null);

                // Add form components for entering the medication details
                JLabel weightLabel = new JLabel("Weight");
                JTextField weightField = new JTextField();
                addVitalFrame.add(weightLabel);
                addVitalFrame.add(weightField);

                JLabel heightLabel = new JLabel("Height");
                JTextField heightField = new JTextField();
                addVitalFrame.add(heightLabel);
                addVitalFrame.add(heightField);

                JLabel sysbpLabel = new JLabel("Systemic Blood Pressure");
                JTextField sysbpField = new JTextField();
                addVitalFrame.add(sysbpLabel);
                addVitalFrame.add(sysbpField);

                JLabel diabpLabel = new JLabel("Diastolic Blood Pressure");
                JTextField diabpField = new JTextField();
                addVitalFrame.add(diabpLabel);
                addVitalFrame.add(diabpField);

                JLabel heartRateLabel = new JLabel("Heart Rate:");
                JTextField heartRateField = new JTextField();
                addVitalFrame.add(heartRateLabel);
                addVitalFrame.add(heartRateField);

                JLabel oxygenLabel = new JLabel("Oxygen:");
                JTextField oxygenField = new JTextField();
                addVitalFrame.add(oxygenLabel);
                addVitalFrame.add(oxygenField);

                // Add a button for submitting the form
                JButton submitButton = new JButton("Submit");
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Get the values from the form fields
                        String weight = weightField.getText();
                        String height = heightField.getText();
                        String sysbp = sysbpField.getText();
                        String diabp = diabpField.getText();
                        String heartRate = heartRateField.getText();
                        String oxygen = oxygenField.getText();

                        // Upload the new medication to the SQL server
                        try {
                            // Load the MySQL JDBC driver
                            HealthConn newConnection = new HealthConn();
                            Connection con = newConnection.connect();

                            // Create a SQL statement to insert the new vital
                            String sql = "INSERT INTO vitals (id, weight, height, sysbp, diabp, hr, oxygen) VALUES (?, ?, ?, ?, ?, ?, ?)";
                            PreparedStatement statement = con.prepareStatement(sql);
                            statement.setString(1, id);
                            statement.setString(2, weight);
                            statement.setString(3, height);
                            statement.setString(4, sysbp);
                            statement.setString(5, diabp);
                            statement.setString(6, heartRate);
                            statement.setString(7, oxygen);
                            statement.executeUpdate();

                            // Refresh the vital table to show the newly added vital
                            DefaultTableModel tableModel = (DefaultTableModel) vitalSigns.getModel();
                            tableModel.addRow(new Object[] { weight, height, sysbp, diabp, heartRate, oxygen });

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

                        // Close the add vital frame
                        addVitalFrame.dispose();
                    }
                });
                addVitalFrame.add(submitButton);

                // Display the add vital frame
                addVitalFrame.setVisible(true);
            }
        });

        // Create a panel for the add button
        JPanel addButtonPanel = new JPanel();
        addButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        // Show the "Add" button only for the provider role
        if (userRole == UserRole.PROVIDER) {
            addButtonPanel.add(addButton);
        }


        // Create the vital signs tab panel and add the vital signs table and add button
        // panel
        JPanel vitalSignsPanel = new JPanel(new BorderLayout());
        vitalSignsPanel.add(new JScrollPane(vitalSigns), BorderLayout.CENTER);
        vitalSignsPanel.add(addButtonPanel, BorderLayout.SOUTH);

        return vitalSignsPanel;
    }

}