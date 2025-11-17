package ums.model.enums;

// ? NOTE: Based on West Visayas State University (WVSU) - Main Campus College Departments
public enum Department {
    CAS("CAS", "College of Arts and Sciences"),
    CICT("CICT", "College of Information & Communications Technology"),
    CBM("CBM", "College of Business & Management"),
    COM("COM", "College of Medicine"),
    COP("COP", "College of PESCAR"),
    COE("COE", "College of Education"),
    CON("CON", "College of Nursing"),
    COC("COC", "College of Communication"),
    COL("COL", "College of Law"),
    COD("COD", "College of Dentistry"),
    ILS("ILS", "Integrated Laboratory School"),
    UNASSIGNED("", "Unassigned");

    // * Attribute
    private final String fullName;
    private final String code;

    // * Constructor (Parameterized)
    Department(String code, String fullName) {
        this.code = code;
        this.fullName = fullName;
    }

    // * Getter
    public String getCode() { return this.code; }
    public String getFullName() { return this.fullName; }

    // [METHOD] Return stringified name
    @Override
    public String toString() { return fullName; }

    // [METHOD] Return department from code or full name
    public static Department fromCodeOrFullName(String input) {
        // If no input, return 'UNASSIGNED' value
        if (input == null || input.isBlank()) return UNASSIGNED;

        // Remove leading and trailing whitespaces
        input = input.trim();

        for (Department dept : Department.values()) {
            // Match full name or enum code
            if (dept.getFullName().equalsIgnoreCase(input) || dept.name().equalsIgnoreCase(input)) {
                return dept;
            }
        }

        // If none, return 'UNASSIGNED' value
        return UNASSIGNED;
    }
}