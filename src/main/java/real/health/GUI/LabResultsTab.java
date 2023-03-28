package real.health.GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.basic.DefaultMenuLayout;
import javax.swing.table.*;

import com.mysql.cj.conf.ConnectionUrl.Type;

import real.health.Patient.BloodTest;
import real.health.Patient.Patient;
import real.health.Patient.BloodItem;
import real.health.SQL.*;
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

    public HashMap<Integer, BloodTest> bloodTestMap = new HashMap<>();

    public ArrayList<Object> getColumnValues(JTable table, int columnIndex) {
        ArrayList<Object> columnValues = new ArrayList<>();
        for (int row = 0; row < table.getRowCount(); row++) {
            columnValues.add(table.getValueAt(row, columnIndex));
        }
        return columnValues;
    }
    // TODO add race as a parameter to be based on patient race
    public JComponent createLabResultsTab(String id) throws IOException {

        JPanel labResultsPanel = new JPanel(new BorderLayout());

        // Create table to display lab results
        DefaultTableModel model = new DefaultTableModel();
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
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        BloodTest selectedBT = bloodTestMap.get(row);
                        JFrame bloodFrame = new JFrame(selectedBT.testName + " " + selectedBT.testDate);
                        bloodFrame.setSize(800, 600);
                        DefaultTableModel bloodTestModel = new DefaultTableModel();

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
                        JScrollPane scrollPane = new JScrollPane(bloodTable);

                        bloodFrame.add(scrollPane, BorderLayout.CENTER);
                        bloodFrame.setLocationRelativeTo(null);
                        bloodFrame.setVisible(true);

                    }
                }
            }
        });
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table);
        labResultsPanel.add(scrollPane, BorderLayout.CENTER);

        JButton newButton2 = new JButton("New Test");
        JButton nextButton2 = new JButton("Next");
        nextButton2.setPreferredSize(new Dimension(100, nextButton2.getPreferredSize().height));

        JFrame newFrame = new JFrame("Test Information");
        JPanel nextButtonPanel = new JPanel();
        nextButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        nextButtonPanel.add(nextButton2);

        String[] testName = { "Generic Blood Panel" };
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
        newFrame.add(resultLabel);
        newFrame.add(resultCombo);
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

        nextButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BloodTest newBloodTest = new BloodTest("White");
                LocalDate currentDate = LocalDate.now();
                LocalDate oldDate = currentDate.minusDays(4);

                String curr = currentDate.toString();
                String old = oldDate.toString();

                newBloodTest.testDate = old;
                newBloodTest.resultDate = curr;
                String testName = (String) nameCombo.getSelectedItem();
                newBloodTest.testName = testName;

                String resultSelect = (String) resultCombo.getSelectedItem();
                newBloodTest.resultIndicator = resultSelect;

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
                JScrollPane scrollPane = new JScrollPane(bloodTable);

                JButton saveButton = new JButton("Save");
                JPanel saveButtonPanel = new JPanel();
                saveButtonPanel.setLayout(new BorderLayout());
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
                        // TODO: Save the test information to the database

                        // Add the new test to the table on the first screen
                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        int rowIndex = table.getRowCount();
                        bloodTestMap.put(rowIndex, newBloodTest);

                        try {
                            BTest convert = new BTest(id);
                            String json = convert.bloodToJSON(newBloodTest);

                            HealthConn newConnection = new HealthConn();
                            Connection conn = newConnection.connect();
                            String sql = "INSERT INTO bloodtest (id, test) VALUES (?, ?)";

                            PreparedStatement statement = conn.prepareStatement(sql);
                            statement.setString(1,id);
                            statement.setString(2, json);
                            statement.executeUpdate();

                            statement.close();
                            conn.close();

                        } catch (ClassNotFoundException c) {
                            c.printStackTrace();
                        } catch (SQLException ex) {
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
            String sql = "SELECT test FROM bloodtest WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result2 = statement.executeQuery();

            BTest newTest = new BTest(id);
            int count = 0;
            while (result2.next()) {
                BloodTest newBlood;
                newBlood = newTest.jsonToBT(result2.getString(1));
                newTest.addToList(newBlood);
                model.addRow(new Object[] { newBlood.testName, newBlood.resultIndicator, newBlood.testDate,
                        newBlood.testInterp, newBlood.resultDate, newBlood.signature, newBlood.comment});
                        bloodTestMap.put(count, newBlood);
                count++;
            }

            table.setModel(model);

            // Clean up resources
            result2.close();
            statement.close();
            conn.close();

        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Test Information
        JPanel testInformationPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 5, 10, 5);
        JPanel statusPanel = new JPanel();
        JPanel statusPanel1 = new JPanel();
        JPanel statusPanel2 = new JPanel();

        JLabel testDateLabel = new JLabel("Disease Indicator:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        testInformationPanel.add(testDateLabel, constraints);

        JLabel accuracyLabel = new JLabel();
        constraints.gridx = 0;
        constraints.gridy = 1;
        testInformationPanel.add(accuracyLabel, constraints);

        JLabel statusLabel = new JLabel();
        constraints.gridx = 0;
        constraints.gridy = 2;
        statusPanel.add(statusLabel);
        testInformationPanel.add(statusPanel, constraints);

        JButton predBtn = new JButton("Predict");
        constraints.gridx = 0;
        constraints.gridy = 3;
        testInformationPanel.add(predBtn, constraints);

        JLabel testTypeLabel = new JLabel("Heart Disease:");
        constraints.gridx = 0;
        constraints.gridy = 6;
        testInformationPanel.add(testTypeLabel, constraints);

        JLabel statusLabel1 = new JLabel();
        constraints.gridx = 0;
        constraints.gridy = 7;
        statusPanel1.add(statusLabel1, constraints);

        JButton predBtn1 = new JButton("Predict");
        constraints.gridx = 0;
        constraints.gridy = 8;
        testInformationPanel.add(predBtn1, constraints);

        JLabel orderingProviderLabel = new JLabel("Diabetes:");
        constraints.gridx = 0;
        constraints.gridy = 12;
        testInformationPanel.add(orderingProviderLabel, constraints);

        JLabel statusLabel2 = new JLabel();
        constraints.gridx = 0;
        constraints.gridy = 13;
        statusPanel2.add(statusLabel2, constraints);

        JButton predBtn2 = new JButton("Predict");
        constraints.gridx = 0;
        constraints.gridy = 14;
        testInformationPanel.add(predBtn2, constraints);

        labResultsPanel.add(testInformationPanel, BorderLayout.WEST);

        predBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)  {
                try {

                    heartDisease newHD = new heartDisease(id);
                    ArrayList<String> predic = newHD.predict();
                    String acc = predic.get(0);
                    accuracyLabel.setText(acc);

                    String pred = predic.get(1);

                    if ("Absence".equals(pred)) {
                        statusLabel.setText("Absence");
                        statusPanel.setBackground(Color.GREEN);
                        statusPanel.add(statusLabel);
                    } else if ("Presence".equals(pred)) {
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

        // Download/Print Options
        JPanel downloadPrintPanel = new JPanel();
        JButton downloadButton = new JButton("Download");
        JButton printButton = new JButton("Print");
        downloadPrintPanel.add(downloadButton);
        downloadPrintPanel.add(printButton);
        downloadPrintPanel.add(newButton2);
        labResultsPanel.add(downloadPrintPanel, BorderLayout.SOUTH);

        return labResultsPanel;
    }
}
