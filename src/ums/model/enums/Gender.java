package ums.model.enums;

// * [OPTION] Gender Options
public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    // * Attribute
    private final String displayName;

    // * Constructor (Parameterized)
    Gender(String displayName) {
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