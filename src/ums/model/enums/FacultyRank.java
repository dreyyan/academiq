package ums.model.enums;

// * [POSITION] Faculty Rank
public enum FacultyRank {
    INSTRUCTOR("Instructor", 10000, 15),
    ASSISTANT_PROFESSOR("Assistant Professor", 20000, 20),
    ASSOCIATE_PROFESSOR("Associate Professor", 30000, 25),
    FULL_PROFESSOR("Full Professor", 45000, 30);

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