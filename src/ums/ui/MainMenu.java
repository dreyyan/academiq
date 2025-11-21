package ums.ui;

// [IMPORT] Standard
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

// [IMPORT] Models
import ums.model.GraduateStudent;
import ums.model.AcademicStaff;
import ums.model.NonAcademicStaff;
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
import ums.util.CSV;
import ums.util.Auth;
import ums.util.Settings;
import ums.util.console.ConsoleAnimation;
import ums.util.console.ConsoleDisplay;
import ums.util.console.ConsoleInput;
import ums.util.console.ConsoleUI;
import ums.util.console.ConsoleFormatting;

// [IMPORT] Enums
import ums.model.enums.FacultyRank;


public class MainMenu {
    private static Student currentStudent;
    private static AcademicStaff currentAcademicStaff;
    private static NonAcademicStaff currentNonAcademicStaff;

    // * Menu operations
    // [OPERATION] Student Menu
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

    // [OPERATION] Student: Courses Menu
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

    // [OPERATION] NonAcademicStaff Menu
    static MenuOperation[] NonAcademicStaffMenuOperations = {
        new MenuOperation(new String[]{"1"}, "Add Student", () -> {
            try {
                addStudentMenu(); // implement this
            } catch (IOException e) {
                e.printStackTrace();
            }
        }),
        new MenuOperation(new String[]{"2"}, "Add Faculty", () -> {
            try {
                addFacultyMenu(); // implement this
            } catch (IOException e) {
                e.printStackTrace();
            }
        }),
        new MenuOperation(new String[]{"3"}, "View Students", () -> {
            try {
                viewAllStudents(); // implement this
            } catch (IOException e) {
                e.printStackTrace();
            }
        }),
        new MenuOperation(new String[]{"4"}, "View Faculty", () -> {
            try {
                viewAllFaculty(); // implement this
            } catch (IOException e) {
                e.printStackTrace();
            }
        }),
        new MenuOperation(new String[]{"5"}, "Remove Student", () -> {
            try {
                removeStudentMenu(); // implement this
            } catch (IOException e) {
                e.printStackTrace();
            }
        }),
        new MenuOperation(new String[]{"6"}, "Remove Faculty", () -> {
            try {
                removeFacultyMenu(); // implement this
            } catch (IOException e) {
                e.printStackTrace();
            }
        }),
        new MenuOperation(new String[]{"ESC"}, "Logout", () -> {
            try {
                displayLoginScreen(); // go back to login
            } catch (IOException e) {
                e.printStackTrace();
            }
        })
    };
    
    // * UI: Student Sub-menu
    // [METHOD] Display student "Enroll in Course Offering" menu
    public static void displayStudentEnrollInCourseOfferingMenu(Student student) throws IOException {
        // ! [ERROR] Null student record
        if (student == null) {
            ConsoleDisplay.dialogBox("error", "Student record is null!");
            return;
        }

        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Student: Enroll in Course Offering");
        System.out.println();

        // Get available course offerings from CSV file
        List<CourseOffering> availableOfferings = CSV.readCourseOfferings();

        // ? [INFO] No course offerings
        if (availableOfferings.isEmpty()) {
            ConsoleDisplay.dialogBox("info", "No course offerings available at this time.");

            // Prompt user to press [ENTER] before navigating back to 'Student Menu'
            ConsoleInput.pressEnterToContinue();
            displayStudentCoursesMenu(student);
            return;
        }

        // Course offerings table setup
        int tableWidth = 90;
        String border = "=".repeat(tableWidth);
        String separator = "-".repeat(tableWidth);

        // Display table for course offerings
        ConsoleUI.moveCursor(5); ConsoleInput.printCentered(border, Settings.CONSOLE_WIDTH - 10);
        ConsoleUI.moveCursor(5); ConsoleInput.printCentered("Available Course Offerings", Settings.CONSOLE_WIDTH - 10);
        ConsoleUI.moveCursor(5); ConsoleInput.printCentered(separator, Settings.CONSOLE_WIDTH - 10);

        // Display table column headers
        String header = String.format("| %-3s | %-12s | %-12s | %-4s | %-25s | %-9s |",
                "No", "Course", "Semester", "Year", "Instructor", "Seats");
        ConsoleUI.moveCursor(5); ConsoleInput.printCentered(header, Settings.CONSOLE_WIDTH - 10);
        ConsoleUI.moveCursor(5); ConsoleInput.printCentered(separator, Settings.CONSOLE_WIDTH - 10);

        // Display table rows
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

        // Display input fields
        ConsoleUI.drawInputFields(36, "Course Offering No. to Enroll");

        // Setup input fields
        int[] yPositions = { 27 };
        int[] xPositions = { 66 };
        int maxLength = 2;

        // This loop runs indefinitely until user enters valid credentials
        while (true) {
            // Prompt user to fill out input fields
            String[] input = ConsoleInput.captureFormInputs(yPositions, xPositions, maxLength);

            // ? [INFO] Cancel form editing
            if (input == null) {
                ConsoleDisplay.dialogBox("info", "Edit canceled. No changes were made.");

                ConsoleInput.pressEnterToContinue();
                displayStudentCoursesMenu(student);
                return;
            }

            // Validate input field
            String inputStr = input[0].trim();
            int choice;
            try {
                choice = Integer.parseInt(inputStr);
            } catch (NumberFormatException e) {
                // ! [ERROR] Invalid input
                ConsoleDisplay.dialogBox("error", "Invalid input. Please enter a number.");
                continue;
            }

            // ! [ERROR] Out-of-range choice
            if (choice < 1 || choice > availableOfferings.size()) {
                ConsoleDisplay.dialogBox("error", "Invalid choice. Please try again.");
                continue;
            }

            // Enroll student
            CourseOffering selected = availableOfferings.get(choice - 1);

            // Check if student is already enrolled
            if (CSV.isStudentEnrolled(student.getStudentId(), selected.getOfferingId())) {
                ConsoleDisplay.dialogBox("error", "You are already enrolled in this course offering!");
                continue; // Ask for input again
            }

            // Safe to enroll
            student.enrollInOffering(selected);
            CSV.updateCourseOffering(selected);

            // Append to CSV
            String[] enrollmentRow = new String[] {
                student.getStudentId(),
                selected.getOfferingId(),
                LocalDate.now().toString()
            };
            CSV.appendRow(Settings.ENROLLMENTS_FILE, enrollmentRow);

            break;
        }

        // Prompt user to press [ENTER] before navigating back to 'Student: Courses Menu'
        ConsoleInput.pressEnterToContinue();
        displayStudentCoursesMenu(student);
    }

    // [METHOD] Display student "Drop Course Offering" menu
    public static void displayStudentDropCourseOfferingMenu(Student student) throws IOException {
        // ! [ERROR] Null student record
        if (student == null) {
            ConsoleDisplay.dialogBox("error", "Student record is null!");
            return;
        }

        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Student: Drop Course Offering");
        System.out.println();

        // Get available course offerings from CSV file
        List<CourseOffering> enrolledCourses = student.getEnrolledCourses();

        // ? [INFO] No course offerings
        if (enrolledCourses.isEmpty()) {
            ConsoleDisplay.dialogBox("info", "You are not enrolled in any course offerings.");

            // Prompt user to press [ENTER] before navigating back to 'Student Menu'
            ConsoleInput.pressEnterToContinue();
            displayStudentCoursesMenu(student);
            return;
        }

        // Course offerings table setup
        int tableWidth = 90;
        String border = "=".repeat(tableWidth);
        String separator = "-".repeat(tableWidth);

        // Display table for course offerings enrolled
        ConsoleUI.moveCursor(5); ConsoleInput.printCentered(border, Settings.CONSOLE_WIDTH - 10);
        ConsoleUI.moveCursor(5); ConsoleInput.printCentered("Enrolled Course Offerings", Settings.CONSOLE_WIDTH - 10);
        ConsoleUI.moveCursor(5); ConsoleInput.printCentered(separator, Settings.CONSOLE_WIDTH - 10);

        // Display table column headers
        String header = String.format("| %-3s | %-12s | %-12s | %-4s | %-25s | %-9s |",
                "No", "Course", "Semester", "Year", "Instructor", "Seats");
        ConsoleUI.moveCursor(5); ConsoleInput.printCentered(header, Settings.CONSOLE_WIDTH - 10);
        ConsoleUI.moveCursor(5); ConsoleInput.printCentered(separator, Settings.CONSOLE_WIDTH - 10);

        // Display table rows
        for (int i = 0; i < enrolledCourses.size(); i++) {
            CourseOffering offering = enrolledCourses.get(i);

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
                    offering.getCapacity()
            );

            ConsoleUI.moveCursor(5);
            ConsoleInput.printCentered(body, Settings.CONSOLE_WIDTH - 10, false);
        }

        // Bottom border
        ConsoleUI.moveCursor(5); 
        ConsoleInput.printCentered(border, Settings.CONSOLE_WIDTH - 10, true);

        // Display input fields
        ConsoleUI.goTo(Settings.CONSOLE_HEIGHT - 8, 0);
        ConsoleUI.drawInputFields(34, "Course Offering No. to drop");

        // Setup input fields
        int[] yPositions = { 33 };
        int[] xPositions = { 65 };
        int maxLength = 2;

        // This loop runs indefinitely until user enters valid credentials
        while (true) {
            // Prompt user to fill out input field
            String[] inputArray = ConsoleInput.captureFormInputs(yPositions, xPositions, maxLength);

            // ? [INFO] Cancel form editing
            if (inputArray == null) {
                ConsoleDisplay.dialogBox("info", "Drop canceled. No changes were made.");

                ConsoleInput.pressEnterToContinue();
                displayStudentCoursesMenu(student);
                return;
            }

            // Validate input field
            String inputStr = inputArray[0].trim();
            int choice;
            try {
                choice = Integer.parseInt(inputStr);
            } catch (NumberFormatException e) {
                // ! [ERROR] Invalid input
                ConsoleDisplay.dialogBox("error", "Invalid input. Please enter a number.");
                continue;
            }

            // ! [ERROR] Out-of-range choice
            if (choice < 1 || choice > enrolledCourses.size()) {
                ConsoleDisplay.dialogBox("error", "Invalid choice. Please try again.");
                continue;
            }

            CourseOffering selected = enrolledCourses.get(choice - 1);
            
            // Drop course offering
            ConsoleUI.goTo(Settings.CONSOLE_HEIGHT - 5, 0);
            student.dropOffering(selected);

            // Update course offering from course offering's CSV
            CSV.updateCourseOffering(selected);

            // Remove enrollment from CSV
            CSV.removeEnrollment(student.getStudentId(), selected.getOfferingId());

            // Update student's record
            CSV.updateStudentRecord(student);
            break;
        }

        // Prompt user to press [ENTER] before navigating back to 'Student: Courses Menu'
        ConsoleInput.pressEnterToContinue();
        displayStudentCoursesMenu(student);
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
        // ! [ERROR] Null student record
        if (student == null) {
            ConsoleDisplay.dialogBox("error", "Student record is null!");
            return;
        }

        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Student: Profile");
        System.out.println();

        // Setup input fields
        String[] profileFields = {
            "First Name",
            "Middle Name",
            "Last Name",
            "Date of Birth",
            "Gender",
            "Address",
            "Contact Number",
            "Email"
        };
        int[] yPositions = { 18, 20, 22, 24, 26, 28, 30, 32 };
        int[] xPositions = { 44, 44, 44, 44, 44, 44, 44, 44 };
        int maxLength = 20;

        // Display profile information
        ConsoleUI.drawInputFields(50, profileFields);
        ConsoleUI.displayInputFields(student.getProfileInformation(), yPositions, xPositions, maxLength);

        // Prompt user to navigate using pressing a key
        ConsoleUI.goTo(Settings.CONSOLE_HEIGHT - 3, 0);
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("[ESC] Back  |  [E] Edit", Settings.CONSOLE_WIDTH - 3);

        // This loop runs indefinitely until the user presses a valid key
        while (true) {
            int key = ConsoleInput.readKey();

            // Handle keypress
            if (key == Settings.ESC_KEY) {  
                break; // Return to student menu
            } else if (key == 'E' || key == 'e') {
                ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
                ConsoleUI.clearDialogBox();

                // Prompt user to navigate using pressing a key
                ConsoleUI.goTo(Settings.CONSOLE_HEIGHT - 3, 0);
                ConsoleUI.moveCursor(3);
                ConsoleInput.printCentered("[ESC] Cancel", Settings.CONSOLE_WIDTH - 3);

                // Edit student profile
                // This loop runs until user performs a successful operation
                while (true) {
                    // Prompt user to fill out input fields
                    String[] inputs = ConsoleInput.captureFormInputs(yPositions, xPositions, maxLength);

                    // Validate input fields
                    // ? [INFO] Cancel form editing
                    if (inputs == null) {
                        ConsoleDisplay.dialogBox("info", "Edit canceled. No changes were made.");
                        ConsoleInput.pressEnterToContinue();

                        displayStudentMenu(student);
                        break;
                    }

                    // ! [ERROR] Invalid email address
                    if (!inputs[7].isEmpty() && !Auth.isValidEmail(inputs[7])) {
                        ConsoleDisplay.dialogBox("error", "Please enter a valid email address (e.g. example@domain.com).");

                        ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
                        continue;
                    }

                    // ! [ERROR] Invalid gender
                    if (!inputs[4].isEmpty()) {
                        try {
                            student.setGender(Gender.valueOf(inputs[4].toUpperCase()));
                        } catch (IllegalArgumentException e) {
                            ConsoleDisplay.dialogBox("error", "Gender must be 'Male' or 'Female'.");
                            ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
                            continue;
                        }
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
                            // ! [ERROR] Invalid date format
                            ConsoleDisplay.dialogBox("error", "Invalid date format! Use yyyy-MM-dd.");

                            ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
                            continue;
                        }
                    }
                    if (!inputs[5].isEmpty()) student.setAddress(inputs[5]);
                    if (!inputs[6].isEmpty()) student.setContactNumber(inputs[6]);
                    if (!inputs[7].isEmpty()) student.setEmail(inputs[7]);

                    // Save changes made to the student's profile into the CSV file
                    CSV.updateStudentRecord(student);

                    // ? [SUCCESS] Profile updated
                    ConsoleDisplay.dialogBox("success", "Profile updated successfully!");
                    break;
                }
                continue;
            } else { continue; }
        }

        // Navigate back to 'Student Menu'
        displayStudentMenu(student);
    }

    // [METHOD] Display student "Academics" menu
    public static void displayStudentAcademicsMenu(Student student) throws IOException {
        // ! [ERROR] Null student record
        if (student == null) {
            ConsoleDisplay.dialogBox("error", "Student record is null!");
            return;
        }

        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Student: Academics");
        System.out.println();

        // Setup input fields
        String[] academicsFields = {
            "Student ID",
            "Enrollment Date",
            "Department",
            "Course",
            "Credits",
            "GPA",
            "Academic Standing"
        };
        int[] yPositions = { 18, 20, 22, 24, 26, 28, 30 };
        int[] xPositions = { 32, 32, 32, 32, 32, 32, 32 };
        int maxLength = 49;

        // Display profile information
        ConsoleUI.drawInputFields(80, academicsFields);
        ConsoleUI.displayInputFields(student.getStudentAcademicsInformation(), yPositions, xPositions, maxLength);

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
                ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
                ConsoleUI.clearDialogBox();

                // Prompt user to navigate using pressing a key
                ConsoleUI.goTo(Settings.CONSOLE_HEIGHT - 3, 0);
                ConsoleUI.moveCursor(3);
                ConsoleInput.printCentered("[ESC] Cancel", Settings.CONSOLE_WIDTH - 3);

                // Edit student academics
                // This loop runs until user performs a successful operation
                while (true) {
                    // Prompt user to fill out input fields
                    String[] inputs = ConsoleInput.captureFormInputs(yPositions, xPositions, maxLength);

                    // ? [INFO] Cancel form editing
                    if (inputs == null) {
                        ConsoleDisplay.dialogBox("info", "Edit canceled. No changes were made.");
                        ConsoleInput.pressEnterToContinue();

                        displayStudentMenu(student);
                        break;
                    }

                    // Validate input fields (department & course)
                    Department newDept = null;
                    if (!inputs[0].isEmpty()) {
                        newDept = Department.fromCodeOrFullName(inputs[0]);
                        // ! [ERRROR] Invalid department
                        if (newDept == Department.UNASSIGNED) {
                            ConsoleDisplay.dialogBox("error", "Invalid department. Please try again.");
                            ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
                            continue;
                        }
                    }

                    Course newCourse = null;
                    if (!inputs[1].isEmpty()) {
                        String courseInput = inputs[1].trim();

                        // Map course input to 'Course' object using department
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
                            default -> throw new IllegalArgumentException("Unexpected value: " + deptToUse);
                        }

                        // ! [ERROR] Invalid course
                        if (newCourse == null) {
                            ConsoleDisplay.dialogBox("error", "Invalid course for the selected department.");
                            ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
                            continue;
                        }
                    }

                    // Update fields
                    if (newDept != null) student.setDepartment(newDept);
                    if (newCourse != null) student.setCourse(newCourse);

                    // Save changes made to the student's profile into the CSV file
                    CSV.updateStudentRecord(student);

                    // ? [SUCCESS] Academics information updated
                    ConsoleDisplay.dialogBox("success", "Academics updated successfully!");
                    break;
                }
            } else { continue; }
        }

        // Navigate back to 'Student Menu'
        displayStudentMenu(student);
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
            } else if (key == '2') {
                displayStudentDropCourseOfferingMenu(student);
            } else { continue; }
        }

        // Navigate back to 'Student Menu'
        displayStudentMenu(student);
    }

    // * UI: Faculty Menu
    // [METHOD] Display "Academic Staff" menu
    public static void displayAcademicStaffMenu(AcademicStaff staff) throws IOException {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(16, 0);
        ConsoleDisplay.displayHeaderSubtitle("Academic Staff Menu");

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
                
            } else if (key == '2') {
                
            } else if (key == '3') {
                
            } else { continue; }
            break;
        }
    }

    // [METHOD] Display "NonAcademicStaff" menu
    public static void displayNonAcademicStaffMenu(NonAcademicStaff staff) throws IOException {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(16, 0);
        ConsoleDisplay.displayHeaderSubtitle("Administrator Menu");

        // Display top border
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("=".repeat(16), Settings.CONSOLE_WIDTH - 6);
        
        // Display operations
        for (MenuOperation operation : NonAcademicStaffMenuOperations) {
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

            boolean handled = false;

            // Handle keypress
            if (key == Settings.ESC_KEY) {
                displayLoginScreen();
                handled = true;
            } else if (key >= 32 && key <= 126) { // printable ASCII
                char inputChar = (char) key;
                for (MenuOperation operation : NonAcademicStaffMenuOperations) {
                    if (operation.matches(inputChar)) {
                        operation.action.run();
                        handled = true;
                        break;
                    }
                }
            }

            if (handled) break;
        }
    }

    private static void addStudentMenu() throws IOException {
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Administrator: Add Student");

        ConsoleUI.goTo(17, 0);
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("Complete all fields. Press [ESC] at any time to cancel.", Settings.CONSOLE_WIDTH - 6, true);

        String[] fields = {
            "First Name",
            "Middle Name",
            "Last Name",
            "Date of Birth (MM-dd-YYYY)",
            "Gender (Male/Female/Other)",
            "Address",
            "Contact Number",
            "Email",
            "Student ID",
            "Enrollment Date (MM-dd-YYYY)",
            "Department",
            "Course",
            "Program Level (Freshman/Sophomore/Junior/Senior/Master/PhD)",
            "Thesis Title (Graduate only)"
        };

        int[] yPositions = { 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35 };
        int[] xPositions = { 44, 44, 44, 60, 60, 42, 48, 44, 44, 60, 32, 36, 7, 36 };
        int maxLength = 40;

        ConsoleUI.drawInputFields(80, fields);

        while (true) {
            String[] inputs = ConsoleInput.captureFormInputs(yPositions, xPositions, maxLength);

            if (inputs == null) {
                ConsoleDisplay.dialogBox("info", "Add student canceled.");
                ConsoleInput.pressEnterToContinue();
                returnToNonAcademicStaffMenu();
                return;
            }

            String firstName = inputs[0].trim();
            String middleName = inputs[1].trim();
            String lastName = inputs[2].trim();
            String dobStr = inputs[3].trim();
            String genderStr = inputs[4].trim();
            String address = inputs[5].trim();
            String contact = inputs[6].trim();
            String email = inputs[7].trim();
            String studentId = inputs[8].trim();
            String enrollmentStr = inputs[9].trim();
            String deptStr = inputs[10].trim();
            String courseStr = inputs[11].trim();
            String programLevelInput = inputs[12].trim();
            String thesisTitle = inputs[13].trim();

            if (firstName.isBlank() || lastName.isBlank() || dobStr.isBlank() || genderStr.isBlank()
                || email.isBlank() || studentId.isBlank() || enrollmentStr.isBlank()
                || deptStr.isBlank() || courseStr.isBlank() || programLevelInput.isBlank()) {
                ConsoleDisplay.dialogBox("error", "Please fill in all required fields.");
                ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
                continue;
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                LocalDate dob = LocalDate.parse(dobStr, formatter);
                LocalDate enrollmentDate = LocalDate.parse(enrollmentStr, formatter);
                Gender gender = Gender.valueOf(genderStr.toUpperCase());
                Department department = Department.fromCodeOrFullName(deptStr);
                if (department == Department.UNASSIGNED) {
                    throw new IllegalArgumentException("Invalid department provided.");
                }
                Course course = Course.fromCodeOrFullName(courseStr);

                List<CourseOffering> enrolled = new ArrayList<>();
                Student newStudent;
                YearLevel yearLevel = resolveYearLevel(programLevelInput);

                if (yearLevel != null) {
                    newStudent = new UndergraduateStudent(
                        firstName, middleName, lastName, dob, gender,
                        address, contact, email,
                        studentId, enrollmentDate, department, course,
                        AcademicStanding.GOOD, 0.0, 0, enrolled,
                        yearLevel
                    );
                } else {
                    GraduateProgram gradProgram = resolveGraduateProgram(programLevelInput);
                    if (gradProgram == null) {
                        throw new IllegalArgumentException("Program level must be a valid year level or graduate program.");
                    }
                    newStudent = new GraduateStudent(
                        firstName, middleName, lastName, dob, gender,
                        address, contact, email,
                        studentId, enrollmentDate, department, course,
                        AcademicStanding.GOOD, 0.0, 0, enrolled,
                        gradProgram, thesisTitle.isBlank() ? null : thesisTitle, null
                    );
                }

                CSV.appendRow(Settings.STUDENTS_FILE, newStudent.toCSVRow());
                ConsoleDisplay.dialogBox("success", "Student record added successfully!");
                break;
            } catch (Exception e) {
                ConsoleDisplay.dialogBox("error", "Failed to add student: " + e.getMessage());
                ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
            }
        }

        ConsoleInput.pressEnterToContinue();
        returnToNonAcademicStaffMenu();
    }

    private static void addFacultyMenu() throws IOException {
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Administrator: Add Faculty");

        ConsoleUI.goTo(17, 0);
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("Enter faculty information. Press [ESC] to cancel.", Settings.CONSOLE_WIDTH - 6, true);

        String[] fields = {
            "First Name",
            "Middle Name",
            "Last Name",
            "Date of Birth (MM-dd-YYYY)",
            "Gender (Male/Female/Other)",
            "Address",
            "Contact Number",
            "Email",
            "Faculty ID",
            "Department",
            "Rank (Instructor/Assistant/Associate/Full Professor)",
            "Hire Date (MM-dd-YYYY)",
            "Office Location",
            "Salary",
            "Teaching Hours / Week",
            "Max Teaching Load",
            "Tenured (Yes/No)"
        };

        int[] yPositions = { 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40 };
        int[] xPositions = { 44, 44, 44, 60, 60, 42, 48, 44, 44, 45, 6, 60, 44, 44, 52, 50, 45 };
        int maxLength = 40;

        ConsoleUI.drawInputFields(90, fields);

        while (true) {
            String[] inputs = ConsoleInput.captureFormInputs(yPositions, xPositions, maxLength);

            if (inputs == null) {
                ConsoleDisplay.dialogBox("info", "Add faculty canceled.");
                ConsoleInput.pressEnterToContinue();
                returnToNonAcademicStaffMenu();
                return;
            }

            String firstName = inputs[0].trim();
            String middleName = inputs[1].trim();
            String lastName = inputs[2].trim();
            String dobStr = inputs[3].trim();
            String genderStr = inputs[4].trim();
            String address = inputs[5].trim();
            String contact = inputs[6].trim();
            String email = inputs[7].trim();
            String facultyId = inputs[8].trim();
            String deptStr = inputs[9].trim();
            String rankStr = inputs[10].trim();
            String hireDateStr = inputs[11].trim();
            String officeLocation = inputs[12].trim();
            String salaryStr = inputs[13].trim();
            String hoursStr = inputs[14].trim();
            String maxLoadStr = inputs[15].trim();
            String tenuredStr = inputs[16].trim();

            if (firstName.isBlank() || lastName.isBlank() || dobStr.isBlank() || genderStr.isBlank()
                || facultyId.isBlank() || deptStr.isBlank() || rankStr.isBlank()
                || hireDateStr.isBlank() || officeLocation.isBlank() || salaryStr.isBlank()
                || hoursStr.isBlank()) {
                ConsoleDisplay.dialogBox("error", "Please fill in all required fields.");
                ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
                continue;
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                LocalDate dob = LocalDate.parse(dobStr, formatter);
                LocalDate hireDate = LocalDate.parse(hireDateStr, formatter);
                Gender gender = Gender.valueOf(genderStr.toUpperCase());
                Department department = Department.fromCodeOrFullName(deptStr);
                if (department == Department.UNASSIGNED) {
                    throw new IllegalArgumentException("Invalid department provided.");
                }
                FacultyRank rank = FacultyRank.fromString(rankStr);

                double salary = Double.parseDouble(salaryStr);
                int teachingHours = Integer.parseInt(hoursStr);
                int maxTeachingLoad = maxLoadStr.isBlank() ? rank.getDefaultTeachingHours() : Integer.parseInt(maxLoadStr);
                boolean isTenured = tenuredStr.equalsIgnoreCase("yes") || tenuredStr.equalsIgnoreCase("true");

                AcademicStaff staffMember = new AcademicStaff(
                    firstName, middleName, lastName, dob, gender,
                    address, contact, email,
                    facultyId, department, rank, hireDate, officeLocation,
                    salary, isTenured, new ArrayList<>(),
                    facultyId, teachingHours, maxTeachingLoad
                );

                CSV.appendRow(Settings.ACADEMIC_STAFF_FILE, staffMember.toCSVRow());
                ConsoleDisplay.dialogBox("success", "Faculty record added successfully!");
                break;
            } catch (Exception e) {
                ConsoleDisplay.dialogBox("error", "Failed to add faculty: " + e.getMessage());
                ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
            }
        }

        ConsoleInput.pressEnterToContinue();
        returnToNonAcademicStaffMenu();
    }

    private static void viewAllStudents() throws IOException {
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Administrator: Student Records");

        List<String[]> rows = CSV.readCSV(Settings.STUDENTS_FILE);
        List<String[]> students = new ArrayList<>();
        for (String[] row : rows) {
            if (!safeValue(row, Settings.COL_STUDENT_ID).isBlank()) {
                students.add(row);
            }
        }

        if (students.isEmpty()) {
            ConsoleDisplay.dialogBox("info", "No student records found.");
            ConsoleInput.pressEnterToContinue();
            returnToNonAcademicStaffMenu();
            return;
        }

        String border = "=".repeat(92);
        String separator = "-".repeat(92);
        String header = String.format("| %-3s | %-12s | %-28s | %-20s | %-20s |",
                "No", "Student ID", "Name", "Department", "Course");

        ConsoleUI.moveCursor(5); ConsoleInput.printCentered(border, Settings.CONSOLE_WIDTH - 8);
        ConsoleUI.moveCursor(5); ConsoleInput.printCentered(header, Settings.CONSOLE_WIDTH - 8);
        ConsoleUI.moveCursor(5); ConsoleInput.printCentered(separator, Settings.CONSOLE_WIDTH - 8);

        int maxDisplay = 15;
        int displayed = 0;
        for (int i = 0; i < students.size() && displayed < maxDisplay; i++) {
            String[] row = students.get(i);
            String studentId = safeValue(row, Settings.COL_STUDENT_ID);
            String firstName = safeValue(row, Settings.COL_FIRST_NAME);
            String middleName = safeValue(row, Settings.COL_MIDDLE_NAME);
            String lastName = safeValue(row, Settings.COL_LAST_NAME);
            String dept = safeValue(row, Settings.COL_DEPARTMENT);
            String course = safeValue(row, Settings.COL_COURSE);
            String name = (firstName + " " + (middleName.isBlank() ? "" : middleName + " ") + lastName).trim().replaceAll("\\s+", " ");

            String body = String.format("| %-3d | %-12s | %-28s | %-20s | %-20s |",
                i + 1, studentId, name, dept, course);
            ConsoleUI.moveCursor(5); ConsoleInput.printCentered(body, Settings.CONSOLE_WIDTH - 8);
            displayed++;
        }

        ConsoleUI.moveCursor(5); ConsoleInput.printCentered(border, Settings.CONSOLE_WIDTH - 8);

        if (students.size() > maxDisplay) {
            ConsoleDisplay.dialogBox("info", "Displaying first " + maxDisplay + " of " + students.size() + " students.");
        } else {
            ConsoleDisplay.dialogBox("info", "Total students: " + students.size());
        }

        ConsoleInput.pressEnterToContinue();
        returnToNonAcademicStaffMenu();
    }

    private static void viewAllFaculty() throws IOException {
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Administrator: Faculty Records");

        List<String[]> rows = CSV.readCSV(Settings.ACADEMIC_STAFF_FILE);
        if (rows.isEmpty()) {
            ConsoleDisplay.dialogBox("info", "No faculty records found.");
            ConsoleInput.pressEnterToContinue();
            returnToNonAcademicStaffMenu();
            return;
        }

        String border = "=".repeat(92);
        String separator = "-".repeat(92);
        String header = String.format("| %-3s | %-12s | %-28s | %-20s | %-20s |",
                "No", "Faculty ID", "Name", "Department", "Rank");

        ConsoleUI.moveCursor(5); ConsoleInput.printCentered(border, Settings.CONSOLE_WIDTH - 8);
        ConsoleUI.moveCursor(5); ConsoleInput.printCentered(header, Settings.CONSOLE_WIDTH - 8);
        ConsoleUI.moveCursor(5); ConsoleInput.printCentered(separator, Settings.CONSOLE_WIDTH - 8);

        int maxDisplay = 15;
        int displayed = 0;
        for (int i = 0; i < rows.size() && displayed < maxDisplay; i++) {
            String[] row = rows.get(i);
            String facultyId = safeValue(row, Settings.COL_FACULTY_ID);
            String firstName = safeValue(row, Settings.COL_FIRST_NAME);
            String middleName = safeValue(row, Settings.COL_MIDDLE_NAME);
            String lastName = safeValue(row, Settings.COL_LAST_NAME);
            String dept = safeValue(row, Settings.COL_DEPARTMENT_FAC);
            String rank = safeValue(row, Settings.COL_RANK);
            String name = (firstName + " " + (middleName.isBlank() ? "" : middleName + " ") + lastName).trim().replaceAll("\\s+", " ");

            if (facultyId.isBlank() && name.isBlank()) continue;

            String body = String.format("| %-3d | %-12s | %-28s | %-20s | %-20s |",
                i + 1, facultyId, name, dept, rank);
            ConsoleUI.moveCursor(5); ConsoleInput.printCentered(body, Settings.CONSOLE_WIDTH - 8);
            displayed++;
        }

        ConsoleUI.moveCursor(5); ConsoleInput.printCentered(border, Settings.CONSOLE_WIDTH - 8);

        if (rows.size() > maxDisplay) {
            ConsoleDisplay.dialogBox("info", "Displaying first " + maxDisplay + " of " + rows.size() + " faculty records.");
        } else {
            ConsoleDisplay.dialogBox("info", "Total faculty: " + rows.size());
        }

        ConsoleInput.pressEnterToContinue();
        returnToNonAcademicStaffMenu();
    }

    private static void removeStudentMenu() throws IOException {
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Administrator: Remove Student");

        ConsoleUI.goTo(17, 0);
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("Enter a Student ID or email to remove.", Settings.CONSOLE_WIDTH - 6, true);

        ConsoleUI.drawInputFields(50, "Student ID / Email");
        int[] yPositions = { 21 };
        int[] xPositions = { 52 };
        int maxLength = 40;

        String[] inputs = ConsoleInput.captureFormInputs(yPositions, xPositions, maxLength);

        if (inputs == null) {
            ConsoleDisplay.dialogBox("info", "Remove student canceled.");
            ConsoleInput.pressEnterToContinue();
            returnToNonAcademicStaffMenu();
            return;
        }

        String identifier = inputs[0].trim();

        if (identifier.isBlank()) {
            ConsoleDisplay.dialogBox("error", "Identifier cannot be empty.");
            ConsoleInput.pressEnterToContinue();
            returnToNonAcademicStaffMenu();
            return;
        }

        List<String[]> rows = CSV.readCSV(Settings.STUDENTS_FILE);
        List<String> removedIds = new ArrayList<>();
        boolean removed = false;

        for (int i = rows.size() - 1; i >= 0; i--) {
            String[] row = rows.get(i);
            String studentId = safeValue(row, Settings.COL_STUDENT_ID);
            String email = safeValue(row, Settings.COL_EMAIL);

            if (identifier.equalsIgnoreCase(studentId) || identifier.equalsIgnoreCase(email)) {
                rows.remove(i);
                removedIds.add(studentId);
                removed = true;
            }
        }

        if (removed) {
            CSV.writeCSV(Settings.STUDENTS_FILE, rows);
            for (String id : removedIds) {
                removeStudentEnrollments(id);
            }
            ConsoleDisplay.dialogBox("success", "Student record removed.");
        } else {
            ConsoleDisplay.dialogBox("error", "Student not found.");
        }

        ConsoleInput.pressEnterToContinue();
        returnToNonAcademicStaffMenu();
    }

    private static void removeFacultyMenu() throws IOException {
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Administrator: Remove Faculty");

        ConsoleUI.goTo(17, 0);
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("Enter a Faculty ID or email to remove.", Settings.CONSOLE_WIDTH - 6, true);

        ConsoleUI.drawInputFields(50, "Faculty ID / Email");
        int[] yPositions = { 21 };
        int[] xPositions = { 52 };
        int maxLength = 40;

        String[] inputs = ConsoleInput.captureFormInputs(yPositions, xPositions, maxLength);

        if (inputs == null) {
            ConsoleDisplay.dialogBox("info", "Remove faculty canceled.");
            ConsoleInput.pressEnterToContinue();
            returnToNonAcademicStaffMenu();
            return;
        }

        String identifier = inputs[0].trim();

        if (identifier.isBlank()) {
            ConsoleDisplay.dialogBox("error", "Identifier cannot be empty.");
            ConsoleInput.pressEnterToContinue();
            returnToNonAcademicStaffMenu();
            return;
        }

        List<String[]> rows = CSV.readCSV(Settings.ACADEMIC_STAFF_FILE);
        boolean removed = false;

        for (int i = rows.size() - 1; i >= 0; i--) {
            String[] row = rows.get(i);
            String facultyId = safeValue(row, Settings.COL_FACULTY_ID);
            String email = safeValue(row, Settings.COL_EMAIL);

            if (identifier.equalsIgnoreCase(facultyId) || identifier.equalsIgnoreCase(email)) {
                rows.remove(i);
                removed = true;
            }
        }

        if (removed) {
            CSV.writeCSV(Settings.ACADEMIC_STAFF_FILE, rows);
            ConsoleDisplay.dialogBox("success", "Faculty record removed.");
        } else {
            ConsoleDisplay.dialogBox("error", "Faculty not found.");
        }

        ConsoleInput.pressEnterToContinue();
        returnToNonAcademicStaffMenu();
    }

    private static void returnToNonAcademicStaffMenu() throws IOException {
        if (currentNonAcademicStaff != null) {
            displayNonAcademicStaffMenu(currentNonAcademicStaff);
        } else {
            displayLoginScreen();
        }
    }

    private static YearLevel resolveYearLevel(String input) {
        if (input == null || input.isBlank()) return null;
        String normalized = input.trim().toUpperCase().replace("-", "_").replace(" ", "_");
        for (YearLevel level : YearLevel.values()) {
            if (level.name().equals(normalized) || level.getDisplayName().equalsIgnoreCase(input.trim())) {
                return level;
            }
        }
        return null;
    }

    private static GraduateProgram resolveGraduateProgram(String input) {
        if (input == null || input.isBlank()) return null;
        String normalized = input.trim().toUpperCase().replace("'", "").replace(".", "");
        normalized = normalized.replace(" ", "");
        if (normalized.equals("MASTER")) normalized = "MASTERS";
        for (GraduateProgram program : GraduateProgram.values()) {
            if (program.name().equalsIgnoreCase(normalized) || program.getDisplayName().equalsIgnoreCase(input.trim())) {
                return program;
            }
        }
        return null;
    }

    private static String safeValue(String[] row, int index) {
        if (row == null || index < 0 || index >= row.length) return "";
        return row[index] != null ? row[index].trim() : "";
    }

    private static void removeStudentEnrollments(String studentId) {
        if (studentId == null || studentId.isBlank()) return;
        List<String[]> rows = CSV.readCSV(Settings.ENROLLMENTS_FILE);
        boolean updated = false;
        for (int i = rows.size() - 1; i >= 0; i--) {
            String[] row = rows.get(i);
            if (row.length > 0 && studentId.equalsIgnoreCase(row[0])) {
                rows.remove(i);
                updated = true;
            }
        }
        if (updated) {
            CSV.writeCSV(Settings.ENROLLMENTS_FILE, rows);
        }
    }

    // * UI: User Authentication
    // [METHOD] Display "Setup Information" screen
    public static void displayStudentSetupInformationScreen(String email, String userType) throws IOException {
        // Display UI
        ConsoleUI.clearScreen();
        ConsoleDisplay.displayBorder(2, 3);
        ConsoleUI.goTo(4, 0);
        ConsoleDisplay.displayHeaderSubtitle("Setup Information");

        // Prompt user to fill out personal information before proceeding to 'Student Menu'
        ConsoleUI.goTo(6, 0);
        ConsoleUI.moveCursor(3); ConsoleInput.printCentered("Please fill out all personal information fields to proceed.", Settings.CONSOLE_WIDTH - 3, true);

        // Setup input fields
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
        int[] yPositions = {9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29};
        int[] xPositions = {44, 45, 43, 60, 60, 41, 48, 44, 62, 44, 40};
        int maxLength = 60;

        // Display input fields
        ConsoleUI.drawInputFields(40, fields);

        while (true) {
            String[] inputs = ConsoleInput.captureFormInputs(yPositions, xPositions, maxLength);

            // IF [ESC] pressed, go to 'Login Screen'
            if (inputs == null) {
                displayLoginScreen();
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
            // ! [ERROR] Blank input
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
                    // Create new 'UndergraduateStudent' user
                    YearLevel yl = YearLevel.valueOf(userType.toUpperCase());
                    student = new UndergraduateStudent(
                        firstName, middleName, lastName, dob, gender,
                        address, contact, email,
                        studentId, enrollmentDate, dept, course,
                        AcademicStanding.GOOD, 0.0, 0, new ArrayList<>(), yl
                    );
                } else { // Create new 'GraduateStudent' user
                    GraduateProgram program = GraduateProgram.valueOf(userType.toUpperCase());
                    student = new GraduateStudent(
                        firstName, middleName, lastName, dob, gender,
                        address, contact, email,
                        studentId, enrollmentDate, dept, course,
                        AcademicStanding.GOOD, 0.0, 0, new ArrayList<>(),
                        program, null, null
                    );
                }

                // Append the student's personal information to student's CSV
                CSV.appendRow(Settings.STUDENTS_FILE, student.toCSVRow());

                // Set current student
                currentStudent = student;

                // ? [SUCCESS] Saved student personal info
                ConsoleDisplay.dialogBox("success", "Student information saved successfully!");

                // Prompt user to press [ENTER] before navigating to next screen
                ConsoleInput.pressEnterToContinue();
                displayStudentMenu(student);
                return;
            } catch (Exception e) {
                // ! [ERROR] Invalid input
                ConsoleDisplay.dialogBox("error", "Invalid input: " + e.getMessage());
                ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
            }
            break;
        }
    }

    // [METHOD] Display "Setup Information" screen for Academic Staff
    public static void displayAcademicStaffSetupInformationScreen(String email, String userType) throws IOException {
        // Display UI
        ConsoleUI.clearScreen();
        ConsoleDisplay.displayBorder(2, 3);
        ConsoleUI.goTo(4, 0);
        ConsoleDisplay.displayHeaderSubtitle("Academic Staff Setup Information");

        // Prompt user to fill out personal information before proceeding to 'Academic Staff Menu'
        ConsoleUI.goTo(6, 0);
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("Please fill out all personal information fields to proceed.", Settings.CONSOLE_WIDTH - 3, true);

        // Setup input fields
        String[] fields = {
            "First Name",
            "Middle Name",
            "Last Name",
            "Date of Birth (MM-dd-YYYY)",
            "Gender (Male/Female/Other)",
            "Address",
            "Contact Number",
            "Teacher ID",
            "Department",
            "Rank",
            "Hire Date (MM-dd-YYYY)",
            "Office Location"
        };

        int[] yPositions = {9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31};
        int[] xPositions = {51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51};
        int maxLength = 30;

        // Display input fields
        ConsoleUI.drawInputFields(60, fields);

        while (true) {
            String[] inputs = ConsoleInput.captureFormInputs(yPositions, xPositions, maxLength);

            // IF [ESC] pressed, go to 'Login Screen'
            if (inputs == null) {
                displayLoginScreen();
                return;
            }

            // Extract values
            String firstName = inputs[0];
            String middleName = inputs[1];
            String lastName = inputs[2];
            String dobStr = inputs[3];
            String genderStr = inputs[4];
            String address = inputs[5];
            String contact = inputs[6];
            String teacherId = inputs[7];
            String deptStr = inputs[8];
            String rankStr = inputs[9];
            String hireDateStr = inputs[10];
            String officeLocation = inputs[11];

            // Validate required fields
            if (firstName.isBlank() || lastName.isBlank() || dobStr.isBlank() || genderStr.isBlank()
                || teacherId.isBlank() || deptStr.isBlank() || rankStr.isBlank() || hireDateStr.isBlank()
                || officeLocation.isBlank()) {
                ConsoleDisplay.dialogBox("error", "Please fill in all required fields!");
                ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
                continue;
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                LocalDate dob = LocalDate.parse(dobStr, formatter);
                Gender gender = Gender.valueOf(genderStr.toUpperCase());
                Department department = Department.fromCodeOrFullName(deptStr);
                FacultyRank rank = FacultyRank.fromString(rankStr);
                LocalDate hireDate = LocalDate.parse(hireDateStr, formatter);

                // Compute salary and teaching hours based on rank (from enum)
                double salary = rank.getDefaultSalary();
                int teachingHours = rank.getDefaultTeachingHours();
                int maxTeachingHours = 18; // add this line — define maxTeachingHours

                // Create AcademicStaff object
                AcademicStaff staff = new AcademicStaff(
                    firstName, middleName, lastName, dob, gender,
                    address, contact, email,
                    teacherId, // facultyId
                    department, rank, hireDate, officeLocation, // faculty attributes in correct order
                    salary, false, new ArrayList<GraduateStudent>(), // advisees
                    teacherId, teachingHours, maxTeachingHours // academic staff attributes
                );

                // Append academic staff info to CSV
                CSV.appendRow(Settings.ACADEMIC_STAFF_FILE, staff.toCSVRow());

                // Set current academic staff
                currentAcademicStaff = staff;

                // Success message
                ConsoleDisplay.dialogBox("success", "Academic staff information saved successfully!");

                // Prompt user to press [ENTER] before navigating to Academic Staff Menu
                ConsoleInput.pressEnterToContinue();
                displayAcademicStaffMenu(staff);
                return;
            } catch (Exception e) {
                ConsoleDisplay.dialogBox("error", "Invalid input: " + e.getMessage());
                ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
            }
        }
    }

    // [METHOD] Display "Setup Information" screen for Non-Academic Staff (Administrator)
    public static void displayNonAcademicStaffSetupInformationScreen(String email, String userType) throws IOException {

        // Display UI
        ConsoleUI.clearScreen();
        ConsoleDisplay.displayBorder(2, 3);
        ConsoleUI.goTo(4, 0);
        ConsoleDisplay.displayHeaderSubtitle("Non-Academic Staff Setup Information");

        // Instruction
        ConsoleUI.goTo(6, 0);
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered(
            "Please fill out all personal information fields to proceed.",
            Settings.CONSOLE_WIDTH - 3, true
        );

        // Input labels
        String[] fields = {
            "First Name",
            "Middle Name",
            "Last Name",
            "Date of Birth (MM-dd-YYYY)",
            "Gender (Male/Female/Other)",
            "Address",
            "Contact Number",
            "Admin ID",
            "Department",
            "Job Title / Position",
            "Hire Date (MM-dd-YYYY)",
            "Office Location"
        };

        // Cursor positions
        int[] yPositions = {9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31};
        int[] xPositions = {51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51};
        int maxLength = 30;

        // Display input fields
        ConsoleUI.drawInputFields(60, fields);

        while (true) {

            // Capture user inputs
            String[] inputs = ConsoleInput.captureFormInputs(yPositions, xPositions, maxLength);

            // ESC pressed = go back to Login Screen
            if (inputs == null) {
                displayLoginScreen();
                return;
            }

            // Extract values
            String firstName = inputs[0];
            String middleName = inputs[1];
            String lastName = inputs[2];
            String dobStr = inputs[3];
            String genderStr = inputs[4];
            String address = inputs[5];
            String contact = inputs[6];
            String adminId = inputs[7];
            String deptStr = inputs[8];
            String jobTitle = inputs[9];
            String hireDateStr = inputs[10];
            String officeLocation = inputs[11];

            // Validation
            if (firstName.isBlank() || lastName.isBlank() || dobStr.isBlank() || genderStr.isBlank()
                || adminId.isBlank() || deptStr.isBlank() || jobTitle.isBlank()
                || hireDateStr.isBlank() || officeLocation.isBlank()) {

                ConsoleDisplay.dialogBox("error", "Please fill in all required fields!");
                ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
                continue;
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

                LocalDate dob = LocalDate.parse(dobStr, formatter);
                Gender gender = Gender.valueOf(genderStr.toUpperCase());
                Department department = Department.fromCodeOrFullName(deptStr);
                LocalDate hireDate = LocalDate.parse(hireDateStr, formatter);

                // Default salary / hours for administrators
                double salary = 35000; // You may change this
                int workHours = 40;

                // Create NonAcademicStaff/Administrator object
                NonAcademicStaff admin = new NonAcademicStaff(
                    firstName, middleName, lastName, dob, gender, address, contact, email,
                    adminId, department, jobTitle, hireDate, officeLocation, salary, workHours
                );

                // Save to CSV
                CSV.appendRow(Settings.NON_ACAD_STAFF_FILE, admin.toCSVRow());

                // Set currently logged-in admin
                currentNonAcademicStaff = admin;

                // Success dialog
                ConsoleDisplay.dialogBox("success", "Administrator information saved successfully!");

                ConsoleInput.pressEnterToContinue();
                displayNonAcademicStaffMenu(admin);
                return;

            } catch (Exception e) {
                ConsoleDisplay.dialogBox("error", "Invalid input: " + e.getMessage());
                ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
            }
        }
    }

    // [METHOD] Display "Login" screen
    public static void displayLoginScreen() throws IOException {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("LOGIN");

        // Setup input fields
        int[] yPositions = { 17, 19 };
        int[] xPositions = { 43, 43 };
        int maxLength = 28;

        String email, password;

        // Display input fields
        ConsoleUI.drawInputFields(40, "Email", "Password");

        // This loop runs indefinitely until user enters valid credentials
        while (true) {
            String[] inputs = ConsoleInput.captureFormInputs(yPositions, xPositions, maxLength);

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

    // ! [ERROR] Invalid user type
    if (userType == null) {
        ConsoleDisplay.dialogBox("error", "Unable to determine user type. Login failed.");
        return;
    }

    // Convert user type to lowercase (normalization)
    userType = userType.toLowerCase();

    // ? [SUCCESS] Successful login
    ConsoleDisplay.dialogBox("success", "Login successful! Press [Enter] to continue...");

    // Wait until user presses [ENTER] on an empty line and ignore any non-empty input
    while (!ConsoleInput.getString("").equals("")) {}

    // Redirect based on user type
    switch (userType) {
        // ? Type: UndergraduateStudent
        case "freshman":
        case "sophomore":
        case "junior":
        case "senior":
            Student student = Auth.getUndergraduateByEmail(email);
            if (student == null) {
                // First-time login - redirect to setup information screen
                displayStudentSetupInformationScreen(email, userType);
                return;
            }

            currentStudent = student; // Set current student
            displayStudentMenu(student);
            break;

        // ? Type: GraduateStudent
        case "master":
        case "phd":
            Student gradStudent = Auth.getGraduateByEmail(email);
            if (gradStudent == null) {
                // First-time login - redirect to setup information screen
                displayStudentSetupInformationScreen(email, userType);
                return;
            }
            displayStudentMenu(gradStudent);
            break;

        // ? Type: AcademicStaff
        case "academic staff":
        case "teacher":
            displayAcademicStaffSetupInformationScreen(email, userType);
            break;

        // ? Type: Librarian
        case "librarian":
            break;

        // ? Type: Administrator
        case "administrator":
            displayNonAcademicStaffSetupInformationScreen(email, userType);
            break;

        default:
            // ! [ERROR] Invalid user type
            ConsoleDisplay.dialogBox("error", "Unknown user type: " + userType);
            break;
        }
    }

    public static void displayRegisterScreen() throws IOException {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("REGISTER");

        ConsoleUI.drawInputFields(40, "Email", "Password", "Confirm Password", "User Type");

        int[] yPositions = { 17, 19, 21, 23 };
        int[] xPositions = { 51, 51, 51, 51 };
        int maxLength = 20;

        String[] inputs; // To store user input
        String email, password, confirmPassword, userType;

        // This loop runs indefinitely until user enters valid credentials
        while (true) {
            inputs = ConsoleInput.captureFormInputs(yPositions, xPositions, maxLength);

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
        
        // If successful registration, save credentials to backend
        CSV.appendRow(Settings.CREDENTIALS_FILE, new String[]{email, password, userType});

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