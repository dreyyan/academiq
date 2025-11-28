package ums.model;

import java.time.LocalDate;

import ums.model.entity.Course;
import ums.model.enums.AcademicStanding;
import ums.model.enums.GraduateProgram;

// attributes 
public class MasterStudent extends GraduateStudent {
    private boolean courseworkCompleted; 
    private boolean comprehensiveExamPassed;
    private boolean thesisProposalApproved;

    // comstructor
public MasterStudent(String studentId, LocalDate enrollmentDate, Department department, Course course, AcademicStanding academicStanding, double GPA, int creditsEarned, GraduateProgram programLevel, String thesisTitle, Faculty advisor, boolean courseworkCompleted, boolean comprehensiveExamPassed, boolean thesisProposalApproved){
    super(studentId, enrollmentDate, department, course, academicStanding, GPA, creditsEarned, programLevel, thesisTitle, advisor);
    this.courseworkCompleted = courseworkCompleted;
    this.comprehensiveExamPassed = comprehensiveExamPassed;
    this.thesisProposalApproved = thesisProposalApproved; 
} 

// method
// method for completion of course work 
public void completeCourseWork(){
    if (courseworkCompleted == true){
        System.out.println(getStudentId() + " course work completed.");
    } else {
        System.out.println(getStudentId() + " course work not completed.");
    }
}

// validating if student passed the comprehensive exam
public void passComprehensiveExam(){
    if (comprehensiveExamPassed == true){
        System.out.println(getStudentId() + " passed the comprehensive exam");
    } else {
        System.out.println(getStudentId() + " did not pass the comprehensive exam.");
    }
}

// method for approval of thesis
public void approveThesisProposal(){
    if (thesisProposalApproved){
        System.out.println(getStudentId() + "'s thesis is approved by " + (getAdvisor() != null ? getAdvisor().getFacultyId() : "Not Assigned"));
    } else {
        System.out.println(getStudentId() + "'s thesis is not approved by " + (getAdvisor() != null ? getAdvisor().getFacultyId() : "Not Assigned"));
    }
}


// overriden method for calculating year level
@Override
public String calculateYearLevel(){
    return "Master's Student";
}

// overriden method for eligibilty for graduation 
@Override
public boolean isEligibleForGraduation(){
    if (getCreditsEarned() >= 60){
        return true;
    } else {
        return false; 
    }
}

// methof for display info 
@Override
public void displayInfo(){
    super.displayInfo();
    System.out.println("Course Work: " + courseworkCompleted);
    System.out.println("Comprehensive Exam: " + comprehensiveExamPassed);
    System.out.println("Thesis Status: "  + thesisProposalApproved);
}

// overriden method for report card
@Override
public String generateReport() {
    String report = super.generateReport();
    report += "\nCourse Work Completed: " + courseworkCompleted
           + "\nComprehensive Exam Passed: " + comprehensiveExamPassed
           + "\nThesis Proposal Approved: " + thesisProposalApproved;
    return report;
}
    
}
