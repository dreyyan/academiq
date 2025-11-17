package ums.model.entity;

// [IMPORT] Standard
import java.time.LocalDate;

// [IMPORT] Models
import ums.model.enums.EnrollmentStatus;

public class Enrollment {
    // * Attributes
    private String enrollmentId;
    private LocalDate enrollmentDate;
    private EnrollmentStatus status;
    private double finalGrade;

    // * Constructor (Parameterized)
    Enrollment(String enrollmentId, LocalDate enrollmentDate, EnrollmentStatus status, double finalGrade) {
        this.enrollmentId = enrollmentId;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
        this.finalGrade = finalGrade;
    }

    // * Getters
   public String getEnrollmentId() { return this.enrollmentId; }
   public LocalDate getEnrollmentDate() { return this.enrollmentDate; }
   public EnrollmentStatus getStatus() { return this.status; }
   public double getFinalGrade() { return this.finalGrade; }

    // * Setters
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    public void setStatus(EnrollmentStatus status) { this.status = status; }
    public void assignGrade(double grade) { this.finalGrade = grade; }
}
