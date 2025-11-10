package ums.util;

public final class Settings {
    private Settings() {} // ? Prevent instantiation

    // * Console Settings
    public static final int CONSOLE_WIDTH = 100;
    public static final int CONSOLE_HEIGHT = 30;
    public static final String SYMBOL = "#"; // ? Default symbol for UI display
    public static final int MS_DELAY = 150;
    
    // * User Interface (UI) Labels
    public static final String SYSTEM_TITLE = "AcademIQ";
    public static final String SYSTEM_VERSION = "v1.0";

    // * File Paths
    public static final String STUDENTS_FILE = "data/students.csv";
    public static final String FACULTY_FILE = "data/faculty.csv";
    public static final String COURSES_FILE = "data/courses.csv";
    public static final String ENROLLMENTS_FILE = "data/enrollments.csv";
}
