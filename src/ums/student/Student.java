package ums.student;

import java.time.LocalDate;
import java.util.ArrayList;
import javax.xml.transform.Source;

// private attributes 
class Student {
    private String studentId;
    private LocalDate enrollmentDate; 
    private Department department;
    private Course course;
    private AcademicStanding academicStanding; 
    private double gpa;
    private int creditsEarned; 
    private List<CourseOffering> enrolledCourses; 

// student constructor 
public Student(String studentId, LocalDate enrollmentDate, Department department, Course course, AcademicStanding academicStanding, double gpa, int creditsEarned){
    this.studentId = studentId;
    this.enrollmentDate = enrollmentDate;
    this.department = department; 
    this.course = course;
    this.acaacademicStanding = academicStanding;
    this.gpa = gpa;
    this.creditsEarned = creditsEarned;
    this.enrolledCourses = new ArrayList<>();
}

// getters 
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
    return this.acaacademicStanding;
}

public double getGpa(){
    return this.gpa;
}

public int getCreditsEarned(){
    return this.creditsEarned;
}

public List<CourseOffering> getEnrolledCourses(){
    return this.enrolledCourses; 
}

// setters 
public void setDepartment(Department department){
    this.department = department; 
}

public void setCourse(Course course){
    this.course = course; 
}

public setAcademicStanding(AcademicStanding academicStanding){
    this.acaacademicStanding = academicStanding; 
}

// method update for gpa
public void updateGpa(double newGpa){
    if (newGpa >= 1.0 && newGpa <= 5.0){
        this.gpa = newGpa;
        System.out.println(this.studentId + " GPA is updated to " + newGpa);
    } else {
        System.out.println("Invali GPA of" + this.studentId);
    }
}

// method adding of credits 
public void addCredits(int newCredits){
    if(newCredits > 0){
        this.creditsEarned += newCredits;
        System.out.println(this.studentId + " earned " + newCredits + ". Total credits earned is: " +  this.creditsEarned);
    } else {
        System.out.println("Invalid Credits.");
    }
}

// method for student enrollment offering 
public void enrollInOffering(CourseOffering offering){
    if (this.enrolledCourses.contains(offering)){
        System.out.println(this.studentId + " is already enrolled in " + offering.getCourse());
    } else (this.enrolledCourses.add(offering)){
        System.out.println(this.studentId + " enrolled in " + offering.getCourse());
    }
}

// method for student dropping 
public void dropOffering(CourseOffering offering){
    if(this.enrolledCourses.remove(offering)){
        System.out.println(this.studentId + " dropped " + offering.getCourse());
    } else {
        System.err.println(this.studentId + " is stil enrolled in " + offering.getCourse());
    }
}

// method for calculating student's year level
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

// method for eligible for graduation 
public boolean isEligibleForGradution(){
    return this.creditsEarned >= 120 && this.gpa <= 3.0; 
}

// method for displaying of info
public void displayInfo(){
    System.out.println("Student ID: " + this.studentId);
    System.out.println("Student's Department: " + this.department);
    System.out.println("Student's Course: " + this.course);
    System.out.println("GPA: " + this.gpa);
    System.out.println("Total Credits: " + this.creditsEarned);
}

// method for report card 
public String generateReport(){
    return "Student's ID: " + studentId + "\nGPA: " + gpa + "\nCredits: " + creditsEarned;
}

}



