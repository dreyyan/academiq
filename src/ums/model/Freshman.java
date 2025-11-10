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

public class Freshman extends UndergraduateStudent {
    // * Attribute
    private boolean orientationCompleted; 

    // * Constructor (Parameterized)
    public Freshman(
        // Person Attributes
        String personId, String firstName, String middleName, String lastName, LocalDate dateOfBirth, Gender gender, String address, String contactNumber, String email,
        // Student Attributes
        String studentId, LocalDate enrollmentDate, Department department, Course course, AcademicStanding academicStanding, double GPA, int creditsEarned, List<CourseOffering> enrolledCourses,
        // UndergraduateStudent Attributes
        YearLevel yearLevel,
        // Freshman Attributes
        boolean orientationCompleted
        ) {
        super(
            // Person Attributes
            personId, firstName, middleName, lastName, dateOfBirth, gender, address, contactNumber, email,
            // Student Attributes
            studentId, enrollmentDate, department, course, academicStanding, GPA, creditsEarned, enrolledCourses,
            // UndergraduateStudent Attributes
            yearLevel
        );

        this.orientationCompleted = orientationCompleted; 
    }

    // * Methods
    // [METHOD] Mark orientation as complete
    public void completeOrientation() { orientationCompleted = true; }

    // [METHOD] Returns 'true' if freshman has completed orientation; prints status message
    public boolean isOrientationComplete() {
        if (orientationCompleted) {
            System.out.println(getStudentId() + " has completed the freshman orientation.");
        } else {
            System.out.println(getStudentId() + " has not yet completed the freshman orientation.");
        } return orientationCompleted;
    }

    // [METHOD: Override] Calculate Freshman's current year level
    @Override
    public String calculateYearLevel() {
        this.setYearLevel(YearLevel.FRESHMAN);
        return "Freshman";
    }

    // [METHOD: Override] Freshman is never eligible for graduation
    @Override
    public boolean isEligibleForGraduation() {
        return false; 
    }

    // [METHOD: Override] Display freshman's information
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Year Level: Freshman\n");
        System.out.println("Orientation Completed: " + (orientationCompleted ? "Yes" : "No") + '\n');
    }

    // [METHOD: Override] Generate freshman's stringified report
    @Override
    public String generateReport(){
        return super.generateReport() +
        "Year Level: Freshman\n" +
        "Orientation Completed: " + (orientationCompleted ? "Yes" : "No") + '\n';
    }
}
