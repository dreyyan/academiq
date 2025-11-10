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

public class Sophomore extends UndergraduateStudent {
    // * Constructor (Parameterized)
    public Sophomore(
        // Person Attributes
        String personId, String firstName, String middleName, String lastName, LocalDate dateOfBirth, Gender gender, String address, String contactNumber, String email,
        // Student Attributes
        String studentId, LocalDate enrollmentDate, Department department, Course course, AcademicStanding academicStanding, double GPA, int creditsEarned, List<CourseOffering> enrolledCourses,
        // UndergraduateStudent Attributes
        YearLevel yearLevel
    ) {
        super(
            // Person Attributes
            personId, firstName, middleName, lastName, dateOfBirth, gender, address, contactNumber, email,
            // Student Attributes
            studentId, enrollmentDate, department, course, academicStanding, GPA, creditsEarned, enrolledCourses,
            // UndergraduateStudent Attributes
            yearLevel
        );
    }

    // * Methods
    // [METHOD: Override] Calculate Sophomore's current year level
    @Override
    public String calculateYearLevel() {
        this.setYearLevel(YearLevel.SOPHOMORE);
        return "Sophomore";
    }

    // [METHOD: Override] Sophomore is never eligible for graduation
    @Override
    public boolean isEligibleForGraduation() {
        return false; 
    }

    // [METHOD: Override] Display sophomore's information
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Year Level: Sophomore\n");
    }

    // [METHOD: Override] Generate sophomore's stringified report
    @Override
    public String generateReport() {
        return super.generateReport() +
        "Year Level: Sophomore\n";
    }
}