package real.health.API;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;
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
            drugDose = dosages;
            return true;
        }

    }

}
