package ums.util;

public final class Settings {
    private Settings() {} // Prevent instantiation

    // * Console Settings
    public static final int CONSOLE_WIDTH = 100;
    public static final int CONSOLE_HEIGHT = 40;
    public static final String SYMBOL = "#";
    public static final int MS_DELAY = 50;

    // * Labels
    public static final String SYSTEM_TITLE = "AcademIQ";
    public static final String SYSTEM_VERSION = "v1.0";

    // * File Paths
    public static final String CREDENTIALS_FILE = "data/credentials.csv";
    public static final String STUDENTS_FILE = "data/students.csv";
    public static final String ACADEMIC_STAFF_FILE = "data/teachers.csv";
    public static final String NON_ACADEMIC_STAFF_FILE = "data/administrators.csv";
    public static final String COURSES_FILE = "data/courses.csv";
    public static final String COURSE_OFFERINGS_FILE = "data/course-offerings.csv";
    public static final String ENROLLMENTS_FILE = "data/enrollments.csv";

    // ======================
    // Common Columns (ALL USERS)
    // ======================
    public static final int COL_PERSON_ID   = 0;
    public static final int COL_FIRST_NAME  = 1;
    public static final int COL_MIDDLE_NAME = 2;
    public static final int COL_LAST_NAME   = 3;
    public static final int COL_DOB         = 4;
    public static final int COL_GENDER      = 5;
    public static final int COL_ADDRESS     = 6;
    public static final int COL_CONTACT     = 7;
    public static final int COL_EMAIL       = 8; // login key

    // ======================
    // Students.csv
    // ======================
    public static final int COL_ENROLL_DATE     = 9;
    public static final int COL_DEPARTMENT      = 10;
    public static final int COL_COURSE          = 11;
    public static final int COL_ACAD_STANDING   = 12;
    public static final int COL_GPA             = 13;
    public static final int COL_CREDITS         = 14;

    // Graduate-specific
    public static final int COL_PROGRAM_LEVEL   = 15;
    public static final int COL_THESIS_TITLE    = 16;
    public static final int COL_ADVISOR_ID      = 17;

    // ======================
    // Teachers.csv (Academic Staff)
    // ======================
    public static final int COL_DEPARTMENT_FAC  = 9;
    public static final int COL_RANK            = 10;
    public static final int COL_HIRE_DATE       = 11;
    public static final int COL_OFFICE          = 12;
    public static final int COL_SALARY          = 13;
    public static final int COL_TEACHING_HOURS  = 14;
    public static final int COL_MAX_COURSES     = 15;
    public static final int COL_FULL_TIME       = 16;

    // ======================
    // Administrators.csv (Non-Academic Staff)
    // ======================
    public static final int COL_DEPARTMENT_STAFF = 9;
    public static final int COL_POSITION         = 10;
    public static final int COL_HIRE_DATE_ADMIN  = 11;
    public static final int COL_OFFICE_ADMIN     = 12;
    public static final int COL_SALARY_ADMIN     = 13;
    public static final int COL_ACTIVE           = 14;
    public static final int COL_LEAVE_DAYS       = 15;
    public static final int COL_ROLE_START_DATE  = 16;
    public static final int COL_ROLE_TITLE       = 17;
    public static final int COL_SHIFT_SCHEDULE   = 18;

    // ======================
    // CSV Limits
    // ======================
    public static final int STUDENTS_CSV_MAX_COLUMNS = 24;
    public static final int TOTAL_COLS = 19; // highest index + 1

    // ======================
    // Keys
    // ======================
    public static final int ESC_KEY = 27;
    public static final int UP_KEY = 1001;
    public static final int DOWN_KEY = 1002;
    public static final int LEFT_KEY = 1003;
    public static final int RIGHT_KEY = 1004;
}