package real.health.Patient;

import javax.swing.text.Highlighter.Highlight;

public class BloodItem {
    private String testName;
    private double result;
    // Need to use a pair for future for now vvvvv
    private double lowBound;
    private double upBound;
    private String units;
    private boolean flag;

    // blood item simulates one line in a blood test, in this format it is the name of the test, the result, lower bound, upper bound, units of the test, and if the test is in the normal range
    public BloodItem(String name, double nResult, double nlowB, double nhighB, String nUnits, boolean nFlag) {
        this.testName = name;
        this.result = nResult;
        this.lowBound = nlowB;
        this.upBound = nhighB;
        this.units = nUnits;
        this.flag = nFlag;

        if ((getResult() != 0) && ((getResult() > getHigh()) || (getResult() < getLow()))) {
            this.flag = true;
        }
    }

    public double getResult() {
        return result;
    }

    public void setResult(double newResult) {
        this.result = newResult;
    }

    public boolean getFlag() {
        return flag;
    }

    // sets the flag to true if out of normal range, false otherwise based on the specfic test
    public void setFlag(double Result) {
        if (Result == 0) {
            this.flag = false;
        }
        else if ((Result >= getLow()) && (Result <= getHigh())) {
            this.flag = false;
        }
        else {
            this.flag = true;
        }
    }

    public String getTestName() {
        return testName;
    }

    public double getLow() {
        return lowBound;
    }

    public double getHigh() {
        return upBound;
    }

    public String getUnits() {
        return units;
    }



}
