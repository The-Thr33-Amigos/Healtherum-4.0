package real.health.GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.basic.DefaultMenuLayout;
import javax.swing.table.*;
import real.health.Patient.*;
import real.health.SQL.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.awt.event.*;

public class LabResultsTab {
    public BloodTest newBloodTest = new BloodTest("White");
    
    public JComponent createLabResultsTab(String id) {

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
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table);
        labResultsPanel.add(scrollPane, BorderLayout.CENTER);


        JButton newButton = new JButton("New Test");
        JButton nextButton = new JButton("Next");
        nextButton.setPreferredSize(new Dimension(100, nextButton.getPreferredSize().height));
        

        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame newFrame = new JFrame("Test Information");
                // JPanel newPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
                newFrame.setSize(600, 400);
                newFrame.setLayout(new GridLayout(6, 2, 5, 5));
                newFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                newFrame.setLocationRelativeTo(null);

                JLabel emptyLabel = new JLabel();

                JPanel nextButtonPanel = new JPanel();
                nextButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                nextButtonPanel.add(nextButton);
                
                String[] testName = {"Generic Blood Panel"};
                JComboBox<String> nameCombo = new JComboBox<>(testName);
                JLabel tNameLabel = new JLabel("Test Name:");

                String[] result = {"Normal", "Elevated", "Severe"};
                JComboBox<String> resultCombo = new JComboBox<>(result);
                JLabel resultLabel = new JLabel("Result:");

                LocalDate currentDate = LocalDate.now();
        
                String[] interp = {"No Action", "Further Testing", "See Specialist"};
                JComboBox<String> interpCombo = new JComboBox<>(interp);
                JLabel interpLabel = new JLabel("Interpretation:");
               
                LocalDate oldDate = currentDate.minusDays(4);
        
                JLabel sigLabel = new JLabel("Signature:");
                JTextField sigField = new JTextField(20);

                JLabel commentLabel = new JLabel("Comments:");
                JTextField commentField = new JTextField(20);

                newBloodTest.testDate = oldDate;
                newBloodTest.resultDate = currentDate;

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
                newFrame.add(nextButtonPanel, BorderLayout.SOUTH);

                newFrame.pack();

                newFrame.setVisible(true);

                nextButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
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

                        newFrame.dispose();

                        JPanel bloodTest = new JPanel();

                        DefaultTableModel model = new DefaultTableModel();
                        model.addColumn("Name");
                        model.addColumn("Result");
                        model.addColumn("Lower Bound");
                        model.addColumn("Upper Bound");
                        model.addColumn("Units");
                        model.addColumn("Flag");

                        JTable table = new JTable(model);
                        table.setEnabled(false);
                        JScrollPane scrollPane = new JScrollPane(table);
                        bloodTest.add(scrollPane, BorderLayout.CENTER);              
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
            ResultSet result = statement.executeQuery();

            BTest newTest = new BTest(id);
            while (result.next()) {
                BloodTest newBlood;
                newBlood = newTest.jsonToBT(result.getString(2));
                newTest.addToList(newBlood);
                model.addRow(new Object[] {newBlood.testName, newBlood.resultIndicator, newBlood.testDate, newBlood.testInterp, newBlood.resultDate, newBlood.signature, newBlood.comment, newBlood});
            }
            table.addMouseListener(new MouseInputAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        JTable target = (JTable)e.getSource();
                        int row = target.getSelectedRow();


                        BloodTest currentTest = (BloodTest)target.getModel().getValueAt(row, 7);
                        HashMap<String, BloodItem> testData = currentTest.getMap();

                    }
                }
            });
            table.setModel(model);
            // Clean up resources
            result.close();
            statement.close();
            conn.close();
        }
        catch(ClassNotFoundException c) {
            c.printStackTrace();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        // Test Information
        JPanel testInformationPanel = new JPanel();
        testInformationPanel.setLayout(new BoxLayout(testInformationPanel, BoxLayout.Y_AXIS));
        JLabel testDateLabel = new JLabel("Test Date:");
        JTextField testDateField = new JTextField();
        testDateField.setEditable(false);
        JLabel testTypeLabel = new JLabel("Test Type:");
        JTextField testTypeField = new JTextField();
        testTypeField.setEditable(false);
        JLabel orderingProviderLabel = new JLabel("Ordering Provider:");
        JTextField orderingProviderField = new JTextField();
        orderingProviderField.setEditable(false);
        testInformationPanel.add(testDateLabel);
        testInformationPanel.add(testDateField);
        testInformationPanel.add(testTypeLabel);
        testInformationPanel.add(testTypeField);
        testInformationPanel.add(orderingProviderLabel);
        testInformationPanel.add(orderingProviderField);
        labResultsPanel.add(testInformationPanel, BorderLayout.WEST);

        // Download/Print Options
        JPanel downloadPrintPanel = new JPanel();
        JButton downloadButton = new JButton("Download");
        JButton printButton = new JButton("Print");
        downloadPrintPanel.add(downloadButton);
        downloadPrintPanel.add(printButton);
        downloadPrintPanel.add(newButton);
        labResultsPanel.add(downloadPrintPanel, BorderLayout.SOUTH);

        return labResultsPanel;
    }
}
