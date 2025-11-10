package ums.model;

// [IMPORT] Standard
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ums.model.entity.CourseOffering;
import ums.model.enums.Department;
import ums.model.enums.FacultyRank;

public abstract class AcademicStaff extends Faculty {
    // * Attributes
    private String teacherId;
    private List<CourseOffering> coursesTaught;
    private int teachingHoursPerWeek;
    private int maxTeachingLoad = 18;

    // * Constructor (Parameterized)
    public AcademicStaff(String facultyId, Department department, FacultyRank rank, 
                        LocalDate hireDate, String officeLocation, double salary, 
                        boolean isTenured, String teacherId) {
        super(facultyId, department, rank, hireDate, officeLocation, salary, isTenured);
        this.teacherId = teacherId;
        this.coursesTaught = new ArrayList<>();
        this.teachingHoursPerWeek = 0;
    }

    // * Getters
    public String getTeacherId() { return this.teacherId; }
    public List<CourseOffering> getCoursesTaught() { return new ArrayList<>(coursesTaught); }
    public int getTeachingHoursPerWeek() { return this.teachingHoursPerWeek; }

    // * Methods
    // [METHOD] Add a course offering to the teaching list
    public void addCourseOffering(CourseOffering offering) {
        if (offering != null && !coursesTaught.contains(offering)) {
            if (!isOverloaded()) {
                coursesTaught.add(offering);
                updateTeachingHours(offering.getCourse().getCredits());
            } else {
                System.out.println("Cannot add course - teaching load would exceed maximum.");
            }
        }
    }

    // [METHOD] Remove a course offering from the teaching list
    public void removeCourseOffering(CourseOffering offering) {
        if (coursesTaught.remove(offering)) {
            updateTeachingHours(-offering.getCourse().getCredits());
        }
    }

    // [METHOD] Update teaching hours
    public void updateTeachingHours(int hours) {
        this.teachingHoursPerWeek += hours;
        if (this.teachingHoursPerWeek < 0) {
            this.teachingHoursPerWeek = 0;
        }
    }

    // [METHOD] Calculate total teaching load
    public int calculateTeachingLoad() {
        return this.teachingHoursPerWeek;
    }

    // [METHOD] Check if teaching load exceeds maximum
    public boolean isOverloaded() {
        return this.teachingHoursPerWeek >= maxTeachingLoad;
    }

    // [METHOD] Generate performance report 
    public String generatePerformanceReport() {
        StringBuilder report = new StringBuilder();
        report.append("======= Academic Staff Performance Report =======\n");
        report.append(super.generateReport()); // Call parent's generateReport()
        report.append("Teacher ID: ").append(teacherId).append("\n");
        report.append("Teaching Hours Per Week: ").append(teachingHoursPerWeek).append("\n");
        report.append("Max Teaching Load: ").append(maxTeachingLoad).append("\n");
        report.append("Overloaded: ").append(isOverloaded() ? "Yes" : "No").append("\n");
        report.append("Courses Taught: ").append(coursesTaught.size()).append("\n");
        
        if (!coursesTaught.isEmpty()) {
            report.append("\nCourse Details:\n");
            for (CourseOffering course : coursesTaught) {
                report.append("  - ").append(course.toString()).append("\n");
            }
        }
        
        return report.toString();
    }

    // [METHOD] Display academic staff information
    @Override
    public void displayInfo() {
        super.displayInfo(); // Call parent's displayInfo()
        System.out.println("Teacher ID: " + getTeacherId());
        System.out.println("Teaching Hours Per Week: " + getTeachingHoursPerWeek());
        System.out.println("Max Teaching Load: " + maxTeachingLoad);
        System.out.println("Overloaded: " + (isOverloaded() ? "Yes" : "No"));
        System.out.println("Number of Courses: " + getCoursesTaught().size());
    }

    // [METHOD] Generate report 
    @Override
    public String generateReport() {
        return generatePerformanceReport();
    }
}