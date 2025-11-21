package ums.model.enums;

// * [POSITION] Faculty Rank
public enum FacultyRank {
    INSTRUCTOR("Instructor", 30000, 12),
    ASSISTANT_PROFESSOR("Assistant Professor", 40000, 14),
    ASSOCIATE_PROFESSOR("Associate Professor", 50000, 16),
    FULL_PROFESSOR("Full Professor", 65000, 18);

    // * Attributes
    private final String displayName;
    private final double defaultSalary;
    private final int defaultTeachingHours;

    // * Constructor (Parameterized)
    FacultyRank(String displayName, double defaultSalary, int defaultTeachingHours) {
        this.displayName = displayName;
        this.defaultSalary = defaultSalary;
        this.defaultTeachingHours = defaultTeachingHours;
    }

    // * Getters
    public String getDisplayName() { return displayName; }
    public double getDefaultSalary() { return defaultSalary; }
    public int getDefaultTeachingHours() { return defaultTeachingHours; }

    // [METHOD] Return stringified name
    @Override
    public String toString() { return displayName; }

    // [METHOD] Get FacultyRank from string (safe lookup)
    public static FacultyRank fromString(String rankStr) {
        for (FacultyRank rank : values()) {
            if (rank.displayName.equalsIgnoreCase(rankStr.replace("_", " "))) {
                return rank;
            }
        }
        throw new IllegalArgumentException("Invalid faculty rank: " + rankStr);
    }
}