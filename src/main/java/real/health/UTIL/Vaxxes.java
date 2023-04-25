package real.health.UTIL;

import java.util.List;

import real.health.SQL.HealthConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Vaxxes {
    private  String id;
    public Vaxxes(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    private static List<Pair<String, Double>> vaccinations = new ArrayList<>() {{
        add(new Pair<>("Hepatitis B", 0.0));
        add(new Pair<>("Hepatitis B 2", 0.1));
        add(new Pair<>("DTaP", 0.1));
        add(new Pair<>("Hib", 0.1));
        add(new Pair<>("Polio(IPV)", 0.1));
        add(new Pair<>("Pneumococcal(PCV)", 0.1));
        add(new Pair<>("Rotavirus", 0.1));
        add(new Pair<>("Influenza", 0.6));
        add(new Pair<>("Chickenpox", 1.0));
        add(new Pair<>("MMR", 1.0));
        add(new Pair<>("Hepatitis A", 1.0));
        add(new Pair<>("Meningococcal Conjugate", 11.0));
        add(new Pair<>("HPV", 11.0));
        add(new Pair<>("Tdap", 11.0));
        add(new Pair<>("Zoster", 60.0));
    }};

    private static List<Pair<String, Double>> repeatVax = new ArrayList<>() {{
        // DTAP
        add(vaccinations.get(2));
        // MMR
        add(vaccinations.get(9));
        // HPV
        add(vaccinations.get(12));

    }};

    public ArrayList<String> vaxToList () {

        ArrayList<String> existing = new ArrayList<>();

        try {
            HealthConn newConnection = new HealthConn();
            Connection con = newConnection.connect();
            // Create a SQL statement to retrieve the patient's vaccination history
            String sql = "SELECT vaccine FROM vaccinations WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String vax = result.getString("vaccine");
                existing.add(vax);
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
        int age = getAge();
        ArrayList<String> vaxxes = new ArrayList<>();
        for (Pair<String, Double> v : vaccinations) {
            if (age > v.getSecond() && !existing.contains(v.getFirst())) {
                vaxxes.add(v.getFirst());
            }
            
        }
        return vaxxes;
    }



    private int getAge() {
        int age = 0;
        HealthConn newConnection = new HealthConn();
        try {
            Connection con = newConnection.connect();
            // Create a SQL statement to retrieve the patient's vaccination history
            String sql = "SELECT bdate FROM basic WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String bdateStr = result.getString("bdate");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate bdate = LocalDate.parse(bdateStr, formatter);
                LocalDate now = LocalDate.now();
                age = Period.between(bdate, now).getYears();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
        
        return age;
    }
}
