package ums.model.enums;

// * [POSITION] Graduate Program
public enum GraduateProgram {
    MASTERS("Master's"),
    PHD("PhD");

    // * Attribute
    private final String displayName;

    // * Constructor (Parameterized)
    GraduateProgram(String displayName) { this.displayName = displayName; }

    // * Getter
    public String getDisplayName() { return displayName; }

    // [METHOD] Return stringified name
    @Override
    public String toString() { return displayName; }
}
