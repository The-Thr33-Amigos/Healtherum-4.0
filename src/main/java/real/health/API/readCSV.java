package real.health.API;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class readCSV {
    public List<String> readDrugNames(String fileName) {
        List<String> drugNames = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                drugNames.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return drugNames;
    }

}
