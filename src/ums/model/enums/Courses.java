package ums.model.enums;

// ? NOTE: Based on West Visayas State University (WVSU) - Main Campus College Departments' Courses
public class Courses {
    // * [COURSE] College of Arts and Sciences
    public enum CASCourse {
        BA_ENGLISH_LANGUAGE_STUDIES("Bachelor of Arts in English Language Studies"),
        BA_FOREIGN_LANGUAGES("Bachelor of Arts in Foreign Languages"),
        BA_POLITICAL_SCIENCE("Bachelor of Arts in Political Science"),
        BS_APPLIED_MATHEMATICS("Bachelor of Science in Applied Mathematics"),
        BS_BIOLOGY("Bachelor of Science in Biology"),
        BS_CHEMISTRY("Bachelor of Science in Chemistry");

        // * Attribute
        private final String fullName;

        // * Constructor (Parameterized)
        CASCourse(String fullName) { this.fullName = fullName; }

        // * Getter
        public String getFullName() { return fullName; }

        // [METHOD] Return stringified name
        @Override
        public String toString() { return fullName; }
    }

    // * [COURSE] College of Information and Communications Technology
    public enum CICTCourse {
        BS_COMPUTER_SCIENCE("Bachelor of Science in Computer Science"),
        BS_ENTERTAINMENT_AND_MULTIMEDIA_COMPUTING("Bachelor of Science in Entertainment and Multimedia Computing"),
        BS_INFORMATION_SYSTEMS("Bachelor of Science in Information Systems"),
        BS_INFORMATION_TECHNOLOGY("Bachelor of Science in Information Technology"),
        BS_LIBRARY_AND_INFORMATION_SCIENCE("Bachelor of Science in Library and Information Science");

        // * Attribute
        private final String fullName;

        // * Constructor (Parameterized)
        CICTCourse(String fullName) { this.fullName = fullName; }

        // * Getter
        public String getFullName() { return fullName; }

        // [METHOD] Return stringified name
        @Override
        public String toString() { return fullName; }
    }

    // * [COURSE] College of Business & Management
    public enum CBMCourse {
        BS_BUSINESS_ADMINISTRATION("Bachelor of Science in Business Administration"),
        BS_COOPERATIVES_MANAGEMENT("Bachelor of Science in Cooperatives Management"),
        BS_HOSPITALITY_MANAGEMENT("Bachelor of Science in Hospitality Management"),
        BS_TOURISM_MANAGEMENT("Bachelor of Science in Tourism Management");

        // * Attribute
        private final String fullName;

        // * Constructor (Parameterized)
        CBMCourse(String fullName) { this.fullName = fullName; }

        // * Getter
        public String getFullName() { return fullName; }

        // [METHOD] Return stringified name
        @Override
        public String toString() { return fullName; }
    }

    // * [COURSE] College of PESCAR
    public enum COPCourse {
        BACHELOR_OF_CULTURE_AND_ARTS_EDUCATION("Bachelor of Culture and Arts Education"),
        BACHELOR_OF_PERFORMING_ARTS("Bachelor of Performing Arts"),
        BACHELOR_OF_PHYSICAL_EDUCATION("Bachelor of Physical Education"),
        BS_EXERCISE_AND_SPORTS_SCIENCE("Bachelor of Science in Exercise and Sports Science");

        // * Attribute
        private final String fullName;

        // * Constructor (Parameterized)
        COPCourse(String fullName) { this.fullName = fullName; }

        // * Getter
        public String getFullName() { return fullName; }

        // [METHOD] Return stringified name
        @Override
        public String toString() { return fullName; }
    }

    // * [COURSE] College of Medicine
    public enum COMCourse {
        DOCTOR_OF_MEDICINE("Doctor of Medicine");

        // * Attribute
        private final String fullName;

        // * Constructor (Parameterized)
        COMCourse(String fullName) { this.fullName = fullName; }

        // * Getter
        public String getFullName() { return fullName; }

        // [METHOD] Return stringified name
        @Override
        public String toString() { return fullName; }
    }

    // * [COURSE] College of Education
    public enum COECourse {
        BACHELOR_OF_EARLY_CHILDHOOD_EDUCATION("Bachelor of Early Childhood Education"),
        BACHELOR_OF_ELEMENTARY_EDUCATION("Bachelor of Elementary Education"),
        BACHELOR_OF_SECONDARY_EDUCATION("Bachelor of Secondary Education"),
        BACHELOR_OF_SPECIAL_NEEDS_EDUCATION("Bachelor of Special Needs Education");

        // * Attribute
        private final String fullName;

        // * Constructor (Parameterized)
        COECourse(String fullName) { this.fullName = fullName; }

        // * Getter
        public String getFullName() { return fullName; }

        // [METHOD] Return stringified name
        @Override
        public String toString() { return fullName; }
    }

    // * [COURSE] College of Nursing
    public enum CONCourse {
        BS_NURSING("Bachelor of Science in Nursing");

        // * Attribute
        private final String fullName;

        // * Constructor (Parameterized)
        CONCourse(String fullName) { this.fullName = fullName; }

        // * Getter
        public String getFullName() { return fullName; }

        // [METHOD] Return stringified name
        @Override
        public String toString() { return fullName; }
    }

    // * [COURSE] College of Communication
    public enum COCCourse {
        BA_BROADCASTING("Bachelor of Arts in Broadcasting"),
        BA_JOURNALISM("Bachelor of Arts in Journalism"),
        BS_DEVELOPMENT_COMMUNICATION("Bachelor of Science in Development Communication");

        // * Attribute
        private final String fullName;

        // * Constructor (Parameterized)
        COCCourse(String fullName) { this.fullName = fullName; }

        // * Getter
        public String getFullName() { return fullName; }

        // [METHOD] Return stringified name
        @Override
        public String toString() { return fullName; }
    }

    // * [COURSE] College of Law
    public enum COLCourse {
        JURIS_DOCTOR("Juris Doctor");

        // * Attribute
        private final String fullName;

        // * Constructor (Parameterized)
        COLCourse(String fullName) { this.fullName = fullName; }

        // * Getter
        public String getFullName() { return fullName; }

        // [METHOD] Return stringified name
        @Override
        public String toString() { return fullName; }
    }

    // * [COURSE] College of Dentistry
    public enum CODCourse {
        DOCTOR_OF_DENTAL_MEDICINE("Doctor of Dental Medicine");

        // * Attribute
        private final String fullName;

        // * Constructor (Parameterized)
        CODCourse(String fullName) { this.fullName = fullName; }

        // * Getter
        public String getFullName() { return fullName; }

        // [METHOD] Return stringified name
        @Override
        public String toString() { return fullName; }
    }

    // * [COURSE] Integrated Laboratory School
    public enum ILSCourse {
        PRE_ELEMENTARY("Pre-Elementary"),
        ELEMENTARY("Elementary"),
        JUNIOR_HIGH_SCHOOL("Junior High School"),
        SENIOR_HIGH_SCHOOL("Senior High School");

        // * Attribute
        private final String fullName;

        // * Constructor (Parameterized)
        ILSCourse(String fullName) { this.fullName = fullName; }

        // * Getter
        public String getFullName() { return fullName; }

        // [METHOD] Return stringified name
        @Override
        public String toString() { return fullName; }
    }
}