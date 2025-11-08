package ums.model.enums;

// * [POSITION] Faculty Rank
public enum FacultyRank {
    INSTRUCTOR("Instructor"),
    ASSISTANT_PROFESSOR("Assistant Professor"),
    ASSOCIATE_PROFESSOR("Associate Professor"),
    FULL_PROFESSOR("Full Professor");

    // * Attribute
    private final String displayName;

    // * Constructor (Parameterized)
    FacultyRank(String displayName) {
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
