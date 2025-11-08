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
    GradeType(String displayName) {
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
