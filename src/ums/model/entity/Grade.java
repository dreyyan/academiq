package ums.model.entity;

// [IMPORT] Standard
import java.time.LocalDate;

// [IMPORT] Enums
import ums.model.enums.GradeType;

public class Grade {
    // * Attributes
    private String assignmentId;
    private GradeType type;
    private double score;
    private double weight;
    private LocalDate dateSubmitted;

    // * Constructor (Parameterized)
    Grade(String assignmentId, GradeType type, double score, double weight, LocalDate dateSubmitted) {
        this.assignmentId = assignmentId;
        this.type = type;
        this.score = score;
        this.weight = weight;
        this.dateSubmitted = dateSubmitted;
    }

    // * Getters
    public String getAssignmentId() { return this.assignmentId; }
    public GradeType getType() { return this.type; }
    public double getScore() { return this.score; }
    public double getWeight() { return this.weight; }
    public LocalDate getDateSubmitted() { return this.dateSubmitted; }

    // * Setters
    public void setType(GradeType type) { this.type = type; }
    public void setScore(double score) { this.score = score; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setDateSubmitted(LocalDate dateSubmitted) { this.dateSubmitted = dateSubmitted; }
}
