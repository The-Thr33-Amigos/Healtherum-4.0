package real.health.GUI;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import real.health.SQL.HealthConn;

public class NotesTab {
    public JComponent createNotesTab(String id) {
        JTable notesTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(notesTable);
        try {
            // Load the MySQL JDBC driver
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();
            // Create a SQL statement to retrieve the patient's notes
            String sql = "SELECT * FROM notes WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();

            DefaultTableModel model = new DefaultTableModel(
                    new Object[] { "Date", "Note" },
                    0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            // Populate the table model with the retrieved data
            while (result.next()) {
                model.addRow(new Object[] { result.getDate("date"), result.getString("note") });
            }

            notesTable.setModel(model);

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

        // Add a button to add new notes
        JButton addButton = new JButton("Add New Note");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame addNoteFrame = new JFrame("Add New Note");
                addNoteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                addNoteFrame.setSize(400, 200);
                addNoteFrame.setLayout(new GridLayout(2, 1, 5, 5));
                addNoteFrame.setLocationRelativeTo(null);

                // Add components to enter new note details
                JTextArea noteArea = new JTextArea();
                JScrollPane scrollPane = new JScrollPane(noteArea);
                addNoteFrame.add(scrollPane);

                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                addNoteFrame.add(buttonPanel);

                JButton saveButton = new JButton("Save");
                saveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            HealthConn newConnection = new HealthConn();
                            Connection con = newConnection.connect();
                            String sql = "INSERT INTO notes (id, date, note) VALUES (?, ?, ?)";
                            PreparedStatement statement = con.prepareStatement(sql);
                            statement.setString(1, id);
                            statement.setDate(2, new java.sql.Date(System.currentTimeMillis()));
                            statement.setString(3, noteArea.getText());
                            statement.executeUpdate();

                            DefaultTableModel tableModel = (DefaultTableModel) notesTable.getModel();
                            tableModel.addRow(new Object[] { new java.sql.Date(System.currentTimeMillis()),
                                    noteArea.getText() });

                            statement.close();
                            con.close();
                            addNoteFrame.dispose();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        } catch (SQLException s) {
                            s.printStackTrace();
                        }
                    }
                });
                buttonPanel.add(saveButton);

                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addNoteFrame.dispose();
                    }
                });
                // Add the cancel button to the button panel
                buttonPanel.add(cancelButton);

                addNoteFrame.setVisible(true);

            }
        });

        JPanel notesPanel = new JPanel();
        notesPanel.setLayout(new BorderLayout());
        notesPanel.add(scrollPane, BorderLayout.CENTER);
        notesPanel.add(addButton, BorderLayout.SOUTH);
        return notesPanel;
    }
}
