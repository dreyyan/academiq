package ums.model;

// [IMPORT] Standard
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

// [IMPORT] Enums
import ums.model.enums.Department;
import ums.model.enums.FacultyRank;
import ums.model.enums.Gender;

public abstract class NonAcademicStaff extends Faculty {
    // * Attributes
    private String staffId;
    private String position;
    private String shiftSchedule;
    private NonAcademicStaff supervisor;

    // * Constructor (Parameterized)
    public NonAcademicStaff(
        // Person Attributes
        String personId, String firstName, String middleName, String lastName, LocalDate dateOfBirth, Gender gender, String address, String contactNumber, String email,
        // Faculty Attributes
        String facultyId, Department department, FacultyRank rank, LocalDate hireDate, String officeLocation, double salary, boolean isTenured, List<GraduateStudent> advisees,
        // NonAcademicStaff Attributes
        String staffId, String position, String shiftSchedule, NonAcademicStaff supervisor
    ) {
        super(
            // Person Attributes
            personId, firstName, middleName, lastName, dateOfBirth, gender, address, contactNumber, email,
            // Faculty Attributes
            facultyId, department, rank, hireDate, officeLocation, salary, isTenured, advisees
        );

        this.staffId = staffId;
        this.position = position;
        this.shiftSchedule = shiftSchedule;
        this.supervisor = supervisor;
    }

    // * Getters
    public String getStaffId() { return this.staffId; }
    public String getPosition() { return this.position; }
    public String getShiftSchedule() { return this.shiftSchedule; }
    public NonAcademicStaff getSupervisor() { return this.supervisor; }

    // * Setters
    public void setPosition(String position) { this.position = position; }
    public void setShiftSchedule(String schedule) { this.shiftSchedule = schedule; }
    public void setSupervisor(NonAcademicStaff supervisor) { this.supervisor = supervisor; }

    // * Methods
    // [METHOD] Return a stringified work schedule
    public String generateWorkSchedule() {
        StringBuilder schedule = new StringBuilder();

        schedule.append("===== Work Schedule =====\n");
        schedule.append("Staff ID: ").append(staffId).append("\n");
        schedule.append("Faculty ID: ").append(getFacultyId()).append("\n");
        schedule.append("Position: ").append(position).append("\n");
        schedule.append("Shift: ").append(shiftSchedule).append("\n");
        schedule.append("Department: ").append(getDepartment()).append("\n");
        schedule.append("Office: ").append(getOfficeLocation()).append("\n");

        if (supervisor != null) {
            schedule.append("Supervisor: ").append(supervisor.getStaffId())
                    .append(" (").append(supervisor.getPosition()).append(")\n");
        } else {
            schedule.append("Supervisor: None\n");
        } return schedule.toString();
    }

    // [METHOD: Override] Display non-academic staff's information
    @Override
    public void displayInfo() {
        System.out.println("Staff ID: " + staffId);
        System.out.println("Faculty ID: " + getFacultyId());
        System.out.println("Department: " + getDepartment());
        System.out.println("Position: " + position);
        System.out.println("Rank: " + getRank());
        System.out.println("Office: " + getOfficeLocation());
        System.out.println("Hire Date: " + getHireDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        System.out.println("Years of Service: " + calculateYearsOfService());
        System.out.println("Salary: $" + String.format("%.2f", getSalary()));
        System.out.println("Shift Schedule: " + shiftSchedule);
        System.out.println("Tenured: " + (isTenured() ? "Yes" : "No"));

        if (supervisor != null) {
            System.out.println("Supervisor: " + supervisor.getStaffId() + " - " + supervisor.getPosition());
        } else {
            System.out.println("Supervisor: None");
        } System.out.println("Number of Advisees: " + getAdvisees().size());
    }

    // [METHOD: Override] Generate staff's stringified report
    @Override
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("===== Non-Academic Staff Report =====\n");
        report.append("Staff ID: ").append(staffId).append("\n");
        report.append("Faculty ID: ").append(getFacultyId()).append("\n");
        report.append("Department: ").append(getDepartment()).append("\n");
        report.append("Position: ").append(position).append("\n");
        report.append("Rank: ").append(getRank()).append("\n");
        report.append("Hire Date: ").append(getHireDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))).append("\n");
        report.append("Years of Service: ").append(calculateYearsOfService()).append("\n");
        report.append("Office: ").append(getOfficeLocation()).append("\n");
        report.append("Salary: $").append(String.format("%.2f", getSalary())).append("\n");
        report.append("Shift Schedule: ").append(shiftSchedule).append("\n");
        report.append("Tenured: ").append(isTenured() ? "Yes" : "No").append("\n");
        if (supervisor != null) {
            report.append("Supervisor: ").append(supervisor.getStaffId())
                  .append(" - ").append(supervisor.getPosition()).append("\n");
        } else {
            report.append("Supervisor: None\n");
        }
        report.append("Advisees: ").append(getAdvisees().size()).append("\n");
        return report.toString();
    }
}