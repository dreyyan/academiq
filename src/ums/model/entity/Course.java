package ums.model.entity;

import ums.model.enums.Courses;
// [IMPORT] Enums
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

    // Helper to create Course from a name or code
    public static Course fromCodeOrFullName(String input) {
        if (input == null || input.isBlank()) return new Course("Not Assigned");

        input = input.trim().replace(" ", "_").toUpperCase(); // normalize for enum matching

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

        // If not found, just return a course with the input as title
        return new Course(input.replace("_", " "));
    }

    @Override
    public String toString() {
        return this.title != null ? this.title : "Not Assigned";
    }
}
