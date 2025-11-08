package ums.model.entity;

// [IMPORT] Standard
import java.util.List;

// [IMPORT] Enums
import ums.model.Faculty;

public class Department {
    // * Attributes
    private String deptCode;
    private String name;
    private Faculty head;
    private List<Course> courses;

    // * Constructor (Parameterized)
    Department(String deptCode, String name, Faculty head, List<Course> courses) {
        this.deptCode = deptCode;
        this.name = name;
        this.head = head;
        this.courses = courses;
    }

    // * Getters
    public String getDeptCode() { return this.deptCode; }
    public String getName() { return this.name; }
    public Faculty getHead() { return this.head; }
    public List<Course> getCourses() { return this.courses; }

    // * Setters
    public void setHead(Faculty head) { this.head = head; }

    // * Methods
    public void addCourse(Course course) {}
    public void removeCourse(Course course) {}
}