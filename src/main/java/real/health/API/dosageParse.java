package real.health.API;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;

public class dosageParse {

    public List<String> drugDose;

    public Boolean isValidMG(String inputText) {
        Pattern pattern = Pattern.compile("(\\d+\\.?\\d*)\\s+mg");
        Matcher matcher = pattern.matcher(inputText);
        List<String> dosages = new ArrayList<>();

        while (matcher.find()) {
            if (!dosages.contains(matcher.group())) {
                dosages.add(matcher.group());
            }
        }

        if (dosages.size() == 0) {
            return false;
        } else {
            drugDose = sortDrugDose(dosages);
            return true;
        }

    }

    public List<String> sortDrugDose(List<String> mgs) {
        HashMap<Integer,String> doseMap = new HashMap<>();
        Pattern pattern = Pattern.compile("\\d+");
        

        List<String> sortedDoses = new ArrayList<>();
        for (int i = 0; i < mgs.size(); i++) {
            String currentVal = mgs.get(i);
            Matcher matcher = pattern.matcher(currentVal);
            if (matcher.find()) {
                int key = Integer.parseInt(matcher.group());
                doseMap.put(key, mgs.get(i));
            }
            
        }

        List<Integer> keys = new ArrayList<>(doseMap.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            sortedDoses.add(doseMap.get(keys.get(i)));
        }

        return sortedDoses;


    }

}
