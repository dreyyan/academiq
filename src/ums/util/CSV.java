package ums.util;

// [IMPORT] Standard
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.DayOfWeek;
import java.util.stream.Collectors;


// [IMPORT] Exceptions
import java.io.IOException;

// [IMPORT] Enums
import ums.model.enums.GraduateProgram;
import ums.model.enums.YearLevel;
import ums.model.enums.Department;
import ums.model.enums.FacultyRank;

// [IMPORT] Models
import ums.model.AcademicStaff;
import ums.model.Student;
import ums.model.AcademicStaff;
import ums.model.CourseCatalog;

// [IMPORT] Entities
import ums.model.entity.Course;
import ums.model.entity.CourseOffering;
import ums.model.entity.TimeSlot;

// [IMPORT] Enums
import ums.model.enums.AcademicStanding;
import ums.model.enums.Gender;
import ums.model.enums.Semester;

// [IMPORT] Utilities
import ums.util.console.ConsoleDisplay;
import ums.util.console.ConsoleInput;
import ums.util.Settings;

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
            ConsoleDisplay.dialogBox("error", "Failed to read CSV: " + e.getMessage());
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
            ConsoleDisplay.dialogBox("error", "Failed to read CSV: " + e.getMessage());
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
        // PersonId is generated automatically by Person constructor
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

            Semester semester;
            switch (row[2].trim().toUpperCase()) {
                case "FIRST_SEM":
                case "FIRST":
                case "1ST SEM":
                case "1ST SEMESTER":
                    semester = Semester.FIRST_SEM;
                    break;

                case "SECOND_SEM":
                case "SECOND":
                case "2ND SEM":
                case "2ND SEMESTER":
                    semester = Semester.SECOND_SEM;
                    break;

                default:
                    throw new IllegalArgumentException("Invalid semester: " + row[2]);
            }

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
        if (student == null || student.getPersonId() == null) {
            ConsoleDisplay.dialogBox("error", "Cannot update null student or student without ID.");
            return;
        }

        List<String[]> rows = readCSV(Settings.STUDENTS_FILE);
        boolean updated = false;

        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);

            // Match by Person ID
            if (row.length > Settings.COL_PERSON_ID && row[Settings.COL_PERSON_ID].equals(student.getPersonId())) {

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
            ConsoleDisplay.dialogBox("error", "Student ID not found in CSV: " + student.getPersonId());
        }
    }

    // [UTILITY] Update academic staff record
    public static void updateAcademicStaffRecord(AcademicStaff staff) {
        if (staff == null || staff.getPersonId() == null || staff.getPersonId().isBlank()) {
            ConsoleDisplay.dialogBox("error", "Cannot update null staff or staff without ID.");
            return;
        }

        List<String[]> rows = readCSV(Settings.ACADEMIC_STAFF_FILE);
        boolean updated = false;

        String staffId = staff.getPersonId().trim();  // Trim to avoid whitespace issues

        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);

            // Skip empty or malformed rows
            if (row == null || row.length <= Settings.COL_PERSON_ID) continue;

            String rowId = row[Settings.COL_PERSON_ID].trim();

            if (rowId.equalsIgnoreCase(staffId)) { // match ignoring case
                // Convert staff to a new CSV row
                String[] newRow = staff.toCSVRow();

                // Ensure proper row size
                int requiredLength = Settings.STAFF_TOTAL_COLS;
                if (newRow.length < requiredLength) {
                    String[] adjusted = new String[requiredLength];
                    System.arraycopy(newRow, 0, adjusted, 0, newRow.length);
                    for (int j = newRow.length; j < requiredLength; j++) adjusted[j] = "";
                    newRow = adjusted;
                }

                rows.set(i, newRow);
                updated = true;
                break;
            }
        }

        if (updated) {
            writeCSV(Settings.ACADEMIC_STAFF_FILE, rows);
        } else {
            ConsoleDisplay.dialogBox("error", "Academic Staff ID not found in CSV: " + staffId);
        }
    }

    // [UTILITY] Check if a student is already enrolled in a course offering
    public static boolean isStudentEnrolled(String personId, String offeringId) {
        List<String[]> rows = readCSV(Settings.ENROLLMENTS_FILE);

        for (String[] row : rows) {
            if (row.length >= 2) { // Ensure there are at least two columns: personId and offeringId
                String sId = row[0].trim();
                String oId = row[1].trim();
                if (sId.equals(personId) && oId.equals(offeringId)) {
                    return true; // Found a match
                }
            }
        }

        return false; // Not enrolled
    }

    // [UTILITY] Check and remove a student's enrollment
    public static void removeEnrollment(String personId, String offeringId) {
        List<String[]> rows = readCSV(Settings.ENROLLMENTS_FILE);
        boolean removed = false;

        for (int i = rows.size() - 1; i >= 0; i--) { // iterate backwards to safely remove
            String[] row = rows.get(i);
            if (row.length >= 2 && row[0].equals(personId) && row[1].equals(offeringId)) {
                rows.remove(i);
                removed = true;
            }
        }

        if (removed) {
            writeCSV(Settings.ENROLLMENTS_FILE, rows);
        }
    }

    // [UTILITY] Generate a new unique Person ID
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

    // [UTILITY] Resolve YearLevel from input string
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

    // [UTILITY] Resolve GraduateProgram from input string
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

    // [UTILITY] Safely get value from CSV row by index
    public static String safeValue(String[] row, int index) {
        if (row == null || index < 0 || index >= row.length) return "";
        return row[index] != null ? row[index].trim() : "";
    }

    // [UTILITY] Abbreviate department name to its code or acronym up to 6 characters
    public static String abbreviateDepartment(String deptName) {
        if (deptName == null || deptName.isBlank()) return "N/A";
        Department dept = Department.fromCodeOrFullName(deptName);
        if (dept != Department.UNASSIGNED) {
            return dept.getCode();
        }
        return generateAcronym(deptName, 6);
    }

    // [UTILITY] Abbreviate course name to acronym up to 10 characters
    public static String abbreviateCourse(String courseName) {
        if (courseName == null || courseName.isBlank()) return "N/A";
        return generateAcronym(courseName, 10);
    }

    // [UTILITY] Generate acronym from a given string up to maxLength
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

    // [UTILITY] Remove all enrollments of a student by personId
    public static void removeStudentEnrollments(String personId) {
        if (personId == null || personId.isBlank()) return;
        List<String[]> rows = CSV.readCSV(Settings.ENROLLMENTS_FILE);
        boolean updated = false;
        for (int i = rows.size() - 1; i >= 0; i--) {
            String[] row = rows.get(i);
            if (row.length > 0 && personId.equalsIgnoreCase(row[0])) {
                rows.remove(i);
                updated = true;
            }
        }
        if (updated) {
            CSV.writeCSV(Settings.ENROLLMENTS_FILE, rows);
        }
    }

    public static int parsePositiveInt(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }

        try {
            int parsed = Integer.parseInt(value.trim());
            if (parsed <= 0) {
                throw new IllegalArgumentException(fieldName + " must be greater than 0.");
            }
            return parsed;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid number.");
        }
    }

    public static Semester parseSemesterInput(String input) {
        String sem = input.trim().toUpperCase();
        switch (sem) {
            case "FIRST":
            case "FIRST_SEM":
            case "1ST SEM":
            case "1ST SEMESTER":
                return Semester.FIRST_SEM;
            case "SECOND":
            case "SECOND_SEM":
            case "2ND SEM":
            case "2ND SEMESTER":
                return Semester.SECOND_SEM;
            default:
                throw new IllegalArgumentException("Invalid semester input: " + input);
        }
    }

    public static DayOfWeek parseDayOfWeekInput(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Day of week is required.");
        }

        String normalized = value.trim().toUpperCase();
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day.name().startsWith(normalized)) {
                return day;
            }
        }

        throw new IllegalArgumentException("Invalid day of week. Use names like MON or Monday.");
    }

    public static LocalTime parseTimeInput(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Time is required.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        return LocalTime.parse(value.trim().toUpperCase(), formatter);
    }

    public static String generateOfferingId(Course course, AcademicStaff staff) {
        String courseCode = (course != null) ? course.getCourseCode() : null;
        if (courseCode == null || courseCode.isBlank()) {
            courseCode = (course != null)
                    ? CSV.abbreviateCourse(course.getTitle())
                    : "CRS";
        }
        String prefix = courseCode.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
        if (prefix.isBlank() && course != null && course.getTitle() != null) {
            prefix = CSV.abbreviateCourse(course.getTitle());
        }
        if (prefix.length() > 8) {
            prefix = prefix.substring(0, 8);
        }

        String staffId = (staff != null && staff.getPersonId() != null)
                ? staff.getPersonId()
                : "STAFF";
        if (staffId.length() > 4) {
            staffId = staffId.substring(0, 4);
        }

        String suffix = UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 4)
                .toUpperCase();

        return prefix + "-" + staffId + "-" + suffix;
    }

    public static String[] buildCourseOfferingCsvRow(CourseOffering offering) {
        Course course = offering.getCourse();
        String courseIdentifier = (course != null && course.getCourseCode() != null && !course.getCourseCode().isBlank())
                ? course.getCourseCode()
                : (course != null ? course.getTitle() : "");

        return new String[] {
                offering.getOfferingId(),
                courseIdentifier != null ? courseIdentifier : "",
                offering.getSemester() != null ? offering.getSemester().toString() : "",
                String.valueOf(offering.getYear()),
                offering.getInstructor() != null ? offering.getInstructor().getPersonId() : "",
                offering.getSchedule() != null ? offering.getSchedule().toString() : "",
                String.valueOf(offering.getCapacity()),
                String.valueOf(offering.getEnrolledCount())
        };
    }

public static List<CourseOffering> fetchCourseOfferingsForStaff(AcademicStaff staff) throws IOException {
    if (staff == null) return List.of();

    List<String[]> rows = CSV.readCSV(Settings.COURSE_OFFERINGS_FILE);
    List<CourseOffering> staffOfferings = new ArrayList<>();

    for (int i = 0; i < rows.size(); i++) { // skip header
        String[] row = rows.get(i);
        if (row.length < 8) continue; // ensure all columns exist

        String offeringId = row[0];
        String instructorId = row[4].trim();

        // Get course by title only
        Course course = CourseCatalog.getCourseByName(row[1].trim());
        if (course == null) {
            System.err.println("Warning: Course not found in catalog: " + row[1]);
            continue; // skip invalid course
        }

        Semester semester = Semester.fromString(row[2]);
        int year = Integer.parseInt(row[3]);
        TimeSlot schedule = TimeSlot.fromString(row[5]);
        int capacity = Integer.parseInt(row[6]);
        int enrolled = Integer.parseInt(row[7]);

        System.out.println("staffId='" + staff.getPersonId() + "', instructorId='" + instructorId + "'");

        // Only add courses taught by this staff
        if (!staff.getPersonId().trim().equalsIgnoreCase(instructorId)) continue;

        staffOfferings.add(new CourseOffering(
            offeringId, course, semester, year, staff, schedule, capacity, enrolled
        ));
    }

    staff.setCourseOfferingsTaught(staffOfferings);
    return staffOfferings;
}


public static List<CourseOffering> fetchAllCourseOfferings() throws IOException {
    List<String[]> rows = CSV.readCSV(Settings.COURSE_OFFERINGS_FILE);
    List<CourseOffering> offerings = new ArrayList<>();

    for (int i = 1; i < rows.size(); i++) {
        String[] row = rows.get(i);
        if (row.length < 7) continue;

        String offeringId = row[0];
        String courseCode = row[1];
        Course course = CourseCatalog.getCourseByCode(courseCode); // can be null if not in catalog
        AcademicStaff instructor = null; // we don’t need full staff here, just ID for removal
        Semester semester = Semester.valueOf(row[3].toUpperCase().replace(" ", "_"));
        int year = Integer.parseInt(row[4]);
        int capacity = Integer.parseInt(row[5]);
        int enrolled = Integer.parseInt(row[6]);

        offerings.add(new CourseOffering(offeringId, course, semester, year, instructor, null, capacity, enrolled));
    }
    return offerings;
}

    public static List<String[]> fetchAllCourseOfferingsRaw() throws IOException {
        // Reads all rows from the CSV including the header
        return CSV.readCSV(Settings.COURSE_OFFERINGS_FILE);
    }

    public static void saveAllCourseOfferingsRaw(List<String[]> rows) throws IOException {
        CSV.writeCSV(Settings.COURSE_OFFERINGS_FILE, rows);
    }

    public static void saveAllCourseOfferings(List<CourseOffering> offerings) throws IOException {
        List<String[]> rows = new ArrayList<>();

        // Header
        rows.add(new String[] { "OfferingID", "CourseCode", "InstructorID", "Semester", "Year", "Capacity", "EnrolledCount" });

        for (CourseOffering co : offerings) {
            String courseCode = co.getCourse() != null ? co.getCourse().getCourseCode() : "";
            String instructorId = co.getInstructor() != null ? co.getInstructor().getPersonId() : "";

            rows.add(new String[] {
                co.getOfferingId(),
                courseCode,
                instructorId,
                co.getSemester() != null ? co.getSemester().toString() : "",
                String.valueOf(co.getYear()),
                String.valueOf(co.getCapacity()),
                String.valueOf(co.getEnrolledCount())
            });
        }

        CSV.writeCSV(Settings.COURSE_OFFERINGS_FILE, rows);
    }

    public static AcademicStaff fetchStaffById(String id) throws IOException {
        List<String[]> staffRows = CSV.readCSV(Settings.ACADEMIC_STAFF_FILE);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        for (int i = 1; i < staffRows.size(); i++) { // skip header
            String[] row = staffRows.get(i);
            if (row[0].equals(id)) {

                // Parse dateOfBirth
                LocalDate dob = row[4].isBlank() ? null : LocalDate.parse(row[4], dateFormatter);

                // Parse gender
                Gender gender = row[5].isBlank() ? null : Gender.valueOf(row[5].toUpperCase());

                // Other string fields
                String address = row[6];
                String contact = row[7];
                String email = row[8];

                // Parse department
                Department department = row[9].isBlank() ? Department.UNASSIGNED : Department.valueOf(row[9]);

                // Parse rank
                FacultyRank rank = row[10].isBlank() ? null : FacultyRank.valueOf(row[10].toUpperCase());

                // Parse hire date
                LocalDate hireDate = row[11].isBlank() ? null : LocalDate.parse(row[11], dateFormatter);

                // Office location
                String officeLocation = row[12];

                // Salary
                double salary = row[13].isBlank() ? 0 : Double.parseDouble(row[13]);

                // Tenure
                boolean isTenured = row[16].equalsIgnoreCase("true") || row[16].equalsIgnoreCase("yes");

                // Create AcademicStaff
                AcademicStaff staff = new AcademicStaff(
                    row[0], row[1], row[2], row[3],
                    dob, gender,
                    address, contact, email,
                    department, rank, hireDate,
                    officeLocation, salary, isTenured,
                    new ArrayList<>(), // empty advisees list
                    0,                // teachingHoursPerWeek default
                    18                // maxTeachingLoad default
                );

                return staff;
            }
        }
        return null;
    }
}