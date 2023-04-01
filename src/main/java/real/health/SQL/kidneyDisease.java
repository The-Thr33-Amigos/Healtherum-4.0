package real.health.SQL;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class kidneyDisease {

    public ArrayList<String> kPredict() throws IOException {

        int kidneyIndicator = 1;
        ProcessBuilder processBuilder = new ProcessBuilder("python", "ML/main.py", String.valueOf(kidneyIndicator));
        Process process = processBuilder.start();


        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.out.println("Python error: " + errorLine);
            }
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String accuracy = reader.readLine();
            String result = reader.readLine();
            System.out.println("Accuracy: " + accuracy);
            System.out.println("Result: " + result);
            ArrayList<String> resAcc = new ArrayList<>();
            resAcc.add(accuracy);
            resAcc.add(result);
            return resAcc;
        }
    }
    
}
