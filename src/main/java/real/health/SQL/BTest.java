package real.health.SQL;

import real.health.Patient.*;
import java.sql.*;
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

    public String BJson(BloodTest bloodTest) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(bloodTest);
            return json;
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
