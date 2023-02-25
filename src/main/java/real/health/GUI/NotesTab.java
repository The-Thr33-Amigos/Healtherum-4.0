package real.health.GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class NotesTab {
    public JComponent createNotesAndProgressTab() {
        JPanel panel = new JPanel(new BorderLayout());

        JTable notesTable = new JTable();
        notesTable.setEnabled(false);
        // TODO: Initialize the table with data from the database

        JScrollPane notesScrollPane = new JScrollPane(notesTable);
        panel.add(notesScrollPane, BorderLayout.CENTER);

        JLabel noteLabel = new JLabel("Note:");
        JTextArea noteTextArea = new JTextArea();
        noteTextArea.setEditable(false);
        noteTextArea.setText("Example hardcoded note");
        JScrollPane noteTextAreaScrollPane = new JScrollPane(noteTextArea);
        panel.add(noteLabel, BorderLayout.NORTH);
        panel.add(noteTextAreaScrollPane, BorderLayout.CENTER);

        return panel;
    }
}
