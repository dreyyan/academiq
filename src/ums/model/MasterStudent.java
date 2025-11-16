// package ums.model;

// // [IMPORT] Standard
// import java.time.LocalDate;
// import java.util.List;

// // [IMPORT] Entities
// import ums.model.entity.Course;
// import ums.model.entity.CourseOffering;

// // [IMPORT] Enums
// import ums.model.enums.AcademicStanding;
// import ums.model.enums.Department;
// import ums.model.enums.Gender;
// import ums.model.enums.GraduateProgram;

// public class MasterStudent extends GraduateStudent {
//     // * Attributes
//     private boolean courseworkCompleted;
//     private boolean comprehensiveExamPassed;
//     private boolean thesisProposalApproved;

//     // * Constructor (Parameterized)
//     public MasterStudent(
//         // Person Attributes
//         String personId, String firstName, String middleName, String lastName, LocalDate dateOfBirth, Gender gender, String address, String contactNumber, String email,
//         // Student Attributes
//         String studentId, LocalDate enrollmentDate, Department department, Course course, AcademicStanding academicStanding, double GPA, int creditsEarned, List<CourseOffering> enrolledCourses,
//         // GraduateStudent Attributes
//         GraduateProgram programLevel, String thesisTitle, Faculty advisor,
//         // MasterStudent Attributes
//         boolean courseworkCompleted, boolean comprehensiveExamPassed, boolean thesisProposalApproved
//     ) {
//         super(
//             // Person Attributes
//             personId, firstName, middleName, lastName, dateOfBirth, gender, address, contactNumber, email,
//             // Student Attributes
//             studentId, enrollmentDate, department, course, academicStanding, GPA, creditsEarned, enrolledCourses,
//             // GraduateStudent Attributes
//             programLevel, thesisTitle, advisor
//         );

//         this.courseworkCompleted = courseworkCompleted;
//         this.comprehensiveExamPassed = comprehensiveExamPassed;
//         this.thesisProposalApproved = thesisProposalApproved;
//     }

//     // * Getters
//     public boolean isCourseworkCompleted() { return courseworkCompleted; }
//     public boolean isComprehensiveExamPassed() { return comprehensiveExamPassed; }
//     public boolean isThesisProposalApproved() { return thesisProposalApproved; }

//     // * Setters
//     public void completeCourseWork() { this.courseworkCompleted = true; }
//     public void passComprehensiveExam() { this.comprehensiveExamPassed = true; }
//     public void approveThesisProposal() { this.thesisProposalApproved = true; }

//     // * Methods
//     // [METHOD: Override] Calculate Master student's current year level
//     @Override
//     public String calculateYearLevel() {
//         return "Master";
//     }

//     // [METHOD: Override] Check Master student's eligibility for graduation
//     @Override
//     public boolean isEligibleForGraduation() {
//         return courseworkCompleted && comprehensiveExamPassed && thesisProposalApproved && getCreditsEarned() >= 60;
//     }
    
//     // [METHOD: Override] Display master student's academics information
//     @Override
//     public List<String> getAcademicsInformation() {
//         List<String> info = super.getAcademicsInformation();

//         // Add master student–specific information
//         info.add(isCourseworkCompleted() ? "Yes" : "No");
//         info.add(isComprehensiveExamPassed() ? "Yes" : "No");
//         info.add(isThesisProposalApproved() ? "Yes" : "No");

//         return info;
//     }

//     // [METHOD: Override] Generate Master student's stringified report
//     @Override
//     public String generateReport() {
//         StringBuilder report = new StringBuilder(super.generateReport());

//         report.append("\nCoursework Completed: ").append(courseworkCompleted ? "Yes" : "No");
//         report.append("\nComprehensive Exam Passed: ").append(comprehensiveExamPassed ? "Yes" : "No");
//         report.append("\nThesis Proposal Approved: ").append(thesisProposalApproved ? "Yes" : "No");

//         return report.toString();
//     }
// }