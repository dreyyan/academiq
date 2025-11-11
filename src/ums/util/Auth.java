package ums.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Auth {
    // [AUTH] Check if email is valid
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // * [AUTH] Check if password is valid
    public static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*\\d)[A-Za-z\\d]{8,20}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    // [AUTH] Check if account exists via email
    public static boolean accountExists(String email) {
        List<String[]> records = CSV.readCSV(Settings.CREDENTIALS_FILE);
        for (String[] record : records) {
            if (record.length > 0 && record[0].equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    // [AUTH] Check if email and password match
    public static boolean matchingUsernamePassword(String email, String password) {
        List<String[]> records = CSV.readCSV(Settings.CREDENTIALS_FILE);
        for (String[] record : records) {
            if (record.length >= 2 && record[0].equalsIgnoreCase(email) && record[1].equals(password)) {
                return true;
            }
        }
        return false;
    }

    // [AUTH] Check if valid student type
    public static boolean isValidStudentType(String subtype) {
        return subtype.equalsIgnoreCase("Freshman") ||
            subtype.equalsIgnoreCase("Sophomore") ||
            subtype.equalsIgnoreCase("Junior") ||
            subtype.equalsIgnoreCase("Senior") ||
            subtype.equalsIgnoreCase("Master") ||
            subtype.equalsIgnoreCase("PhD");
    }

    // [AUTH] Check if valid faculty type
    public static boolean isValidFacultyType(String subtype) {
        return subtype.equalsIgnoreCase("Academic Staff") ||
            subtype.equalsIgnoreCase("Teacher") ||
            subtype.equalsIgnoreCase("Librarian") ||
            subtype.equalsIgnoreCase("Administrator");
    }

    // * [AUTH] Check if user type is valid
    public static boolean isValidUserType(String userType) {
        return isValidStudentType(userType) || isValidStudentType(userType);
    }

    // [AUTH] Check if email/password exists and return user type
    public static String getUserType(String email, String password) {
        List<String[]> rows = CSV.readCSV(Settings.CREDENTIALS_FILE);
        for (String[] row : rows) {
            if (row.length >= 3 && row[0].equalsIgnoreCase(email) && row[1].equals(password)) {
                return row[2]; // Return the stored user type
            }
        }
        return null; // credentials not found or mismatch
    }
}
