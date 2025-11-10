package ums.model.enums;

// ? NOTE: Based on West Visayas State University (WVSU) - Main Campus College Departments
public enum Department {
    CAS("College of Arts and Sciences"),
    CICT("College of Information & Communications Technology"),
    CBM("College of Business & Management"),
    COM("College of Medicine"),
    COP("College of PESCAR"),
    COE("College of Education"),
    CON("College of Nursing"),
    COC("College of Communication"),
    COL("College of Law"),
    COD("College of Dentistry"),
    ILS("Integrated Laboratory School");

    // * Attribute
    private final String fullName;

    // * Constructor (Parameterized)
    Department(String fullName) {
        this.fullName = fullName;
    }

    // * Getter
    public String getFullName() {
        return fullName;
    }

    @Override
    // [METHOD] Return stringified name
    public String toString() {
        return fullName;
    }
}