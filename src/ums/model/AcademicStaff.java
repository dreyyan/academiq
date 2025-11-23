package ums.model;

// [IMPORT] Standard
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// [IMPORT] Models
import ums.model.GraduateStudent;

// [IMPORT] Entities
import ums.model.entity.CourseOffering;

// [IMPORT] Enums
import ums.model.enums.Department;
import ums.model.enums.FacultyRank;
import ums.model.enums.Gender;

// [IMPORT] Utilities
import ums.util.Logger;

public class AcademicStaff extends Faculty {
    // * Attributes
    private List<CourseOffering> coursesTaught;
    private int teachingHoursPerWeek;
    private int maxTeachingLoad = 18;

    // * Constructor (Parameterized)
    public AcademicStaff(
        // Person Attributes
        String firstName, String middleName, String lastName, LocalDate dateOfBirth, Gender gender, String address, String contactNumber, String email,
        // Faculty Attributes
        Department department, FacultyRank rank, LocalDate hireDate, String officeLocation, double salary, boolean isTenured, List<GraduateStudent> advisees,
        // AcademicStaff Attributes
        int teachingHoursPerWeek, int maxTeachingLoad
        ) {
        super(
            // Person Attributes
            firstName, middleName, lastName, dateOfBirth, gender, address, contactNumber, email,
            // Faculty Attributes
            department, rank, hireDate, officeLocation, salary, isTenured, advisees
        );

        this.coursesTaught = new ArrayList<>();
        this.teachingHoursPerWeek = teachingHoursPerWeek;
        this.maxTeachingLoad = maxTeachingLoad;
    }

    // * Getters
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
                Logger.errorMessage("Cannot add course - teaching load would exceed maximum");
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
        report.append(super.generateReport());
        report.append("Teaching Hours Per Week: ").append(teachingHoursPerWeek).append("\n");
        report.append("Max Teaching Load: ").append(maxTeachingLoad).append("\n");
        report.append("Overloaded: ").append(isOverloaded() ? "Yes" : "No").append("\n");
        report.append("Courses Taught: ").append(coursesTaught.size()).append("\n");
        
        if (!coursesTaught.isEmpty()) {
            report.append("\nCourse Details:\n");
            for (CourseOffering course : coursesTaught) {
                report.append(" - ").append(course.toString()).append("\n");
            }
        } return report.toString();
    }

    // [METHOD: Override] Generate academic staff's stringified report
    @Override
    public String generateReport() {
        return generatePerformanceReport();
    }

    // [METHOD] Convert AcademicStaff to CSV row
    public String[] toCSVRow() {
        return new String[] {
            getPersonId(),
            getFirstName(),
            getMiddleName(),
            getLastName(),
            getDateOfBirth().toString(),
            getGender().toString(),
            getAddress(),
            getContactNumber(),
            getEmail(),
            getDepartment() != null ? getDepartment().toString() : "",
            getRank() != null ? getRank().toString() : "",
            getHireDate() != null ? getHireDate().toString() : "",
            getOfficeLocation() != null ? getOfficeLocation() : "",
            String.valueOf(getSalary()),
            String.valueOf(getTeachingHoursPerWeek()),
            String.valueOf(maxTeachingLoad),
            String.valueOf(isTenured())
        };
    }
}