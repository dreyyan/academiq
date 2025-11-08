package ums.model.entity;

// [IMPORT] Enums
import ums.model.enums.Semester;

public class CourseSection {
    // * Attributes
    private String sectionId;
    private Semester semester;
    private int year;
    private TimeSlot schedule;
    
    // * Constructor (Parameterized)
    CourseSection(String sectionId, Semester semester, int year, TimeSlot schedule) {
        this.sectionId = sectionId;
        this.semester = semester;
        this.year = year;
        this.schedule = schedule;
    }

    // * Getters
    public String getSectionId() { return this.sectionId; }
    public Semester getSemeseter() { return this.semester; }
    public int getYear() { return this.year; }
    public TimeSlot getSchedule() { return this.schedule; }

    // * Setters
    public void setSemester(Semester semester) { this.semester = semester; }
    public void setSchedule(TimeSlot schedule) { this.schedule = schedule; }
}
