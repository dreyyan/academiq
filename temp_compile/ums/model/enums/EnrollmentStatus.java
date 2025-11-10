package ums.model.enums;

public enum EnrollmentStatus {
    ENROLLED("Enrolled"),
    WAITLISTED("Waitlisted"),
    DROPPED("Dropped"),
    COMPLETED("Completed");

    // * Attribute
    private final String displayName;

    // * Constructor (Parameterized)
    EnrollmentStatus(String displayName) {
        this.displayName = displayName;
    }

    // * Getter
    public String getDisplayName() {
        return displayName;
    }

    @Override
    // [METHOD] Return stringified name
    public String toString() {
        return displayName;
    }
}