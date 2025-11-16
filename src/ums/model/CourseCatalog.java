package ums.model;

// [IMPORT] Standard
import java.util.ArrayList;
import java.util.List;

// [IMPORT] Entities
import ums.model.entity.Course;
import ums.model.enums.Courses;

// [IMPORT] Enums
import ums.model.enums.Department;

public class CourseCatalog {
    private static final List<Course> allCourses = new ArrayList<>();

    static {
        // Populate courses from all enums
        for (Courses.CASCourse c : Courses.CASCourse.values())
            allCourses.add(new Course(c.getFullName(), c.getFullName(), "", 3, Department.CAS));

        for (Courses.CICTCourse c : Courses.CICTCourse.values())
            allCourses.add(new Course(c.getFullName(), c.getFullName(), "", 3, Department.CICT));

        for (Courses.CBMCourse c : Courses.CBMCourse.values())
            allCourses.add(new Course(c.getFullName(), c.getFullName(), "", 3, Department.CBM));

        // ... repeat for other departments
    }

    public static List<Course> getAllCourses() { return allCourses; }

    public static List<Course> getCoursesByDepartment(Department dept) {
        List<Course> filtered = new ArrayList<>();
        for (Course c : allCourses) {
            if (c.getDepartment() == dept) filtered.add(c);
        }
        return filtered;
    }

    public static Course getCourseByName(String name) {
        for (Course c : allCourses) {
            if (c.getTitle().equalsIgnoreCase(name)) return c;
        }
        return null;
    }
}
