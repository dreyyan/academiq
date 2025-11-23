package ums.model.enums;

// * [POSITION] Student Year Levels
public enum YearLevel {
    FRESHMAN("Freshman (1st Year)"),
    SOPHOMORE("Sophomore (2nd Year)"),
    JUNIOR("Junior (3rd Year)"),
    SENIOR("Senior (4th Year)");

    // * Attribute
    private final String displayName;

    // * Constructor (Parameterized)
    YearLevel(String displayName) { this.displayName = displayName; }

    // * Getter
    public String getDisplayName() { return displayName; }

    public static YearLevel fromDisplayName(String displayName) {
        for (YearLevel yl : YearLevel.values()) {
            if (yl.displayName.equalsIgnoreCase(displayName.trim())) {
                return yl;
            }
        }
        throw new IllegalArgumentException("No enum constant for display name: " + displayName);
    }

    // [METHOD] Return stringified name
    @Override
    public String toString() { return displayName; }
}