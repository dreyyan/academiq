package ums.model;

import java.time.LocalDate;

import ums.model.entity.Course;
import ums.model.enums.AcademicStanding;

// attributes
public class Senior extends UndergraduateStudent {
    private int internshipHours;
    private String capstoneProject;
    private int jobOffers;

// constructor
public Senior(String studentId, LocalDate enrollmentDate, Department department, Course course, AcademicStanding academicStanding, double GPA, int creditsEarned, String yearLevel, int internshipHours, String capstoneProject, int jobOffers){
    super(studentId, enrollmentDate, department, course, academicStanding, GPA, creditsEarned, yearLevel);  
    this.internshipHours = internshipHours;
    this.capstoneProject = capstoneProject;
    this.jobOffers = jobOffers; 
}

// getters
public int getInternshipHours(){
    return internshipHours;
}

public String getCapstoneProject(){
    return capstoneProject;
}

public int getJobOffers(){
    return jobOffers;
}

// setters
public void setInternshipHours(int intershipHours){
    this.internshipHours = internshipHours;
}

public void setCapstoneProject(String capstoneProject){
    this.capstoneProject = capstoneProject; 
}

public void setJobOffers(int jobOffers){
    this.jobOffers = jobOffers;
}

// method
// validating if student submitted capstone 
public void submitCapstone(String project){
    this.capstoneProject = project; 
    System.out.println(getStudentId() + " has submitted the Capstone Project: " + project);
}

// method for job offers
public void addJobOffer(){
    jobOffers++;
    System.out.println(getStudentId() + " received a new job offer. Total job offers: "+ jobOffers);
}

// overriden method of year level
@Override
public String calculateYearLevel(){
    return "Senior";
}

// overriden method of eligibility for graduation
@Override
public boolean isEligibleForGraduation(){
    return getCreditsEarned() >= 120 && getGPA() >= 3.0 && capstoneProject != null && !capstoneProject.isBlank();  
}

// overriden method of displaying of info
@Override
public void displayInfo(){
    super.displayInfo();
    System.out.println("Year Level: Senior");
    System.out.println("Internship Hours: " + internshipHours);
    System.out.println("Capstone Project: " + (capstoneProject != null ? capstoneProject : "Not submitted"));
    System.out.println("Job Offers: " + jobOffers);
}

// overriden method of generate report
@Override
public String generateReport(){
    return super.generateReport() +
    "\nYear Level: Senior" +
    "\nInternship Hours: " + internshipHours +
    "\nCapstone Project: " + (capstoneProject != null && !capstoneProject.isBlank() ? capstoneProject : "Not submitted") +
    "\nJob Offers: " + jobOffers;
}

}
