package real.health.Patient;

import java.util.ArrayList;

import javax.swing.JComponent;

import real.health.GUI.UserRole;

// The main patient class which takes in all of the smaller classes and uses them in coherent unit will probably need helper functions eventually but we will see
public class Patient {
     private UserPass userPass;
     private BasicInfo basicInformation;
     private MedicalHistory medicalHistory;
     private BloodTest bloodTest;
     private Vitals newVitals;
     private Notes newNotes;


     public void setBasicInfo(String first, String last, String email, String phone, String date, String bio, String mail, String race) {
          this.basicInformation = new BasicInfo(first, last, email, phone, bio, date, mail, race);
     }

     public BasicInfo getBasicInfo() {
          return basicInformation;
     }

     public void setMedicalHistory() {
          this.medicalHistory = new MedicalHistory();
     }

     public MedicalHistory getMedicalHistory() {
          return medicalHistory;
     }

     public void setBloodTest(String race) {
          this.bloodTest = new BloodTest(race);
     }

     public BloodTest getBloodTest() {
          return bloodTest;
     }


     public void setVitals(double newWeight, int newHeight, int newSys, int newDia, int newHR, double newOxPer) {
          this.newVitals = new Vitals(newWeight, newHeight, newSys, newDia, newHR, newOxPer);
     }

     public Vitals getVitals() {
          return newVitals;
     }

     public void setNotes() {
          this.newNotes = new Notes();
     }

     public Notes getNotes() {
          return newNotes;
     }

     public void setUser(String username, String password) {
          this.userPass = new UserPass(username, password);
     }

     public UserPass getUserPass() {
          return userPass;
     }

    public static JComponent createMedicalRecordsView(int id, UserRole role) {
        return null;
    }

     



}
