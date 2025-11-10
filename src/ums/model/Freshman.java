package ums.model;

import java.time.LocalDate;

import ums.model.entity.Course;
import ums.model.enums.AcademicStanding;

// attributes
public class Freshman extends UndergraduateStudent {
    private boolean orientationCompleted; 

// constructor
    public Freshman(String studentId, LocalDate enrollmentDate, Department department, Course course, AcademicStanding academicStanding, double GPA, int creditsEarned, String yearLevel, boolean orientationCompleted){
        super(studentId, enrollmentDate, department, course, academicStanding, GPA, creditsEarned, yearLevel);
        this.orientationCompleted = orientationCompleted; 
    }

// method
// marks orientation as complete
public void completeOrientation(){
    orientationCompleted = true; 
}

// valiating if freshman is done with orientation 
public boolean isOrientationComplete(){
    if (orientationCompleted){
        System.out.println(getStudentId() + " is already done with freshman orientation.");
    } else {
        System.out.println(getStudentId() + " did not attend freshman orientation.");
    }

    return orientationCompleted; 
}

// overriden method of year level
@Override
public String calculateYearLevel(){
    return "Freshman"; 
}

// overriden method of eligibilty for graduation 
@Override
public boolean isEligibleForGraduation(){
    return false; 
}

// overriden method of display info 
@Override
public void displayInfo(){
    super.displayInfo();
    System.out.println("Year Level: Freshman");
    System.out.println("Orientation Completed: " + (orientationCompleted ? "Yes" : "No"));
}

// overriden method of generate report 
@Override
public String generateReport(){
    return super.generateReport() +
    "\nYear Level: Freshman" +
    "\nOrientation Completed: " + (orientationCompleted ? "Yes" : "No");

}

}
