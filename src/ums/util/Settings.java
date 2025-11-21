package ums.util;

public final class Settings {
    private Settings() {} // ? Prevent instantiation

    // * Console Settings
    public static final int CONSOLE_WIDTH = 100;
    public static final int CONSOLE_HEIGHT = 40;
    public static final String SYMBOL = "#"; // ? Default symbol for UI display
    public static final int MS_DELAY = 50;
    
    // * User Interface (UI) Labels
    public static final String SYSTEM_TITLE = "AcademIQ";
    public static final String SYSTEM_VERSION = "v1.0";

    // * File Paths
    public static final String CREDENTIALS_FILE = "data/credentials.csv";
    public static final String STUDENTS_FILE = "data/students.csv";
    public static final String FACULTY_FILE = "data/faculty.csv";
    public static final String ACADEMIC_STAFF_FILE = "data/faculty.csv";
    public static final String NON_ACAD_STAFF_FILE = "data/faculty.csv";
    public static final String COURSES_FILE = "data/courses.csv";
    public static final String COURSE_OFFERINGS_FILE = "data/course-offerings.csv";
    public static final String ENROLLMENTS_FILE = "data/enrollments.csv";

    // * CSV Col Indices
    // [BASE: Student] - 0 to 15
    public static final int COL_PERSON_ID       = 0;  // e.g., P001
    public static final int COL_FIRST_NAME      = 1;
    public static final int COL_MIDDLE_NAME     = 2;
    public static final int COL_LAST_NAME       = 3;
    public static final int COL_DOB             = 4;  // yyyy-MM-dd
    public static final int COL_GENDER          = 5;  // MALE, FEMALE
    public static final int COL_ADDRESS         = 6;
    public static final int COL_CONTACT         = 7;
    public static final int COL_EMAIL           = 8;  // Login key
    public static final int COL_STUDENT_ID      = 9;  // Official ID (e.g., S2023001)
    public static final int COL_ENROLL_DATE     = 10; // yyyy-MM-dd
    public static final int COL_DEPARTMENT      = 11; // Full name string
    public static final int COL_COURSE          = 12; // Full name string
    public static final int COL_ACAD_STANDING   = 13; // GOOD, PROBATION, etc.
    public static final int COL_GPA             = 14; // e.g., 3.25
    public static final int COL_CREDITS         = 15; // Integer

    // [Graduate-only]
    public static final int COL_PROGRAM_LEVEL   = 16; // MASTER, PHD
    public static final int COL_THESIS_TITLE    = 17;
    public static final int COL_ADVISOR_ID      = 18; // Faculty ID (e.g., F001)

    // [Master-specific]
    public static final int COL_MASTER_COURSEWORK_COMP   = 19; // true/false
    public static final int COL_MASTER_COMP_EXAM_PASS    = 20; // true/false
    public static final int COL_MASTER_THESIS_APPROVED   = 21; // true/false

    // [PhD-specific]
    public static final int COL_PHD_QUAL_EXAM_PASS       = 19; // true/false
    public static final int COL_PHD_PROP_DEFENDED        = 20; // true/false
    public static final int COL_PHD_PUBLICATIONS        = 21; // Comma-separated titles
    public static final int COL_PHD_CANDIDACY           = 22; // true/false
    public static final int COL_PHD_DEFENSE_DATE         = 23; // yyyy-MM-dd or empty

    public static final int COL_FACULTY_ID      = 24; // e.g., F001
    public static final int COL_HIRE_DATE       = 25; // yyyy-MM-dd
    public static final int COL_RANK            = 26; // Assistant, Associate, Full
    public static final int COL_DEPARTMENT_FAC  = 27; // Department full name (override / separate from student dept)
    public static final int COL_OFFICE          = 28; // Office location (Room 101)
    public static final int COL_SALARY          = 29; // double
    public static final int COL_FULL_TIME       = 30; // true/false

    // CSV Preferences
    public static final int STUDENTS_CSV_MAX_COLUMNS = 24;
    public static final int TOTAL_COLS = 16;
    public static final int COL_TEACHING_HOURS  = 31; // e.g., 18
    public static final int COL_MAX_COURSES     = 32; // e.g., 3 (or use your default)

    // Keys - ASCII Value
    public static final int ESC_KEY = 27;
    public static final int UP_KEY = 1001;
    public static final int DOWN_KEY = 1002;
    public static final int LEFT_KEY = 1003;
    public static final int RIGHT_KEY = 1004;
}
