package real.health.PatientLogin;

public class validatePassword {
    public static boolean validatePassword(String password, String confirmPassword) {
        // Check if the two passwords match
        if (!password.equals(confirmPassword)) {
            return false;
        }
        return true;
    }
}
