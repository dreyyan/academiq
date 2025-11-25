package ums.model;

// [IMPORT] Standard
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;

// [IMPORT] Models
import ums.model.GraduateStudent;

// [IMPORT] Entities
import ums.model.entity.CourseOffering;

// [IMPORT] Enums
import ums.model.enums.Department;
import ums.model.enums.FacultyRank;
import ums.model.enums.Gender;

// [IMPORT] Utilities
import ums.util.console.ConsoleDisplay;

public class AcademicStaff extends Faculty {
    // * Attributes
    private List<CourseOffering> courseOfferingsTaught;
    private int teachingHoursPerWeek;
    private int maxTeachingLoad = 18;

    // * Constructor (Parameterized)
    public AcademicStaff(
        String personId, String firstName, String middleName, String lastName,
        LocalDate dateOfBirth, Gender gender,
        String address, String contactNumber, String email,
        Department department, FacultyRank rank, LocalDate hireDate,
        String officeLocation, double salary, boolean isTenured,
        List<GraduateStudent> advisees,
        int teachingHoursPerWeek, int maxTeachingLoad
    ) {
        super(
            personId,
            firstName, middleName, lastName, dateOfBirth, gender,
            address, contactNumber, email,
            department, rank, hireDate, officeLocation,
            salary, isTenured, advisees
        );

        this.courseOfferingsTaught = new ArrayList<>();
        this.teachingHoursPerWeek = teachingHoursPerWeek;
        this.maxTeachingLoad = maxTeachingLoad;
    }

    // * Getters
    public List<CourseOffering> getCourseOfferingsTaught() { return courseOfferingsTaught; }
    public int getTeachingHoursPerWeek() { return this.teachingHoursPerWeek; }
    public int getMaxTeachingLoad() { return this.maxTeachingLoad; }

    // * Setters
    public void setMaxTeachingLoad(int maxLoad) { this.maxTeachingLoad = maxLoad; }

    // * Methods
    // [METHOD] Add a course offering to the teaching list
    public void addCourseOffering(CourseOffering offering) {
        if (offering != null && !courseOfferingsTaught.contains(offering)) {
            if (!isOverloaded()) {
                courseOfferingsTaught.add(offering);
                updateTeachingHours(offering.getCourse().getCredits());
            } else {
                ConsoleDisplay.dialogBox("error", "Cannot add course - teaching load would exceed maximum");
            }
        }
    }

    // [METHOD] Remove a course offering from the teaching list
    public void removeCourseOffering(CourseOffering offering) {
        if (courseOfferingsTaught.remove(offering)) {
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
        report.append("Courses Taught: ").append(courseOfferingsTaught.size()).append("\n");
        
        if (!courseOfferingsTaught.isEmpty()) {
            report.append("\nCourse Details:\n");
            for (CourseOffering course : courseOfferingsTaught) {
                report.append(" - ").append(course.toString()).append("\n");
            }
        } return report.toString();
    }

    // [METHOD] Get AcademicStaff academics information
    public List<String> getAcademicsInformation() {
        List<String> info = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        info.add(getPersonId()); // Person ID
        info.add(getDepartment() != null ? getDepartment().toString() : "Not Assigned"); // Department
        info.add(getRank() != null ? getRank().toString() : "Not Assigned"); // Rank
        info.add(getHireDate() != null ? getHireDate().format(formatter) : "Not Assigned"); // Hire Date
        info.add(getOfficeLocation() != null ? getOfficeLocation() : "Not Assigned"); // Office Location
        info.add(String.valueOf(getSalary())); // Salary
        info.add(String.valueOf(getTeachingHoursPerWeek())); // Teaching Hours/Week
        info.add(String.valueOf(maxTeachingLoad)); // Max Teaching Load
        info.add(isOverloaded() ? "Yes" : "No"); // Overloaded
        info.add(isTenured() ? "Yes" : "No"); // Tenured

        // Courses Taught
        if (courseOfferingsTaught.isEmpty()) {
            info.add("No courses assigned");
        } else {
            StringBuilder courses = new StringBuilder();
            for (CourseOffering c : courseOfferingsTaught) {
                courses.append(c.getCourse().getTitle()).append(", ");
            }
            // Remove trailing comma
            if (courses.length() >= 2) courses.setLength(courses.length() - 2);
            info.add(courses.toString());
        }

        // Advisees
        if (getAdvisees().isEmpty()) {
            info.add("No advisees assigned");
        } else {
            StringBuilder advisees = new StringBuilder();
            for (GraduateStudent s : getAdvisees()) {
                advisees.append(s.getFirstName()).append(" ").append(s.getLastName()).append(", ");
            }
            // Remove trailing comma
            if (advisees.length() >= 2) advisees.setLength(advisees.length() - 2);
            info.add(advisees.toString());
        }

        return info;
    }

    // [METHOD: Override] Generate academic staff's stringified report
    @Override
    public String generateReport() {
        return generatePerformanceReport();
    }

    // [METHOD] Convert AcademicStaff to CSV row
    public String[] toCSVRow() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        return new String[] {
            getPersonId(),
            getFirstName(),
            getMiddleName(),
            getLastName(),
            getDateOfBirth() != null ? getDateOfBirth().format(formatter) : "",
            getGender() != null ? getGender().toString() : "",
            getAddress() != null ? getAddress() : "",
            getContactNumber() != null ? getContactNumber() : "",
            getEmail() != null ? getEmail() : "",
            getDepartment() != null ? getDepartment().toString() : "",
            getRank() != null ? getRank().toString() : "",
            getHireDate() != null ? getHireDate().format(formatter) : "",
            getOfficeLocation() != null ? getOfficeLocation() : "",
            String.valueOf(getSalary()),
            String.valueOf(getTeachingHoursPerWeek()),
            String.valueOf(maxTeachingLoad),
            String.valueOf(isTenured())
        };
    }
}