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

import ums.model.AcademicStaff;
import ums.model.entity.Course;
import ums.model.entity.CourseOffering;
import ums.model.entity.TimeSlot;
import ums.model.enums.AcademicStanding;
import ums.model.enums.Gender;
import ums.model.enums.Semester;

// [IMPORT] Project Files
import static ums.util.Logger.errorMessage;

public class CSV {
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

    // Append a single row to a CSV file
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

    public static void registerNewStudent(String email, String password, String userType) {
        // 1️⃣ Add credentials to credentials.csv
        CSV.appendRow(Settings.CREDENTIALS_FILE, new String[] { email, password, userType });

        // 2️⃣ Add a default student row to students.csv
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

    public static List<CourseOffering> readCourseOfferings() {
        List<String[]> rows = readCSV(Settings.COURSE_OFFERINGS_FILE); // Your CSV read method
        List<CourseOffering> offerings = new ArrayList<>();

        for (String[] row : rows) {
            Course course = Course.fromCodeOrFullName(row[1]); // adjust index to match CSV
            Semester semester = Semester.valueOf(row[2].toUpperCase());
            int year = Integer.parseInt(row[3]);
            AcademicStaff instructor = Auth.getAcademicStaffById(row[4]); // implement lookup
            TimeSlot schedule = TimeSlot.fromString(row[5]); // implement parsing if needed
            int capacity = Integer.parseInt(row[6]);
            int enrolled = Integer.parseInt(row[7]);

            offerings.add(new CourseOffering(row[0], course, semester, year, instructor, schedule, capacity, enrolled));
        }

        return offerings;
    }

    public static void updateCourseOffering(CourseOffering offering) {
        List<String[]> rows = readCSV(Settings.COURSE_OFFERINGS_FILE);

        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i)[0].equals(offering.getOfferingId())) {
                // Update enrolled count
                rows.get(i)[7] = String.valueOf(offering.getEnrolledCount());
                break;
            }
        }

        writeCSV(Settings.COURSE_OFFERINGS_FILE, rows); // overwrite file
    }
}
