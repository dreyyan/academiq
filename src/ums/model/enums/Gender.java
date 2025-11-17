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

    // [METHOD] Return stringified name
    @Override
    public String toString() { return displayName; }
}