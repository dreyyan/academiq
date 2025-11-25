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
    // * Attrirbute
    private static final List<Course> allCourses = new ArrayList<>();

    static {
        // Populate courses for all deparatments
        for (Courses.CASCourse c : Courses.CASCourse.values())
            allCourses.add(new Course(c.getFullName(), c.getFullName(), "", 3, Department.CAS));

        for (Courses.CICTCourse c : Courses.CICTCourse.values())
            allCourses.add(new Course(c.getFullName(), c.getFullName(), "", 3, Department.CICT));

        for (Courses.CBMCourse c : Courses.CBMCourse.values())
            allCourses.add(new Course(c.getFullName(), c.getFullName(), "", 3, Department.CBM));

        for (Courses.COPCourse c : Courses.COPCourse.values())
            allCourses.add(new Course(c.getFullName(), c.getFullName(), "", 3, Department.COP));

        for (Courses.COMCourse c : Courses.COMCourse.values())
            allCourses.add(new Course(c.getFullName(), c.getFullName(), "", 3, Department.COM));

        for (Courses.COECourse c : Courses.COECourse.values())
            allCourses.add(new Course(c.getFullName(), c.getFullName(), "", 3, Department.COE));

        for (Courses.CONCourse c : Courses.CONCourse.values())
            allCourses.add(new Course(c.getFullName(), c.getFullName(), "", 3, Department.CON));

        for (Courses.COCCourse c : Courses.COCCourse.values())
            allCourses.add(new Course(c.getFullName(), c.getFullName(), "", 3, Department.COC));

        for (Courses.COLCourse c : Courses.COLCourse.values())
            allCourses.add(new Course(c.getFullName(), c.getFullName(), "", 3, Department.COL));

        for (Courses.CODCourse c : Courses.CODCourse.values())
            allCourses.add(new Course(c.getFullName(), c.getFullName(), "", 3, Department.COD));

        for (Courses.ILSCourse c : Courses.ILSCourse.values())
            allCourses.add(new Course(c.getFullName(), c.getFullName(), "", 3, Department.ILS));
    }

    // * Getters
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

    public static Course getCourseByCode(String code) {
        if (code == null || code.isBlank()) return null;

        for (Course c : getAllCourses()) { // assuming getAllCourses() returns List<Course>
            if (code.equalsIgnoreCase(c.getCourseCode())) {
                return c;
            }
        }
        return null;
    }
}
