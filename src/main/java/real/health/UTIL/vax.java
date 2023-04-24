package real.health.UTIL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import real.health.SQL.HealthConn;


public class vax {
    private String id;
    public vax(String id) {
        this.id = id;
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

    private static ArrayList<String> vaxToList () {
        ArrayList<String> vaxxes = new ArrayList<>();
        for (Pair<String, Double> v : vaccinations) {
            vaxxes.add(v.getFirst());
        }
        return vaxxes;
    }

    private  int getAge() {
        // HealthConn newConnection = new HealthConn();
        // Connection con = newConnection.connect();
        // // Create a SQL statement to retrieve the patient's vaccination history
        // String sql = "SELECT vaccine, dateAdministered, locationAdministered, administeringProvider FROM vaccinations WHERE id = ?";
        // PreparedStatement statement = con.prepareStatement(sql);
        // statement.setString(1, id);
        // ResultSet result = statement.executeQuery();
        return 0;
    }

}
