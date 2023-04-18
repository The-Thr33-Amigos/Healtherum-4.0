package real.health.UTIL;

import java.util.regex.*;

public class ValidFields {
    
    private static final String VALIDEMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL = Pattern.compile(VALIDEMAIL);

    private static final String VALIDNAME = "^[A-Z][a-zA-Z]*$";
    private static final Pattern NAME = Pattern.compile(VALIDNAME);

    private static final String VALIDDATE = "^(0[1-9]|1[0-2])/(0[1-9]|1[0-9]|2[0-9]|3[01])/((19|20)\\d\\d)$";
    private static final Pattern DATE = Pattern.compile(VALIDDATE);

    public static boolean isValidEmail(String email) {
        Matcher matcher = EMAIL.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidName(String name) {
        Matcher matcher = NAME.matcher(name);
        return matcher.matches();
    }

    public static boolean isValidDate(String date) {
        Matcher matcher = DATE.matcher(date);
        return matcher.matches();
    }

    public static String[] formatName(String name) {
        String first;
        String last;

        String[] nameParts = name.split(" ");
        first = nameParts[0];
        last = nameParts[1];

        String[] fullNameList = {first, last};

        return fullNameList;
    }
}
