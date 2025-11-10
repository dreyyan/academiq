package ums.model.enums;

// * [ADMIN LEVEL] Administrative hierarchy in university
public enum AdminLevel {
    JUNIOR("Junior"),
    MID("Mid-Level"),
    SENIOR("Senior"),
    DIRECTOR("Director"),
    EXECUTIVE("Executive");

    // * Attribute
    private final String displayName;

    // * Constructor (Parameterized)
    AdminLevel(String displayName) {
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