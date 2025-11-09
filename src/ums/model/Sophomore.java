package ums.model;

import java.time.LocalDate;

import ums.model.entity.Course;
import ums.model.enums.AcademicStanding;

public class Sophomore extends UndergraduateStudent {

// constructor 
public Sophomore(String studentId, LocalDate enrollmentDate, Department department, Course course, AcademicStanding academicStanding, double GPA, int creditsEarned, String yearLevel){
    super(studentId, enrollmentDate, department, course, academicStanding, GPA, creditsEarned, yearLevel);  
}

// overriden method of year level
@Override
public String calculateYearLevel(){
    return "Sophomore";
}

// overriden method of eligibility for graduation
@Override
public boolean isEligibleForGraduation(){
    return false; 
}

// overriden method of displaying of info
@Override
public void displayInfo(){
    super.displayInfo();
    System.out.println("Year Level: Sophomore");
}

// overriden method of generate report
@Override
public String generateReport(){
    return super.generateReport() +
    "\nYear Level: Sophomore" ;
}

}

