package ums.model.enums;

// * [STATUS] Enrollment
public enum EnrollmentStatus {
    ENROLLED("Enrolled"),
    WAITLISTED("Waitlisted"),
    DROPPED("Dropped"),
    COMPLETED("Completed");

    // * Attribute
    private final String displayName;

    // * Constructor (Parameterized)
    EnrollmentStatus(String displayName) { this.displayName = displayName; }

    // * Getter
    public String getDisplayName() { return displayName; }

    // [METHOD] Return stringified name
    @Override
    public String toString() { return displayName; }
}