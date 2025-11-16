// package ums.model;

// // [IMPORT] Standard
// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.List;

// // [IMPORT] Entities
// import ums.model.entity.Course;
// import ums.model.entity.CourseOffering;

// // [IMPORT] Enums
// import ums.model.enums.AcademicStanding;
// import ums.model.enums.Department;
// import ums.model.enums.Gender;
// import ums.model.enums.GraduateProgram;

// public class PhDStudent extends GraduateStudent {
//     // * Attributes
//     private boolean qualifyingExamPassed;
//     private boolean dissertationProposalDefended;
//     private List<String> publications;
//     private boolean candidacyStatus;
//     private LocalDate defenseDate;

//     // * Constructor (Parameterized)
//     public PhDStudent(
//         // Person Attributes
//         String personId, String firstName, String middleName, String lastName, LocalDate dateOfBirth, Gender gender, String address, String contactNumber, String email,
//         // Student Attributes
//         String studentId, LocalDate enrollmentDate, Department department, Course course, AcademicStanding academicStanding, double GPA, int creditsEarned, List<CourseOffering> enrolledCourses,
//         // GraduateStudent Attributes
//         GraduateProgram programLevel, String thesisTitle, Faculty advisor,
//         // PhDStudent Attributes
//         boolean qualifyingExamPassed, boolean dissertationProposalDefended, List<String> publications, boolean candidacyStatus, LocalDate defenseDate
//     ) {
//         super(
//             // Person Attributes
//             personId, firstName, middleName, lastName, dateOfBirth, gender, address, contactNumber, email,
//             // Student Attributes
//             studentId, enrollmentDate, department, course, academicStanding, GPA, creditsEarned, enrolledCourses,
//             // GraduateStudent Attributes
//             programLevel, thesisTitle, advisor
//         );

//         this.qualifyingExamPassed = qualifyingExamPassed;
//         this.dissertationProposalDefended = dissertationProposalDefended;
//         this.publications = publications != null ? publications : new ArrayList<>();
//         this.candidacyStatus = candidacyStatus;
//         this.defenseDate = defenseDate;
//     }

//     // * Getters
//     public boolean isQualifyingExamPassed() { return qualifyingExamPassed; }
//     public boolean isDissertationProposalDefended() { return dissertationProposalDefended; }
//     public List<String> getPublications() { return new ArrayList<>(publications); }
//     public boolean isCandidacyStatus() { return candidacyStatus; }
//     public LocalDate getDefenseDate() { return defenseDate; }

//     // * Setters
//     public void setQualifyingExamPassed(boolean qualifyingExamPassed) { this.qualifyingExamPassed = qualifyingExamPassed; }
//     public void setDissertationProposalDefended(boolean dissertationProposalDefended) { this.dissertationProposalDefended = dissertationProposalDefended; }
//     public void addPublication(String title) { this.publications.add(title); }
//     public void setCandidacyStatus(boolean candidacyStatus) { this.candidacyStatus = candidacyStatus; }
//     public void setDefenseDate(LocalDate defenseDate) { this.defenseDate = defenseDate; }

//     // * Methods
//     // [METHOD: Override] Calculate PhD student's current year level
//     @Override
//     public String calculateYearLevel() {
//         return "PhD";
//     }

//     // [METHOD: Override] Check PhD student's eligibility for graduation
//     @Override
//     public boolean isEligibleForGraduation() {
//         return qualifyingExamPassed && dissertationProposalDefended && !publications.isEmpty() && candidacyStatus && defenseDate != null;
//     }

//     // [METHOD: Override] Display PhD student's academics information
//     @Override
//     public List<String> getAcademicsInformation() {
//         List<String> info = super.getAcademicsInformation();

//         // Add PhD student–specific information
//         info.add(isQualifyingExamPassed() ? "Yes" : "No");
//         info.add(isDissertationProposalDefended() ? "Yes" : "No");
//         info.add(isCandidacyStatus() ? "Achieved" : "Not achieved");
//         info.add((getDefenseDate() != null ? getDefenseDate().toString() : "Not Scheduled"));
//         info.add(publications.isEmpty() ? "None" : String.valueOf(publications.size()));

//         return info;
//     }

//     // [METHOD: Override] Generate PhD student's stringified report
//     @Override
//     public String generateReport() {
//         StringBuilder report = new StringBuilder(super.generateReport());

//         report.append("\nQualifying Exam Passed: ").append(qualifyingExamPassed ? "Yes" : "No");
//         report.append("\nDissertation Proposal Defended: ").append(dissertationProposalDefended ? "Yes" : "No");
//         report.append("\nCandidacy Status: ").append(candidacyStatus ? "Achieved" : "Not Achieved");
//         report.append("\nDefense Date: ").append(defenseDate != null ? defenseDate : "Not Scheduled");
//         report.append("\nPublications: ").append(publications.isEmpty() ? "None" : publications.size());

//         return report.toString();
//     }
// }