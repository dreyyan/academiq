package ums.model.entity;

// [IMPORT] Enums
import ums.model.enums.Department;

public class Course {
    // * Attributes
    private String courseCode;
    private String title;
    private String description;
    private int credits;
    private Department department;

    // * Constructor (Parameterized)
    Course(String courseCode, String title, String description, int credits, Department department) {
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
}
