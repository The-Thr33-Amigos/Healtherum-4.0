package real.health.Patient;

import java.util.Random;

import real.health.SQL.HealthConn;

import java.sql.*;

public class UserPass {
    private String hash;
    private String currentUserName;
    private String currentPassword;

    public static String url = "jdbc:mysql://35.161.231.206:3306/patient";
    public static String user = "Hunter";
    public static String password = "H@mmer2525";

    public UserPass(String user, String pass) {
        this.currentUserName = user;
        this.currentPassword = pass;
    }

    public String getUser() {
        return currentUserName;
    }

    public String getPassword() {
        return currentPassword;
    }

    public static String generateUniqueId() {
        String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        Random rand = new Random();

        int length = 20; // Adjust the length of the ID as needed
        StringBuilder newId = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = rand.nextInt(letters.length());
            char randomChar = letters.charAt(randomIndex);
            newId.append(randomChar);
        }

        return newId.toString();
    }

    public void initializeHash() {
        hash = generateUniqueId();
    }

    public String getHash() {
        return this.hash;
    }

    public boolean checkIfHashExist() throws ClassNotFoundException {
        // if hash exist ...
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        boolean idExist = false;

        HealthConn newConn = new HealthConn();


        try {
            conn = newConn.connect();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM userpass WHERE id=" + getHash();
            rs = stmt.executeQuery(sql);
            idExist = rs.next();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return idExist;

    }

    public String getHashFromSQL() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String hashToBeGotten = "";

        try {
             // connect to the database
            conn = DriverManager.getConnection(url, user, password);
            String sql = "SELECT id FROM userpass WHERE user=? AND password=?"; // create a SQL query to get the ID based on the username and password
            stmt = conn.prepareStatement(sql); // create a prepared statement object
            stmt.setString(1, getUser()); // set the username parameter in the query
            stmt.setString(2, getPassword()); // set the password parameter in the query
            rs = stmt.executeQuery(); // execute the query and get the result set
            if (rs.next()) {
                hashToBeGotten = rs.getString("id"); // get the ID from the result set
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return hashToBeGotten;
    }

}


