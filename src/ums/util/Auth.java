package ums.util;

// [IMPORT] Standard
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ums.model.AcademicStaff;
import ums.model.Faculty;
import ums.model.GraduateStudent;
import ums.model.NonAcademicStaff;
import ums.model.Student;
import ums.model.UndergraduateStudent;
import ums.model.entity.Course;
import ums.model.entity.CourseOffering;
import ums.model.enums.AcademicStanding;
import ums.model.enums.Department;
import ums.model.enums.FacultyRank;
import ums.model.enums.Gender;
import ums.model.enums.GraduateProgram;
import ums.model.enums.YearLevel;

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
        return isValidStudentType(userType) || isValidFacultyType(userType);
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

    // * Safe Parsers
    private static Department safeDept(String[] row, int idx) {
        return (row.length > idx && !row[idx].trim().isEmpty())
                ? Department.fromCodeOrFullName(row[idx].trim())
                : null;
    }

    private static Course safeCourse(String[] row, int idx) {
        return (row.length > idx && !row[idx].trim().isEmpty())
                ? new Course(row[idx].trim())
                : null;
    }

    private static AcademicStanding safeStanding(String[] row, int idx) {
        if (row.length <= idx || row[idx].trim().isEmpty()) {
            return AcademicStanding.GOOD; // Default value
        }
        try {
            return AcademicStanding.valueOf(row[idx].trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            return AcademicStanding.GOOD;
        }
    }

    private static double safeDouble(String[] row, int idx) {
        if (row.length <= idx || row[idx].trim().isEmpty()) return 0.0;
        try {
            return Double.parseDouble(row[idx].trim());
        } catch (NumberFormatException e) {
            System.out.println(e);
            return 0.0;
        }
    }

    private static int safeInt(String[] row, int idx) {
        if (row.length <= idx || row[idx].trim().isEmpty()) return 0;
        try {
            return Integer.parseInt(row[idx].trim());
        } catch (NumberFormatException e) {
            System.out.println(e);
            return 0;
        }
    }

    private static boolean safeBool(String[] row, int idx) {
        return row.length > idx && "true".equalsIgnoreCase(row[idx].trim());
    }

    private static int parseWorkHours(String shift) {
        if (shift == null || shift.isBlank()) return 40;

        Matcher matcher = Pattern.compile("(\\d+)").matcher(shift);
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException ignored) {
                // fall through
            }
        }

        return 40;
    }

    private static AcademicStaff parseAcademicStaffRow(String[] row) {
        if (row == null || row.length == 0) return null;

        try {
            String firstName  = row.length > 1 ? row[1] : "";
            String middleName = row.length > 2 ? row[2] : "";
            String lastName   = row.length > 3 ? row[3] : "";
            LocalDate dob     = row.length > 4 && !row[4].isBlank() ? LocalDate.parse(row[4]) : LocalDate.now();
            Gender gender     = safeGender(row.length > 5 ? row[5] : null);
            String address    = row.length > 6 ? row[6] : "";
            String contact    = row.length > 7 ? row[7] : "";
            String email      = row.length > 8 ? row[8] : "";

            Department dept   = row.length > 10 ? Department.fromCodeOrFullName(row[10]) : Department.UNASSIGNED;
            FacultyRank rank  = row.length > 11 ? safeFacultyRank(row[11]) : FacultyRank.INSTRUCTOR;
            LocalDate hire    = row.length > 12 && !row[12].isBlank() ? LocalDate.parse(row[12]) : LocalDate.now();
            String office     = row.length > 13 ? row[13] : "";
            double salary     = row.length > 14 ? safeDouble(row, 14) : 0.0;
            int teachingHours = row.length > 15 ? safeInt(row, 15) : 0;
            int maxTeaching   = row.length > 16 ? safeInt(row, 16) : 12;
            boolean isTenured = row.length > 17 && "true".equalsIgnoreCase(row[17]);

            return new AcademicStaff(
                firstName, middleName, lastName, dob, gender,
                address, contact, email,
                dept, rank, hire, office,
                salary, isTenured, new ArrayList<>(),
                teachingHours, maxTeaching
            );
        } catch (Exception e) {
            System.out.println("[ERROR][parseAcademicStaffRow] " + e.getMessage());
            return null;
        }
    }

    private static NonAcademicStaff parseNonAcademicStaffRow(String[] row) {
        if (row == null || row.length == 0) return null;

        try {
            String firstName  = row.length > 1 ? row[1] : "";
            String middleName = row.length > 2 ? row[2] : "";
            String lastName   = row.length > 3 ? row[3] : "";
            LocalDate dob     = row.length > 4 && !row[4].isBlank() ? LocalDate.parse(row[4]) : LocalDate.now();
            Gender gender     = safeGender(row.length > 5 ? row[5] : null);
            String address    = row.length > 6 ? row[6] : "";
            String contact    = row.length > 7 ? row[7] : "";
            String email      = row.length > 8 ? row[8] : "";

            Department department = row.length > 10 ? Department.fromCodeOrFullName(row[10]) : Department.UNASSIGNED;
            String position = row.length > 17 ? row[17] : "Staff";
            LocalDate hireDate = row.length > 12 && !row[12].isBlank() ? LocalDate.parse(row[12]) : LocalDate.now();
            String officeLocation = row.length > 13 ? row[13] : "";
            double salary = row.length > 14 ? safeDouble(row, 14) : 0.0;
            int workHours = parseWorkHours(row.length > 18 ? row[18] : "");

            return new NonAcademicStaff(
                firstName, middleName, lastName, dob, gender,
                address, contact, email,
                department, position,
                hireDate, officeLocation, salary, workHours
            );
        } catch (Exception e) {
            System.out.println("[ERROR][parseNonAcademicStaffRow] " + e.getMessage());
            return null;
        }
    }

    // [HELPER] Safely parse GraduateProgram from CSV row
    public static GraduateProgram safeProgramLevel(String[] row, int index) {
        if (row.length <= index || row[index] == null || row[index].isBlank()) {
            return null; // default or unknown program
        }
        try {
            return GraduateProgram.valueOf(row[index].trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Warning: Invalid GraduateProgram '" + row[index] + "'. Defaulting to null.");
            return null;
        }
    }

    // [HELPER] Safely parse Faculty advisor from CSV row
    public static Faculty safeAdvisor(String[] row, int index) {
        if (row.length <= index || row[index] == null || row[index].isBlank()) {
            return null; // no advisor assigned
        }
        String advisorId = row[index].trim();
        // Look up advisor using academic staff records by personId
        Faculty faculty = Auth.getAcademicStaffById(advisorId);
        if (faculty == null) {
            System.out.println("Warning: Advisor with ID '" + advisorId + "' not found.");
        }
        return faculty;
    }

    // [HELPER] Safe parser for FacultyRank
    private static FacultyRank safeFacultyRank(String value) {
        if (value == null || value.isBlank()) return FacultyRank.INSTRUCTOR;

        // Normalize input:
        // - trim
        // - replace spaces or hyphens with underscore
        // - uppercase everything
        String normalized = value
                .trim()
                .replace("-", "_")
                .replace(" ", "_")
                .toUpperCase();

        switch (normalized) {
            case "INSTRUCTOR":
                return FacultyRank.INSTRUCTOR;

            case "ASSISTANT_PROFESSOR":
                return FacultyRank.ASSISTANT_PROFESSOR;

            case "ASSOCIATE_PROFESSOR":
                return FacultyRank.ASSOCIATE_PROFESSOR;

            case "FULL_PROFESSOR":
                return FacultyRank.FULL_PROFESSOR;

            case "PROFESSOR":
                // CSV may store "PROFESSOR" (generic)
                return FacultyRank.FULL_PROFESSOR;

            default:
                System.out.println("Warning: Invalid FacultyRank '" + value + "' normalized to '" + normalized +
                                "'. Defaulting to INSTRUCTOR.");
                return FacultyRank.INSTRUCTOR;
        }
    }

    // [HELPER] Safe parser for Gender
    private static Gender safeGender(String value) {
        if (value == null || value.isBlank()) return Gender.OTHER;
        try {
            return Gender.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Warning: Invalid Gender '" + value + "'. Defaulting to OTHER.");
            return Gender.OTHER;
        }
    }

    public static NonAcademicStaff getNonAcademicStaffById(String personId) {
        if (personId == null || personId.isBlank()) return null;

        List<String[]> rows = CSV.readCSV(Settings.NON_ACADEMIC_STAFF_FILE);
        for (String[] row : rows) {
            if (row.length == 0) continue;

            String id = row.length > 0 ? row[0] : "";

            if (personId.equalsIgnoreCase(id)) {
                return parseNonAcademicStaffRow(row);
            }
        }
        return null;
    }

public static Student getUndergraduateByEmail(String email) {
    List<String[]> rows = CSV.readCSV(Settings.STUDENTS_FILE);

    for (String[] r : rows) {
        // Skip rows that are too short or email doesn't match
        if (r.length <= Settings.COL_EMAIL || 
            !r[Settings.COL_EMAIL].trim().equalsIgnoreCase(email.trim())) {
            continue;
        }

        try {
            String firstName  = r[Settings.COL_FIRST_NAME];
            String middleName = r[Settings.COL_MIDDLE_NAME];
            String lastName   = r[Settings.COL_LAST_NAME];
            LocalDate dob     = LocalDate.parse(r[Settings.COL_DOB]);
            Gender gender     = Gender.valueOf(r[Settings.COL_GENDER].toUpperCase());
            String address    = r[Settings.COL_ADDRESS];
            String contact    = r[Settings.COL_CONTACT];
            LocalDate enroll  = LocalDate.parse(r[Settings.COL_ENROLL_DATE]);

            Department dept   = safeDept(r, Settings.COL_DEPARTMENT);
            Course course     = safeCourse(r, Settings.COL_COURSE);
            AcademicStanding standing = safeStanding(r, Settings.COL_ACAD_STANDING);

            int credits = safeInt(r, Settings.COL_CREDITS); // use to determine year level
            YearLevel yl = credits < 30 ? YearLevel.FRESHMAN :
                            credits < 60 ? YearLevel.SOPHOMORE :
                            credits < 90 ? YearLevel.JUNIOR : YearLevel.SENIOR;

            // Construct and return UndergraduateStudent
            return new UndergraduateStudent(
                firstName, middleName, lastName, dob, gender, 
                address, contact, r[Settings.COL_EMAIL],
                enroll, dept, course, standing,
                yl
            );

        } catch (Exception e) {
            System.out.println("Error parsing student row: " + e);
        }
    }

    return null; // no matching student found
}


    // public static Student getGraduateByEmail(String email) {
    //     List<String[]> rows = CSV.readCSV(Settings.STUDENTS_FILE);
    //     for (String[] r : rows) {
    //         if (r.length <= Settings.COL_EMAIL || !r[Settings.COL_EMAIL].trim().equalsIgnoreCase(email.trim())) {
    //             continue;
    //         }

    //         try {
    //             String personId   = r[Settings.COL_PERSON_ID];
    //             String firstName  = r[Settings.COL_FIRST_NAME];
    //             String middleName = r[Settings.COL_MIDDLE_NAME];
    //             String lastName   = r[Settings.COL_LAST_NAME];
    //             LocalDate dob     = LocalDate.parse(r[Settings.COL_DOB]);
    //             Gender gender     = Gender.valueOf(r[Settings.COL_GENDER].toUpperCase());
    //             String address    = r[Settings.COL_ADDRESS];
    //             String contact    = r[Settings.COL_CONTACT];
    //             LocalDate enroll  = LocalDate.parse(r[Settings.COL_ENROLL_DATE]);

    //             Department dept   = safeDept(r, Settings.COL_DEPARTMENT);
    //             Course course     = safeCourse(r, Settings.COL_COURSE);
    //             AcademicStanding standing = safeStanding(r, Settings.COL_ACAD_STANDING);
    //             double gpa        = safeDouble(r, Settings.COL_GPA);
    //             int credits       = safeInt(r, Settings.COL_CREDITS);
    //             List<CourseOffering> offerings = new ArrayList<>();

    //             // Graduate-specific fields
    //             GraduateProgram programLevel = safeProgramLevel(r, Settings.COL_PROGRAM_LEVEL); // e.g., MASTER, PHD
    //             String thesisTitle        = r.length > Settings.COL_THESIS_TITLE ? r[Settings.COL_THESIS_TITLE] : null;
    //             Faculty advisor           = safeAdvisor(r, Settings.COL_ADVISOR_ID);

    //             return new GraduateStudent(
    //                 firstName, middleName, lastName, dob, gender, address, contact, r[Settings.COL_EMAIL],
    //                 enroll, dept, course, standing, gpa, credits, offerings,
    //                 programLevel, thesisTitle, advisor
    //             );
    //         } catch (Exception e) {
    //             System.out.println(e);
    //         }
    //     }
    //     return null;
    // }

    public static AcademicStaff getAcademicStaffByEmail(String email) {
        if (email == null || email.isBlank()) return null;

        List<String[]> rows = CSV.readCSV(Settings.ACADEMIC_STAFF_FILE);

        for (String[] r : rows) {
            if (r.length <= 8) continue;
            if (!r[8].trim().equalsIgnoreCase(email.trim())) continue;

            AcademicStaff staff = parseAcademicStaffRow(r);
            if (staff != null) return staff;
        }

        return null;
    }

public static NonAcademicStaff getNonAcademicStaffByEmail(String email) {
    List<String[]> rows = CSV.readCSV(Settings.NON_ACADEMIC_STAFF_FILE);

    for (String[] r : rows) {
        if (r.length <= 8) continue; // at least 9 columns including email
        if (!r[8].trim().equalsIgnoreCase(email.trim())) continue;

        NonAcademicStaff staff = parseNonAcademicStaffRow(r);
        if (staff != null) return staff;
    }

    return null; // no match found
}


    // [METHOD] Read academic staff from .csv
    public static List<AcademicStaff> readAllAcademicStaff() {
        List<String[]> rows = CSV.readCSV(Settings.ACADEMIC_STAFF_FILE);
        List<AcademicStaff> staffList = new ArrayList<>();

        for (String[] r : rows) {
            AcademicStaff staff = parseAcademicStaffRow(r);
            if (staff != null) staffList.add(staff);
        }

        return staffList;
    }

    // [METHOD] Get
    public static AcademicStaff getAcademicStaffById(String personId) {
        List<AcademicStaff> allStaff = readAllAcademicStaff(); // you need a method that reads all staff from CSV
        for (AcademicStaff staff : allStaff) {
            if (staff.getPersonId().equalsIgnoreCase(personId)) {
                return staff;
            }
        }
        return null; // or throw an exception if not found
    }

public static List<NonAcademicStaff> readAllNonAcademicStaff() {
    List<String[]> rows = CSV.readCSV(Settings.NON_ACADEMIC_STAFF_FILE);
    List<NonAcademicStaff> staffList = new ArrayList<>();

    for (String[] r : rows) {
        NonAcademicStaff staff = parseNonAcademicStaffRow(r);
        if (staff != null) staffList.add(staff);
    }

    return staffList;
}
}
