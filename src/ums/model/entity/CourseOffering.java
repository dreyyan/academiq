package ums.model.entity;

// [IMPORT] Enums
import ums.model.enums.Semester;

// [IMPORT] Class
import ums.model.AcademicStaff;

public class CourseOffering {
    // * Attributes
    private String offeringId;
    private Course course;
    private Semester semester;
    private int year;
    private AcademicStaff instructor;
    private TimeSlot schedule;
    private int capacity;
    private int enrolledCount;

    // * Constructor (Parameterized)
    public CourseOffering(String offeringId, Course course, Semester semester, int year, AcademicStaff instructor, TimeSlot schedule, int capacity, int enrolledCount) {
        this.offeringId = offeringId;
        this.course = course;
        this.semester = semester;
        this.year = year;
        this.instructor = instructor;
        this.schedule = schedule;
        this.capacity = capacity;
        this.enrolledCount = enrolledCount;
    }

    // * Getters
    public String getOfferingId() { return this.offeringId; }
    public Course getCourse() { return this.course; }
    public Semester getSemester() { return this.semester; }
    public int getYear() { return this.year; }
    public AcademicStaff getInstructor() { return this.instructor; }
    public TimeSlot getSchedule() { return this.schedule; }
    public int getCapacity() { return this.capacity; }
    public int getEnrolledCount() { return this.enrolledCount; }

    // * Setters
    public void setCourse(Course course) { this.course = course; }
    public void setInstructor(AcademicStaff instructor) { this.instructor = instructor; }
    public void setSemester(Semester semester) { this.semester = semester; }
    public void setYear(int year) { this.year = year; }
    public void setSchedule(TimeSlot schedule) { this.schedule = schedule; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void updateEnrolledCount(int enrolledCount) { this.enrolledCount = enrolledCount; }
}