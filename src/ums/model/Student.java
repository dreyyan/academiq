package ums.model;

// [IMPORT] Standard
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// [IMPORT] Enums
import ums.model.enums.Department;
import ums.model.enums.AcademicStanding;

// [IMPORT] Entities
import ums.model.entity.Course;
import ums.model.entity.CourseOffering;

public abstract class Student {
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
    public Student(String studentId, LocalDate enrollmentDate, Department department, Course course, AcademicStanding academicStanding, double GPA, int creditsEarned){
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
    public String getStudentId(){
        return this.studentId; 
    }

    public LocalDate getEnLocalDate(){
        return this.enrollmentDate;
    }

    public Department getDepartment(){
        return this.department;
    }

    public Course getCourse(){
        return this.course; 
    }

    public AcademicStanding getAcademicStanding(){
        return this.academicStanding;
    }

    public double getGPA(){
        return this.GPA;
    }

    public int getCreditsEarned(){
        return this.creditsEarned;
    }

    public List<CourseOffering> getEnrolledCourses(){
        return this.enrolledCourses; 
    }

    // * Setters
    public void setDepartment(Department department){
        this.department = department; 
    }

    public void setCourse(Course course){
        this.course = course; 
    }

    public void setAcademicStanding(AcademicStanding academicStanding){
        this.academicStanding = academicStanding; 
    }

    // * Methods
    // [METHOD] Update student GPA
    public void updateGPA(double newGPA){
        if (newGPA >= 1.0 && newGPA <= 5.0){
            this.GPA = newGPA;
            System.out.println(this.studentId + " GPA is updated to " + newGPA);
        } else {
            System.out.println("Invali GPA of" + this.studentId);
        }
    }

    // [METHOD] Add student credits
    public void addCredits(int newCredits){
        if(newCredits > 0){
            this.creditsEarned += newCredits;
            System.out.println(this.studentId + " earned " + newCredits + ". Total credits earned is: " +  this.creditsEarned);
        } else {
            System.out.println("Invalid Credits.");
        }
    }

    // [METHOD] Enroll student in an existing course offering
    public void enrollInOffering(CourseOffering offering){
        if (getEnrolledCourses().contains(offering)){
            System.out.println(this.studentId + " is already enrolled in " + offering.getCourse());
        } else {
            getEnrolledCourses().add(offering);
            System.out.println(this.studentId + " enrolled in " + offering.getCourse());
        }
    }

    // [METHOD] Drop student's existing course offering
    public void dropOffering(CourseOffering offering){
        if(getEnrolledCourses().remove(offering)){
            System.out.println(this.studentId + " dropped " + offering.getCourse());
        } else {
            System.err.println(this.studentId + " is stil enrolled in " + offering.getCourse());
        }
    }

    // [METHOD] Calculate student's current year level
    public String calculateYearLevel(){
        if (this.creditsEarned < 30){
            return "Freshman";
        } else if (this.creditsEarned < 60){
            return "Sophomore";
        } else if (this.creditsEarned < 90){
            return "Junior";
        } else {
            return "Senior";
        }
    }

    // [METHOD] Check student's eligibility for graduation
    public boolean isEligibleForGradution(){
        return getCreditsEarned() >= 120 && getGPA() <= 3.0; 
    }

    // [METHOD] Display student's information
    public void displayInfo(){
        System.out.println("Student ID: " + getStudentId());
        System.out.println("Student's Department: " + getDepartment());
        System.out.println("Student's Course: " + getCourse());
        System.out.println("GPA: " + getGPA());
        System.out.println("Total Credits: " + getCreditsEarned());
    }

    // [METHOD] Generate student's stringified report
    public String generateReport(){
        return "Student's ID: " + getStudentId() + "\nGPA: " + getGPA() + "\nCredits: " + getCreditsEarned();
    }
}



