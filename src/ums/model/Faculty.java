package ums.model;

// [IMPORT] Standard
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// [IMPORT] Enums
import ums.model.enums.Department;
import ums.model.enums.FacultyRank;
import ums.model.enums.Gender;

public abstract class Faculty extends Person {
    // * Attributes
    private Department department;
    private FacultyRank rank;
    private LocalDate hireDate;
    private String officeLocation;
    private double salary;
    private boolean isTenured;
    private List<GraduateStudent> advisees;

    // * Constructor (Parameterized)
    public Faculty(
        // Person Attributes
        String firstName, String middleName, String lastName, LocalDate dateOfBirth, Gender gender, String address, String contactNumber, String email,    
        // Faculty Attributes
        Department department, FacultyRank rank, LocalDate hireDate, String officeLocation, double salary, boolean isTenured, List<GraduateStudent> advisees
    ) {
        super(
            // Person Attributes
            firstName, middleName, lastName, dateOfBirth, gender, address, contactNumber, email
        );

        this.department = department;
        this.rank = rank;
        this.hireDate = hireDate;
        this.officeLocation = officeLocation;
        this.salary = salary;
        this.isTenured = isTenured;
        this.advisees = new ArrayList<GraduateStudent>();
    }

    // * Constructor (Parameterized with ID)
    public Faculty(
        String personId,
        String firstName, String middleName, String lastName, LocalDate dateOfBirth,
        Gender gender, String address, String contactNumber, String email,
        Department department, FacultyRank rank, LocalDate hireDate,
        String officeLocation, double salary, boolean isTenured, List<GraduateStudent> advisees
    ) {
        super(personId, firstName, middleName, lastName, dateOfBirth, gender,
            address, contactNumber, email);

        this.department = department;
        this.rank = rank;
        this.hireDate = hireDate;
        this.officeLocation = officeLocation;
        this.salary = salary;
        this.isTenured = isTenured;
        this.advisees = new ArrayList<>(advisees);
    }

   // * Getters
    public Department getDepartment() { return this.department; }
    public FacultyRank getRank() { return this.rank; }
    public LocalDate getHireDate() { return this.hireDate; }
    public String getOfficeLocation() { return this.officeLocation; }
    public double getSalary() { return this.salary; }
    public boolean isTenured() { return this.isTenured; }
    public List<GraduateStudent> getAdvisees() { return new ArrayList<>(advisees); }

   // * Setters
    public void setRank(FacultyRank rank) { this.rank = rank; }
    public void setOfficeLocation(String location) { this.officeLocation = location; }   
    public void setSalary(double salary) { this.salary = salary; }
    public void setDepartment(Department department) { this.department = department; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
    public void setTenured(boolean tenured) { this.isTenured = tenured; }

    // * Methods
    // [METHOD] Grant tenure to faculty member
    public void grantTenure() { this.isTenured = true; }

    // [METHOD] Add a graduate student to advisee list
    public void addAdvisee(GraduateStudent student) {
        if (student !=null && !advisees.contains(student)){
            advisees.add(student);
        }
    }

    // [METHOD] Remove a graduate student from advisee list
    public void removeAdvisee(GraduateStudent student) { 
        advisees.remove(student); 
    }

    // [METHOD] Calculate faculty's working years
    public int calculateYearsOfService() { 
        return LocalDate.now().getYear() - hireDate.getYear();
    }

    // [METHOD] Generate faculty's stringified report
    public String generateReport() {
        StringBuilder report = new StringBuilder();

        report.append("======= Faculty Report =======\n");
        report.append("ID: ").append(getPersonId()).append("\n");
        report.append("Department: ").append(getDepartment()).append("\n");
        report.append("Rank: ").append(getRank()).append("\n");
        report.append("Hire Date: ").append(getHireDate()).append("\n");
        report.append("Years of Service: ").append(calculateYearsOfService()).append("\n");
        report.append("Office: ").append(getOfficeLocation()).append("\n");
        report.append("Salary: $").append(String.format("%.2f", getSalary())).append("\n");
        report.append("Tenured: ").append(isTenured() ? "Yes" : "No").append("\n");
        report.append("Advisees: ").append(getAdvisees().size()).append("\n");

        return report.toString();
    }
}
