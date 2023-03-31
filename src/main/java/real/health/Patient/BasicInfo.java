package real.health.Patient;

import java.util.regex.*;

public class BasicInfo {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String dateOfBirth;
    private String bioSex;
    private String mailingAdress;
    private String race;

    // Basic info class is designed to hold the basic information of a patient
    public BasicInfo(String fName, String lName, String em, String pNum, String bSex, String PdateOfBirth, String pMailing, String pRace) {
        this.firstName = fName;
        this.lastName = lName;
        // Validates email structure, if not valid then email is blank
        if (validEmail(em)) {
            this.email = em;
        } else {
            this.email = "";
        }
        // checks if phonenumber is made of numbers, and is 10 in length, (720529a356) not valid, (72051939101) not valid
        if (isNumberString(pNum) && pNum.length() == 10) {
            pNum = formatNumber(pNum);
        }
        else {
            pNum = "";
        }
        this.phoneNumber = pNum;
        // generic format of date, has no correction yet
        this.dateOfBirth = PdateOfBirth;
        this.bioSex = bSex;
        this.mailingAdress = pMailing;
        this.race = pRace;
    }

    private String fullName;

    public void createFullName() {
        String full = getFirstName() + " " + getLastName();
        this.fullName = full;
    }

    public String getFullName() {
        return fullName;
    }

    // checks if string for phone number contains any non digit characters
    public boolean isNumberString(String num) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*(+";
        for (int i = 0; i < characters.length(); i++) {
            for (int j = 0; j < num.length(); j++) {
                if (num.charAt(j) == characters.charAt(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    // formats the number adding '-' in between 7202602349 will = 720-260-2349
    public String formatNumber(String fNum) {
        String formatString = fNum.substring(0,3) + "-" + fNum.substring(3,6) + "-" + fNum.substring(6);
        return formatString;
    }

    // formats date into "american standard", 05261998 = 05/26/1998
    public String formatDate(String nDate) {
        String formatString = nDate.substring(0,2) + "/" + nDate.substring(2, 4) + "/" + nDate.substring(5);
        return formatString;
    }

    // uses reg expression to check if the email follows the standard email format, email = [letterfirst then whatever] @ [letters only] . [com, us, gov, etc]
    public boolean validEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pat.matcher(email).matches();
    }

    public String getFirstName() {
        return firstName;
    }

    public void newFirstName(String newFirst) {
        this.firstName = newFirst;
    }

    public String getLastName() {
        return lastName;
    }

    public void newLastName(String newLast) {
        this.lastName = newLast;
    }

    public String getEmail() {
        return email;
    }

    public void newEmail(String newEmail) {
        this.email = newEmail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void newPhoneNumber(String newPhoneNumber) {
        this.phoneNumber = newPhoneNumber;
    }

    public String getDOB() {
        return dateOfBirth;
    }

    public String getBioSex() {
        return bioSex;
    }

    // eventually will auto complete suggestions
    public String getMailing() {
        return mailingAdress;
    }

    public void newMailing(String newMail) {
        this.mailingAdress = newMail;
    }

    public String getRace() {
        return race;
    }

    // test function
    public void formatPersonal() {
        String first = "Name: " + getFirstName() + " " + getLastName() + "\n";
        String emailn = "Email: " + getEmail() + "\n";
        String phone = "Phone: " + getPhoneNumber() + "\n";
        String bio = "Biological Sex: " + getBioSex() + "\n";

        System.out.println(first + emailn + phone + bio);

    }

    // used for gui input
    public String[] basicList() {
        String[] pList = new String[7];
        String fullName1 = getFirstName() + " " + getLastName();
        pList[0] = fullName1;
        pList[1] = getDOB();
        pList[2] = getBioSex();
        pList[3] = getRace();
        pList[4] = getPhoneNumber();
        pList[5] = getEmail();
        pList[6] = getMailing();

        return pList;
    }



}
