package ums.model.enums;

// * [POSITION] Student Year Levels
public enum YearLevel {
    FRESHMAN("1st Year"),
    SOPHOMORE("2nd Year"),
    JUNIOR("3rd Year"),
    SENIOR("4th Year");

    // * Attribute
    private final String displayName;

    // * Constructor (Parameterized)
    YearLevel(String displayName) {
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