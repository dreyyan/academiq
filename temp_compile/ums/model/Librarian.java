package ums.model;

// [IMPORT] Standard
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// [IMPORT] Enums
import ums.model.enums.Department;
import ums.model.enums.FacultyRank;
import ums.model.enums.Gender;

public class Librarian extends NonAcademicStaff {
    // * Attributes
    private int libraryCardIssuedCount;
    private List<String> managedSections;

    // * Constructor (Parameterized)
    public Librarian(
        // Person Attributes
        String personId, String firstName, String middleName, String lastName, LocalDate dateOfBirth, Gender gender, String address, String contactNumber, String email,
        // Faculty Attributes
        String facultyId, Department department, FacultyRank rank, LocalDate hireDate, String officeLocation, double salary, boolean isTenured, List<GraduateStudent> advisees,
        // NonAcademicStaff Attributes
        String staffId, String position, String shiftSchedule, NonAcademicStaff supervisor,
        // Librarian Attributes
        int libraryCardIssuedCount, List<String> managedSections
        ) {
        super(
            // Person Attributes
            personId, firstName, middleName, lastName, dateOfBirth, gender, address, contactNumber, email,
            // Faculty Attributes
            facultyId, department, rank, hireDate, officeLocation, salary, isTenured, advisees,
            // NonAcademicStaff Attributes
            staffId, position, shiftSchedule, supervisor
        );

        this.libraryCardIssuedCount = libraryCardIssuedCount;
        this.managedSections = new ArrayList<>();
    }

    // * Getters
    public int getLibraryCardIssuedCount() { return this.libraryCardIssuedCount; }
    public List<String> getManagedSections() { return new ArrayList<>(managedSections); }

    // * Methods
    // [METHOD] Issue a library card and increment the issued count
    public void issueLibraryCard() { this.libraryCardIssuedCount++; }

    // [METHOD] Add a section to the list of managed sections if it is valid and not already present
    public void addManagedSection(String section) {
        if (section != null && !section.trim().isEmpty() && !managedSections.contains(section)) {
            managedSections.add(section);
        }
    }

    // [METHOD: Override] Display librarian info
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Library Cards Issued: " + libraryCardIssuedCount);
        System.out.println("Managed Sections: " + managedSections.size());

        if (!managedSections.isEmpty()) {
            System.out.println("Sections:");
            for (String section : managedSections) {
                System.out.println("  - " + section);
            }
        }
    }

    // [METHOD: Override] Generate librarian report
    @Override
    public String generateReport() {
        StringBuilder report = new StringBuilder(super.generateReport());

        report.append("Library Cards Issued: ").append(libraryCardIssuedCount).append("\n");
        report.append("Managed Sections: ").append(managedSections.size()).append("\n");

        if (!managedSections.isEmpty()) {
            for (String section : managedSections) {
                report.append("  - ").append(section).append("\n");
            }
        } return report.toString();
    }
}