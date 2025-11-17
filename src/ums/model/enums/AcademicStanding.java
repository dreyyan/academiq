package ums.model.enums;

// * [STATUS] Academic Standing
public enum AcademicStanding {
    GOLD(1.00, 1.24),
    SILVER(1.25, 1.49),
    BRONZE(1.50, 1.74),
    GOOD(1.75,2.99),
    PROBATION(3.00,4.99),
    SUSPENDED(5.00, Double.MAX_VALUE);

    // * Attributes
    private final double minGWA;
    private final double maxGWA;

    // * Constructor (Parameterized)
    AcademicStanding(double minGWA, double maxGWA) {
        this.minGWA = minGWA;
        this.maxGWA = maxGWA;
    }

    // * Getters
    public double getMinGWA() { return minGWA; }
    public double getMaxGWA() { return maxGWA; }

    // * Methods
    // [METHOD] Determine academic standing category based on GWA
    public static AcademicStanding fromGWA(double gwa) {
        for (AcademicStanding h : AcademicStanding.values()) {
            if (gwa >= h.minGWA && gwa <= h.maxGWA) {
                return h;
            }
        }
        return null;
    }
}