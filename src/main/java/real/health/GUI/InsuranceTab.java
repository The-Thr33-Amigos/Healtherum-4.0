package real.health.GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import real.health.SQL.HealthConn;

public class InsuranceTab {
    private static boolean DEBUG = false;

    public static JComponent createInsuranceTab(String id) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        // Create the table to display insurance information
        InsuranceTableModel model = new InsuranceTableModel(id);
        model.loadData(id);
        JTable table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(500, 120));
        table.setFillsViewportHeight(true);

        // Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        // Add the scroll pane to this panel.
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(scrollPane, constraints);

        JButton editButton = new JButton("Edit");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(editButton, constraints);

        JButton submitButton = new JButton("Submit");
        constraints.gridx = 1;
        panel.add(submitButton, constraints);
        submitButton.setVisible(false);

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.setEnabled(true);
                editButton.setVisible(false);
                submitButton.setVisible(true);
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.submitChanges();
                table.setEnabled(false);
                editButton.setVisible(true);
                submitButton.setVisible(false);
            }
        });

        return panel;
    }

    public static class InsuranceTableModel extends AbstractTableModel {
        private String[] columnNames = { "Field", "Value" };
        private Object[][] data;
        private String id;
        private HealthConn conn;

        public InsuranceTableModel(String id) {
            this.id = id;
            this.conn = new HealthConn();
            loadData(id);
        }

        public void loadData(String id) {
            try {
                Connection con = conn.connect();
                String sql = "SELECT * FROM insurance WHERE id = ?";
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setString(1, id);
                ResultSet result = statement.executeQuery();

                if (result.next()) {
                    // Populate the data array with the insurance information
                    data = new Object[][] {
                            { "Provider Name", result.getString("provider_name") },
                            { "Policy Number", result.getString("policy_number") },
                            { "Group Number", result.getString("group_number") },
                            { "Policy Holder Name", result.getString("policy_holder_name") },
                            { "Policy Holder DOB", result.getString("policy_holder_dob") },
                            { "Policy Holder SSN", result.getString("policy_holder_ssn") }
                    };
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

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
                    return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
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

            data[row][col] = value;
            fireTableCellUpdated(row, col);

            if (DEBUG) {
                System.out.println("New value of data:");
                printDebugData();
            }
        }

        public void submitChanges() {
            try {
                Connection con = conn.connect();
                String sql = "UPDATE insurance SET provider_name = ?, policy_number = ?, group_number = ?, policy_holder_name = ?, policy_holder_dob = ?, policy_holder_ssn = ? WHERE id = ?";
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setString(1, (String) data[0][1]);
                statement.setString(2, (String) data[1][1]);
                statement.setString(3, (String) data[2][1]);
                statement.setString(4, (String) data[3][1]);
                statement.setString(5, (String) data[4][1]);
                statement.setString(6, (String) data[5][1]);
                statement.setString(7, id);
                statement.executeUpdate();
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

        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();

            for (int i = 0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j = 0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    }

}
