package real.health.SQL;

import java.sql.*;

public class HealthConn {

    private static String masterUrl = "jdbc:mysql://35.161.231.206:3306/patient";
    private static String masterUser = "Hunter";
    private static String masterPass = "H@mmer2525";

    public Connection connect() throws SQLException, ClassNotFoundException{
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(masterUrl, masterUser, masterPass);
        return conn;
    }
}
