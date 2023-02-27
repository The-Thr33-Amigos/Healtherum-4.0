package real.health.SQL;

import real.health.Patient.*;
import java.sql.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class BTest {
    public BloodTest newTest = new BloodTest("White");

    // public static void main(String[] args) {
    //     BloodTest test = new BloodTest("White");
    //     double[] resul = test.getResultNumbers();
    //     for (int i = 0; i < resul.length; i++) {
    //         System.out.println(resul[i]);
    //     }

    // }
    public void addNewTest() throws ClassNotFoundException {
        try {
            HealthConn newConnection = new HealthConn();
            Connection conn = newConnection.connect();
            String jsonData = new Gson().toJson(newTest);

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
