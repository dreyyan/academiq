package ums.util;

// [IMPORT] Standard
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// [IMPORT] Regex
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// [IMPORT] Models
import ums.model.AcademicStaff;
import ums.model.Faculty;
import ums.model.GraduateStudent;
import ums.model.NonAcademicStaff;
import ums.model.Student;
import ums.model.UndergraduateStudent;

// [IMPORT] Entities
import ums.model.entity.Course;
import ums.model.entity.CourseOffering;

// [IMPORT] Enums
import ums.model.enums.AcademicStanding;
import ums.model.enums.Department;
import ums.model.enums.FacultyRank;
import ums.model.enums.Gender;
import ums.model.enums.GraduateProgram;
import ums.model.enums.YearLevel;

public class Auth {
    // * Methods: Authentication & Validation
    // [AUTH] Check if email is valid
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // [AUTH] Check if password is valid
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
        return subtype.equalsIgnoreCase("Student");
    }

    // [AUTH] Check if valid faculty type
    public static boolean isValidFacultyType(String subtype) {
        return subtype.equalsIgnoreCase("Academic Staff") || subtype.equalsIgnoreCase("Non-Academic Staff");
    }

    // [AUTH] Check if user type is valid
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

    // * Methods: Safe Parsers
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

// [HELPER] Parse AcademicStaff from CSV row
private static AcademicStaff parseAcademicStaffRow(String[] row) {
    if (row == null || row.length == 0) return null;

    try {
        String firstName  = row.length > 1 ? row[1] : "";
        String middleName = row.length > 2 ? row[2] : "";
        String lastName   = row.length > 3 ? row[3] : "";

        LocalDate dob     = row.length > 4 && !row[4].isBlank()
                ? LocalDate.parse(row[4])
                : LocalDate.now();

        Gender gender     = safeGender(row.length > 5 ? row[5] : null);
        String address    = row.length > 6 ? row[6] : "";
        String contact    = row.length > 7 ? row[7] : "";
        String email      = row.length > 8 ? row[8] : "";

        // FIXED: department is at column 9
        Department dept   = row.length > 9
                ? Department.fromCodeOrFullName(row[9])
                : Department.UNASSIGNED;

        // FIXED: rank is at column 10
        FacultyRank rank  = row.length > 10
                ? safeFacultyRank(row[10])
                : FacultyRank.INSTRUCTOR;

        // FIXED: hire date at column 11
        LocalDate hire    = row.length > 11 && !row[11].isBlank()
                ? LocalDate.parse(row[11])
                : LocalDate.now();

        // FIXED: office at column 12
        String office     = row.length > 12 ? row[12] : "";

        // FIXED: salary at column 13
        double salary     = row.length > 13 ? safeDouble(row, 13) : 0.0;

        // FIXED: tenure at column 14
        boolean isTenured = row.length > 14 && "true".equalsIgnoreCase(row[14]);

        // FIXED: teaching hours at column 15
        int teachingHours = row.length > 15 ? safeInt(row, 15) : 0;

        // CSV does NOT have maxTeaching → default it
        int maxTeaching = 12;

        // These columns exist but your constructor doesn't use them:
        // 16 jobTitle
        // 17 workHours
        // We simply ignore them for now.

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


    // [HELPER] Parse NonAcademicStaff from CSV row
    public static NonAcademicStaff parseNonAcademicStaffRow(String[] r) {
        try {
            String firstName  = r[Settings.COL_FIRST_NAME].trim();
            String middleName = r[Settings.COL_MIDDLE_NAME].trim();
            String lastName   = r[Settings.COL_LAST_NAME].trim();

            LocalDate dob = LocalDate.parse(r[Settings.COL_DOB].trim(), DateTimeFormatter.ISO_LOCAL_DATE);
            Gender gender = Gender.valueOf(r[Settings.COL_GENDER].trim().toUpperCase());
            String address = r[Settings.COL_ADDRESS].trim();
            String contact = r[Settings.COL_CONTACT].trim();
            String email = r[Settings.COL_EMAIL].trim();

            Department department = Department.fromString(r[Settings.COL_DEPARTMENT_STAFF].trim());
            String position = r[Settings.COL_POSITION].trim();
            LocalDate hireDate = LocalDate.parse(r[Settings.COL_HIRE_DATE_ADMIN].trim(), DateTimeFormatter.ISO_LOCAL_DATE);
            String office = r[Settings.COL_OFFICE_ADMIN].trim();
            double salary = Double.parseDouble(r[Settings.COL_SALARY_ADMIN].trim());
            int workHours = Integer.parseInt(r[Settings.COL_SHIFT_SCHEDULE].replace(" hrs/week","").trim());

            return new NonAcademicStaff(
                firstName, middleName, lastName, dob, gender,
                address, contact, email,
                department, position,
                hireDate, office, salary, workHours
            );

        } catch (Exception e) {
            System.out.println("Error parsing NonAcademicStaff row: " + e);
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

        // Normalize input
        String normalized = value.trim().replace("-", "_").replace(" ", "_").toUpperCase();

        // Switch based on normalized value
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
                System.out.println("Warning: Invalid FacultyRank '" + value + "' normalized to '" + normalized + "'. Defaulting to INSTRUCTOR.");
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

    // * Methods: Read from CSV
    // [METHOD] Read non-academic staff from .csv
    public static NonAcademicStaff getNonAcademicStaffById(String personId) {
        if (personId == null || personId.isBlank()) return null;

        List<String[]> rows = CSV.readCSV(Settings.NON_ACADEMIC_STAFF_FILE);

        for (String[] row : rows) {
            if (row.length <= Settings.COL_PERSON_ID) continue;

            String id = row[Settings.COL_PERSON_ID].trim();

            if (personId.equalsIgnoreCase(id)) {
                return parseNonAcademicStaffRow(row);
            }
        }

        return null;
    }

    // [METHOD] Read undergraduate student from .csv by email
    public static Student getUndergraduateByEmail(String email) {
        if (email == null || email.isBlank()) return null;

        List<String[]> rows = CSV.readCSV(Settings.STUDENTS_FILE);

        for (String[] r : rows) {
            if (r.length <= Settings.COL_EMAIL) continue;
            if (!r[Settings.COL_EMAIL].trim().equalsIgnoreCase(email.trim())) continue;

            try {
                String firstName  = r[Settings.COL_FIRST_NAME];
                String middleName = r[Settings.COL_MIDDLE_NAME];
                String lastName   = r[Settings.COL_LAST_NAME];
                LocalDate dob     = LocalDate.parse(r[Settings.COL_DOB]);
                Gender gender     = Gender.valueOf(r[Settings.COL_GENDER].toUpperCase());
                String address    = r[Settings.COL_ADDRESS];
                String contact    = r[Settings.COL_CONTACT];

                LocalDate enrollDate = LocalDate.parse(r[Settings.COL_ENROLL_DATE]);
                Department dept = safeDept(r, Settings.COL_DEPARTMENT);
                Course course   = safeCourse(r, Settings.COL_COURSE);
                AcademicStanding standing = safeStanding(r, Settings.COL_ACAD_STANDING);

                int credits = safeInt(r, Settings.COL_CREDITS);
                YearLevel yearLevel =
                        credits < 30 ? YearLevel.FRESHMAN :
                        credits < 60 ? YearLevel.SOPHOMORE :
                        credits < 90 ? YearLevel.JUNIOR :
                                    YearLevel.SENIOR;

                return new UndergraduateStudent(
                    firstName, middleName, lastName, dob, gender,
                    address, contact, email,
                    enrollDate, dept, course, standing,
                    yearLevel
                );

            } catch (Exception e) {
                System.out.println("Error parsing undergraduate: " + e);
            }
        }

        return null;
    }

    // [METHOD] Read academic staff from .csv by email
    public static AcademicStaff getAcademicStaffByEmail(String email) {
        if (email == null || email.isBlank()) return null;

        List<String[]> rows = CSV.readCSV(Settings.ACADEMIC_STAFF_FILE);

        for (String[] r : rows) {
            if (r.length <= Settings.COL_EMAIL) continue;
            if (!r[Settings.COL_EMAIL].trim().equalsIgnoreCase(email.trim())) continue;

            return parseAcademicStaffRow(r);
        }

        return null;
    }

    // [METHOD] Read non-academic staff from .csv by email
    public static NonAcademicStaff getNonAcademicStaffByEmail(String email) {
        if (email == null || email.isBlank()) return null;

        List<String[]> rows = CSV.readCSV(Settings.NON_ACADEMIC_STAFF_FILE);

        for (String[] r : rows) {
            // Skip rows that are too short
            if (r.length <= Settings.COL_EMAIL) continue;

            // Trim whitespace in the CSV field
            String rowEmail = r[Settings.COL_EMAIL].trim();

            if (rowEmail.equalsIgnoreCase(email.trim())) {
                return parseNonAcademicStaffRow(r);
            }
        }

        return null;
    }

    // [METHOD] Read all academic staff from .csv
    public static List<AcademicStaff> readAllAcademicStaff() {
        List<String[]> rows = CSV.readCSV(Settings.ACADEMIC_STAFF_FILE);
        List<AcademicStaff> staffList = new ArrayList<>();

        for (String[] r : rows) {
            AcademicStaff staff = parseAcademicStaffRow(r);
            if (staff != null) staffList.add(staff);
        }

        return staffList;
    }

    // [METHOD] Read academic staff from .csv by personId
    public static AcademicStaff getAcademicStaffById(String personId) {
        List<AcademicStaff> allStaff = readAllAcademicStaff();
        for (AcademicStaff staff : allStaff) {
            if (staff.getPersonId().equalsIgnoreCase(personId)) {
                return staff;
            }
        }
        return null;
    }

    // [METHOD] Read all undergraduate students from .csv
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
