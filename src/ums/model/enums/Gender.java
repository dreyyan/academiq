package ums.model.enums;

// * [OPTION] Gender Options
public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    // * Attribute
    private final String displayName;

    // * Constructor (Parameterized)
    Gender(String displayName) { this.displayName = displayName; }

    // * Getter
    public String getDisplayName() { return displayName; }

    // [METHOD] Convert string
    public static Gender fromString(String s) {
        if (s == null) return OTHER;
        switch (s.trim().toLowerCase()) {
            case "male": return MALE;
            case "female": return FEMALE;
            case "prefer not to say":
            case "other":
                return OTHER;
            default:
                return OTHER;
        }
    }

    // [METHOD] Return stringified name
    @Override
    public String toString() { return displayName; }
}