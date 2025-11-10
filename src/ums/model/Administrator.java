package ums.model;

// [IMPORT] Standard
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// [IMPORT] Enums
import ums.model.enums.AdminLevel;
import ums.model.enums.Department;
import ums.model.enums.FacultyRank;

public class Administrator extends NonAcademicStaff {
    // * Attributes
    private AdminLevel adminLevel;
    private List<String> managedUnits;
    private int clearanceLevel;

    // * Constructor (Parameterized)
    public Administrator(String facultyId, Department department, FacultyRank rank, LocalDate hireDate, 
                        String officeLocation, double salary, boolean isTenured, String staffId, 
                        String position, String shiftSchedule, NonAcademicStaff supervisor,
                        AdminLevel adminLevel, int clearanceLevel) {
        super(facultyId, department, rank, hireDate, officeLocation, salary, isTenured, 
              staffId, position, shiftSchedule, supervisor);
        this.adminLevel = adminLevel;
        this.managedUnits = new ArrayList<>();
        this.clearanceLevel = clearanceLevel;
    }

    // * Getters
    public AdminLevel getAdminLevel() { return this.adminLevel; }
    public List<String> getManagedUnits() { return new ArrayList<>(managedUnits); }
    public int getClearanceLevel() { return this.clearanceLevel; }

    // * Methods
    // [METHOD] Add a unit to the managed units list
    public void addManagedUnit(String unit) {
        if (unit != null && !unit.trim().isEmpty() && !managedUnits.contains(unit)) {
            managedUnits.add(unit);
        }
    }

    // [METHOD] Approve or reject a request based on clearance
    public boolean approveRequest(String request) {
        if (request == null || request.trim().isEmpty()) {
            return false;
        }
        return clearanceLevel >= 3; 
    }

    // [METHOD] Generate an administrator report
    public String generateAdminReport() {
        StringBuilder report = new StringBuilder();
        report.append("===== Administrator Report =====\n");
        report.append("Staff ID: ").append(getStaffId()).append("\n");
        report.append("Faculty ID: ").append(getFacultyId()).append("\n");
        report.append("Position: ").append(getPosition()).append("\n");
        report.append("Department: ").append(getDepartment()).append("\n");
        report.append("Admin Level: ").append(adminLevel).append("\n");
        report.append("Clearance Level: ").append(clearanceLevel).append("\n");
        report.append("Managed Units (").append(managedUnits.size()).append("):\n");
        if (managedUnits.isEmpty()) {
            report.append("  - None\n");
        } else {
            for (String unit : managedUnits) {
                report.append("  - ").append(unit).append("\n");
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

    // [METHOD] Display administrator's information 
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Admin Level: " + adminLevel);
        System.out.println("Clearance Level: " + clearanceLevel);
        System.out.println("Managed Units: " + managedUnits.size());
        if (!managedUnits.isEmpty()) {
            System.out.println("Units:");
            for (String unit : managedUnits) {
                System.out.println("  - " + unit);
            }
        }
    }

    // [METHOD] Generate administrator's stringified report 
    @Override
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append(super.generateReport());
        report.append("Admin Level: ").append(adminLevel).append("\n");
        report.append("Clearance Level: ").append(clearanceLevel).append("\n");
        report.append("Managed Units: ").append(managedUnits.size()).append("\n");
        if (!managedUnits.isEmpty()) {
            for (String unit : managedUnits) {
                report.append("  - ").append(unit).append("\n");
            }
        }
        return report.toString();
    }
}