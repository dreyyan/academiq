package ums.model.enums;

// * [PERIOD] Grading period
public enum Period {
    MIDTERM("Midterm"),
    FINAL("Final");

    // * Attribute
    private final String displayName;

    // * Constructor (Parameterized)
    Period(String displayName) {
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