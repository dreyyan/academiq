package ums.model;

import java.time.LocalDate;
import ums.model.entity.Course;
import ums.model.enums.AcademicStanding;
import ums.model.enums.Department;
import ums.model.enums.YearLevel;

// private attributes
public class UndergraduateStudent extends Student {
    private YearLevel yearLevel;
    
// consturctor
public UndergraduateStudent(String studentId, LocalDate enrollmentDate, Department department, Course course, AcademicStanding academicStanding, double GPA, int creditsEarned, String yearLevel){
    super(studentId, enrollmentDate, department, course, academicStanding, GPA, creditsEarned);
    this.yearLevel = yearLevel; 
}

// getter
public YearLevel getYearLevel(){
    return this.yearLevel; 
}

// setter
public void setYearLevel(YearLevel yearLevel){
    this.yearLevel = yearLevel; 
}

// methods
// overriden method of calculateYearLevel from Student class
@Override
public String calculateYearLevel(){
    String level;
    if (getCreditsEarned() < 30){
        level = "Freshman";
    } else if (getCreditsEarned() < 60){
        level = "Sophomore";
    } else if (getCreditsEarned() < 90){
        level = "Junior";
    } else {
        level = "Senior";
    }

    this.yearLevel = YearLevel.valueOf(level.toUpperCase());
    return level; 
}

// overriden method of calculateYearLevel from Student class
@Override
public boolean isEligibleForGraduation(){
   return super.isEligibleForGraduation();
}

// overriden method of displayInfo from Student class
@Override
public void displayInfo(){
    super.displayInfo();
    System.out.println("Year Level: " + getYearLevel());
}

// overriden method of generateReport from Student class
@Override
public String generateReport(){
String report = super.generateReport();
report +="\nYear Level: " + getYearLevel(); 
return report; 
}

}

