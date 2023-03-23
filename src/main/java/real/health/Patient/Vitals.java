package real.health.Patient;

import java.util.HashMap;

import javax.management.remote.rmi.RMIConnector;

public class Vitals {

    private double weight;
    private int height;
    private int sysBP;
    private int diaBP;
    private int heartRate;
    private double oxygenPercent;

    // the vitals class is used to store an instance of a daily vitals at a visit
    public Vitals(double newWeight, int newHeight, int newSys, int newDia, int newHR, double newOxPer) {
        this.weight = newWeight;
        this.height = newHeight;
        this.sysBP = newSys;
        this.diaBP = newDia;
        this.heartRate = newHR;
        this.oxygenPercent = newOxPer;
        implementFlags();
    }

    public double getWeight() {
        return weight;
    }

    public void newWeight(double setWeight) {
        this.weight = setWeight;
    }

    public int getHeight() {
        return height;
    }

    public void newHeight(int setHeight) {
        this.height = setHeight;
    }

    public int getSys() {
        return sysBP;
    }

    public void newSys(int setSys) {
        this.sysBP = setSys;
    }

    public int getDia() {
        return diaBP;
    }

    public void newDia(int setDia) {
        this.diaBP = setDia;
    }

    public int getHR() {
        return heartRate;
    }

    public void newHR(int setHR) {
        this.heartRate = setHR;
    }

    public double getOxyPer() {
        return oxygenPercent;
    }

    public void newOxyPer(double setOxyPer) {
        this.oxygenPercent = setOxyPer;
    }

    private HashMap<String, Boolean> vitalFlags = new HashMap<String,Boolean>();

    // this function checks flag values based on normal range for test
    // possibly reduntant as if someones blood oxygen was below a certain threshold, they would not be well and would need medical attention
    public void implementFlags() {
        double bmi = getWeight() / (getHeight() * getHeight()) * 703;
        if (bmi >= 30.0) {
            vitalFlags.put("Weight", true);
        }
        else {
            vitalFlags.put("Weight", false);
        }

        if (getSys() > 120) {
            vitalFlags.put("Systemic Blood Pressure",true);
        }
        else {
            vitalFlags.put("Stemic Blood Pressure",false);
        }

        if (getDia() > 80) {
            vitalFlags.put("Diastolic Blood Pressure", true);
        }
        else {
            vitalFlags.put("Diastolic Blood Pressure", false);
        }

        if (getHR() > 100) {
            vitalFlags.put("Heart Rate", true);
        }
        else {
            vitalFlags.put("Heart Rate", false);
        }

        if (getOxyPer() < 95.0) {
            vitalFlags.put("Oxygen Percent", true);
        }
        else {
            vitalFlags.put("Oxygen Percent", false);
        }

    }

    public Boolean getFlag(String name) {
        return vitalFlags.get(name);
    }

}
