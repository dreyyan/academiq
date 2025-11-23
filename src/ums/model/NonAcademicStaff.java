package ums.model;

// [IMPORT] Standard
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

// [IMPORT] Enums
import ums.model.enums.Department;
import ums.model.enums.FacultyRank;
import ums.model.enums.Gender;

public class NonAcademicStaff extends Faculty {
    // * Attributes
    private String position;
    private String shiftSchedule;
    private NonAcademicStaff supervisor;

    // * Constructor (Parameterized)
    public NonAcademicStaff(
        // Person Attributes
        String firstName, String middleName, String lastName, LocalDate dateOfBirth, Gender gender, String address, String contactNumber, String email,
        // Faculty Attributes
        Department department, FacultyRank rank, LocalDate hireDate, String officeLocation, double salary, boolean isTenured, List<GraduateStudent> advisees,
        // NonAcademicStaff Attributes
        String position, String shiftSchedule, NonAcademicStaff supervisor
    ) {
        super(
            // Person Attributes
            firstName, middleName, lastName, dateOfBirth, gender, address, contactNumber, email,
            // Faculty Attributes
            department, rank, hireDate, officeLocation, salary, isTenured, advisees
        );

        this.position = position;
        this.shiftSchedule = shiftSchedule;
        this.supervisor = supervisor;
    }

    public NonAcademicStaff(
        // Person Attributes
        String firstName, String middleName, String lastName, LocalDate dob, Gender gender, String address, String contact, String email,
        Department department, String position,
        LocalDate hireDate, String officeLocation, double salary, int workHours
    ) {
        super(
            firstName, middleName, lastName, dob, gender,
            address, contact, email,
            department,
            FacultyRank.INSTRUCTOR,
            hireDate, officeLocation,
            salary, false, 
            new ArrayList<GraduateStudent>()
        );

        this.position = position;
        this.shiftSchedule = workHours + " hrs/week";
        this.supervisor = null;
    }

    // * Getters
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
        schedule.append("Person ID: ").append(getPersonId()).append("\n");
        schedule.append("Position: ").append(position).append("\n");
        schedule.append("Shift: ").append(shiftSchedule).append("\n");
        schedule.append("Department: ").append(getDepartment()).append("\n");
        schedule.append("Office: ").append(getOfficeLocation()).append("\n");

        if (supervisor != null) {
            schedule.append("Supervisor: ").append(supervisor.getPersonId())
                    .append(" (").append(supervisor.getPosition()).append(")\n");
        } else {
            schedule.append("Supervisor: None\n");
        } return schedule.toString();
    }

    // [METHOD: Override] Generate staff's stringified report
    @Override
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        
        report.append("===== Non-Academic Staff Report =====\n");
        report.append("Person ID: ").append(getPersonId()).append("\n");
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
            report.append("Supervisor: ").append(supervisor.getPersonId())
                  .append(" - ").append(supervisor.getPosition()).append("\n");
        } else {
            report.append("Supervisor: None\n");
        }
        report.append("Advisees: ").append(getAdvisees().size()).append("\n");
        return report.toString();
    }

    public String[] toCSVRow() {
        return new String[] {
            getPersonId(),
            getFirstName(),
            getMiddleName(),
            getLastName(),
            getDateOfBirth().toString(),
            getGender().toString(),
            getAddress(),
            getContactNumber(),
            getEmail(),
            getDepartment() != null ? getDepartment().toString() : "",
            getRank() != null ? getRank().toString() : "",
            getHireDate().toString(),
            getOfficeLocation(),
            String.valueOf(getSalary()),
            String.valueOf(isTenured()),
            "0",
            getPosition(),
            getShiftSchedule() != null ? getShiftSchedule() : "",
            supervisor != null ? supervisor.getPersonId() : ""
        };
    }
}