package real.health.SQL;

import java.sql.*;

import real.health.Patient.*;

public class retrievePatient {
    Patient tempPatient = new Patient();

    public boolean verifyID(String username, String password) {
        // check if password(hashing) id has username in same row
        // if in row returns true, and will then get patient information
        return true;
    }

    public Patient returnPatient() {
        // gets all the values in the patient
        // stores them into respective patient subclasses
        // then returns the full patient
        return tempPatient;
    }

    public static String[] splitName(String name) {
        String[] names = name.split(" ");
        if (names.length == 2) {
            return names;
        } else if (names.length == 1) {
            // If there is only one name, assume it is the first name and return an empty string for the last name
            String[] result = { names[0], "" };
            return result;
        } else {
            // If there are more than two names, assume the first and last names are the first and last elements of the array
            String[] result = { names[0], names[names.length - 1] };
            return result;
        }
    }
    public void retrieveTable(String hash) {

        HealthConn hcon = new HealthConn();



        try (Connection conn = hcon.connect()) {
            String sql = ("SELECT * FROM basic WHERE id = ?");
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1,hash);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String[] names = splitName(name);
                String first = names[0];
                String last = names[1];
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String bdate = rs.getString("bdate");
                String bio = rs.getString("bio");
                String race = rs.getString("race");
                String mailing = rs.getString("mailing");
                tempPatient.setBasicInfo(first, last, email, phone, bdate, bio, mailing, race);
            }
            conn.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

}

