package ums.ui;

// [IMPORT] Standard
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ums.model.CourseCatalog;
import ums.model.GraduateStudent;
import ums.model.Student;
import ums.model.enums.Department;
import ums.model.UndergraduateStudent;
import ums.model.entity.Course;
import ums.model.entity.CourseOffering;
import ums.model.enums.AcademicStanding;
import ums.model.enums.Courses;
import ums.model.enums.Gender;
import ums.model.enums.GraduateProgram;
import ums.model.enums.YearLevel;
// [IMPORT] Utilities
import ums.ui.MenuOperation;
import ums.util.CSV;
import ums.util.Auth;
import ums.util.Logger;
import ums.util.Settings;
import ums.util.console.ConsoleAnimation;
import ums.util.console.ConsoleDisplay;
import ums.util.console.ConsoleInput;
import ums.util.console.ConsoleUI;
import ums.util.console.ConsoleFormatting;

public class MainMenu {
    static Scanner scanner = new Scanner(System.in);
    private static Student currentStudent;

    // * Menu operations
    static MenuOperation[] studentMenuOperations = {
        new MenuOperation(new String[]{"1"}, "Profile", () -> {
            try {
                displayStudentProfileMenu(currentStudent); // throws IOException
            } catch (IOException e) {
                e.printStackTrace();
            }
        }),
        new MenuOperation(new String[]{"2"}, "Academics", () -> {
            try {
                displayStudentAcademicsMenu(currentStudent); // throws IOException
            } catch (IOException e) {
                e.printStackTrace();
            }
        }),
        new MenuOperation(new String[]{"3"}, "Courses", () -> {
            try {
                displayStudentCoursesMenu(currentStudent); // throws IOException
            } catch (IOException e) {
                e.printStackTrace();
            }
        })
    };

    static MenuOperation[] studentCoursesMenuOperations = {
        new MenuOperation(new String[]{"1"}, "Enroll in Course Offering", () -> {
            try {
                displayStudentEnrollInCourseOfferingMenu(currentStudent); // throws IOException
            } catch (IOException e) {
                e.printStackTrace();
            }
        }),
        new MenuOperation(new String[]{"2"}, "Drop Course Offering", () -> {
            try {
                displayStudentDropCourseOfferingMenu(currentStudent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        })
    };
    
    // * UI: Student Sub-menu
    // [METHOD] Display student "Edit Profile" menu
    public static void displayEditStudentProfileMenu(Student student) throws IOException {
        if (student == null) {
            ConsoleDisplay.dialogBox("error", "Student record is null!");
            return;
        }

        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Student: Edit Profile");
        System.out.println();

        String[] profileFields = {
            "First Name", "Middle Name", "Last Name", "Date of Birth",
            "Gender", "Address", "Contact Number", "Email"
        };

        int[] yPositions = { 18, 20, 22, 24, 26, 28, 30, 32 };
        int[] xPositions = { 34, 35, 33, 37, 30, 31, 38, 29 };
        int maxLength = 32;

        // Pre-fill student info
        String[] currentValues = {
            student.getFirstName(),
            student.getMiddleName(),
            student.getLastName(),
            student.getDateOfBirth().toString(),
            student.getGender().toString(),
            student.getAddress(),
            student.getContactNumber(),
            student.getEmail()
        };

        // Display input boxes
        ConsoleUI.drawInputBoxes(60, profileFields);

        // Fill initial values
        for (int i = 0; i < profileFields.length; i++) {
            ConsoleUI.goTo(yPositions[i], xPositions[i]);
            System.out.print(currentValues[i]);
        }

        while (true) {
            String[] inputs = ConsoleInput.navigateInputs(yPositions, xPositions, maxLength);

            if (inputs == null) {
                ConsoleDisplay.dialogBox("info", "Edit canceled. No changes were made.");
                return;
            }

            // Only validate non-empty fields
            if (!inputs[7].isEmpty() && !Auth.isValidEmail(inputs[7])) {
                ConsoleDisplay.dialogBox("error", "Please enter a valid email address (e.g. example@domain.com).");
                ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
                continue;
            }

            if (!inputs[4].isEmpty() &&
                !inputs[4].equalsIgnoreCase("male") &&
                !inputs[4].equalsIgnoreCase("female")) {
                ConsoleDisplay.dialogBox("error", "Gender must be 'Male' or 'Female'.");
                ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
                continue;
            }

            // Update fields only if input is not empty
            if (!inputs[0].isEmpty()) student.setFirstName(inputs[0]);
            if (!inputs[1].isEmpty()) student.setMiddleName(inputs[1]);
            if (!inputs[2].isEmpty()) student.setLastName(inputs[2]);

            if (!inputs[3].isEmpty()) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    student.setDateOfBirth(LocalDate.parse(inputs[3], formatter));
                } catch (DateTimeParseException e) {
                    ConsoleDisplay.dialogBox("error", "Invalid date format! Use yyyy-MM-dd.");
                    ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
                    continue;
                }
            }

            if (!inputs[4].isEmpty()) {
                try {
                    student.setGender(Gender.valueOf(inputs[4].toUpperCase()));
                } catch (IllegalArgumentException e) {
                    ConsoleDisplay.dialogBox("error", "Gender must be 'Male' or 'Female'.");
                    ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
                    continue;
                }
            }

            if (!inputs[5].isEmpty()) student.setAddress(inputs[5]);
            if (!inputs[6].isEmpty()) student.setContactNumber(inputs[6]);
            if (!inputs[7].isEmpty()) student.setEmail(inputs[7]);

            CSV.updateStudentRecord(student);
            ConsoleDisplay.dialogBox("success", "Profile updated successfully!");
            break;
        }

        ConsoleInput.pressEnterToContinue();
        displayStudentMenu(currentStudent);
    }

    // [METHOD] Display student "Edit Academics" menu
    public static void displayEditStudentAcademicsMenu(Student student) throws IOException {
        if (student == null) {
            ConsoleDisplay.dialogBox("error", "Student record is null!");
            return;
        }

        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Student: Edit Academics");
        System.out.println();

        String[] academicFields = { "Department", "Course" };
        int[] yPositions = { 18, 20 };
        int[] xPositions = { 34, 31 };
        int maxLength = 82;

        // Pre-fill current values
        String[] currentValues = {
            student.getDepartment() != null ? student.getDepartment().toString() : "",
            student.getCourse() != null ? student.getCourse().getTitle() : ""
        };

        // Display input boxes
        ConsoleUI.drawInputBoxes(60, academicFields);

        // Fill initial values
        for (int i = 0; i < academicFields.length; i++) {
            ConsoleUI.goTo(yPositions[i], xPositions[i]);
            System.out.print(currentValues[i]);
        }

        while (true) {
            String[] inputs = ConsoleInput.navigateInputs(yPositions, xPositions, maxLength);

            // Cancel edit
            if (inputs == null) {
                ConsoleDisplay.dialogBox("info", "Edit canceled. No changes were made.");
                return;
            }

            // --- DEPARTMENT ---
            Department newDept = null;
            if (!inputs[0].isEmpty()) {
                newDept = Department.fromCodeOrFullName(inputs[0]);
                if (newDept == Department.UNASSIGNED) {
                    ConsoleDisplay.dialogBox("error", "Invalid department. Please try again.");
                    ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
                    continue;
                }
            }

            // --- COURSE ---
            Course newCourse = null;
            if (!inputs[1].isEmpty()) {
                String courseInput = inputs[1].trim();

                // Map course input to Course object using department
                Department deptToUse = newDept != null ? newDept : student.getDepartment();
                switch (deptToUse) {
                    case CAS -> newCourse = Course.mapCourseInput(courseInput, Courses.CASCourse.values(), Department.CAS);
                    case CICT -> newCourse = Course.mapCourseInput(courseInput, Courses.CICTCourse.values(), Department.CICT);
                    case CBM -> newCourse = Course.mapCourseInput(courseInput, Courses.CBMCourse.values(), Department.CBM);
                    case COP -> newCourse = Course.mapCourseInput(courseInput, Courses.COPCourse.values(), Department.COP);
                    case COM -> newCourse = Course.mapCourseInput(courseInput, Courses.COMCourse.values(), Department.COM);
                    case COE -> newCourse = Course.mapCourseInput(courseInput, Courses.COECourse.values(), Department.COE);
                    case CON -> newCourse = Course.mapCourseInput(courseInput, Courses.CONCourse.values(), Department.CON);
                    case COC -> newCourse = Course.mapCourseInput(courseInput, Courses.COCCourse.values(), Department.COC);
                    case COL -> newCourse = Course.mapCourseInput(courseInput, Courses.COLCourse.values(), Department.COL);
                    case COD -> newCourse = Course.mapCourseInput(courseInput, Courses.CODCourse.values(), Department.COD);
                    case ILS -> newCourse = Course.mapCourseInput(courseInput, Courses.ILSCourse.values(), Department.ILS);
                }

                if (newCourse == null) {
                    ConsoleDisplay.dialogBox("error", "Invalid course for the selected department.");
                    ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
                    continue;
                }
            }

            // --- UPDATE STUDENT ---
            if (newDept != null) student.setDepartment(newDept);
            if (newCourse != null) student.setCourse(newCourse);

            CSV.updateStudentRecord(student);
            ConsoleDisplay.dialogBox("success", "Academics updated successfully!");
            break;
        }

        ConsoleInput.pressEnterToContinue();
        displayStudentMenu(student);
    }

    // [METHOD] Display student "Enroll in Course Offering" menu
    public static void displayStudentEnrollInCourseOfferingMenu(Student student) throws IOException {
        if (student == null) {
            ConsoleDisplay.dialogBox("error", "Student record is null!");
            return;
        }

        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Student: Enroll in Course Offering");
        System.out.println();

        List<CourseOffering> availableOfferings = CSV.readCourseOfferings();
        if (availableOfferings.isEmpty()) {
            ConsoleDisplay.dialogBox("info", "No course offerings available at this time.");
            ConsoleInput.pressEnterToContinue();
            displayStudentMenu(student);
            return;
        }

        int tableWidth = 90; // total width of table
        String border = "=".repeat(tableWidth);
        String separator = "-".repeat(tableWidth);

        while (true) {
            // Table Header
            ConsoleUI.moveCursor(5); ConsoleInput.printCentered(border, Settings.CONSOLE_WIDTH - 10);
            ConsoleUI.moveCursor(5); ConsoleInput.printCentered("Available Course Offerings", Settings.CONSOLE_WIDTH - 10);
            ConsoleUI.moveCursor(5); ConsoleInput.printCentered(separator, Settings.CONSOLE_WIDTH - 10);

            // Column headers
            String header = String.format("| %-3s | %-12s | %-12s | %-4s | %-25s | %-9s |",
                    "No", "Course", "Semester", "Year", "Instructor", "Seats");
            ConsoleUI.moveCursor(5); ConsoleInput.printCentered(header, Settings.CONSOLE_WIDTH - 10);
            ConsoleUI.moveCursor(5); ConsoleInput.printCentered(separator, Settings.CONSOLE_WIDTH - 10);

            // Table rows
            for (int i = 0; i < availableOfferings.size(); i++) {
                CourseOffering offering = availableOfferings.get(i);
                String instructorName = (offering.getInstructor() != null)
                        ? offering.getInstructor().getFullName()
                        : "TBD";

                String courseTitle = offering.getCourse().getTitle();
                if (courseTitle.length() > 12) courseTitle = courseTitle.substring(0, 12);
                if (instructorName.length() > 25) instructorName = instructorName.substring(0, 25);

                String body = String.format("| %-3d | %-12s | %-12s | %-4s | %-25s | %3d/%-5d |",
                        i + 1,
                        courseTitle,
                        offering.getSemester(),
                        offering.getYear(),
                        instructorName,
                        offering.getEnrolledCount(),
                        offering.getCapacity());

                ConsoleUI.moveCursor(5); ConsoleInput.printCentered(body, Settings.CONSOLE_WIDTH - 10, false);
            }

            ConsoleUI.moveCursor(5); ConsoleInput.printCentered(border, Settings.CONSOLE_WIDTH - 10, true);

            ConsoleUI.drawInputBoxes(40, "Course Offering No. to Enroll");

            int[] yPositions = { 27 }; // y coordinates of each input line
            int[] xPositions = { 63 }; // cursor start position
            int maxLength = 32;

            String[] inputArray;

            // This loop runs indefinitely until user enters valid credentials
            while (true) {
                inputArray = ConsoleInput.navigateInputs(yPositions, xPositions, maxLength);

                if (inputArray == null) {
                    displayStudentMenu(student);
                    return;
                }

                String inputStr = inputArray[0].trim();
                int choice;
                try {
                    choice = Integer.parseInt(inputStr);
                } catch (NumberFormatException e) {
                    ConsoleDisplay.dialogBox("error", "Invalid input. Please enter a number.");
                    continue;
                }

                if (choice == 0) return; // go back
                if (choice < 1 || choice > availableOfferings.size()) {
                    ConsoleDisplay.dialogBox("error", "Invalid choice. Please try again.");
                    continue;
                }

                // Enroll student
                CourseOffering selected = availableOfferings.get(choice - 1);

                // Update in-memory student object
                student.enrollInOffering(selected);

                // Update offering seats
                CSV.updateCourseOffering(selected);

                // Save enrollment record
                String[] enrollmentRow = new String[] {
                    student.getStudentId(),
                    selected.getOfferingId(),
                    LocalDate.now().toString()
                };
                CSV.appendRow(Settings.ENROLLMENTS_FILE, enrollmentRow);
                break;
            }
            ConsoleInput.pressEnterToContinue();
            displayStudentMenu(student);
        }
    }

    // [METHOD] Display student "Drop Course Offering" menu
    public static void displayStudentDropCourseOfferingMenu(Student student) throws IOException {
        if (student == null) {
            ConsoleDisplay.dialogBox("error", "Student record is null!");
            return;
        }

        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Student: Drop Course Offering");
        System.out.println();

        List<CourseOffering> enrolledCourses = student.getEnrolledCourses();
        if (enrolledCourses.isEmpty()) {
            ConsoleDisplay.dialogBox("info", "You are not enrolled in any course offerings.");
            ConsoleInput.pressEnterToContinue();
            displayStudentMenu(student);
            return;
        }

        while (true) {
            ConsoleFormatting.displayFormat(Settings.CONSOLE_WIDTH, '=', true);
            System.out.println("Enrolled Course Offerings:");
            ConsoleFormatting.displayFormat(Settings.CONSOLE_WIDTH, '-', false);

            // Display enrolled offerings
            for (int i = 0; i < enrolledCourses.size(); i++) {
                CourseOffering offering = enrolledCourses.get(i);
                System.out.printf("[%d] %s | %s | %s | Instructor: %s\n",
                        i + 1,
                        offering.getCourse().getTitle(),
                        offering.getSemester(),
                        offering.getYear(),
                        offering.getInstructor().getFullName()
                );
            }

            ConsoleFormatting.displayFormat(Settings.CONSOLE_WIDTH, '=', true);
            System.out.println("[0] Go Back");

            int choice = ConsoleInput.getInt("Enter the number of the course offering to drop: ");
            if (choice == 0) return; // go back
            if (choice < 1 || choice > enrolledCourses.size()) {
                ConsoleDisplay.dialogBox("error", "Invalid choice. Please try again.");
                continue;
            }

            CourseOffering selected = enrolledCourses.get(choice - 1);

            // Drop course
            student.dropOffering(selected);
            CSV.updateCourseOffering(selected); // update enrolled count in CSV
            CSV.appendRow(Settings.STUDENTS_FILE, student.toCSVRow());
            ConsoleDisplay.dialogBox("success", "Successfully dropped " + selected.getCourse().getTitle() + "!");
            ConsoleInput.pressEnterToContinue();
            return;
        }
    }

    // * UI: Student Menu
    // [METHOD] Display "Student" menu
    public static void displayStudentMenu(Student student) throws IOException {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(16, 0);
        ConsoleDisplay.displayHeaderSubtitle("Student Menu");

        // Display top border
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("=".repeat(16), Settings.CONSOLE_WIDTH - 6);
        
        // Display operations
        for (MenuOperation operation : studentMenuOperations) {
            ConsoleUI.moveCursor(42);
            ConsoleAnimation.lineDelayAnimation('[' + operation.inputKeys[0] + "] " + operation.getDisplayName(), 50); System.out.println();
        }

        // Display bottom border
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("=".repeat(16), Settings.CONSOLE_WIDTH - 6);

        // Display ASCII art of school
        ConsoleUI.moveCursor(42);
        ConsoleDisplay.displaySchool();

        // This loop runs indefinitely until the user enters a valid choice
        while (true) {
            // Prompt user to enter choice
            int key = ConsoleInput.readKey();

            // Handle keypress
            if (key == Settings.ESC_KEY) {  
                displayLoginScreen();
            } else if (key == '1') {
                displayStudentProfileMenu(student);
            } else if (key == '2') {
                displayStudentAcademicsMenu(student);
            } else if (key == '3') {
                displayStudentCoursesMenu(student);
            } else { continue; }
            break;
        }
    }



    // [METHOD] Display student "Profile" menu
    public static void displayStudentProfileMenu(Student student) throws IOException {
        // ! [ERROR] No student record
        if (student == null) {
            ConsoleDisplay.dialogBox("error", "Student record is null!");
            return;
        }

        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Student: Profile");
        System.out.println();

        String[] profileFields = {
            "First Name", "Middle Name", "Last Name", "Date of Birth",
            "Gender", "Address", "Contact Number", "Email"
        };

        int[] yPositions = { 18, 20, 22, 24, 26, 28, 30, 32 }; // y coordinates of each input line
        int[] xPositions = { 34, 35, 33, 37, 30, 31, 38, 29 }; // cursor start position
        int maxLength = 32;

        // Display profile information
        ConsoleUI.drawInputBoxes(60, profileFields);
        ConsoleUI.displayInputFields(student.getProfileInformation(), yPositions, xPositions, maxLength);

        // Prompt user to press [ENTER] to continue
        ConsoleUI.goTo(Settings.CONSOLE_HEIGHT - 3, 0);
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("[ESC] Back  |  [E] Edit", Settings.CONSOLE_WIDTH - 3);

        // This loop runs indefinitely until the user enters a valid choice
        while (true) {
            int key = ConsoleInput.readKey();

            // Handle keypress
            if (key == Settings.ESC_KEY) {  
                break; // Return to student menu
            } else if (key == 'E' || key == 'e') {
                displayEditStudentProfileMenu(student);
                continue; // Redisplay updated data
            } else { continue; }
        }

        // Return to student menu
        displayStudentMenu(currentStudent);
    }

    // [METHOD] Display student "Academics" menu
    public static void displayStudentAcademicsMenu(Student student) throws IOException {
        // ! [ERROR] No student record
        if (student == null) {
            ConsoleDisplay.dialogBox("error", "Student record is null!");
            return;
        }

        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Student: Academics");
        System.out.println();

        String[] academicsFields = {
            "Department", "Course"
        };

        int[] yPositions = { 18, 20 }; // y coordinates of each input line
        int[] xPositions = { 34, 31 }; // cursor start position
        int maxLength = 62;

        // Display profile information
        ConsoleUI.drawInputBoxes(60, academicsFields);
        ConsoleUI.displayInputFields(student.getStudentCoursesInformation(), yPositions, xPositions, maxLength);

        // Prompt user to press [ESC] to return or [E] to edit
        ConsoleUI.goTo(Settings.CONSOLE_HEIGHT - 3, 0);
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("[ESC] Back  |  [E] Edit", Settings.CONSOLE_WIDTH - 3);

        // This loop runs indefinitely until the user enters a valid choice
        while (true) {
            int key = ConsoleInput.readKey();

            // Handle keypress
            if (key == Settings.ESC_KEY) {  
                break; // Return to student menu
            } else if (key == 'E' || key == 'e') {
                displayEditStudentAcademicsMenu(student);
            } else { continue; }
        }

        // Return to student menu
        displayStudentMenu(currentStudent);
    }

    // [METHOD] Display student "Courses" menu
    public static void displayStudentCoursesMenu(Student student) throws IOException {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(16, 0);
        ConsoleDisplay.displayHeaderSubtitle("Student: Courses");

        // Display top border
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("=".repeat(28), Settings.CONSOLE_WIDTH - 6);

        // Display operations
        for (MenuOperation operation : studentCoursesMenuOperations) {
            ConsoleUI.moveCursor(36);
            ConsoleAnimation.lineDelayAnimation('[' + operation.inputKeys[0] + "] " + operation.getDisplayName(), 50); System.out.println();
        }

        // Display bottom border
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("=".repeat(28), Settings.CONSOLE_WIDTH - 6);

        ConsoleUI.goTo(Settings.CONSOLE_HEIGHT - 3, 0);
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("[ESC] Back", Settings.CONSOLE_WIDTH - 3);

        // This loop runs indefinitely until the user enters a valid choice
        while (true) {
            int key = ConsoleInput.readKey();

            // Handle keypress
            if (key == Settings.ESC_KEY) {
                displayStudentMenu(student);
                break; // Return to student menu
            } else if (key == '1') {
                displayStudentEnrollInCourseOfferingMenu(student);
            } else if (key == 2) {
                displayStudentDropCourseOfferingMenu(student);
            } else { continue; }
        }
    }

    // * UI: Faculty Menu
    // [METHOD] Display "Faculty" menu
    public static void displayFacultyMenu() {

    }

    // * UI: User Authentication
    public static void displaySetupInformationScreen(String email, String userType) throws IOException {
        // Display UI
        ConsoleUI.clearScreen();
        ConsoleDisplay.displayBorder(2, 3);
        ConsoleUI.goTo(4, 0);
        ConsoleDisplay.displayHeaderSubtitle("Setup Information");

        ConsoleUI.goTo(6, 0);
        ConsoleUI.moveCursor(3); ConsoleInput.printCentered("Please fill out all personal information fields to proceed.", Settings.CONSOLE_WIDTH - 3, true);

        // Fields for all students
        String[] fields = {
            "First Name",
            "Middle Name",
            "Last Name",
            "Date of Birth (MM-dd-YYYY)",
            "Gender (Male/Female/Other)",
            "Address",
            "Contact Number",
            "Student ID",
            "Enrollment Date (MM-dd-YYYY)",
            "Department",
            "Course"
        };
        
        ConsoleUI.drawInputBoxes(40, fields);

        int[] yPositions = {9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29};
        int[] xPositions = {44, 45, 43, 60, 60, 41, 48, 44, 62, 44, 40};
        int maxLength = 60;

        String[] inputs;

        while (true) {
            inputs = ConsoleInput.navigateInputs(yPositions, xPositions, maxLength);

            if (inputs == null) {
                displayLoginScreen(); // go back if ESC pressed
                return;
            }

            // Extract values
            String firstName  = inputs[0];
            String middleName = inputs[1];
            String lastName   = inputs[2];
            String dobStr     = inputs[3];
            String genderStr  = inputs[4];
            String address    = inputs[5];
            String contact    = inputs[6];
            String studentId  = inputs[7];
            String enrollStr  = inputs[8];
            String deptName   = inputs[9];
            String courseName = inputs[10];

            // Validate required fields
            if (firstName.isBlank() || lastName.isBlank() || dobStr.isBlank() || genderStr.isBlank() || studentId.isBlank() || enrollStr.isBlank()) {
                ConsoleDisplay.dialogBox("error", "Please fill in all required fields!");
                ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
                continue;
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy"); // Date formatter
                LocalDate dob = LocalDate.parse(dobStr, formatter);
                LocalDate enrollmentDate = LocalDate.parse(enrollStr, formatter);

                Gender gender = Gender.valueOf(genderStr.toUpperCase());

                Department dept = Department.fromCodeOrFullName(deptName);
                Course course = Course.fromCodeOrFullName(courseName);

                Student student;

                if (userType.equalsIgnoreCase("freshman") ||
                    userType.equalsIgnoreCase("sophomore") ||
                    userType.equalsIgnoreCase("junior") ||
                    userType.equalsIgnoreCase("senior")) {

                    YearLevel yl = YearLevel.valueOf(userType.toUpperCase());
                    student = new UndergraduateStudent(
                        firstName, middleName, lastName, dob, gender,
                        address, contact, email,
                        studentId, enrollmentDate, dept, course,
                        AcademicStanding.GOOD, 0.0, 0, new ArrayList<>(), yl
                    );

                } else { // Graduate
                    GraduateProgram program = GraduateProgram.valueOf(userType.toUpperCase());
                    student = new GraduateStudent(
                        firstName, middleName, lastName, dob, gender,
                        address, contact, email,
                        studentId, enrollmentDate, dept, course,
                        AcademicStanding.GOOD, 0.0, 0, new ArrayList<>(),
                        program, null, null
                    );
                }

                // Save to CSV
                CSV.appendRow(Settings.STUDENTS_FILE, student.toCSVRow());

                // Set current student
                currentStudent = student;

                // Display success message
                ConsoleDisplay.dialogBox("success", "Student information saved successfully!");

                // Prompt user to press [ENTER] before navigating to next screen
                ConsoleInput.pressEnterToContinue();
                displayStudentMenu(student);
                return;

            } catch (Exception e) {
                ConsoleDisplay.dialogBox("error", "Invalid input: " + e.getMessage());
                ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
            }
        }
    }

    public static void displayLoginScreen() throws IOException {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("LOGIN");

        ConsoleUI.drawInputBoxes(40, "Email", "Password");

        int[] yPositions = { 17, 19 }; // y coordinates of each input line
        int[] xPositions = { 39, 42 }; // cursor start position
        int maxLength = 32;

        String[] inputs; // To store user input
        String email;
        String password;

        // This loop runs indefinitely until user enters valid credentials
        while (true) {
            inputs = ConsoleInput.navigateInputs(yPositions, xPositions, maxLength);

            // If user pressed ESC, navigate to 'Register' screen
            if (inputs == null) {
                displayRegisterScreen();
                return;
            }

            email = inputs[0];
            password = inputs[1];

            // ! [ERROR] Invalid email input
            if (!Auth.isValidEmail(inputs[0])) {
                ConsoleDisplay.dialogBox("error", "Please enter a valid email address (e.g. example@domain.com).");
            } else if (!Auth.isValidPassword(password)) {
                // ! [ERROR] Invalid password
                ConsoleDisplay.dialogBox("error", "Password must be 8–20 characters long and include at least one number.");
            } else if (!Auth.accountExists(email)) {
                // ! [ERROR] Non-existing account
                ConsoleDisplay.dialogBox("error", "Account not found. Please register first.");
            } else if (!Auth.matchingUsernamePassword(email, password)) {
                    // ! [ERROR] Non-matching credentials
                    ConsoleDisplay.dialogBox("error", "Incorrect email or password. Try again.");
            } else break;

            // Clear input fields
            ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
        }
        
    // Get user type
    String userType = Auth.getUserType(email, password);
    if (userType == null) {
        ConsoleDisplay.dialogBox("error", "Unable to determine user type. Login failed.");
        return;
    }

    userType = userType.toLowerCase();

    ConsoleDisplay.dialogBox("success", "Login successful! Press [Enter] to continue...");
    while (!ConsoleInput.getString("").equals("")) {}

    // Redirect based on user type
    switch (userType) {
        case "freshman":
        case "sophomore":
        case "junior":
        case "senior":
            Student student = Auth.getUndergraduateByEmail(email);
            if (student == null) {
                // First-time login - redirect to setup information screen
                displaySetupInformationScreen(email, userType);
                return;
            }

            currentStudent = student; // Set current student
            displayStudentMenu(student);
            break;

        case "master":
        case "phd":
            Student gradStudent = Auth.getGraduateByEmail(email);
            if (gradStudent == null) {
                // First-time login - redirect to setup information screen
                displaySetupInformationScreen(email, userType);
                return;
            }
            displayStudentMenu(gradStudent);
            break;

        case "academic staff":
        case "teacher":
        case "librarian":
        case "administrator":
            displayFacultyMenu();
            break;

        default:
            ConsoleDisplay.dialogBox("error", "Unknown user type: " + userType);
            break;
        }
    }

    public static void displayRegisterScreen() throws IOException {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("REGISTER");

        ConsoleUI.drawInputBoxes(40, "Email", "Password", "Confirm Password", "User Type");

        int[] yPositions = { 17, 19, 21, 23 }; // y coordinates of each input line
        int[] xPositions = { 39, 42, 50, 43 }; // cursor start position
        int maxLength = 32;

        String[] inputs; // To store user input
        String email, password, confirmPassword, userType;

        // This loop runs indefinitely until user enters valid credentials
        while (true) {
            inputs = ConsoleInput.navigateInputs(yPositions, xPositions, maxLength);

            // If user pressed ESC, navigate to 'Register' screen
            if (inputs == null) {
                displayLoginScreen();
                return;
            }

            email = inputs[0];
            password = inputs[1];
            confirmPassword = inputs[2];
            userType = inputs[3];

            // ! [ERROR] Invalid email input
            if (!Auth.isValidEmail(email)) {
                ConsoleDisplay.dialogBox("error", "Please enter a valid email address (e.g. example@domain.com).");
            } else if (!Auth.isValidPassword(password)) {
                // ! [ERROR] Invalid password input
                ConsoleDisplay.dialogBox("error", "Password must be 8–20 characters long and at least one number.");
            } else if (!password.equals(confirmPassword)) {
                // ! [ERROR] Mismatching password
                ConsoleDisplay.dialogBox("error", "Passwords do not match.");
            } else if (!Auth.isValidUserType(userType)) {
                // ! [ERROR] Invalid user type
                ConsoleDisplay.dialogBox("error", "Invalid user type. Please enter a valid student or faculty type.");
            } else break;

            // Clear input fields
            ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
        }

        // If successful registration, save credentials to backend
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{email, password, userType});
        
        CSV.writeCSV(Settings.CREDENTIALS_FILE, data);

        // Display success message
        ConsoleDisplay.dialogBox("success", "Registration successful! Press [Enter] to go to login...");

        // This loop runs indefinitely until the user presses the 'Enter' key
        while (true) {
            String input = ConsoleInput.getString("");
            if (input.equals("")) break;
        }

        // Redirect to login
        displayLoginScreen();
    }
}