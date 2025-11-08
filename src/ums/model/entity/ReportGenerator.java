package ums.model.entity;

public abstract class ReportGenerator {
    // * Methods
    public abstract String generateStudentTranscript();
    public String generateCourseRoster() { return ""; }
    public String generateFacultyLoad() { return ""; }
    public String generateEnrollmentSummary() { return ""; }
}