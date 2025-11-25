package ums.model.enums;

public enum Semester {
    FIRST_SEM("1st Semester"),
    SECOND_SEM("2nd Semester");

    // * Attribute
    private final String displayName;

    // * Constructor (Parameterized)
    Semester(String displayName) { this.displayName = displayName; }

    // * Getter
    public String getDisplayName() { return displayName; }

    // [METHOD] Return stringified name
    @Override
    public String toString() { return displayName; }


    public static Semester fromString(String s) {
        if (s == null) return null;
        s = s.trim().toLowerCase();
        return switch (s) {
            case "1st semester", "first semester", "1st sem", "first sem" -> FIRST_SEM;
            case "2nd semester", "second semester", "2nd sem", "second sem" -> SECOND_SEM;
            default -> throw new IllegalArgumentException("Invalid semester: " + s);
        };
    }
}