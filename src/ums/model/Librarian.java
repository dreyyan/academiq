package ums.model;

// [IMPORT] Standard
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ums.model.enums.Department;
import ums.model.enums.FacultyRank;

public class Librarian extends NonAcademicStaff {
    // * Attributes
    private int libraryCardIssuedCount;
    private List<String> managedSections;

    // * Constructor (Parameterized)
    public Librarian(String facultyId, Department department, FacultyRank rank, LocalDate hireDate, 
                     String officeLocation, double salary, boolean isTenured, String staffId, 
                     String position, String shiftSchedule, NonAcademicStaff supervisor) {
        super(facultyId, department, rank, hireDate, officeLocation, salary, isTenured, 
              staffId, position, shiftSchedule, supervisor);
        this.libraryCardIssuedCount = 0;
        this.managedSections = new ArrayList<>();
    }

    // * Getters
    public int getLibraryCardIssuedCount() { 
        return this.libraryCardIssuedCount; 
    }
    
    public List<String> getManagedSections() { 
        return new ArrayList<>(managedSections); 
    }

    // * Methods
    // [METHOD] Issue a library card and increment count
    public void issueLIbraryCard() {
        this.libraryCardIssuedCount++;
    }

    // [METHOD] Add a section to the managed sections list
    public void addManagedSection(String section) {
        if (section != null && !section.trim().isEmpty() && !managedSections.contains(section)) {
            managedSections.add(section);
        }
    }

    // [METHOD] Generate a library report
    public String generateLibraryReport() {
        StringBuilder report = new StringBuilder();
        report.append("===== Library Staff Report =====\n");
        report.append("Staff ID: ").append(getStaffId()).append("\n");
        report.append("Faculty ID: ").append(getFacultyId()).append("\n");
        report.append("Position: ").append(getPosition()).append("\n");
        report.append("Department: ").append(getDepartment()).append("\n");
        report.append("Library Cards Issued: ").append(libraryCardIssuedCount).append("\n");
        report.append("Managed Sections (").append(managedSections.size()).append("):\n");
        if (managedSections.isEmpty()) {
            report.append("  - None\n");
        } else {
            for (String section : managedSections) {
                report.append("  - ").append(section).append("\n");
            }
        }
        report.append("Shift Schedule: ").append(getShiftSchedule()).append("\n");
        if (getSupervisor() != null) {
            report.append("Supervisor: ").append(getSupervisor().getStaffId())
                  .append(" - ").append(getSupervisor().getPosition()).append("\n");
        } else {
            report.append("Supervisor: None\n");
        }
        return report.toString();
    }

    // [METHOD] Display librarian's information 
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

    // [METHOD] Generate librarian's stringified report 
    @Override
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append(super.generateReport());
        report.append("Library Cards Issued: ").append(libraryCardIssuedCount).append("\n");
        report.append("Managed Sections: ").append(managedSections.size()).append("\n");
        if (!managedSections.isEmpty()) {
            for (String section : managedSections) {
                report.append("  - ").append(section).append("\n");
            }
        }
        return report.toString();
    }
}