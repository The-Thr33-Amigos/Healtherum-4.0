package real.health.Patient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BloodTest {
    public HashMap<String,BloodItem> bloodTest = new HashMap<>();
    // Will combine all the test for one person into a personal hash map
    
    public String testName;
    public String resultIndicator;
    public String testDate;
    public String testInterp;
    public String resultDate;
    public String signature;
    public String comment;

    public String mgDL = "mg/dL";
    public String mmol = "mmol/L";
    public String gdl = "g/dL";
    public String IUL = "IU/L";

    public String race;

    // blood test is the first iteration of a generic blood panel
    public BloodTest(String newRace, String testName) {
        this.race = newRace;

        if (testName.equals("Generic Blood Panel")) {
            this.createBloodTestGen();
            this.initResults();
        }
        else if (testName.equals("Liver Panel")) {
            this.createLiverPanel();
            this.initResults();
        }
        
    }


    // Race of patient is important for GFR which means creatine levels, people with african heritage have a higher level of GFR then other races/ethnicities
    public String getRace() {
        return race;
    }

    public HashMap<String, BloodItem> getMap() {
        return bloodTest;
    }

    public ArrayList<Double> generateBloodTest() {
        ArrayList<String> testNames = new ArrayList<>();
        ArrayList<Double> results = new ArrayList<>();
        for (HashMap.Entry<String, BloodItem> entry : bloodTest.entrySet()) {
            testNames.add(entry.getKey());
        }

        for (String i : testNames) {
            double min = bloodTest.get(i).getLow();
            double max = bloodTest.get(i).getHigh();

            min = min - (min * 0.1);
            max = max + (max * 0.1);

            Random rand = new Random();
            double randomDouble = rand.nextDouble();
            double randomRes = min + (randomDouble * (max - min));
            randomRes = Math.round(randomRes * 100.0) / 100.0;

            results.add(randomRes);
        }

        return results;
        
    }

    public void initResults() {
        ArrayList<Double> randResults = generateBloodTest();
        int i = 0;
        for (HashMap.Entry<String, BloodItem> entry : bloodTest.entrySet()) {
            
            entry.getValue().setResult(randResults.get(i));
            i++;
            entry.getValue().setFlag(entry.getValue().getResult());
        }
    }


    // generic blood test, hard coded, eventually it will be dynamic in the sense that you could customize it to specific test, for example a liver panel
    public BloodItem glucose = new BloodItem("Glucose", 0, 65.0, 99.0, mgDL, false);
    // BUN is kidney function
    public BloodItem BUN = new BloodItem("BUN", 0, 6, 24, mgDL, false);
    public BloodItem creatine = new BloodItem("Creatine", 0, 0.76, 1.27, mgDL, false);

    public double getCreatine() {
        if (this.BUN.getResult() == 0 || this.creatine.getResult() == 0) {
            return 0;
        }
        return this.BUN.getResult() / this.creatine.getResult();
    }
    public BloodItem nonAfricanAmericanGFR = new BloodItem("eGFR  If NonAfricanAmerican", 0, 90, 100, "mL/min/1.73", false);
    public BloodItem africanAmericanGFR = new BloodItem("eGFR If African American", 0, 90, 110, "mL/min/1.73", false);
    public BloodItem bunCreatineRatio = new BloodItem("BUN/Creatine Ratio",this.getCreatine(), 9, 20, "N/A", false);
    public BloodItem sodium = new BloodItem("Sodium", 0, 134, 144, mmol, false);
    public BloodItem Potassium = new BloodItem("Potassium", 0, 3.5, 5.2, mmol,false);
    public BloodItem Chloride = new BloodItem("Chloride", 0, 96, 106, mmol, false);
    public BloodItem CO2 = new BloodItem("Carbon Dioxide, Total", 0, 20, 29, mmol, false);

    public BloodItem Calcium = new BloodItem("Calcium", 0, 8.7, 10.2, mgDL, false);
    public BloodItem Protein = new BloodItem("Protein, Total", 0 ,6.0, 8.5, gdl, false);
    public BloodItem Albumin = new BloodItem("Albumin", 0, 3.5, 5.5, gdl, false);
    public BloodItem Globulin = new BloodItem("Globulin, Total", 0, 1.5, 4.5, gdl, false);

    public double getAG() {
        if (this.Albumin.getResult() == 0 || this.Globulin.getResult() == 0) {
            return 0;
        }
        return this.Albumin.getResult() / this.Globulin.getResult();
    }
    public BloodItem AG = new BloodItem("A/G Ratio", getAG(), 1.2, 2.2, "N/A", false);
    public BloodItem Bilirubin = new BloodItem("Bilirubin, Total", 0, 0.0, 1.2, mgDL, false);
    public BloodItem Alkaline = new BloodItem("Alkaline Phosphatase", 0, 39, 117, IUL, false);
    public BloodItem AST = new BloodItem("AST (SGOT)", 0, 0, 40, IUL, false);
    public BloodItem ALT = new BloodItem("ALT (SGPT)", 0, 0, 44, IUL, false);
    public BloodItem Chol = new BloodItem("Cholesterol", 0, 110, 220, mgDL, false);
    public BloodItem ALP = new BloodItem("ALP", 0, 24, 147, IUL, false);
    public BloodItem GGP = new BloodItem("GGP", 0, 0, 25, IUL, false);

    public void add_Item(String newName, BloodItem new_BI) {
        bloodTest.put(newName, new_BI);
    }

    public BloodItem getItem(String itemName) {
        return bloodTest.get(itemName);
    }

    public void createLiverPanel() {
        this.add_Item(BUN.getTestName(), BUN);
        this.add_Item(Protein.getTestName(), Protein);
        this.add_Item(Albumin.getTestName(), Albumin);
        this.add_Item(Chol.getTestName(), Chol);
        this.add_Item(glucose.getTestName(), glucose);
        this.add_Item(ALT.getTestName(), ALT);
        this.add_Item(AST.getTestName(), AST);
        this.add_Item(ALP.getTestName(), ALP);
        this.add_Item(GGP.getTestName(), GGP);
        this.add_Item(Bilirubin.getTestName(), Bilirubin);
    }

    // this creates the test and puts it into the hash-map/dictionary to be used
    // once again will be dynamic eventually
    public void createBloodTestGen() {
        this.add_Item(glucose.getTestName(), glucose);
        this.add_Item(BUN.getTestName(), BUN);
        this.add_Item(creatine.getTestName(), creatine);
        if ("African American".equals(getRace())) {
            this.add_Item(africanAmericanGFR.getTestName(), africanAmericanGFR);
        }
        else {
            this.add_Item(nonAfricanAmericanGFR.getTestName(), nonAfricanAmericanGFR);
        }
        this.add_Item(bunCreatineRatio.getTestName(), bunCreatineRatio);
        this.add_Item(sodium.getTestName(), sodium);
        this.add_Item(Potassium.getTestName(), Potassium);
        this.add_Item(Chloride.getTestName(), Chloride);
        this.add_Item(CO2.getTestName(), CO2);
        this.add_Item(Calcium.getTestName(), Calcium);
        this.add_Item(Protein.getTestName(), Protein);
        this.add_Item(Albumin.getTestName(), Albumin);
        this.add_Item(Globulin.getTestName(), Globulin);
        this.add_Item(AG.getTestName(), AG);
        this.add_Item(Bilirubin.getTestName(), Bilirubin);
        this.add_Item(Alkaline.getTestName(), Alkaline);
        this.add_Item(AST.getTestName(), AST);
        this.add_Item(ALT.getTestName(), ALT);
    }


}
