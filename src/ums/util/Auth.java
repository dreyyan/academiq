package ums.util;

// [IMPORT] Standard
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// [IMPORT] Models
import ums.model.Faculty;
import ums.model.Student;
import ums.model.AcademicStaff;
import ums.model.GraduateStudent;
import ums.model.UndergraduateStudent;
import ums.model.enums.Department;
import ums.model.entity.Course;
import ums.model.entity.CourseOffering;
import ums.model.enums.AcademicStanding;
import ums.model.enums.Gender;
import ums.model.enums.GraduateProgram;
import ums.model.enums.YearLevel;
import ums.model.enums.FacultyRank;

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
            return AcademicStanding.GOOD;                 // default
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
        // You can look up a Faculty object by ID if you have a getFacultyById method
        Faculty faculty = Auth.getFacultyById(advisorId); // implement this method
        if (faculty == null) {
            System.out.println("Warning: Advisor with ID '" + advisorId + "' not found.");
        }
        return faculty;
    }

public static Faculty getFacultyById(String facultyId) {
    if (facultyId == null || facultyId.isBlank()) return null;

    List<String[]> rows = CSV.readCSV(Settings.FACULTY_FILE);
    for (String[] r : rows) {
        if (r.length == 0) continue;

        if (r[0].trim().equalsIgnoreCase(facultyId.trim())) {
            try {
                // Person attributes
                String id         = r[0];
                String firstName  = r[1];
                String middleName = r[2];
                String lastName   = r[3];
                LocalDate dob     = r.length > 4 && !r[4].isBlank() ? LocalDate.parse(r[4]) : LocalDate.now();
                Gender gender     = r.length > 5 ? Gender.valueOf(r[5].toUpperCase()) : Gender.OTHER;
                String email      = r.length > 6 ? r[6] : "";
                String contact    = r.length > 7 ? r[7] : "";
                String departmentName = r.length > 8 ? r[8] : null;

                Department dept = null;
                if (departmentName != null && !departmentName.isBlank()) {
                    dept = Department.fromCodeOrFullName(departmentName);
                }

                // Faculty attributes (defaults if missing)
                FacultyRank rank      = r.length > 9 ? FacultyRank.valueOf(r[9].toUpperCase()) : FacultyRank.INSTRUCTOR;
                LocalDate hireDate    = r.length > 10 && !r[10].isBlank() ? LocalDate.parse(r[10]) : LocalDate.now();
                String officeLocation = r.length > 11 ? r[11] : "Office TBD";
                double salary         = r.length > 12 && !r[12].isBlank() ? Double.parseDouble(r[12]) : 0.0;
                boolean isTenured     = r.length > 13 && !r[13].isBlank() && r[13].equalsIgnoreCase("true");
                List<GraduateStudent> advisees = new ArrayList<>(); // empty for now

                // AcademicStaff attributes
                String teacherId = r.length > 14 ? r[14] : id;
                int teachingHoursPerWeek = r.length > 15 ? Integer.parseInt(r[15]) : 0;
                int maxTeachingLoad       = r.length > 16 ? Integer.parseInt(r[16]) : 12;

                return new AcademicStaff(
                    firstName, middleName, lastName, dob, gender, "", contact, email,
                    id, dept, rank, hireDate, officeLocation, salary, isTenured, advisees,
                    teacherId, teachingHoursPerWeek, maxTeachingLoad
                );

            } catch (Exception e) {
                System.out.println("Error parsing faculty with ID: " + facultyId);
                e.printStackTrace();
            }
        }
    }
    return null;
}

    public static Student getUndergraduateByEmail(String email) {
        List<String[]> rows = CSV.readCSV(Settings.STUDENTS_FILE);
        for (String[] r : rows) {
            if (r.length <= Settings.COL_EMAIL || !r[Settings.COL_EMAIL].trim().equalsIgnoreCase(email.trim())) {
                continue;
            }

            try {
                String personId   = r[Settings.COL_PERSON_ID];
                String firstName  = r[Settings.COL_FIRST_NAME];
                String middleName = r[Settings.COL_MIDDLE_NAME];
                String lastName   = r[Settings.COL_LAST_NAME];
                LocalDate dob     = LocalDate.parse(r[Settings.COL_DOB]);
                Gender gender     = Gender.valueOf(r[Settings.COL_GENDER].toUpperCase());
                String address    = r[Settings.COL_ADDRESS];
                String contact    = r[Settings.COL_CONTACT];
                String studentId  = r[Settings.COL_STUDENT_ID];
                LocalDate enroll  = LocalDate.parse(r[Settings.COL_ENROLL_DATE]);

                Department dept   = safeDept(r, Settings.COL_DEPARTMENT);
                Course course     = safeCourse(r, Settings.COL_COURSE);
                AcademicStanding standing = safeStanding(r, Settings.COL_ACAD_STANDING);
                double gpa        = safeDouble(r, Settings.COL_GPA);
                int credits       = safeInt(r, Settings.COL_CREDITS);
                List<CourseOffering> offerings = new ArrayList<>();

                YearLevel yl = credits < 30 ? YearLevel.FRESHMAN :
                            credits < 60 ? YearLevel.SOPHOMORE :
                            credits < 90 ? YearLevel.JUNIOR : YearLevel.SENIOR;

                return new UndergraduateStudent(
                    firstName, middleName, lastName, dob, gender, address, contact, r[Settings.COL_EMAIL],
                    studentId, enroll, dept, course, standing, gpa, credits, offerings, yl
                );
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return null;
    }

    public static Student getGraduateByEmail(String email) {
        List<String[]> rows = CSV.readCSV(Settings.STUDENTS_FILE);
        for (String[] r : rows) {
            if (r.length <= Settings.COL_EMAIL || !r[Settings.COL_EMAIL].trim().equalsIgnoreCase(email.trim())) {
                continue;
            }

            try {
                String personId   = r[Settings.COL_PERSON_ID];
                String firstName  = r[Settings.COL_FIRST_NAME];
                String middleName = r[Settings.COL_MIDDLE_NAME];
                String lastName   = r[Settings.COL_LAST_NAME];
                LocalDate dob     = LocalDate.parse(r[Settings.COL_DOB]);
                Gender gender     = Gender.valueOf(r[Settings.COL_GENDER].toUpperCase());
                String address    = r[Settings.COL_ADDRESS];
                String contact    = r[Settings.COL_CONTACT];
                String studentId  = r[Settings.COL_STUDENT_ID];
                LocalDate enroll  = LocalDate.parse(r[Settings.COL_ENROLL_DATE]);

                Department dept   = safeDept(r, Settings.COL_DEPARTMENT);
                Course course     = safeCourse(r, Settings.COL_COURSE);
                AcademicStanding standing = safeStanding(r, Settings.COL_ACAD_STANDING);
                double gpa        = safeDouble(r, Settings.COL_GPA);
                int credits       = safeInt(r, Settings.COL_CREDITS);
                List<CourseOffering> offerings = new ArrayList<>();

                // Graduate-specific fields
                GraduateProgram programLevel = safeProgramLevel(r, Settings.COL_PROGRAM_LEVEL); // e.g., MASTER, PHD
                String thesisTitle        = r.length > Settings.COL_THESIS_TITLE ? r[Settings.COL_THESIS_TITLE] : null;
                Faculty advisor           = safeAdvisor(r, Settings.COL_ADVISOR_ID);

                return new GraduateStudent(
                    firstName, middleName, lastName, dob, gender, address, contact, r[Settings.COL_EMAIL],
                    studentId, enroll, dept, course, standing, gpa, credits, offerings,
                    programLevel, thesisTitle, advisor
                );
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return null;
    }

    public static List<AcademicStaff> readAllAcademicStaff() {
        List<String[]> rows = CSV.readCSV(Settings.ACADEMIC_STAFF_FILE); // CSV file for staff
        List<AcademicStaff> staffList = new ArrayList<>();

        for (String[] r : rows) {
            if (r.length == 0) continue;

            try {
                // Person attributes
                String id         = r[0];
                String firstName  = r[1];
                String middleName = r.length > 2 ? r[2] : "";
                String lastName   = r.length > 3 ? r[3] : "";
                LocalDate dob     = r.length > 4 && !r[4].isBlank() ? LocalDate.parse(r[4]) : LocalDate.now();
                Gender gender     = r.length > 5 ? Gender.valueOf(r[5].toUpperCase()) : Gender.OTHER;
                String email      = r.length > 6 ? r[6] : "";
                String contact    = r.length > 7 ? r[7] : "";
                Department dept   = r.length > 8 && !r[8].isBlank() ? Department.fromCodeOrFullName(r[8]) : null;

                // Faculty attributes
                FacultyRank rank      = r.length > 9 ? FacultyRank.valueOf(r[9].toUpperCase()) : FacultyRank.INSTRUCTOR;
                LocalDate hireDate    = r.length > 10 && !r[10].isBlank() ? LocalDate.parse(r[10]) : LocalDate.now();
                String officeLocation = r.length > 11 ? r[11] : "Office TBD";
                double salary         = r.length > 12 && !r[12].isBlank() ? Double.parseDouble(r[12]) : 0.0;
                boolean isTenured     = r.length > 13 && "true".equalsIgnoreCase(r[13]);
                List<GraduateStudent> advisees = new ArrayList<>(); // empty for now

                // AcademicStaff-specific attributes
                String teacherId            = r.length > 14 ? r[14] : id;
                int teachingHoursPerWeek    = r.length > 15 ? Integer.parseInt(r[15]) : 0;
                int maxTeachingLoad         = r.length > 16 ? Integer.parseInt(r[16]) : 12;

                AcademicStaff staff = new AcademicStaff(
                        firstName, middleName, lastName, dob, gender, "", contact, email,
                        id, dept, rank, hireDate, officeLocation, salary, isTenured, advisees,
                        teacherId, teachingHoursPerWeek, maxTeachingLoad
                );

                staffList.add(staff);

            } catch (Exception e) {
                System.out.println("Error parsing academic staff row: " + String.join(",", r));
                e.printStackTrace();
            }
        }

        return staffList;
    }

    public static AcademicStaff getAcademicStaffById(String staffId) {
        List<AcademicStaff> allStaff = readAllAcademicStaff(); // you need a method that reads all staff from CSV
        for (AcademicStaff staff : allStaff) {
            if (staff.getFacultyId().equalsIgnoreCase(staffId)) {
                return staff;
            }
        }
        return null; // or throw an exception if not found
    }
}
