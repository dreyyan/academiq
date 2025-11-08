package ums.faculty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Faculty {
    // ===== Attributes =====
    private String facultyId;
    private Department department;
    private FacultyRank rank;
    private LocalDate hireDate;
    private String officeLocation;
    private double salary;
    private boolean isTenured;
    private List<GraduateStudents> advisees;

    // ===== Constructor ======
    public Faculty(String facultyId, Department department, FacultyRank rank, LocalDate hireDate, String officeLocation, double salary, boolean isTenured) {
        this.facultyId = facultyId;
        this.department = department;
        this.rank = rank;
        this.hireDate = hireDate;
        this.officeLocation = officeLocation;
        this.salary = salary;
        this.isTenured = isTenured;
        this.advisees = new ArrayList<>();
    }

   // ===== Getters =====
    public String getFacultyId() { return this.facultyId; }
    public Department getDepartment() { return this.department; }
    public FacultyRank getRank() { return this.rank; }
    public LocalDate getHireDate() { return this.hireDate; }
    public String getOfficeLocation() { return this.officeLocation; }
    public double getSalary() { return this.salary; }
    public boolean isTenured() { return this.isTenured; }
    public List<GraduateStudents> getAdvisees() { return new ArrayList<>(advisees); }

   // ===== Setters =====
    public void setRank(FacultyRank rank) { this.rank = rank; }
    public void setOfficeLocation(String location) { this.officeLocation = location; }   
    public void setSalary(double salary) { this.salary = salary; }
    public void setTenured(boolean tenured) {this.isTenured = tenured;}

    // ===== Methods =====
    public void grantTenure() { this.isTenured = true;} // Grant tenure to faculty member

    // Add a graduate student to advisee list
    public void addAdvisee(GraduateStudent student) {
        if (student !=null && !advisees.contains(student)){
            advisees.add(student);
        }
    }

    // Remove a graduate student from advisee list
    public void removeAdvisee(GraduateStudent student) { 
        advisees.remove(student); 
    }

    // Calculate how many years faculty has worked
    public int calculateYearsOfService() { 
        return LocalDate.now().getYear() - hireDate.getYear();
    }

    // Display faculty information 
    public void displayInfo() {
        System.out.println("Faculty ID: " + facultyId);
        System.out.println("Department: " + department);
        System.out.println("Rank: " + rank);
        System.out.println("Office: " + officeLocation);
        System.out.println("Salary: $" + salary);
        System.out.println("Tenured: " + (isTenured ? "Yes" : "No"));
        System.out.println("Number of Advisees: " + advisees.size());
    }

    // Generate a formatted report of faculty information
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("======= Faculty Report =======\n");
        report.append("ID: ").append(facultyId).append("\n");
        report.append("Department: ").append(department).append("\n");
        report.append("Rank: ").append(rank).append("\n");
        report.append("Hire Date: ").append(hireDate).append("\n");
        report.append("Years of Service: ").append(calculateYearsOfService()).append("\n");
        report.append("Office: ").append(officeLocation).append("\n");
        report.append("Salary: $").append(String.format("%.2f", salary)).append("\n");
        report.append("Tenured: ").append(isTenured ? "Yes" : "No").append("\n");
        report.append("Advisees: ").append(advisees.size()).append("\n");
        return report.toString();
    }
}
