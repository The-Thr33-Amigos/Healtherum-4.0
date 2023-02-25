package real.health.Patient;
import java.util.ArrayList;

public class MedicalHistory {
	// All of these may go into a seperate class, and use inheritance from this main medical history class
	private ArrayList<String> allergies;
	// eventually add api from fda to get list of medications
	private ArrayList<String> medications;
	private ArrayList<String> conditions;
	private ArrayList<String> surgeries;
	private ArrayList<String> familyHistory;
	private ArrayList<String> vaccinationHistory;
	private ArrayList<String> lifeStyleFactors;
	private ArrayList<String> sexualHistory;
	private ArrayList<String> mentalHealthHistory;
	private ArrayList<String> developmentalMile;

	public void addAllergies(ArrayList<String> newItems) {
		if (this.allergies == null) {
			this.allergies = new ArrayList<String>();
			this.allergies.addAll(newItems);
		} else {
			this.allergies.addAll(newItems);
		}
	}

	public void getAllergies() {
		System.out.println(this.allergies);
	}

	public void addMedications(ArrayList<String> newItems) {
		if (this.medications == null) {
			this.medications = new ArrayList<String>();
			this.medications.addAll(newItems);
		} else {
			this.medications.addAll(newItems);
		}
	}

	public void getMedications() {
		System.out.println(this.medications);
	}

	public void addConditions(ArrayList<String> newItems) {
		if (this.conditions == null) {
			this.conditions = new ArrayList<String>();
			this.conditions.addAll(newItems);
		} else {
			this.conditions.addAll(newItems);
		}
	}

	public void getCondtions() {
		System.out.println(this.conditions);
	}

	public void addSurgeries(ArrayList<String> newItems) {
		if (this.surgeries == null) {
			this.surgeries = new ArrayList<String>();
			this.surgeries.addAll(newItems);
		} else {
			this.surgeries.addAll(newItems);
		}
	}

	public void getSurgeries() {
		System.out.println(this.surgeries);
	}

	public void addFamilyHistory(ArrayList<String> newItems) {
		if (this.familyHistory == null) {
			this.familyHistory = new ArrayList<String>();
			this.familyHistory.addAll(newItems);
		} else {
			this.familyHistory.addAll(newItems);
		}
	}

	public void getFamilyHistory() {
		System.out.println(this.familyHistory);
	}

	public void addVaccination(ArrayList<String> newItems) {
		if (this.vaccinationHistory == null) {
			this.vaccinationHistory = new ArrayList<String>();
			this.vaccinationHistory.addAll(newItems);
		} else {
			this.vaccinationHistory.addAll(newItems);
		}
	}

	public void getVaccination() {
		System.out.println(this.vaccinationHistory);
	}

	public void addLifeStyle(ArrayList<String> newItems) {
		if (this.lifeStyleFactors == null) {
			this.lifeStyleFactors = new ArrayList<String>();
			this.lifeStyleFactors.addAll(newItems);
		} else {
			this.lifeStyleFactors.addAll(newItems);
		}
	}

	public void getLifeStyle() {
		System.out.println(this.lifeStyleFactors);
	}

	public void addSexual(ArrayList<String> newItems) {
		if (this.sexualHistory == null) {
			this.sexualHistory = new ArrayList<String>();
			this.sexualHistory.addAll(newItems);
		} else {
			this.sexualHistory.addAll(newItems);
		}
	}

	public void getSexual() {
		System.out.println(this.sexualHistory);
	}

	public void addMental(ArrayList<String> newItems) {
		if (this.mentalHealthHistory == null) {
			this.mentalHealthHistory = new ArrayList<String>();
			this.mentalHealthHistory.addAll(newItems);
		} else {
			this.mentalHealthHistory.addAll(newItems);
		}
	}

	public void getMental() {
		System.out.println(this.mentalHealthHistory);
	}

	public void addMilestones(ArrayList<String> newItems) {
		if (this.developmentalMile == null) {
			this.developmentalMile = new ArrayList<String>();
			this.developmentalMile.addAll(newItems);
		} else {
			this.developmentalMile.addAll(newItems);
		}
	}

	public void getMileStones() {
		System.out.println(this.developmentalMile);
	}
}
