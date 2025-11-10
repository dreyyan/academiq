package ums.model.enums;

// * [POSITION] Graduate Program
public enum GraduateProgram {
    MASTERS("Master's"),
    PHD("PhD");

    // * Attribute
    private final String displayName;

    // * Constructor (Parameterized)
    GraduateProgram(String displayName) {
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
