package ums.model;

import java.time.LocalDate;

import ums.model.entity.Course;
import ums.model.enums.AcademicStanding;

// attributes

import ums.model.enums.GraduateProgram;

public class GraduateStudent extends Student {
    private GraduateProgram programLevel; 
    private String thesisTitle;
    private Faculty advisor;

    // constructor 
    public GraduateStudent(String studentId, LocalDate enrollmentDate, Department department, Course course, AcademicStanding academicStanding, double GPA, int creditsEarned, GraduateProgram programLevel, String thesisTitle, Faculty advisor){
        super(studentId, enrollmentDate, department, course, academicStanding, GPA, creditsEarned);
        this.programLevel = programLevel;
        this.thesisTitle = thesisTitle;
        this.advisor = advisor;
    }

    // getters
    public GraduateProgram getProgramLevel(){
        return this.programLevel;
    }

    public String getThesisTitle(){
        return this.thesisTitle;
    }

    public Faculty getAdvisor(){
        return this.advisor;
    }

    // setters
    public void setThesisTitle(String thesisTitle){
        this.thesisTitle = thesisTitle;
    }

    public void setAdvisor(Faculty advisor){
        this.advisor = advisor;
    }

    // method
    // ovveriden method of year level
    @Override
    public String calculateYearLevel(){
        if (getCreditsEarned() < 30){
            return "First Year Graduate School Student.";
        } else if (getCreditsEarned() < 60){
            return "Second Year Graduate School Student.";
        } else {
            return "Eligible for Graduation.";
        }
    }

    // overriden method of eligibility for graduation
    @Override
    public boolean isEligibleForGraduation(){
        return getCreditsEarned() >= 60 ;
    }

    // overriden method of display info 
    @Override 
    public void displayInfo(){
        super.displayInfo();
        System.out.println("Program Level: " + programLevel);
        System.out.println("Thesis Title: " + (thesisTitle !=null ? thesisTitle : "Not Set"));
        System.out.println("Advisor: " + (advisor != null ? advisor.getFacultyId() : "Not Assigned"));
        System.out.println("Year Level: " + calculateYearLevel());
        System.out.println("Eligible for Graduation: " + (isEligibleForGraduation() ? "Yes" : "No"));
    }

    // overriden method of report card
    @Override
    public String generateReport(){
        String report = super.generateReport() + "\n";
        report += "\nProgram Level: " + programLevel + 
        "\nThesis Title: " + (thesisTitle != null ? thesisTitle : "Not Set") + 
        "\nAdvisor: " + (advisor != null ? advisor.getFacultyId() : "Not Assigned")   +
        "\nYear Level: " + calculateYearLevel() +
        "\nEligible for Graduation: " + (isEligibleForGraduation() ? "Yes" : "No");

        return report; 
    }
    
    }

