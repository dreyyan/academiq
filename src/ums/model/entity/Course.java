package ums.model.entity;

// [IMPORT] Enums
import ums.model.enums.Courses;
import ums.model.enums.Department;

public class Course {
    // * Attributes
    private String courseCode;
    private String title;
    private String description;
    private int credits;
    private Department department;

    // * Constructor (One Parameter)
    public Course(String title) {
        this.title = title;
    }

    // * Constructor (Parameterized - Title & Department Only)
    public Course(String title, Department department) {
        this.title = title;
        this.department = department;
    }
    
    // * Constructor (Parameterized)
    public Course(String courseCode, String title, String description, int credits, Department department) {
        this.courseCode = courseCode;
        this.title = title;
        this.description = description;
        this.credits = credits;
        this.department = department;
    }

    // * Getters
    public String getCourseCode() { return this.courseCode; }
    public String getTitle() { return this.title; }
    public String getDescription() { return this.description; }
    public int getCredits() { return this.credits; }
    public Department getDepartment() { return this.department; }

    // * Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCredits(int credits) { this.credits = credits; }

    // * Methods
    // [HELPER] Map user input (full name or shorthand) to a 'Course' object
    public static <T extends Enum<T>> Course mapCourseInput(String input, T[] courses, Department department) {
        input = input.trim().toLowerCase(); // Normalize input

        // Check each course in the provided enum array
        for (T c : courses) {
            String fullName = ((Object)c).toString();
            String shortName = generateCourseShortName(fullName);

            // Match input against full name or shorthand
            if (fullName.equalsIgnoreCase(input) || shortName.equalsIgnoreCase(input)) {
                return new Course(fullName, department);
            }
        }

        return null;
    }

    // [METHOD] Generate shorthand for a course from its full name
    public static String generateCourseShortName(String fullName) {
        // Generate shorthand based on common degree prefixes
        if (fullName.startsWith("Bachelor of Science in ")) {
            return "BS " + fullName.substring("Bachelor of Science in ".length());
        } else if (fullName.startsWith("Bachelor of Arts in ")) {
            return "BA " + fullName.substring("Bachelor of Arts in ".length());
        } else if (fullName.startsWith("Doctor of Medicine")) {
            return "MD";
        } else if (fullName.startsWith("Juris Doctor")) {
            return "JD";
        } else if (fullName.startsWith("Doctor of Dental Medicine")) {
            return "DMD";
        }
        return fullName; // Return full name if no shorthand applicable
    }

    // [HELPER] Create Course from name or code
    public static Course fromCodeOrFullName(String input) {
        // Handle null or blank input
        if (input == null || input.isBlank()) return new Course("Not Assigned");

        // Normalize input to match enum values
        input = input.trim().replace(" ", "_").toUpperCase();

        // Check all course enums in each department
        for (Courses.CASCourse c : Courses.CASCourse.values()) {
            if (c.name().equalsIgnoreCase(input) || c.getFullName().equalsIgnoreCase(input.replace("_", " "))) {
                return new Course(c.getFullName());
            }
        }
        for (Courses.CICTCourse c : Courses.CICTCourse.values()) {
            if (c.name().equalsIgnoreCase(input) || c.getFullName().equalsIgnoreCase(input.replace("_", " "))) {
                return new Course(c.getFullName());
            }
        }
        for (Courses.CBMCourse c : Courses.CBMCourse.values()) {
            if (c.name().equalsIgnoreCase(input) || c.getFullName().equalsIgnoreCase(input.replace("_", " "))) {
                return new Course(c.getFullName());
            }
        }
        for (Courses.COPCourse c : Courses.COPCourse.values()) {
            if (c.name().equalsIgnoreCase(input) || c.getFullName().equalsIgnoreCase(input.replace("_", " "))) {
                return new Course(c.getFullName());
            }
        }
        for (Courses.COMCourse c : Courses.COMCourse.values()) {
            if (c.name().equalsIgnoreCase(input) || c.getFullName().equalsIgnoreCase(input.replace("_", " "))) {
                return new Course(c.getFullName());
            }
        }
        for (Courses.COECourse c : Courses.COECourse.values()) {
            if (c.name().equalsIgnoreCase(input) || c.getFullName().equalsIgnoreCase(input.replace("_", " "))) {
                return new Course(c.getFullName());
            }
        }
        for (Courses.CONCourse c : Courses.CONCourse.values()) {
            if (c.name().equalsIgnoreCase(input) || c.getFullName().equalsIgnoreCase(input.replace("_", " "))) {
                return new Course(c.getFullName());
            }
        }
        for (Courses.COCCourse c : Courses.COCCourse.values()) {
            if (c.name().equalsIgnoreCase(input) || c.getFullName().equalsIgnoreCase(input.replace("_", " "))) {
                return new Course(c.getFullName());
            }
        }
        for (Courses.COLCourse c : Courses.COLCourse.values()) {
            if (c.name().equalsIgnoreCase(input) || c.getFullName().equalsIgnoreCase(input.replace("_", " "))) {
                return new Course(c.getFullName());
            }
        }
        for (Courses.CODCourse c : Courses.CODCourse.values()) {
            if (c.name().equalsIgnoreCase(input) || c.getFullName().equalsIgnoreCase(input.replace("_", " "))) {
                return new Course(c.getFullName());
            }
        }
        for (Courses.ILSCourse c : Courses.ILSCourse.values()) {
            if (c.name().equalsIgnoreCase(input) || c.getFullName().equalsIgnoreCase(input.replace("_", " "))) {
                return new Course(c.getFullName());
            }
        }

        // If not found, return a course with input as title
        return new Course(input.replace("_", " "));
    }

    // [HELPER] Create Course from short name
    public static Course fromShortName(String shortName) {
        if (shortName == null || shortName.isBlank()) return null;

        // Search all course enums
        for (Class<?> c : Courses.class.getDeclaredClasses()) {
            try {
                Object[] constants = c.getEnumConstants();
                for (Object constant : constants) {
                    String fullName = constant.toString();
                    String mappedShortName = mapToShortName(fullName);
                    if (mappedShortName.equalsIgnoreCase(shortName)) {
                        // Determine department from enum name prefix
                        String enumName = c.getSimpleName();
                        Department dept = mapEnumNameToDepartment(enumName);
                        return new Course(fullName, dept);
                    }
                }
            } catch (Exception ignored) {}
        }

        return null; // Not found
    }

    // [HELPER] Map full course name to its shorthand
    public static String mapToShortName(String fullName) {
        if (fullName.startsWith("Bachelor of Science in ")) {
            return "BS " + fullName.substring("Bachelor of Science in ".length());
        } else if (fullName.startsWith("Bachelor of Arts in ")) {
            return "BA " + fullName.substring("Bachelor of Arts in ".length());
        } else if (fullName.equalsIgnoreCase("Doctor of Medicine")) {
            return "MD";
        } else if (fullName.equalsIgnoreCase("Doctor of Dental Medicine")) {
            return "DMD";
        } else {
            return fullName;
        }
    }

    // [HELPER] Map enum class name to Department
    public static Department mapEnumNameToDepartment(String enumClassName) {
        switch (enumClassName) {
            case "CASCourse": return Department.CAS;
            case "CICTCourse": return Department.CICT;
            case "CBMCourse": return Department.CBM;
            case "COMCourse": return Department.COM;
            case "COPCourse": return Department.COP;
            case "COECourse": return Department.COE;
            case "CONCourse": return Department.CON;
            case "COCCourse": return Department.COC;
            case "COLCourse": return Department.COL;
            case "CODCourse": return Department.COD;
            case "ILSCourse": return Department.ILS;
            default: return Department.UNASSIGNED;
        }
    }

    // [METHOD: Override] Return course as string
    @Override
    public String toString() {
        return this.title != null ? this.title : "Not Assigned";
    }
}
