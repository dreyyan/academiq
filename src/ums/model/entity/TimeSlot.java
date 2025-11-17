package ums.model.entity;

// [IMPORT] Standard
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeSlot {
    // * Attributes
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");

    // * Constructor (Parameterized)
    public TimeSlot(DayOfWeek day, LocalTime startTime, LocalTime endTime) {
        // ! [ERROR] Invalid time slot
        if (endTime.isBefore(startTime)) { throw new IllegalArgumentException("End time cannot be before start time."); }

        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // * Getters
    public DayOfWeek getDay() { return day; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }

    // * Setters
    public void setDay(DayOfWeek day) { this.day = day; }

    public void setStartTime(LocalTime startTime) {
        if (endTime != null && endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("Start time cannot be after end time.");
        } this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        if (startTime != null && endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End time cannot be before start time.");
        } this.endTime = endTime;
    }

    // [METHOD] Return a readable formatted string (e.g. "Mon 10:00 AM - 12:00 PM")
    @Override
    public String toString() {
        return String.format("%s %s - %s", day, startTime.format(TIME_FORMATTER), endTime.format(TIME_FORMATTER));
    }

    // [METHOD] Convert stringified time to TimeSlot
    public static TimeSlot fromString(String s) {
        if (s == null || s.isBlank()) return null;

        try {
            // Example input: "MON 10:00 AM - 12:00 PM"
            String[] parts = s.split(" ");
            if (parts.length < 4) return null;

            DayOfWeek day = DayOfWeek.valueOf(parts[0].toUpperCase());

            // Combine time strings (parts[1] + " " + parts[2] for start, parts[4] + " " + parts[5] for end)
            String startStr = parts[1] + " " + parts[2];
            String endStr = parts[4] + " " + parts[5];

            LocalTime startTime = LocalTime.parse(startStr, TIME_FORMATTER);
            LocalTime endTime = LocalTime.parse(endStr, TIME_FORMATTER);

            return new TimeSlot(day, startTime, endTime);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
