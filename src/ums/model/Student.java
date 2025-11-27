package ums.model;

// [IMPORT] Standard
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// [IMPORT] Entities
import ums.model.entity.Course;
import ums.model.entity.CourseOffering;

// [IMPORT] Enums
import ums.model.enums.AcademicStanding;
import ums.model.enums.Department;
import ums.model.enums.Gender;

// [IMPORT] Utilities
import ums.util.Logger;
import ums.util.console.ConsoleDisplay;

public abstract class Student extends Person {
    // * Attributes
    private LocalDate enrollmentDate; 
    private Department department;
    private Course course;
    private AcademicStanding academicStanding;
    private double GPA;
    private int creditsEarned; 
    private List<CourseOffering> enrolledCourseOfferings;

    // * Constructor (Parameterized)
        public Student(
            // Person Attributes
            String firstName, String middleName, String lastName, LocalDate dateOfBirth, Gender gender, String address, String contactNumber, String email,
            // Student Attributes
            LocalDate enrollmentDate, Department department, Course course, AcademicStanding academicStanding) {
            super(
                // Person Attributes
                firstName, middleName, lastName, dateOfBirth, gender, address, contactNumber, email
            );

            this.enrollmentDate = enrollmentDate;
            this.department = department; 
            this.course = course;
            this.academicStanding = academicStanding;
            this.GPA = 0.0;
            this.creditsEarned = 0;
            this.enrolledCourseOfferings = new ArrayList<>();
        }

        // * Constructor (Parameterized - WITH PersonID)
    public Student(
        // Person ID
        String personId,
        // Person Attributes
        String firstName, String middleName, String lastName, LocalDate dateOfBirth, Gender gender, String address, String contactNumber, String email,
        // Student Attributes
        LocalDate enrollmentDate, Department department, Course course, AcademicStanding academicStanding) {
        super(
            // Person Attributes WITH ID
            personId, firstName, middleName, lastName, dateOfBirth, gender, address, contactNumber, email
        );

        this.enrollmentDate = enrollmentDate;
        this.department = department; 
        this.course = course;
        this.academicStanding = academicStanding;
        this.GPA = 0.0;
        this.creditsEarned = 0;
        this.enrolledCourseOfferings = new ArrayList<>();
    }

    // * Getters
    public LocalDate getEnrollmentDate() { return this.enrollmentDate; }
    public Department getDepartment() { return this.department; }
    public Course getCourse() { return this.course; }
    public AcademicStanding getAcademicStanding() { return this.academicStanding; }
    public double getGPA() { return this.GPA; }
    public int getCreditsEarned() { return this.creditsEarned; }
    public List<CourseOffering> getEnrolledCourseOfferings() { return this.enrolledCourseOfferings; }

    // * Setters
    public void setDepartment(Department department) { this.department = department; }
    public void setCourse(Course course){ this.course = course; }
    public void setAcademicStanding(AcademicStanding academicStanding) { this.academicStanding = academicStanding; }
    public void setGPA(double GPA) {this.GPA = GPA;}
    public void setCreditsEarned(int credits) { this.creditsEarned = credits;}
    
    // * Methods
    // [METHOD] Enroll student in an existing course offering
    public void enrollInCourseOffering(CourseOffering offering){
        if (getEnrolledCourseOfferings().contains(offering)){ // ! [ERROR] Student already enrolled in course offering
            ConsoleDisplay.dialogBox("error", getPersonId() + " is already enrolled in " + offering.getCourse());
        } else {
            getEnrolledCourseOfferings().add(offering); // Enroll in course offering
            ConsoleDisplay.dialogBox("success", "Successfully enrolled in " + offering.getCourse() + "!");
        }
    }

    // [METHOD] Drop student's existing course offering
    public void dropFromCourseOffering(CourseOffering offering) {
        if(getEnrolledCourseOfferings().remove(offering)){
            ConsoleDisplay.dialogBox("success", "Successfully dropped in " + offering.getCourse() + "!");
        } else {
            ConsoleDisplay.dialogBox("error", getPersonId() + " is stil enrolled in " + offering.getCourse());
        }
    }

    // [METHOD] Calculate student's current year level
    public String calculateYearLevel() {
        if (getCreditsEarned() < 30){
            return "Freshman";
        } else if (getCreditsEarned() < 60){
            return "Sophomore";
        } else if (getCreditsEarned() < 90){
            return "Junior";
        } else {
            return "Senior";
        }
    }

    // [METHOD] Check student's eligibility for graduation
    public boolean isEligibleForGraduation() {
        boolean gpaRequirement = getGPA() <= 2.5;
        boolean seniorRequirement = getCreditsEarned() >= 90;
        boolean totalCreditsRequired = getCreditsEarned() >= 120;

        boolean isEligible = gpaRequirement && seniorRequirement && totalCreditsRequired; 

        // Display eligibility for graduation
        if (isEligible) {
            ConsoleDisplay.dialogBox("info", getPersonId() + " is eligible for Undergraduate Graduation.");
        } else {
            ConsoleDisplay.dialogBox("info", getPersonId() + " is not eligible for Undergraduate Graduation.");
        }

        return isEligible; 
    }

    // [METHOD] Display student's academics information
    public List<String> getAcademicsInformation() {
        List<String> info = new ArrayList<>(); // Stores student's academics information

        info.add(getEnrollmentDate().toString());
        info.add(getDepartment() != null ? getDepartment().toString() : "Not Assigned");
        info.add(getCourse() != null ? getCourse().toString() : "Not Assigned");
        info.add(String.valueOf(getCreditsEarned()));
        info.add(String.valueOf(getGPA()));
        info.add(getAcademicStanding().toString());

        return info;
    }

    // [METHOD] Generate student's stringified report
    public String generateReport() {
        StringBuilder report = new StringBuilder();

        report.append("======= Student Report =======\n");
        report.append("ID: ").append(getPersonId()).append("\n");
        report.append("Department: ").append(getDepartment() != null ? getDepartment() : "Not Assigned").append("\n");
        report.append("Course: ").append(getCourse() != null ? getCourse() : "Not Assigned").append("\n");
        report.append("Year Level: ").append(calculateYearLevel()).append("\n");
        report.append("GPA: ").append(getGPA()).append("\n");
        report.append("Credits: ").append(getCreditsEarned()).append("\n");
        report.append("Academic Standing: ").append(getAcademicStanding()).append("\n");
        report.append("Eligible for Graduation: ").append(isEligibleForGraduation() ? "Yes" : "No").append("\n");

        return report.toString();
    }

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
            getEnrollmentDate().toString(),
            getDepartment() != null ? getDepartment().toString() : "",
            getCourse() != null ? getCourse().toString() : "",
            getAcademicStanding().toString(),  
            String.valueOf(getGPA()),   
            String.valueOf(getCreditsEarned())
        };
    }
}