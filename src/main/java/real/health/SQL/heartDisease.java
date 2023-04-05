package real.health.SQL;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class heartDisease {
    private String id;
    public String accuracy;
    public String result;

    public heartDisease(String currID) {
        id = currID;
    }

    public Integer resultAverage(ArrayList<Integer> nums) {
        double sum = nums.stream().mapToInt(Integer::intValue).sum();
        double avg = sum / nums.size();
        return (int) Math.round(avg);
    }

    public ArrayList<Integer> hData() {

        ArrayList<Integer> availableData = new ArrayList<>();

        try {
            HealthConn newConn = new HealthConn();
            Connection conn = newConn.connect();

            // basic sql statment

            String basicSQL = "SELECT gender FROM basic WHERE id = ?";
            String vitalSQL = "SELECT sysbp, hr FROM vitals WHERE id = ?";

            PreparedStatement statement = conn.prepareStatement(basicSQL);
            statement.setString(1, id);
            ResultSet basicResult = statement.executeQuery();

            PreparedStatement statement2 = conn.prepareStatement(vitalSQL);
            statement2.setString(1, id);
            ResultSet vitalsResult = statement2.executeQuery();

            ArrayList<Integer> sysList = new ArrayList<>();
            ArrayList<Integer> hrList = new ArrayList<>();

            while (vitalsResult.next()) {
                int currentSys = vitalsResult.getInt(1);
                int currentHR = vitalsResult.getInt(2);

                sysList.add(currentSys);
                hrList.add(currentHR);

            }

            String sex = "";

            if (basicResult.next()) {
                sex = basicResult.getString(1);

            }

            int sys = resultAverage(sysList);
            int hr = resultAverage(hrList);
            int bioSex;

            if ("Male".equals(sex)) {
                bioSex = 1;
            } else {
                bioSex = 0;
            }

            availableData.add(bioSex);
            availableData.add(sys);
            availableData.add(hr);

            return availableData;
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Integer> nullReturn = new ArrayList<>();
        return nullReturn;

    }

    public ArrayList<String> predict() throws IOException{

        ArrayList<Integer> heartData = hData();
        int sex = heartData.get(0);
        int sys = heartData.get(1);
        int hr = heartData.get(2);
        int test = 5;
        System.out.println(test + " " + sex + " " + sys + " " + hr);

        ProcessBuilder processBuilder = new ProcessBuilder("python", "ML/main.py",String.valueOf(test), String.valueOf(sex), String.valueOf(sys), String.valueOf(hr));
        Process process = processBuilder.start();


        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.out.println("Python error: " + errorLine);
            }
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            accuracy = reader.readLine();
            result = reader.readLine();
            System.out.println("Accuracy: " + accuracy);
            System.out.println("Result: " + result);
            ArrayList<String> resAcc = new ArrayList<>();
            resAcc.add(accuracy);
            resAcc.add(result);
            return resAcc;
        }

    }


}






