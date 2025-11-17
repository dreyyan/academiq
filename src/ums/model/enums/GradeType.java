package ums.model.enums;

// * [TYPE] Grading Type
public enum GradeType {
    ASSIGNMENT("Assignment"),
    QUIZ("Quiz"),
    MIDTERM("Midterm"),
    FINAL("Final"),
    PROJECT("Project");

    // * Attribute
    private final String displayName;

    // * Constructor (Parameterized)
    GradeType(String displayName) { this.displayName = displayName; }

    // * Getter
    public String getDisplayName() { return displayName; }

    // [METHOD] Return stringified name
    @Override
    public String toString() { return displayName; }
}
