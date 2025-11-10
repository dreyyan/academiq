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

public abstract class Student extends Person {
    // * Attributes
    private String studentId;
    private LocalDate enrollmentDate; 
    private Department department;
    private Course course;
    private AcademicStanding academicStanding; 
    private double GPA;
    private int creditsEarned; 
    private List<CourseOffering> enrolledCourses; 

    // * Constructor (Parameterized)
    public Student(
        // Person Attributes
        String personId, String firstName, String middleName, String lastName, LocalDate dateOfBirth, Gender gender, String address, String contactNumber, String email,
        // Student Attributes
        String studentId, LocalDate enrollmentDate, Department department, Course course, AcademicStanding academicStanding, double GPA, int creditsEarned,  List<CourseOffering> enrolledCourses) {
        super(
            // Person Attributes
            personId, firstName, middleName, lastName, dateOfBirth, gender, address, contactNumber, email
        );

        this.studentId = studentId;
        this.enrollmentDate = enrollmentDate;
        this.department = department; 
        this.course = course;
        this.academicStanding = academicStanding;
        this.GPA = GPA;
        this.creditsEarned = creditsEarned;
        this.enrolledCourses = new ArrayList<>();
    }

    // * Getters
    public String getStudentId() { return this.studentId; }
    public LocalDate getEnLocalDate() { return this.enrollmentDate; }
    public Department getDepartment() { return this.department; }
    public Course getCourse() { return this.course; }
    public AcademicStanding getAcademicStanding() { return this.academicStanding; }
    public double getGPA() { return this.GPA; }
    public int getCreditsEarned() { return this.creditsEarned; }
    public List<CourseOffering> getEnrolledCourses() { return this.enrolledCourses; }

    // * Setters
    public void setDepartment(Department department) { this.department = department; }
    public void setCourse(Course course){ this.course = course; }
    public void setAcademicStanding(AcademicStanding academicStanding) { this.academicStanding = academicStanding; }

    // * Methods
    // [METHOD] Update student GPA
    public void updateGPA(double newGPA){
        if (newGPA >= 1.0 && newGPA <= 5.0){
            this.GPA = newGPA;
            Logger.infoMessage(getStudentId() + " GPA is updated to " + newGPA);
        } else {
            Logger.errorMessage("Invali GPA of" + getStudentId());
        }
    }

    // [METHOD] Add a specified amount to student credits
    public void addCredits(int credits) {
        if(credits > 0) {
            this.creditsEarned += credits;
            Logger.successMessage(getStudentId() + " earned " + credits + " credits");
            Logger.infoMessage("Total Credits: " +  getCreditsEarned());
        } else { // ! [ERROR] Zero or negative credits to be added
            Logger.errorMessage("Invalid amount of credits");
        }
    }

    // [METHOD] Enroll student in an existing course offering
    public void enrollInOffering(CourseOffering offering){
        if (getEnrolledCourses().contains(offering)){ // ! [ERROR] Student already enrolled in course offering
            Logger.errorMessage(getStudentId() + " is already enrolled in " + offering.getCourse());
        } else {
            getEnrolledCourses().add(offering); // Enroll in course offering
            Logger.successMessage(getStudentId() + " enrolled in " + offering.getCourse());
        }
    }

    // [METHOD] Drop student's existing course offering
    public void dropOffering(CourseOffering offering) {
        if(getEnrolledCourses().remove(offering)){
            Logger.successMessage(getStudentId() + " dropped " + offering.getCourse());
        } else {
            Logger.errorMessage(getStudentId() + " is stil enrolled in " + offering.getCourse());
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
            Logger.infoMessage(getStudentId() + " is eligible for Undergraduate Graduation.");
         } else {
            Logger.infoMessage(getStudentId() + " is not eligible for Undergraduate Graduation.");
    }
        return isEligible; 
    }

    // [METHOD] Display student's information
    public void displayInfo() {
        System.out.println("Student ID: " + getStudentId());
        System.out.println("Student's Department: " + getDepartment());
        System.out.println("Student's Course: " + getCourse());
        System.out.println("Student's Year Level: " + calculateYearLevel());
        System.out.println("GPA: " + getGPA());
        System.out.println("Total Credits: " + getCreditsEarned());
        System.out.println("Student's Academic Standing: " + getAcademicStanding());
        System.out.println("Eligible for Graduation: " + (isEligibleForGraduation() ? "Yes" : "No"));
}

    // [METHOD] Generate student's stringified report
    public String generateReport() {
        StringBuilder report = new StringBuilder();

        report.append("======= Student Report =======\n");
        report.append("ID: ").append(getStudentId()).append("\n");
        report.append("Department: ").append(getDepartment()).append("\n");
        report.append("Course: ").append(getCourse()).append("\n");
        report.append("Year Level: ").append(calculateYearLevel()).append("\n");
        report.append("GPA: ").append(getGPA()).append("\n");
        report.append("Credits: ").append(getCreditsEarned()).append("\n");
        report.append("Academic Standing: ").append(getAcademicStanding()).append("\n");
        report.append("Eligible for Graduation: ").append(isEligibleForGraduation() ? "Yes" : "No").append("\n");

        return report.toString();
    }
}