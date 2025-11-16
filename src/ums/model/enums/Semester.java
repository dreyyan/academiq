package ums.model.enums;

public enum Semester {
    FIRST_SEM("1st Semester"),
    SECOND_SEM("2nd Semester");

    // * Attribute
    private final String displayName;

    // * Constructor (Parameterized)
    Semester(String displayName) {
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