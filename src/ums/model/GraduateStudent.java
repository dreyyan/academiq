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
import ums.model.enums.GraduateProgram;

public abstract class GraduateStudent extends Student {
    // * Attributes
    private GraduateProgram programLevel; 
    private String thesisTitle;
    private Faculty advisor;

    // * Constructor (Parameterized)
    public GraduateStudent(
        // Person Attributes
        String personId, String firstName, String middleName, String lastName, LocalDate dateOfBirth, Gender gender, String address, String contactNumber, String email,
        // Student Attributes
        String studentId, LocalDate enrollmentDate, Department department, Course course, AcademicStanding academicStanding, double GPA, int creditsEarned, List<CourseOffering> enrolledCourses,
        // GraduateStudent Attributes
        GraduateProgram programLevel, String thesisTitle, Faculty advisor
        ) {
        super(
            // Person Attributes
            personId, firstName, middleName, lastName, dateOfBirth, gender, address, contactNumber, email,
            // Student Attributes
            studentId, enrollmentDate, department, course, academicStanding, GPA, creditsEarned, enrolledCourses
        );

        this.programLevel = programLevel;
        this.thesisTitle = thesisTitle;
        this.advisor = advisor;
    }

    // * Getters
    public GraduateProgram getProgramLevel() { return this.programLevel; }
    public String getThesisTitle() { return this.thesisTitle; }
    public Faculty getAdvisor() { return this.advisor; }

    // * Setters
    public void setThesisTitle(String thesisTitle) { this.thesisTitle = thesisTitle; }
    public void setAdvisor(Faculty advisor) { this.advisor = advisor; }

    // * Methods
    // [ABSTRACT] Check graduate student's eligibility for graduation
    public abstract boolean isEligibleForGraduation();

    // [METHOD: Override] Display graduate student's information
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Program Level: " + programLevel);
        System.out.println("Thesis Title: " + (thesisTitle !=null ? thesisTitle : "Not Set"));
        System.out.println("Advisor: " + (advisor != null ? advisor.getFacultyId() : "Not Assigned"));
        System.out.println("Year Level: " + calculateYearLevel());
        System.out.println("Eligible for Graduation: " + (isEligibleForGraduation() ? "Yes" : "No"));
    }

    // [METHOD: Override] Generate graduate student's stringified report
    @Override
    public String generateReport() {
        StringBuilder report = new StringBuilder(super.generateReport());

        report.append("\nProgram Level: ").append(programLevel);
        report.append("\nThesis Title: ").append(thesisTitle != null ? thesisTitle : "Not Set");
        report.append("\nAdvisor: ").append(advisor != null ? advisor.getFacultyId() : "Not Assigned");
        report.append("\nYear Level: ").append(calculateYearLevel());
        report.append("\nEligible for Graduation: ").append(isEligibleForGraduation() ? "Yes" : "No");

        return report.toString();
    }
}