package ums.model;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ums.model.entity.Course;
import ums.model.enums.AcademicStanding;
import ums.model.enums.GraduateProgram;

// attributes 
public class PhDStudent extends GraduateStudent {
    private boolean qualifyingExamPassed;
    private boolean dissertationProposalDefended; 
    private List<String> publications;
    private boolean candidacyStatus; 
    private LocalDate defenseDate; 

// constructor
public PhDStudent(String studentId, LocalDate enrollmentDate, Department department, Course course, AcademicStanding academicStanding, double GPA, int creditsEarned, GraduateProgram programLevel, String thesisTitle, Faculty advisor, boolean qualifyingExamPassed, boolean dissertationProposalDefended, boolean candidacyStatus){
    super(studentId, enrollmentDate, department, course, academicStanding, GPA, creditsEarned, programLevel, thesisTitle, advisor);
    this.qualifyingExamPassed = qualifyingExamPassed;
    this.dissertationProposalDefended = dissertationProposalDefended;
    this.candidacyStatus = candidacyStatus;
    this.defenseDate = null; 
    this.publications = new ArrayList<>(); 
}


// method
// method for validating if student passed the qualifying exam
public void passQualifyingExam(){
    if(qualifyingExamPassed == true){
        System.out.println(getStudentId()+ " passed the qualifying exam.");
    } else {
        System.out.println(getStudentId() + " did not pass the qualifying exam.");
    }
}


// method for validating if student is done with dissertation defense
public void defendProposal(){
    if(dissertationProposalDefended == true){
        System.out.println(getStudentId() + " has already defended the dissertation proposal.");
    } else {
        System.out.println(getStudentId() + " has not defended the dissertation proposal.");
    }
}

// method for publication of dissertation 
public void addPublication (String title){
    if (publications == null){
        publications = new ArrayList<>();
    }

    publications.add(title);
    System.out.println(getStudentId() + " added a new publication: " + title);
}

// method for validating if student is eligible for candicacy
public void achieveCandidacy(){
    if (candidacyStatus == true){
        System.out.println(getStudentId() + " has achieved PhD candidacy.");
    } else {
        System.out.println(getStudentId() + " has not achieved PhD candidacy.");
    }
}

// method for defense date
public void scheduleDefense(LocalDate date){
    this.defenseDate = date; 
    System.out.println(getStudentId() + "'s dissertation defense is scheduled on " + date);
}

// overriden method of year level
@Override
public String calculateYearLevel(){
    return "PhD Student"; 
}

// overriden method for eligibilty for graduation 
@Override
public boolean isEligibleForGraduation(){
    if (getCreditsEarned() >= 120){
        return true;
    } else {
        return false; 
    }
}

// method for display info 
@Override
public void displayInfo(){
    super.displayInfo();
    System.out.println("Qualifying Exam: " + qualifyingExamPassed);
    System.out.println("Dissertation Proposal Defense: " + dissertationProposalDefended);
    System.out.println("Dissertation Publication: "  + publications);
    System.out.println("Candidacy Status: " + candidacyStatus);
}

// overriden method for report card
@Override
public String generateReport() {
    String report = super.generateReport();
    report += "\nQualifying Exam Passed: " + qualifyingExamPassed
           + "\nDissertation Proposal Defended: " + dissertationProposalDefended
           + "\nCandidacy Status: " + candidacyStatus
           + "\nDefense Date: " + (defenseDate != null ? defenseDate : "Not Scheduled")
           + "\nPublications: " + (publications != null && !publications.isEmpty() ? publications : "None");
    return report;
}

}
