package ums.model.enums;

// * Department enum - all WVSU - Main Campus colleges
enum Department {
    CAS,  // College of Arts and Sciences
    CICT, // College of Information & Communications Technology
    CBM,  // College of Business & Management
    COP,  // College of PESCAR
    COM,  // College of Medicine
    COE,  // College of Education
    CON,  // College of Nursing
    COC,  // College of Communication
    COL,  // College of Law
    COD,  // College of Dentistry
    ILS   // Integrated Laboratory School

}

// * College of Arts and Sciences courses
enum CASCourse {
    BA_ENGLISH_LANGUAGE_STUDIES,
    BA_FOREIGN_LANGUAGES,
    BA_POLITICAL_SCIENCE,
    BS_APPLIED_MATHEMATICS,
    BS_BIOLOGY,
    BS_CHEMISTRY
}

// * College of Information and Communications Technology courses
enum CICTCourse {
    BS_COMPUTER_SCIENCE,
    BS_ENTERTAINMENT_AND_MULTIMEDIA_COMPUTING,
    BS_INFORMATION_SYSTEMS,
    BS_INFORMATION_TECHNOLOGY,
    BS_LIBRARY_AND_INFORMATION_SCIENCE
}

// * College of Business & Management courses
enum CBMCourse {
    BS_BUSINESS_ADMINISTRATION,
    BS_COOPERATIVES_MANAGEMENT,
    BS_HOSPITALITY_MANAGEMENT,
    BS_TOURISM_MANAGEMENT
}

// * College of PESCAR courses
enum COPCourse {
    BACHELOR_OF_CULTURE_AND_ARTS_EDUCATION,
    BACHELOR_OF_PERFORMING_ARTS,
    BACHELOR_OF_PHYSICAL_EDUCATION,
    BS_EXERCISE_AND_SPORTS_SCIENCE
}

// * College of Medicine course
enum COMCourse {
    DOCTOR_OF_MEDICINE
}

// * College of Education courses
enum COECourse {
    BACHELOR_OF_EARLY_CHILDHOOD_EDUCATION,
    BACHELOR_OF_ELEMENTARY_EDUCATION,
    BACHELOR_OF_SECONDARY_EDUCATION,
    BACHELOR_OF_SPECIAL_NEEDS_EDUCATION
}

// * College of Nursing course
enum CONCourse {
    BS_NURSING
}

// * College of Communication courses
enum COCCourse {
    BA_BROADCASTING,
    BA_JOURNALISM,
    BS_DEVELOPMENT_COMMUNICATION
}

// * College of Law course
enum COLCourse {
    JURIS_DOCTOR
}

// * College of Dentistry course
enum CODCourse {
    DOCTOR_OF_DENTAL_MEDICINE
}

// * Integrated Laboratory School levels
enum ILSCourse {
    PRE_ELEMENTARY,
    ELEMENTARY,
    JUNIOR_HIGH_SCHOOL,
    SENIOR_HIGH_SCHOOL
}

// * Student year levels
enum YearLevel {
    FRESHMAN,
    SOPHOMORE,
    JUNIOR,
    SENIOR
}

// * Academic standing status
enum AcademicStanding {
    GOOD_STANDING,
    PROBATION,
    SUSPENDED,
    HONORS,
    DEANS_LIST
}

// * Faculty rank
enum FacultyRank {
    INSTRUCTOR,
    ASSISTANT_PROFESSOR,
    ASSOCIATE_PROFESSOR,
    FULL_PROFESSOR
}

// * Gender options
enum Gender {
    MALE,
    FEMALE,
    OTHER
}

// * Enrollment status
enum EnrollmentStatus {
    ENROLLED,
    WAITLISTED,
    DROPPED,
    COMPLETED
}

// * Grade types
enum GradeType {
    ASSIGNMENT,
    QUIZ,
    MIDTERM,
    FINAL,
    PROJECT
}

// * Graduate program types
enum GraduateProgram {
    MASTERS,
    PHD
}
