package real.health.Patient;

import java.awt.Component;
import java.util.ArrayList;

public class Notes {
    private ArrayList<String> notes;

    // really generic class to store a list of strings in the form of notes
    public void addNote(ArrayList<String> newNote) {
        if (this.notes == null) {
			this.notes = new ArrayList<String>();
			this.notes.addAll(newNote);
		} else {
			this.notes.addAll(newNote);
		}
    }

    public void printSpecific(Integer index) {
        System.out.println(this.notes.get(index));
    }

    public void printAll() {
        System.out.println(this.notes);
    }

    public Component createNoteTab(String id) {
        return null;
    }
}
