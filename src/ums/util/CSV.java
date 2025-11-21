package ums.util;

// [IMPORT] Standard
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// [IMPORT] Enums
import ums.model.enums.GraduateProgram;
import ums.model.enums.YearLevel;
import ums.model.enums.Department;

// [IMPORT] Models
import ums.model.AcademicStaff;
import ums.model.Student;
import ums.model.entity.Course;
import ums.model.entity.CourseOffering;
import ums.model.entity.TimeSlot;
import ums.model.enums.AcademicStanding;
import ums.model.enums.Gender;
import ums.model.enums.Semester;

// [IMPORT] Project Files
import static ums.util.Logger.errorMessage;

public class CSV {
    // * Methods
    // [UTILITY] Read data from a CSV file
    public static List<String[]> readCSV(String filePath, String delimiter) {
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                // Split line into columns using the delimiter
                String[] columns = line.split(delimiter);
                rows.add(columns);
            }

        } catch (IOException e) {
            errorMessage("Failed to read CSV: " + e.getMessage());
        }

        return rows;
    }

    // [UTILITY] Read data from a CSV file with comma(,) as delimiter
    public static List<String[]> readCSV(String filePath) {
        return readCSV(filePath, ",");
    }

    // [UTILITY] Write data to a CSV file
    public static void writeCSV(String filePath, List<String[]> rows, String delimiter) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] row : rows) {
                // Join each row using the delimiter
                String line = String.join(delimiter, row);
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            errorMessage("Failed to write CSV: " + e.getMessage());
        }
    }

    // [UTILITY] Write data to a CSV file with comma(,) as delimiter
    public static void writeCSV(String filePath, List<String[]> rows) {
        writeCSV(filePath, rows, ",");
    }

    // [UTILITY] Get a specific column value
    public static String getColumnValue(String filePath, int keyColumnIndex, String keyValue, int targetColumnIndex) {
        List<String[]> rows = CSV.readCSV(filePath);
        for (String[] row : rows) {
            if (row.length > Math.max(keyColumnIndex, targetColumnIndex) &&
                row[keyColumnIndex].equalsIgnoreCase(keyValue)) {
                return row[targetColumnIndex];
            }
        }
        return null;
    }

    // [UTILITY] Append a single row to a CSV file
    public static void appendRow(String filePath, String[] row) {
        try (FileWriter writer = new FileWriter(filePath, true)) { // 'true' for append mode
            for (int i = 0; i < row.length; i++) {
                writer.append(row[i] != null ? row[i] : "");
                if (i < row.length - 1) {
                    writer.append(","); // comma separator
                }
            }
            writer.append(System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + filePath);
            e.printStackTrace();
        }
    }

    // [UTILITY] Register a new student's credentials to CSV file
    public static void registerNewStudent(String email, String password, String userType) {
        // Add credentials to credentials.csv
        CSV.appendRow(Settings.CREDENTIALS_FILE, new String[] { email, password, userType });

        // Add a default student row to students.csv
        String[] newStudentRow = new String[Settings.TOTAL_COLS]; // total columns in students.csv

        // Fill known/default values
        newStudentRow[Settings.COL_EMAIL]        = email;
        newStudentRow[Settings.COL_FIRST_NAME]   = "First";
        newStudentRow[Settings.COL_MIDDLE_NAME]  = "";
        newStudentRow[Settings.COL_LAST_NAME]    = "Last";
        newStudentRow[Settings.COL_DOB]          = LocalDate.now().toString();
        newStudentRow[Settings.COL_GENDER]       = Gender.OTHER.toString();
        newStudentRow[Settings.COL_ADDRESS]      = "";
        newStudentRow[Settings.COL_CONTACT]      = "";
        newStudentRow[Settings.COL_STUDENT_ID]   = "STUDENT_ID_" + UUID.randomUUID().toString();
        newStudentRow[Settings.COL_ENROLL_DATE]  = LocalDate.now().toString();
        newStudentRow[Settings.COL_DEPARTMENT]   = "";   // not assigned yet
        newStudentRow[Settings.COL_COURSE]       = "";   // not assigned yet
        newStudentRow[Settings.COL_ACAD_STANDING] = AcademicStanding.GOOD.toString();
        newStudentRow[Settings.COL_GPA]          = "0.0";
        newStudentRow[Settings.COL_CREDITS]      = "0";

        // Append empty placeholders for any extra columns
        for (int i = 0; i < newStudentRow.length; i++) {
            if (newStudentRow[i] == null) newStudentRow[i] = "";
        }

        // Write row to students.csv
        CSV.appendRow(Settings.STUDENTS_FILE, newStudentRow);
    }

    // [UTILITY] Read course offerings in 'course-offerings.csv'
    public static List<CourseOffering> readCourseOfferings() {
        List<String[]> rows = readCSV(Settings.COURSE_OFFERINGS_FILE);
        List<CourseOffering> offerings = new ArrayList<>();

        for (String[] row : rows) {
            Course course = Course.fromCodeOrFullName(row[1]);
            Semester semester = Semester.valueOf(row[2].toUpperCase());
            int year = Integer.parseInt(row[3]);
            AcademicStaff instructor = Auth.getAcademicStaffById(row[4]);
            TimeSlot schedule = TimeSlot.fromString(row[5]);
            int capacity = Integer.parseInt(row[6]);
            int enrolled = Integer.parseInt(row[7]);

            offerings.add(new CourseOffering(row[0], course, semester, year, instructor, schedule, capacity, enrolled));
        }

        return offerings;
    }

    // [UTILITY] Update course offering details
    public static void updateCourseOffering(CourseOffering offering) {
        List<String[]> rows = readCSV(Settings.COURSE_OFFERINGS_FILE);

        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i)[0].equals(offering.getOfferingId())) {
                // Update enrolled count
                rows.get(i)[7] = String.valueOf(offering.getEnrolledCount());
                break;
            }
        }

        writeCSV(Settings.COURSE_OFFERINGS_FILE, rows); // overwrite
    }

    // [UTILITY] Update student record
    public static void updateStudentRecord(Student student) {
        if (student == null || student.getStudentId() == null) {
            errorMessage("Cannot update null student or student without ID.");
            return;
        }

        List<String[]> rows = readCSV(Settings.STUDENTS_FILE);
        boolean updated = false;

        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);

            // Match by STUDENT_ID column
            if (row.length > Settings.COL_STUDENT_ID && row[Settings.COL_STUDENT_ID].equals(student.getStudentId())) {

                // Ensure row has enough columns
                int requiredLength = Settings.TOTAL_COLS;
                if (row.length < requiredLength) {
                    String[] newRow = new String[requiredLength];
                    System.arraycopy(row, 0, newRow, 0, row.length);
                    // fill remaining with empty strings
                    for (int j = row.length; j < requiredLength; j++) newRow[j] = "";
                    row = newRow;
                }

                // Update fields safely
                row[Settings.COL_FIRST_NAME]    = student.getFirstName();
                row[Settings.COL_MIDDLE_NAME]   = student.getMiddleName();
                row[Settings.COL_LAST_NAME]     = student.getLastName();
                row[Settings.COL_DOB]           = student.getDateOfBirth() != null ? student.getDateOfBirth().toString() : "";
                row[Settings.COL_GENDER]        = student.getGender() != null ? student.getGender().toString() : "";
                row[Settings.COL_ADDRESS]       = student.getAddress();
                row[Settings.COL_CONTACT]       = student.getContactNumber();
                row[Settings.COL_EMAIL]         = student.getEmail();
                row[Settings.COL_DEPARTMENT]    = student.getDepartment() != null ? student.getDepartment().toString() : "";
                row[Settings.COL_COURSE]        = student.getCourse() != null ? student.getCourse().toString() : "";
                row[Settings.COL_ACAD_STANDING] = student.getAcademicStanding() != null ? student.getAcademicStanding().toString() : "";
                row[Settings.COL_GPA]           = String.valueOf(student.getGPA());
                row[Settings.COL_CREDITS]       = String.valueOf(student.getCreditsEarned());

                rows.set(i, row);
                updated = true;
                break;
            }
        }

        if (updated) {
            writeCSV(Settings.STUDENTS_FILE, rows);
        } else {
            errorMessage("Student ID not found in CSV: " + student.getStudentId());
        }
    }

    // [UTILITY] Check if a student is already enrolled in a course offering
    public static boolean isStudentEnrolled(String studentId, String offeringId) {
        List<String[]> rows = readCSV(Settings.ENROLLMENTS_FILE);

        for (String[] row : rows) {
            if (row.length >= 2) { // Ensure there are at least two columns: studentId and offeringId
                String sId = row[0].trim();
                String oId = row[1].trim();
                if (sId.equals(studentId) && oId.equals(offeringId)) {
                    return true; // Found a match
                }
            }
        }

        return false; // Not enrolled
    }

    // [UTILITY] Check and remove a student's enrollment
    public static void removeEnrollment(String studentId, String offeringId) {
        List<String[]> rows = readCSV(Settings.ENROLLMENTS_FILE);
        boolean removed = false;

        for (int i = rows.size() - 1; i >= 0; i--) { // iterate backwards to safely remove
            String[] row = rows.get(i);
            if (row.length >= 2 && row[0].equals(studentId) && row[1].equals(offeringId)) {
                rows.remove(i);
                removed = true;
            }
        }

        if (removed) {
            writeCSV(Settings.ENROLLMENTS_FILE, rows);
        }
    }

    public static String generatePersonId() {
        int maxId = 0;

        // Check students.csv
        List<String[]> studentRows = CSV.readCSV(Settings.STUDENTS_FILE);
        for (String[] row : studentRows) {
            String id = row[0];
            if (id.startsWith("P")) maxId = Math.max(maxId, Integer.parseInt(id.substring(1)));
        }

        // Check faculty.csv
        List<String[]> facultyRows = CSV.readCSV(Settings.ACADEMIC_STAFF_FILE);
        for (String[] row : facultyRows) {
            String id = row[0];
            if (id.startsWith("P")) maxId = Math.max(maxId, Integer.parseInt(id.substring(1)));
        }

        return "P" + String.format("%05d", maxId + 1);
    }

    public static YearLevel resolveYearLevel(String input) {
        if (input == null || input.isBlank()) return null;
        String normalized = input.trim().toUpperCase().replace("-", "_").replace(" ", "_");
        for (YearLevel level : YearLevel.values()) {
            if (level.name().equals(normalized) || level.getDisplayName().equalsIgnoreCase(input.trim())) {
                return level;
            }
        }
        return null;
    }

    public static GraduateProgram resolveGraduateProgram(String input) {
        if (input == null || input.isBlank()) return null;
        String normalized = input.trim().toUpperCase().replace("'", "").replace(".", "");
        normalized = normalized.replace(" ", "");
        if (normalized.equals("MASTER")) normalized = "MASTERS";
        for (GraduateProgram program : GraduateProgram.values()) {
            if (program.name().equalsIgnoreCase(normalized) || program.getDisplayName().equalsIgnoreCase(input.trim())) {
                return program;
            }
        }
        return null;
    }

    public static String safeValue(String[] row, int index) {
        if (row == null || index < 0 || index >= row.length) return "";
        return row[index] != null ? row[index].trim() : "";
    }

    public static String abbreviateDepartment(String deptName) {
        if (deptName == null || deptName.isBlank()) return "N/A";
        Department dept = Department.fromCodeOrFullName(deptName);
        if (dept != Department.UNASSIGNED) {
            return dept.getCode();
        }
        return generateAcronym(deptName, 6);
    }

    public static String abbreviateCourse(String courseName) {
        if (courseName == null || courseName.isBlank()) return "N/A";
        return generateAcronym(courseName, 10);
    }

    public static String generateAcronym(String value, int maxLength) {
        String[] tokens = value.replaceAll("[^A-Za-z0-9 ]", " ").split("\\s+");
        StringBuilder acronym = new StringBuilder();
        for (String token : tokens) {
            if (token.isBlank()) continue;
            String lower = token.toLowerCase();
            if (lower.equals("of") || lower.equals("and") || lower.equals("in") || lower.equals("the") || lower.equals("for") || lower.equals("to")) {
                continue;
            }
            acronym.append(Character.toUpperCase(token.charAt(0)));
            if (acronym.length() >= maxLength) break;
        }
        if (acronym.length() == 0) {
            return value.length() <= maxLength ? value : value.substring(0, maxLength);
        }
        return acronym.toString();
    }

    public static void removeStudentEnrollments(String studentId) {
        if (studentId == null || studentId.isBlank()) return;
        List<String[]> rows = CSV.readCSV(Settings.ENROLLMENTS_FILE);
        boolean updated = false;
        for (int i = rows.size() - 1; i >= 0; i--) {
            String[] row = rows.get(i);
            if (row.length > 0 && studentId.equalsIgnoreCase(row[0])) {
                rows.remove(i);
                updated = true;
            }
        }
        if (updated) {
            CSV.writeCSV(Settings.ENROLLMENTS_FILE, rows);
        }
    }
}