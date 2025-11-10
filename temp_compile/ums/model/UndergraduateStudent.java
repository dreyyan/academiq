package ums.model;

// [IMPORT] Standard
import java.time.LocalDate;
import java.util.List;

// [IMPORT] Entities
import ums.model.entity.Course;
import ums.model.entity.CourseOffering;
// [IMPORT] Enums
import ums.model.enums.AcademicStanding;
import ums.model.enums.Department;
import ums.model.enums.Gender;
import ums.model.enums.YearLevel;

public class UndergraduateStudent extends Student {
    // * Attribute
    private YearLevel yearLevel;
    
    // * Constructor (Parameterized)
    public UndergraduateStudent(
        // Person Attributes
        String personId, String firstName, String middleName, String lastName, LocalDate dateOfBirth, Gender gender, String address, String contactNumber, String email,
        // Student Attributes
        String studentId, LocalDate enrollmentDate, Department department, Course course, AcademicStanding academicStanding, double GPA, int creditsEarned, List<CourseOffering> enrolledCourses,
        // UndergraduateStudent attributes
        YearLevel yearLevel
        ) {
        super(
            // Person Attributes
            personId, firstName, middleName, lastName, dateOfBirth, gender, address, contactNumber, email,
            // Student Attributes
            studentId, enrollmentDate, department, course, academicStanding, GPA, creditsEarned, enrolledCourses
        );

        this.yearLevel = yearLevel; 
    }

    // * Getter
    public YearLevel getYearLevel() { return this.yearLevel; }

    // * Setter
    public void setYearLevel(YearLevel yearLevel) { this.yearLevel = yearLevel; }

    // * Methods
    // [METHOD: Override] Calculate undergraduate student's current year level
    @Override
    public String calculateYearLevel() {
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

        // Update year level
        setYearLevel(YearLevel.valueOf(level.toUpperCase()));
        return level;
    }

    // [METHOD: Override] Display student's information
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Year Level: " + getYearLevel());
    }

    // [METHOD: Override] Generate student's stringified report
    @Override
    public String generateReport() {
        String report = super.generateReport();

        report += "Year Level: " + getYearLevel() + '\n';
        return report; 
    }

}