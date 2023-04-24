package real.health.SQL;

import real.health.Patient.*;

import java.awt.List;
import java.sql.*;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;


public class BTest {
    private String patientID;

    public BTest(String id) {
        this.patientID = id;
    }

    private ArrayList<BloodTest> testHistory = new ArrayList<>();

    public BloodTest jsonToBT(String json) {
        try {
            Gson gson = new Gson();
            BloodTest bloodTest = gson.fromJson(json, BloodTest.class);
            return bloodTest;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String bloodToJSON(BloodTest newTest) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(newTest);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        
    }

    public ArrayList<String> BJson(ArrayList<BloodTest> btest) {
        ArrayList<String> jsonList = new ArrayList<>();
        try {
            for (int i = 0; i < btest.size(); i++) {
                Gson gson = new Gson();
                String json = gson.toJson(btest.get(i));
                jsonList.add(json);
            }
            
            return jsonList;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addToList(BloodTest newBlood) {
        testHistory.add(newBlood);
    }

}
