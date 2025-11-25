package ums.ui;

// [IMPORT] Standard
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.Collectors;

// [IMPORT] Models
import ums.model.GraduateStudent;
import ums.model.AcademicStaff;
import ums.model.NonAcademicStaff;
import ums.model.Student;
import ums.model.UndergraduateStudent;

// [IMPORT] Entities
import ums.model.entity.Course;
import ums.model.entity.CourseOffering;
import ums.model.entity.TimeSlot;
import ums.model.CourseCatalog;

// [IMPORT] Enums
import ums.model.enums.Department;
import ums.model.enums.AcademicStanding;
import ums.model.enums.Courses;
import ums.model.enums.Gender;
import ums.model.enums.GraduateProgram;
import ums.model.enums.YearLevel;
import ums.model.enums.Semester;

// [IMPORT] Utilities
import ums.util.CSV;
import ums.util.Auth;
import ums.util.Settings;
import ums.util.console.ConsoleAnimation;
import ums.util.console.ConsoleDisplay;
import ums.util.console.ConsoleInput;
import ums.util.console.ConsoleUI;
import ums.util.console.ConsoleFormatting;
import ums.util.Sound;

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
                viewAllStudents();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }),
        new MenuOperation(new String[]{"4"}, "View Faculty", () -> {
            try {
                viewAllFaculty();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }),
        new MenuOperation(new String[]{"5"}, "Remove Student", () -> {
            try {
                removeStudentMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }),
        new MenuOperation(new String[]{"6"}, "Remove Faculty", () -> {
            try {
                removeFacultyMenu(); 
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
        int yPosition = 27;
        int xPosition = 66;
        int maxLength = 2;

        // This loop runs indefinitely until user enters valid credentials
        while (true) {
            // Prompt user to fill out input fields
            String[] input = ConsoleInput.captureFormInputs(yPosition, xPosition, maxLength, 1);

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
            if (CSV.isStudentEnrolled(student.getPersonId(), selected.getOfferingId())) {
                ConsoleDisplay.dialogBox("error", "You are already enrolled in this course offering!");
                continue; // Ask for input again
            }

            // Safe to enroll
            student.enrollInCourseOffering(selected);
            CSV.updateCourseOffering(selected);

            // Append to CSV
            String[] enrollmentRow = new String[] {
                student.getPersonId(),
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
        List<CourseOffering> enrolledCourses = student.getEnrolledCourseOfferings();

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
        int yPosition = 33;
        int xPosition = 65;
        int maxLength = 2;

        // This loop runs indefinitely until user enters valid credentials
        while (true) {
            // Prompt user to fill out input field
            String[] inputArray = ConsoleInput.captureFormInputs(yPosition, xPosition, maxLength, 1);

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
            student.dropFromCourseOffering(selected);

            // Update course offering from course offering's CSV
            CSV.updateCourseOffering(selected);

            // Remove enrollment from CSV
            CSV.removeEnrollment(student.getPersonId(), selected.getOfferingId());

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
        int yPosition = 18;
        int xPosition = 44;
        int maxLength = 20;

        // Display profile information
        ConsoleUI.drawInputFields(50, profileFields);
        ConsoleUI.displayInputFields(student.getProfileInformation(), yPosition, xPosition, maxLength);

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
                ConsoleUI.clearInputFields(yPosition, xPosition, maxLength, profileFields.length);
                ConsoleUI.clearDialogBox(5);

                // Prompt user to navigate using pressing a key
                ConsoleUI.goTo(Settings.CONSOLE_HEIGHT - 3, 0);
                ConsoleUI.moveCursor(3);
                ConsoleInput.printCentered("[ESC] Cancel", Settings.CONSOLE_WIDTH - 3);

                // Edit student profile
                // This loop runs until user performs a successful operation
                while (true) {
                    // Prompt user to fill out input fields
                    String[] inputs = ConsoleInput.captureFormInputs(yPosition, xPosition, maxLength, profileFields.length);

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

                        ConsoleUI.clearInputFields(yPosition, xPosition, maxLength, profileFields.length);
                        continue;
                    }

                    // ! [ERROR] Invalid gender
                    if (!inputs[4].isEmpty()) {
                        try {
                            student.setGender(Gender.valueOf(inputs[4].toUpperCase()));
                        } catch (IllegalArgumentException e) {
                            ConsoleDisplay.dialogBox("error", "Gender must be 'Male' or 'Female'.");
                            ConsoleUI.clearInputFields(yPosition, xPosition, maxLength, profileFields.length);
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

                            ConsoleUI.clearInputFields(yPosition, xPosition, maxLength, profileFields.length);
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
            "Enrollment Date",
            "Department",
            "Course",
            "Credits",
            "GPA",
            "Academic Standing"
        };

        int yPosition = 18;
        int xPosition = 32;
        int maxLength = 49;

        // Display profile information
        ConsoleUI.drawInputFields(80, academicsFields);
        ConsoleUI.displayInputFields(student.getAcademicsInformation(), yPosition, xPosition, maxLength);

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
                ConsoleUI.clearInputFields(yPosition, xPosition, maxLength, academicsFields.length);
                ConsoleUI.clearDialogBox(5);

                // Prompt user to navigate using pressing a key
                ConsoleUI.goTo(Settings.CONSOLE_HEIGHT - 3, 0);
                ConsoleUI.moveCursor(3);
                ConsoleInput.printCentered("[ESC] Cancel", Settings.CONSOLE_WIDTH - 3);

                // Edit student academics
                // This loop runs until user performs a successful operation
                while (true) {
                    // Prompt user to fill out input fields
                    String[] inputs = ConsoleInput.captureFormInputs(yPosition, xPosition, maxLength, academicsFields.length);

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
                            ConsoleUI.clearInputFields(yPosition, xPosition, maxLength, academicsFields.length);
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
                            ConsoleUI.clearInputFields(yPosition, xPosition, maxLength, academicsFields.length);
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

    // * UI: AcademicStaff Menu
    // [METHOD] Display "Academic Staff" menu
    public static void displayAcademicStaffMenu(AcademicStaff staff) throws IOException {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(16, 0);
        ConsoleDisplay.displayHeaderSubtitle("Academic Staff Menu");

        // Display top border
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("=".repeat(23), Settings.CONSOLE_WIDTH - 5);
        
        // Display operations
        for (MenuOperation operation : studentMenuOperations) {
            ConsoleUI.moveCursor(39);
            ConsoleAnimation.lineDelayAnimation('[' + operation.inputKeys[0] + "] " + operation.getDisplayName(), 50); System.out.println();
        }

        // Display bottom border
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("=".repeat(23), Settings.CONSOLE_WIDTH - 5);

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
                displayAcademicStaffProfileMenu(staff);
            } else if (key == '2') {
                displayAcademicStaffAcademicsMenu(staff);
            } else if (key == '3') {
                displayAcademicStaffCoursesMenu(staff);
            } else { continue; }
            break;
        }
    }

    // [METHOD] Display academic staff "Profile" menu
    public static void displayAcademicStaffProfileMenu(AcademicStaff staff) throws IOException {

        if (staff == null) {
            ConsoleDisplay.dialogBox("error", "Academic staff record is null!");
            return;
        }

        // UI Setup
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Academic Staff: Profile");
        System.out.println();

        String[] profileFields = {
            "First Name",
            "Middle Name",
            "Last Name",
            "Date of Birth (MM-dd-YYYY)",
            "Gender",
            "Address",
            "Contact Number",
            "Email"
        };

        int yPosition = 18;
        int xPosition = 51;
        int maxLength = 29;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        // Prepare display values
        String[] prefilled = {
            staff.getFirstName(),
            staff.getMiddleName(),
            staff.getLastName(),
            staff.getDateOfBirth().format(formatter),
            staff.getGender().toString(),
            staff.getAddress(),
            staff.getContactNumber(),
            staff.getEmail()
        };

        // Draw UI
        ConsoleUI.drawInputFields(60, profileFields);
        ConsoleUI.displayInputFields(Arrays.asList(prefilled), yPosition, xPosition, maxLength);

        ConsoleUI.goTo(Settings.CONSOLE_HEIGHT - 3, 0);
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("[ESC] Back  |  [E] Edit", Settings.CONSOLE_WIDTH - 3);

        while (true) {
            int key = ConsoleInput.readKey();

            if (key == Settings.ESC_KEY) {
                break;
            } else if (key == 'E' || key == 'e') {
                ConsoleUI.clearInputFields(yPosition, xPosition, maxLength, profileFields.length);
                ConsoleUI.clearDialogBox(5);

                ConsoleUI.goTo(Settings.CONSOLE_HEIGHT - 3, 0);
                ConsoleUI.moveCursor(3);
                ConsoleInput.printCentered("[ESC] Cancel", Settings.CONSOLE_WIDTH - 3);

                while (true) {
                    // Gender selection dropdown
                    String[][] selectOptions = new String[profileFields.length][];
                    selectOptions[4] = new String[]{"Male", "Female", "Prefer not to say"};

                    boolean[] hidden = new boolean[profileFields.length];

                    String[] inputs = ConsoleInput.captureFormInputs(
                        yPosition, xPosition, maxLength,
                        profileFields.length, hidden, selectOptions
                    );

                    // Cancel editing
                    if (inputs == null) {
                        ConsoleUI.clearDialogBox(4);
                        ConsoleDisplay.dialogBox("info", "Edit canceled. No changes were made.");
                        ConsoleInput.pressEnterToContinue();
                        displayAcademicStaffMenu(staff);
                        return;
                    }

                    // Validate date
                    if (!inputs[3].isEmpty()) {
                        try {
                            staff.setDateOfBirth(LocalDate.parse(inputs[3], formatter));
                        } catch (Exception e) {
                            ConsoleUI.clearDialogBox(4);
                            ConsoleDisplay.dialogBox("error",
                                "Invalid date! Use MM-dd-YYYY format."
                            );
                            ConsoleUI.clearInputFields(yPosition, xPosition, maxLength, profileFields.length);
                            continue;
                        }
                    }

                    // Validate gender
                    if (!inputs[4].isEmpty()) {
                        try {
                            staff.setGender(Gender.fromString(inputs[4]));
                        } catch (Exception e) {
                            ConsoleUI.clearDialogBox(4);
                            ConsoleDisplay.dialogBox("error", "Invalid gender.");
                            ConsoleUI.clearInputFields(yPosition, xPosition, maxLength, profileFields.length);
                            continue;
                        }
                    }

                    // Validate email
                    if (!inputs[7].isEmpty() && !Auth.isValidEmail(inputs[7])) {
                        ConsoleUI.clearDialogBox(4);
                        ConsoleDisplay.dialogBox("error",
                            "Please enter a valid email (example@domain.com)."
                        );
                        ConsoleUI.clearInputFields(yPosition, xPosition, maxLength, profileFields.length);
                        continue;
                    }

                    // Apply changes
                    if (!inputs[0].isEmpty()) staff.setFirstName(inputs[0]);
                    if (!inputs[1].isEmpty()) staff.setMiddleName(inputs[1]);
                    if (!inputs[2].isEmpty()) staff.setLastName(inputs[2]);
                    if (!inputs[5].isEmpty()) staff.setAddress(inputs[5]);
                    if (!inputs[6].isEmpty()) staff.setContactNumber(inputs[6]);
                    if (!inputs[7].isEmpty()) staff.setEmail(inputs[7]);

                    // Save to CSV
                    CSV.updateAcademicStaffRecord(staff);

                    ConsoleUI.clearDialogBox(4);
                    ConsoleDisplay.dialogBox("success", "Profile updated successfully!");
                    ConsoleInput.pressEnterToContinue();
                    break;
                }
            }
            break;
        }

        // Navigate back to 'Academic Staff Menu'
        displayAcademicStaffMenu(staff);
    }

    // [METHOD] Display Academic Staff "Academics" menu
    public static void displayAcademicStaffAcademicsMenu(AcademicStaff staff) throws IOException {
        if (staff == null) {
            ConsoleDisplay.dialogBox("error", "Academic staff record is null!");
            return;
        }

        ConsoleUI.clearScreen();
        ConsoleDisplay.displayBorder(2, 3);
        ConsoleUI.goTo(4, 0);
        ConsoleDisplay.displayHeaderSubtitle("Academic Staff: Academics");
        System.out.println();

        String[] academicsFields = {
            "Person ID",
            "Department",
            "Rank",
            "Hire Date (MM-dd-YYYY)",
            "Office Location",
            "Salary",
            "Teaching Hours/Week",
            "Max Teaching Load",
            "Overloaded",
            "Tenured (Yes/No)",
            "Courses Taught",
            "Advisees (Grad Students)"
        };

        int yPosition = 7;
        int xPosition = 39;
        int maxLength = 51;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        // Prepare academics information and truncate long values
        List<String> rawInfo = staff.getAcademicsInformation();
        List<String> displayInfo = new ArrayList<>();
        for (String value : rawInfo) {
            displayInfo.add(ConsoleUI.truncate(value, maxLength));
        }

        // Draw UI
        ConsoleUI.drawInputFields(80, academicsFields);
        ConsoleUI.displayInputFields(displayInfo, yPosition, xPosition, maxLength);

        ConsoleUI.goTo(Settings.CONSOLE_HEIGHT - 3, 0);
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("[ESC] Back  |  [E] Edit", Settings.CONSOLE_WIDTH - 3);

        while (true) {
            int key = ConsoleInput.readKey();

            if (key == Settings.ESC_KEY) {
                break;
            } else if (key == 'E' || key == 'e') {
                ConsoleUI.clearInputFields(yPosition, xPosition, maxLength, academicsFields.length);
                ConsoleUI.clearDialogBox(5);

                ConsoleUI.goTo(Settings.CONSOLE_HEIGHT - 3, 0);
                ConsoleUI.moveCursor(3);
                ConsoleInput.printCentered("[ESC] Cancel", Settings.CONSOLE_WIDTH - 3);

                while (true) {
                    // Editable fields: Department, Rank, Hire Date, Office, Salary, Teaching Load, Tenured
                    String[][] selectOptions = new String[academicsFields.length][];
                    selectOptions[1] = Arrays.stream(Department.values())
                                            .filter(d -> d != Department.UNASSIGNED)
                                            .map(Department::getCode)
                                            .toArray(String[]::new);
                    selectOptions[2] = Arrays.stream(FacultyRank.values())
                                            .map(FacultyRank::getDisplayName)
                                            .toArray(String[]::new);
                    selectOptions[9] = new String[]{"Yes", "No"};

                    boolean[] hidden = new boolean[academicsFields.length];
                    Arrays.fill(hidden, false);

                    String[] inputs = ConsoleInput.captureFormInputs(
                        yPosition, xPosition, maxLength,
                        academicsFields.length, hidden, selectOptions
                    );

                    if (inputs == null) {
                        ConsoleUI.clearDialogBox(4);
                        ConsoleDisplay.dialogBox("info", "Edit canceled. No changes were made.");
                        ConsoleInput.pressEnterToContinue();
                        displayAcademicStaffMenu(staff);
                        return;
                    }

                    try {
                        // Apply edits
                        if (!inputs[1].isEmpty()) staff.setDepartment(Department.fromCodeOrFullName(inputs[1]));
                        if (!inputs[2].isEmpty()) staff.setRank(FacultyRank.fromString(inputs[2]));
                        if (!inputs[3].isEmpty()) staff.setHireDate(LocalDate.parse(inputs[3], formatter));
                        if (!inputs[4].isEmpty()) staff.setOfficeLocation(inputs[4]);
                        if (!inputs[5].isEmpty()) staff.setSalary(Double.parseDouble(inputs[5]));
                        if (!inputs[6].isEmpty()) staff.updateTeachingHours(Integer.parseInt(inputs[6]) - staff.getTeachingHoursPerWeek());
                        if (!inputs[7].isEmpty()) staff.setMaxTeachingLoad(Integer.parseInt(inputs[7]));
                        if (!inputs[9].isEmpty()) staff.setTenured(inputs[9].equalsIgnoreCase("Yes"));

                        // Save changes
                        CSV.updateAcademicStaffRecord(staff);

                        ConsoleUI.clearDialogBox(4);
                        ConsoleDisplay.dialogBox("success", "Academic information updated successfully!");
                        ConsoleInput.pressEnterToContinue();
                        break;

                    } catch (Exception e) {
                        ConsoleUI.clearDialogBox(4);
                        ConsoleDisplay.dialogBox("error", "Invalid input: " + e.getMessage());
                        ConsoleUI.clearInputFields(yPosition, xPosition, maxLength, academicsFields.length);
                    }
                }
            }
            break;
        }

        displayAcademicStaffMenu(staff);
    }

// ---------------------------
// [METHOD] Display Academic Staff "Courses" menu
// ---------------------------
public static void displayAcademicStaffCoursesMenu(AcademicStaff staff) throws IOException {
    if (staff == null) {
        ConsoleDisplay.dialogBox("error", "Academic staff record is null!");
        return;
    }

    final int yPosition = 7;
    final int tableWidth = 90;
    final int pageSize = 20;

    final int colNo = 4;
    final int colCode = 20;
    final int colTitle = 30;
    final int colCredit = 7;
    final int colYear = 6;
    final int colSem = 10;

    boolean exitMenu = false;

    while (!exitMenu) {
        List<CourseOffering> courses = CSV.fetchCourseOfferingsForStaff(staff);

        if (courses.isEmpty()) {
            ConsoleUI.clearScreen();
            ConsoleDisplay.displayBorder(2, 3);
            ConsoleUI.goTo(4, 0);
            ConsoleDisplay.displayHeaderSubtitle("Academic Staff: Courses Taught");
            System.out.println();

            ConsoleUI.goTo(yPosition, 0);
            ConsoleUI.moveCursor(3);
            ConsoleInput.printCentered("No courses assigned.", Settings.CONSOLE_WIDTH - 6);
            ConsoleUI.goTo(Settings.CONSOLE_HEIGHT - 4, 0);
            ConsoleUI.moveCursor(3);
            ConsoleInput.printCentered("[A] Add Course  |  [ESC] Back", Settings.CONSOLE_WIDTH - 6);

            int key = ConsoleInput.readKey();
            if (key == Settings.ESC_KEY) exitMenu = true;
            else if (key == 'A' || key == 'a') {
                if (handleCourseOfferingAddition(staff, pageSize)) continue;
            }
            continue;
        }

        int totalPages = Math.max(1, (courses.size() + pageSize - 1) / pageSize);
        int currentPage = 0;

        boolean refresh = false;

        while (true) {
            ConsoleUI.clearScreen();
            ConsoleDisplay.displayBorder(2, 3);
            ConsoleUI.goTo(4, 0);
            ConsoleDisplay.displayHeaderSubtitle(
                "Academic Staff: Courses Taught (Page " + (currentPage + 1) + "/" + totalPages + ")"
            );
            System.out.println();

            int start = currentPage * pageSize;
            int end = Math.min(start + pageSize, courses.size());

            // ===== TABLE TOP BORDER =====
            ConsoleUI.goTo(yPosition, 0);
            ConsoleUI.moveCursor(3);
            ConsoleInput.printCentered("=".repeat(tableWidth), Settings.CONSOLE_WIDTH - 6);

            // ===== TABLE HEADER =====
            ConsoleUI.goTo(yPosition + 1, 0);
            String headerRow = String.format(
                "| %-"+(colNo-1)+"s | %-"+(colCode-1)+"s | %-"+(colTitle-1)+"s | %-"+(colCredit-1)+"s | %-"+(colYear-1)+"s | %-"+(colSem-1)+"s |",
                "No", "Code", "Title", "Credit", "Year", "Sem"
            );
            ConsoleUI.moveCursor(3);
            ConsoleInput.printCentered(headerRow, Settings.CONSOLE_WIDTH - 6);

            // ===== DIVIDER =====
            ConsoleUI.goTo(yPosition + 2, 0);
            ConsoleUI.moveCursor(3);
            ConsoleInput.printCentered("-".repeat(tableWidth), Settings.CONSOLE_WIDTH - 6);

            // ===== TABLE ROWS =====
            int rowY = yPosition + 3;
            for (int i = start; i < end; i++) {
                CourseOffering offering = courses.get(i);
                Course course = offering.getCourse();

                String codeShown = ConsoleUI.truncate(offering.getOfferingId(), colCode-1);
                String titleShown = ConsoleUI.truncate(course != null ? course.getTitle() : "N/A", colTitle-1);
                String creditShown = course != null ? String.valueOf(course.getCredits()) : "N/A";
                String yearShown = String.valueOf(offering.getYear());
                String semText = offering.getSemester() == Semester.FIRST_SEM ? "1st Sem" : "2nd Sem";

                String row = String.format(
                    "| %-"+(colNo-1)+"d | %-"+(colCode-1)+"s | %-"+(colTitle-1)+"s | %-"+(colCredit-1)+"s | %-"+(colYear-1)+"s | %-"+(colSem-1)+"s |",
                    (i - start) + 1, codeShown, titleShown, creditShown, yearShown, semText
                );

                ConsoleUI.goTo(rowY++, 0);
                ConsoleUI.moveCursor(3);
                ConsoleInput.printCentered(row, Settings.CONSOLE_WIDTH - 6);
            }

            // ===== TABLE BOTTOM BORDER =====
            ConsoleUI.goTo(rowY, 0);
            ConsoleUI.moveCursor(3);
            ConsoleInput.printCentered("=".repeat(tableWidth), Settings.CONSOLE_WIDTH - 6);

            // ===== NAVIGATION =====
            ConsoleUI.goTo(Settings.CONSOLE_HEIGHT - 6, 0);
            ConsoleUI.moveCursor(3);
            String nav = "";
            if (totalPages > 1) {
                if (currentPage > 0) nav += "[P] Prev  ";
                if (currentPage < totalPages - 1) nav += "[N] Next  ";
            }
            nav += "[A] Add  |  [R] Remove  |  [ESC] Back";
            ConsoleInput.printCentered(nav, tableWidth);

            int key = ConsoleInput.readKey();
            if (key == Settings.ESC_KEY) {
                exitMenu = true;
                break;
            } else if ((key == 'N' || key == 'n') && currentPage < totalPages - 1) currentPage++;
            else if ((key == 'P' || key == 'p') && currentPage > 0) currentPage--;
            else if ((key == 'A' || key == 'a') && handleCourseOfferingAddition(staff, pageSize)) {
                refresh = true;
                break;
            } else if ((key == 'R' || key == 'r') && handleCourseOfferingRemoval(staff)) {
                ConsoleUI.clearDialogBox(8);
                refresh = true;
                break;
            }
        }

        if (exitMenu) break;
        if (refresh) continue;
    }

    displayAcademicStaffMenu(staff);
}

    private static boolean handleCourseOfferingAddition(AcademicStaff staff, int pageSize) throws IOException {
        List<Course> availableCourses = CourseCatalog.getAllCourses();
        if (availableCourses.isEmpty()) {
            ConsoleUI.clearDialogBox(4);
            ConsoleDisplay.dialogBox("info", "No courses available in the catalog.");
            ConsoleInput.pressEnterToContinue();
            return false;
        }

        int totalPages = (availableCourses.size() + pageSize - 1) / pageSize;
        int currentPage = 0;

        while (true) {
            ConsoleUI.clearScreen();
            ConsoleDisplay.displayBorder(2, 3);
            ConsoleUI.goTo(4, 0);
            ConsoleDisplay.displayHeaderSubtitle("Add Course to Teaching Load (Page " + (currentPage + 1) + "/" + totalPages + ")");
            System.out.println();

            int start = currentPage * pageSize;
            int end = Math.min(start + pageSize, availableCourses.size());

            for (int i = start; i < end; i++) {
                Course course = availableCourses.get(i);
                String code = course.getCourseCode();
                if (code == null || code.isBlank()) {
                    code = CSV.abbreviateCourse(course.getTitle());
                }
                ConsoleUI.moveCursor(14);
                System.out.printf("[%d] %s - %s%n", i + 1, code, CSV.abbreviateCourse(course.getTitle()));
            }

            ConsoleUI.goTo(Settings.CONSOLE_HEIGHT - 4, 0);
            ConsoleUI.moveCursor(3);
            String nav = totalPages > 1
                    ? "[<] Previous  |  [>] Next  |  [S] Select Course  |  [ESC] Cancel"
                    : "[S] Select Course  |  [ESC] Cancel";
            ConsoleInput.printCentered(nav, Settings.CONSOLE_WIDTH - 6);

            int key = ConsoleInput.readKey();
            if (key == Settings.ESC_KEY) {
                return false;
            } else if (key == Settings.RIGHT_KEY && currentPage < totalPages - 1) {
                currentPage++;
            } else if (key == Settings.LEFT_KEY && currentPage > 0) {
                currentPage--;
            } else if (key == 'S' || key == 's' || key == 10 || key == 13) {
                ConsoleUI.clearDialogBox(8);
                Integer selection = promptCourseSelectionNumber(availableCourses.size());
                if (selection == null) continue;

                Course selectedCourse = availableCourses.get(selection - 1);
                CourseOffering offering = promptCourseOfferingDetails(staff, selectedCourse);
                if (offering == null) {
                    ConsoleUI.clearDialogBox(4);
                    ConsoleDisplay.dialogBox("info", "Course offering creation canceled.");
                    ConsoleInput.pressEnterToContinue();
                    continue;
                }

                staff.addCourseOffering(offering);
                CSV.updateAcademicStaffRecord(staff);
                CSV.appendRow(Settings.COURSE_OFFERINGS_FILE, CSV.buildCourseOfferingCsvRow(offering));

                ConsoleUI.clearDialogBox(4);
                ConsoleDisplay.dialogBox("success", "Course offering created successfully!");
                ConsoleInput.pressEnterToContinue();
                return true;
            }
        }
    }

public static boolean handleCourseOfferingRemoval(AcademicStaff staff) {

    try {
        List<CourseOffering> allOfferings = CSV.fetchAllCourseOfferings();
        List<CourseOffering> staffOfferings = allOfferings.stream()
                .filter(o -> o.getInstructor() != null && o.getInstructor().getPersonId().equals(staff.getPersonId()))
                .collect(Collectors.toList());

        if (staffOfferings.isEmpty()) {
            ConsoleDisplay.dialogBox("info", "No courses to remove.");
            ConsoleInput.pressEnterToContinue();
            return false;
        }

        // Display offerings with index
        for (int i = 0; i < staffOfferings.size(); i++) {
            CourseOffering o = staffOfferings.get(i);
            String title = (o.getCourse() != null) ? o.getCourse().getTitle() : "Unknown";
            System.out.printf("[%d] %s (%s)\n", i + 1, title, o.getOfferingId());
        }

        ConsoleUI.drawInputFields(40, "Select course to remove (index)");
        String[] input = ConsoleInput.captureFormInputs(Settings.CONSOLE_HEIGHT - 7, 69, 2, 1);
        if (input == null) return false;

        int sel = Integer.parseInt(input[0].trim()) - 1;
        if (sel < 0 || sel >= staffOfferings.size()) {
            ConsoleDisplay.dialogBox("error", "Invalid choice.");
            return false;
        }

        CourseOffering toRemove = staffOfferings.get(sel);

        // Remove from global CSV
        List<String[]> allRows = CSV.fetchAllCourseOfferingsRaw();
        List<String[]> filtered = new ArrayList<>();
        filtered.add(allRows.get(0)); // header
        for (int i = 1; i < allRows.size(); i++) {
            String[] row = allRows.get(i);
            if (!row[0].equals(toRemove.getOfferingId())) {
                filtered.add(row);
            }
        }
        CSV.saveAllCourseOfferingsRaw(filtered);

        // Remove from staff in-memory
        staff.removeCourseOffering(toRemove);
        CSV.updateAcademicStaffRecord(staff);

        ConsoleDisplay.dialogBox("success", "Course removed.");
        ConsoleInput.pressEnterToContinue();
        return true;

    } catch (IOException | NumberFormatException ex) {
        ConsoleDisplay.dialogBox("error", "Error removing course: " + ex.getMessage());
        return false;
    }
}


    private static Integer promptCourseSelectionNumber(int totalCourses) throws IOException {
        while (true) {
            ConsoleUI.goTo(Settings.CONSOLE_HEIGHT - 8, 0);
            ConsoleUI.drawInputFields(24, "Course No. to add");
            int inputX = 60;
            String[] inputArray = ConsoleInput.captureFormInputs(Settings.CONSOLE_HEIGHT - 7, inputX, 2, 1);
            if (inputArray == null) return null;

            String input = inputArray[0].trim();
            if (input.isEmpty()) {
                ConsoleUI.clearDialogBox(4);
                ConsoleDisplay.dialogBox("error", "Please enter a number.");
                continue;
            }

            try {
                int choice = Integer.parseInt(input);
                if (choice < 1 || choice > totalCourses) {
                    ConsoleUI.clearDialogBox(4);
                    ConsoleDisplay.dialogBox("error", "Choice must be between 1 and " + totalCourses + ".");
                    continue;
                }
                return choice;
            } catch (NumberFormatException e) {
                ConsoleUI.clearDialogBox(4);
                ConsoleDisplay.dialogBox("error", "Invalid number. Try again.");
            }
        }
    }

    private static CourseOffering promptCourseOfferingDetails(AcademicStaff staff, Course course) throws IOException {
        final String[] fields = {
                "Semester (First/Second)",
                "Year (e.g., 2025)",
                "Capacity",
                "Day (e.g., MON or Monday)",
                "Start Time (e.g., 10:30 AM)",
                "End Time (e.g., 12:00 PM)"
        };

        final int yPosition = 9;
        final int xPosition = 57;
        final int maxLength = 18;

        while (true) {
            ConsoleUI.clearScreen();
            ConsoleDisplay.displayBorder(2, 3);
            ConsoleUI.goTo(4, 0);
            ConsoleDisplay.displayHeaderSubtitle("Create Course Offering");

            ConsoleUI.goTo(6, 25);
            System.out.printf("Course: %s%n", course.getTitle());
            ConsoleUI.goTo(7, 25);
            System.out.printf("Instructor: %s%n", staff.getFullName());

            ConsoleUI.drawInputFields(50, fields);
            ConsoleUI.goTo(Settings.CONSOLE_HEIGHT - 4, 0);
            ConsoleUI.moveCursor(3);
            ConsoleInput.printCentered("Fill in the details and press [ENTER]. Press [ESC] to cancel.", Settings.CONSOLE_WIDTH - 6);

            String[] inputs = ConsoleInput.captureFormInputs(yPosition, xPosition, maxLength, fields.length);
            if (inputs == null) return null;

            try {
                Semester semester = CSV.parseSemesterInput(inputs[0]);
                int year = CSV.parsePositiveInt(inputs[1], "Year");
                int capacity = CSV.parsePositiveInt(inputs[2], "Capacity");
                DayOfWeek day = CSV.parseDayOfWeekInput(inputs[3]);
                LocalTime start = CSV.parseTimeInput(inputs[4]);
                LocalTime end = CSV.parseTimeInput(inputs[5]);
                TimeSlot schedule = new TimeSlot(day, start, end);
                String offeringId = CSV.generateOfferingId(course, staff);

                return new CourseOffering(
                        offeringId,
                        course,
                        semester,
                        year,
                        staff,
                        schedule,
                        capacity,
                        0
                );
            } catch (IllegalArgumentException | DateTimeParseException e) {
                ConsoleUI.clearDialogBox(4);
                ConsoleDisplay.dialogBox("error", e.getMessage());
                ConsoleInput.pressEnterToContinue();
            }
        }
    }

    // * UI: NonAcademicStaff Menu
    // [METHOD] Display "NonAcademicStaff" menu
    public static void displayNonAcademicStaffMenu(NonAcademicStaff staff) throws IOException {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(16, 0);
        ConsoleDisplay.displayHeaderSubtitle("Administrator Menu");

        // Display top border
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("=".repeat(22), Settings.CONSOLE_WIDTH - 6);
        
        // Display operations
        for (MenuOperation operation : NonAcademicStaffMenuOperations) {
            ConsoleUI.moveCursor(39);
            ConsoleAnimation.lineDelayAnimation('[' + operation.inputKeys[0] + "] " + operation.getDisplayName(), 50); System.out.println();
        }

        // Display bottom border
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("=".repeat(22), Settings.CONSOLE_WIDTH - 6);

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
        ConsoleUI.clearScreen();
        ConsoleDisplay.displayBorder(2, 3);
        ConsoleUI.goTo(4, 0);
        ConsoleDisplay.displayHeaderSubtitle("Administrator: Add Student");
        ConsoleUI.goTo(5, 0);
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("Fill out all fields. Press [ESC] to cancel.", Settings.CONSOLE_WIDTH - 6, true);

        String[] fields = {
            "First Name", "Middle Name", "Last Name", "Date of Birth (MM-dd-YYYY)",
            "Gender", "Address", "Contact Number", "Email",
            "Enrollment Date (MM-dd-YYYY)", "Department", "Course", "Year Level"
        };

        int yPos = 8, xPos = 43, maxLength = 47;
        ConsoleUI.drawInputFields(80, fields);

        while (true) {
            // Step 1: Capture basic info + enrollment + department
            String[][] selectOptions = new String[fields.length][];
            selectOptions[0] = null; selectOptions[1] = null; selectOptions[2] = null; selectOptions[3] = null;
            selectOptions[4] = new String[]{"Male", "Female", "Prefer not to say"};
            selectOptions[5] = null; selectOptions[6] = null; selectOptions[7] = null;
            selectOptions[8] = null; // Enrollment date free text

            selectOptions[9] = Arrays.stream(Department.values())
                                    .filter(d -> d != Department.UNASSIGNED)
                                    .map(Department::getCode)
                                    .toArray(String[]::new);

            boolean[] hiddenFields = new boolean[fields.length];
            Arrays.fill(hiddenFields, false);

            // Capture first 10 fields (up to Department)
            String[] step1Inputs = ConsoleInput.captureFormInputs(yPos, xPos, maxLength, 10, hiddenFields, Arrays.copyOf(selectOptions, 10));
            if (step1Inputs == null) { displayLoginScreen(); return; }

            // Step 2: Populate courses based on selected department
            String deptCode = step1Inputs[9];
            Department dept = Department.fromCodeOrFullName(deptCode);
            String[] courseOptions = switch (dept) {
                case CAS -> Arrays.stream(Courses.CASCourse.values())
                                .map(c -> Course.generateCourseShortName(c.getFullName()))
                                .toArray(String[]::new);
                case CICT -> Arrays.stream(Courses.CICTCourse.values())
                                .map(c -> Course.generateCourseShortName(c.getFullName()))
                                .toArray(String[]::new);
                case CBM -> Arrays.stream(Courses.CBMCourse.values())
                                .map(c -> Course.generateCourseShortName(c.getFullName()))
                                .toArray(String[]::new);
                case COP -> Arrays.stream(Courses.COPCourse.values())
                                .map(c -> Course.generateCourseShortName(c.getFullName()))
                                .toArray(String[]::new);
                case COM -> Arrays.stream(Courses.COMCourse.values())
                                .map(c -> Course.generateCourseShortName(c.getFullName()))
                                .toArray(String[]::new);
                case COE -> Arrays.stream(Courses.COECourse.values())
                                .map(c -> Course.generateCourseShortName(c.getFullName()))
                                .toArray(String[]::new);
                case CON -> Arrays.stream(Courses.CONCourse.values())
                                .map(c -> Course.generateCourseShortName(c.getFullName()))
                                .toArray(String[]::new);
                case COC -> Arrays.stream(Courses.COCCourse.values())
                                .map(c -> Course.generateCourseShortName(c.getFullName()))
                                .toArray(String[]::new);
                case COL -> Arrays.stream(Courses.COLCourse.values())
                                .map(c -> Course.generateCourseShortName(c.getFullName()))
                                .toArray(String[]::new);
                case COD -> Arrays.stream(Courses.CODCourse.values())
                                .map(c -> Course.generateCourseShortName(c.getFullName()))
                                .toArray(String[]::new);
                case ILS -> Arrays.stream(Courses.ILSCourse.values())
                                .map(c -> Course.generateCourseShortName(c.getFullName()))
                                .toArray(String[]::new);
                default -> new String[0];
            };

            selectOptions[10] = courseOptions; // Course
            selectOptions[11] = new String[]{"Freshman (1st Year)", "Sophomore (2nd Year)", "Junior (3rd Year)", "Senior (4th Year)"}; // Year Level

            // Capture Course + Year Level
            String[] step2Inputs = ConsoleInput.captureFormInputs(yPos + 20, xPos, maxLength, 2, hiddenFields,
                                            new String[][]{courseOptions, selectOptions[11]});
            if (step2Inputs == null) { displayLoginScreen(); return; }

            // Merge all inputs
            String[] finalInputs = Arrays.copyOf(step1Inputs, fields.length);
            finalInputs[10] = step2Inputs[0]; // Course
            finalInputs[11] = step2Inputs[1]; // Year Level

            // Validate & save
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                LocalDate dob = LocalDate.parse(finalInputs[3], formatter);
                LocalDate enrollmentDate = LocalDate.parse(finalInputs[8], formatter);
                Gender gender = Gender.valueOf(finalInputs[4].toUpperCase());
                Course course = Course.fromShortName(finalInputs[10]);
                YearLevel yearLevel = YearLevel.fromDisplayName(finalInputs[11]);

                Student student = new UndergraduateStudent(
                    finalInputs[0], finalInputs[1], finalInputs[2], dob, gender,
                    finalInputs[5], finalInputs[6], finalInputs[7],
                    enrollmentDate, dept, course, AcademicStanding.GOOD, yearLevel
                );

                CSV.appendRow(Settings.STUDENTS_FILE, student.toCSVRow());
                currentStudent = student;
                ConsoleDisplay.dialogBox("success", "Student added successfully!");
                ConsoleInput.pressEnterToContinue();
                displayNonAcademicStaffMenu(currentNonAcademicStaff);
                return;

            } catch (Exception e) {
                ConsoleDisplay.dialogBox("error", "Invalid input: " + e.getMessage());
            }
        }
    }

    private static void addFacultyMenu() throws IOException {
        String[] page1Fields = {
            "First Name", "Middle Name", "Last Name", "Date of Birth (MM-dd-YYYY)",
            "Gender (Male/Female/Other)", "Address", "Contact Number", "Email",
            "Faculty Type (Academic/NonAcademic)"
        };

        int yPos = 8, xPos = 50, maxLength = 40;

        ConsoleUI.clearScreen();
        ConsoleDisplay.displayBorder(2, 3);
        ConsoleUI.goTo(4, 0);
        ConsoleDisplay.displayHeaderSubtitle("Administrator: Add Faculty (Page 1 of 2)");
        ConsoleUI.goTo(5, 0);
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("Fill out all fields. Press [ESC] to cancel.", Settings.CONSOLE_WIDTH - 6, true);

        ConsoleUI.drawInputFields(80, page1Fields);

        boolean[] hiddenFieldsPage1 = new boolean[page1Fields.length];
        Arrays.fill(hiddenFieldsPage1, false);

        String[][] selectOptionsPage1 = new String[page1Fields.length][];
        selectOptionsPage1[4] = new String[]{"Male", "Female", "Other"}; // Gender
        selectOptionsPage1[8] = new String[]{"Academic", "NonAcademic"}; // Faculty Type

        // Capture page 1 inputs
        String[] page1Input = ConsoleInput.captureFormInputs(yPos, xPos, maxLength, page1Fields.length, hiddenFieldsPage1, selectOptionsPage1);
        if (page1Input == null) {
            ConsoleDisplay.dialogBox("info", "Add faculty canceled.");
            ConsoleInput.pressEnterToContinue();
            displayNonAcademicStaffMenu(currentNonAcademicStaff);
            return;
        }

        // Extract page 1 values
        String firstName = page1Input[0].trim();
        String middleName = page1Input[1].trim();
        String lastName = page1Input[2].trim();
        String dobStr = page1Input[3].trim();
        String genderStr = page1Input[4].trim();
        String address = page1Input[5].trim();
        String contact = page1Input[6].trim();
        String email = page1Input[7].trim();
        String facultyType = page1Input[8].trim().toLowerCase();

        if (firstName.isBlank() || lastName.isBlank() || dobStr.isBlank() ||
            genderStr.isBlank() || address.isBlank() || contact.isBlank() || email.isBlank() || facultyType.isBlank()) {
            ConsoleDisplay.dialogBox("error", "Please fill in all required fields on Page 1.");
            ConsoleInput.pressEnterToContinue();
            addFacultyMenu();
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate dob = LocalDate.parse(dobStr, formatter);
        Gender gender = Gender.valueOf(genderStr.toUpperCase());

        // ----------------- PAGE 2 ----------------- //
        String[] page2Fields = {
            "Department", "Rank/Position", "Hire Date (MM-dd-YYYY)", "Office Location",
            "Salary", "Teaching Hours/Week", "Max Teaching Load", "Tenured (Yes/No)"
        };

        yPos = 8;

        ConsoleUI.clearScreen();
        ConsoleDisplay.displayBorder(2, 3);
        ConsoleUI.goTo(4, 0);
        ConsoleDisplay.displayHeaderSubtitle("Administrator: Add Faculty (Page 2 of 2)");
        ConsoleUI.goTo(5, 0);
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("Continue filling out the remaining details.", Settings.CONSOLE_WIDTH - 6, true);

        ConsoleUI.drawInputFields(80, page2Fields);

        boolean[] hiddenFieldsPage2 = new boolean[page2Fields.length];
        Arrays.fill(hiddenFieldsPage2, false);

        String[] deptOptions = Arrays.stream(Department.values())
                                    .filter(d -> d != Department.UNASSIGNED)
                                    .map(Department::getCode)
                                    .toArray(String[]::new);

        String[] rankOptions = null;
        if (facultyType.equals("academic")) {
            rankOptions = Arrays.stream(FacultyRank.values())
                                .map(FacultyRank::getDisplayName)
                                .toArray(String[]::new);
        }

        String[][] selectOptionsPage2 = new String[page2Fields.length][];
        selectOptionsPage2[0] = deptOptions;
        selectOptionsPage2[1] = rankOptions; // null for non-academic staff

        // Capture page 2 inputs
        String[] page2Input = ConsoleInput.captureFormInputs(yPos, xPos - 13, maxLength, page2Fields.length, hiddenFieldsPage2, selectOptionsPage2);
        if (page2Input == null) {
            ConsoleDisplay.dialogBox("info", "Add faculty canceled.");
            ConsoleInput.pressEnterToContinue();
            displayNonAcademicStaffMenu(currentNonAcademicStaff);
            return;
        }

        try {
            Department dept = Department.fromCodeOrFullName(page2Input[0].trim());
            String rankOrPosition = page2Input[1].trim();
            LocalDate hireDate = LocalDate.parse(page2Input[2].trim(), formatter);
            String officeLocation = page2Input[3].trim();
            double salary = Double.parseDouble(page2Input[4].trim());
            int teachingHours = Integer.parseInt(page2Input[5].trim());
            int maxLoad = Integer.parseInt(page2Input[6].trim());
            boolean tenured = page2Input[7].trim().equalsIgnoreCase("yes") || page2Input[7].trim().equalsIgnoreCase("true");

            if (facultyType.equals("academic")) {
                FacultyRank rank = FacultyRank.fromString(rankOrPosition);
                
                // Generate a new Person ID for the staff
                String personId = CSV.generatePersonId();
                
                AcademicStaff staff = new AcademicStaff(
                    personId,
                    firstName, middleName, lastName, 
                    dob, gender,
                    address, contact, email,
                    dept, rank, hireDate, officeLocation,
                    salary, tenured,
                    new ArrayList<GraduateStudent>(),
                    teachingHours, maxLoad
                );

                CSV.appendRow(Settings.ACADEMIC_STAFF_FILE, staff.toCSVRow());
                ConsoleDisplay.dialogBox("success", "Academic Faculty added successfully!");
            } else {
                NonAcademicStaff staff = new NonAcademicStaff(
                    firstName, middleName, lastName, dob, gender,
                    address, contact, email,
                    dept, rankOrPosition, hireDate, officeLocation,
                    salary, teachingHours
                );
                CSV.appendRow(Settings.NON_ACADEMIC_STAFF_FILE, staff.toCSVRow());
                ConsoleDisplay.dialogBox("success", "Non-Academic Staff added successfully!");
            }

        } catch (Exception e) {
            ConsoleDisplay.dialogBox("error", "Failed to add faculty: " + e.getMessage());
        }

        ConsoleInput.pressEnterToContinue();
        displayNonAcademicStaffMenu(currentNonAcademicStaff);
    }

    private static void viewAllStudents() throws IOException {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Administrator: Student Records");
        System.out.println();

        List<String[]> rows = CSV.readCSV(Settings.STUDENTS_FILE);
        List<String[]> students = new ArrayList<>();
        for (String[] row : rows) {
            if (!CSV.safeValue(row, Settings.COL_PERSON_ID).isBlank()) {
                students.add(row);
            }
        }

        if (students.isEmpty()) {
            ConsoleDisplay.dialogBox("info", "No student records found.");
            ConsoleInput.pressEnterToContinue();
            displayNonAcademicStaffMenu(currentNonAcademicStaff);
            return;
        }

        int tableWidth = 70;
        String border = "=".repeat(tableWidth + 11);
        String separator = "-".repeat(tableWidth + 11);
        String header = String.format("| %-3s | %-12s | %-28s | %-12s | %-10s |",
                "No", "Student ID", "Name", "Dept", "Course");

        ConsoleUI.moveCursor(5); ConsoleInput.printCentered(border, Settings.CONSOLE_WIDTH - 8);
        ConsoleUI.moveCursor(5); ConsoleInput.printCentered(header, Settings.CONSOLE_WIDTH - 8);
        ConsoleUI.moveCursor(5); ConsoleInput.printCentered(separator, Settings.CONSOLE_WIDTH - 8);

        int maxDisplay = 15;
        int displayed = 0;
        for (int i = 0; i < students.size() && displayed < maxDisplay; i++) {
            String[] row = students.get(i);
            String studentId = CSV.safeValue(row, Settings.COL_PERSON_ID);
            String firstName = CSV.safeValue(row, Settings.COL_FIRST_NAME);
            String middleName = CSV.safeValue(row, Settings.COL_MIDDLE_NAME);
            String lastName = CSV.safeValue(row, Settings.COL_LAST_NAME);
            String dept = CSV.abbreviateDepartment(CSV.safeValue(row, Settings.COL_DEPARTMENT));
            String course = CSV.abbreviateCourse(CSV.safeValue(row, Settings.COL_COURSE));
            String name = (firstName + " " + (middleName.isBlank() ? "" : middleName + " ") + lastName).trim().replaceAll("\\s+", " ");

            // Truncate with ellipsis
            name = ConsoleUI.truncate(name, 28);
            dept = ConsoleUI.truncate(dept, 12);
            course = ConsoleUI.truncate(course, 10);

            String body = String.format("| %-3d | %-12s | %-28s | %-12s | %-10s |",
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
        displayNonAcademicStaffMenu(currentNonAcademicStaff);
    }

    private static void viewAllFaculty() throws IOException {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Administrator: Faculty Records");
        System.out.println();

        List<String[]> academicRows = CSV.readCSV(Settings.ACADEMIC_STAFF_FILE);
        List<String[]> nonAcademicRows = CSV.readCSV(Settings.NON_ACADEMIC_STAFF_FILE);

        if (academicRows.isEmpty() && nonAcademicRows.isEmpty()) {
            ConsoleDisplay.dialogBox("info", "No faculty records found.");
            ConsoleInput.pressEnterToContinue();
            displayNonAcademicStaffMenu(currentNonAcademicStaff);
            return;
        }

        // Column widths
        int noWidth = 3;
        int idWidth = 12;
        int nameWidth = 28;
        int deptWidth = 10; // reduced by 2
        int rankWidth = 10;

        // Calculate table width including separators (" | " adds 3 chars per column + 1 extra "|")
        int tableWidth = noWidth + idWidth + nameWidth + deptWidth + rankWidth + 6 * 3 + 1;
        String border = "=".repeat(tableWidth);
        String separator = "-".repeat(tableWidth);

        String header = String.format("| %-3s | %-12s | %-28s | %-10s | %-10s |",
                "No", "Faculty ID", "Name", "Dept", "Rank/Position");

        // Center only border and header
        ConsoleUI.moveCursor(4); ConsoleInput.printCentered(border, Settings.CONSOLE_WIDTH - 8);
        ConsoleUI.moveCursor(4); ConsoleInput.printCentered(header, Settings.CONSOLE_WIDTH - 8);
        ConsoleUI.moveCursor(4); ConsoleInput.printCentered(separator, Settings.CONSOLE_WIDTH - 8);

        int maxDisplay = 15;
        int displayed = 0;
        int index = 1;

        // Display Academic Staff
        for (String[] row : academicRows) {
            String facultyId = CSV.safeValue(row, Settings.COL_PERSON_ID);
            String firstName = CSV.safeValue(row, Settings.COL_FIRST_NAME);
            String middleName = CSV.safeValue(row, Settings.COL_MIDDLE_NAME);
            String lastName = CSV.safeValue(row, Settings.COL_LAST_NAME);
            String dept = CSV.abbreviateDepartment(CSV.safeValue(row, Settings.COL_DEPARTMENT_FAC));
            String rank = CSV.safeValue(row, Settings.COL_RANK);
            String name = (firstName + " " + (middleName.isBlank() ? "" : middleName + " ") + lastName)
                    .trim().replaceAll("\\s+", " ");

            if (facultyId.isBlank() && name.isBlank()) continue;

            // Truncate with ConsoleUI.truncate
            name = ConsoleUI.truncate(name, nameWidth);
            dept = ConsoleUI.truncate(dept, deptWidth);
            rank = ConsoleUI.truncate(rank, rankWidth);

            String body = String.format("| %-3d | %-12s | %-28s | %-10s | %-13s |",
                    index++, facultyId, name, dept, rank);
            ConsoleUI.moveCursor(4); ConsoleInput.printCentered(body, Settings.CONSOLE_WIDTH - 8);
            displayed++;
            if (displayed >= maxDisplay) break;
        }

        // Display Non-Academic Staff if space remains
        if (displayed < maxDisplay) {
            for (String[] row : nonAcademicRows) {
                String facultyId = CSV.safeValue(row, Settings.COL_PERSON_ID);
                String firstName = CSV.safeValue(row, Settings.COL_FIRST_NAME);
                String middleName = CSV.safeValue(row, Settings.COL_MIDDLE_NAME);
                String lastName = CSV.safeValue(row, Settings.COL_LAST_NAME);
                String dept = CSV.abbreviateDepartment(CSV.safeValue(row, Settings.COL_DEPARTMENT_FAC));
                String position = CSV.safeValue(row, Settings.COL_POSITION);
                String name = (firstName + " " + (middleName.isBlank() ? "" : middleName + " ") + lastName)
                        .trim().replaceAll("\\s+", " ");

                if (facultyId.isBlank() && name.isBlank()) continue;

                name = ConsoleUI.truncate(name, nameWidth);
                dept = ConsoleUI.truncate(dept, deptWidth);
                position = ConsoleUI.truncate(position, rankWidth);

                String body = String.format("| %-3d | %-12s | %-28s | %-10s | %-13s |",
                        index++, facultyId, name, dept, position);
                ConsoleUI.moveCursor(4); ConsoleInput.printCentered(body, Settings.CONSOLE_WIDTH - 8);
                displayed++;
                if (displayed >= maxDisplay) break;
            }
        }

        // Center only border at the bottom
        ConsoleUI.moveCursor(5); ConsoleInput.printCentered(border, Settings.CONSOLE_WIDTH - 9);

        int totalRecords = academicRows.size() + nonAcademicRows.size();
        if (totalRecords > maxDisplay) {
            ConsoleDisplay.dialogBox("info", "Displaying first " + maxDisplay + " of " + totalRecords + " faculty records.");
        } else {
            ConsoleDisplay.dialogBox("info", "Total faculty: " + totalRecords);
        }

        ConsoleInput.pressEnterToContinue();
        displayNonAcademicStaffMenu(currentNonAcademicStaff);
    }

    private static void removeStudentMenu() throws IOException {
        // Display UI
        ConsoleUI.clearScreen();
        ConsoleDisplay.displayBorder(2, 3);
        ConsoleUI.goTo(4, 0);
        ConsoleDisplay.displayHeaderSubtitle("Administrator: Remove Student");

        ConsoleUI.goTo(6, 0);
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("Enter the Student ID or email. Press [ESC] to cancel.", Settings.CONSOLE_WIDTH - 6, true);

        ConsoleUI.drawInputFields(66, "Student ID / Email");
        int yPosition = 9;
        int xPosition = 40;
        int maxLength = 43;

        String[] inputs = ConsoleInput.captureFormInputs(yPosition, xPosition, maxLength, 1);

        if (inputs == null) {
            ConsoleUI.clearDialogBox(4);
            ConsoleDisplay.dialogBox("info", "Remove student canceled.");
            ConsoleInput.pressEnterToContinue();
            displayNonAcademicStaffMenu(currentNonAcademicStaff);
            return;
        }

        String identifier = inputs[0].trim();

        if (identifier.isBlank()) {
            ConsoleUI.clearDialogBox(4);
            ConsoleDisplay.dialogBox("error", "Identifier cannot be empty.");
            ConsoleInput.pressEnterToContinue();
            displayNonAcademicStaffMenu(currentNonAcademicStaff);
            return;
        }

        List<String[]> rows = CSV.readCSV(Settings.STUDENTS_FILE);
        List<String> removedIds = new ArrayList<>();
        boolean removed = false;

        for (int i = rows.size() - 1; i >= 0; i--) {
            String[] row = rows.get(i);
            String studentId = CSV.safeValue(row, Settings.COL_PERSON_ID);
            String email = CSV.safeValue(row, Settings.COL_EMAIL);

            if (identifier.equalsIgnoreCase(studentId) || identifier.equalsIgnoreCase(email)) {
                rows.remove(i);
                removedIds.add(studentId);
                removed = true;
            }
        }

        if (removed) {
            CSV.writeCSV(Settings.STUDENTS_FILE, rows);
            for (String id : removedIds) {
                CSV.removeStudentEnrollments(id);
            }
            ConsoleDisplay.dialogBox("success", "Student record removed.");
        } else {
            ConsoleDisplay.dialogBox("error", "Student not found.");
        }

        ConsoleInput.pressEnterToContinue();
        displayNonAcademicStaffMenu(currentNonAcademicStaff);
    }

    private static void removeFacultyMenu() throws IOException {
        // --- Display UI ---
        ConsoleUI.clearScreen();
        ConsoleDisplay.displayBorder(2, 3);
        ConsoleUI.goTo(4, 0);
        ConsoleDisplay.displayHeaderSubtitle("Administrator: Remove Faculty / Administrator");

        ConsoleUI.goTo(6, 0);
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("Enter the Faculty/Staff ID or Email. Press [ESC] to cancel.", Settings.CONSOLE_WIDTH - 6, true);

        ConsoleUI.drawInputFields(66, "ID / Email");
        int yPos = 9, xPos = 32, maxLength = 43;

        String[] inputs = ConsoleInput.captureFormInputs(yPos, xPos, maxLength, 1);

        if (inputs == null) {
            ConsoleDisplay.dialogBox("info", "Remove operation canceled.");
            ConsoleInput.pressEnterToContinue();
            displayNonAcademicStaffMenu(currentNonAcademicStaff);
            return;
        }

        String identifier = inputs[0].trim();
        if (identifier.isBlank()) {
            ConsoleDisplay.dialogBox("error", "Identifier cannot be empty.");
            ConsoleInput.pressEnterToContinue();
            displayNonAcademicStaffMenu(currentNonAcademicStaff);
            return;
        }

        // --- Remove from Teachers CSV ---
        List<String[]> teacherRows = CSV.readCSV(Settings.ACADEMIC_STAFF_FILE);
        List<String> removedTeacherIds = new ArrayList<>();
        boolean removedTeacher = false;

        for (int i = teacherRows.size() - 1; i >= 0; i--) {
            String[] row = teacherRows.get(i);
            String facultyId = CSV.safeValue(row, Settings.COL_PERSON_ID);
            String email     = CSV.safeValue(row, Settings.COL_EMAIL);

            // Ensure trimming to avoid invisible whitespace issues
            if (facultyId != null) facultyId = facultyId.trim();
            if (email != null)     email = email.trim();

            if (identifier.equalsIgnoreCase(facultyId) || identifier.equalsIgnoreCase(email)) {
                removedTeacherIds.add(facultyId);
                teacherRows.remove(i);
                removedTeacher = true;
            }
        }

        if (removedTeacher) {
            CSV.writeCSV(Settings.ACADEMIC_STAFF_FILE, teacherRows);
        }

        // --- Remove from Administrators CSV ---
        List<String[]> adminRows = CSV.readCSV(Settings.NON_ACADEMIC_STAFF_FILE);
        List<String> removedAdminIds = new ArrayList<>();
        boolean removedAdmin = false;

        for (int i = adminRows.size() - 1; i >= 0; i--) {
            String[] row = adminRows.get(i);
            String staffId = CSV.safeValue(row, Settings.COL_PERSON_ID);
            String email   = CSV.safeValue(row, Settings.COL_EMAIL);

            if (staffId != null) staffId = staffId.trim();
            if (email != null)   email = email.trim();

            if (identifier.equalsIgnoreCase(staffId) || identifier.equalsIgnoreCase(email)) {
                removedAdminIds.add(staffId);
                adminRows.remove(i);
                removedAdmin = true;
            }
        }

        if (removedAdmin) {
            CSV.writeCSV(Settings.NON_ACADEMIC_STAFF_FILE, adminRows);
        }

        // --- Result Message ---
        if (removedTeacher || removedAdmin) {
            ConsoleDisplay.dialogBox("success", "Faculty/Administrator record removed successfully.");
        } else {
            ConsoleDisplay.dialogBox("error", "Faculty/Administrator not found.");
        }

        ConsoleInput.pressEnterToContinue();
        displayNonAcademicStaffMenu(currentNonAcademicStaff);
    }

        // * UI: User Authentication
        public static void displayStudentSetupInformationScreen(String email, String userType) throws IOException {
            ConsoleUI.clearScreen();
            ConsoleDisplay.displayBorder(2, 3);
            ConsoleUI.goTo(4, 0);
            ConsoleDisplay.displayHeaderSubtitle("Setup Information");
            System.out.println();

            ConsoleUI.goTo(5, 0);
            ConsoleUI.moveCursor(3);
            ConsoleInput.printCentered("Please fill out all personal information fields to proceed.", Settings.CONSOLE_WIDTH - 3, true);

            String[] fields = {
                "First Name", "Middle Name", "Last Name", "Date of Birth (MM-dd-YYYY)",
                "Gender", "Address", "Contact Number", "Enrollment Date (MM-dd-YYYY)",
                "Department", "Course", "Year Level"
            };
            int yPosition = 8;
            int xPosition = 43;
            int maxLength = 47;
            ConsoleUI.drawInputFields(80, fields);

            while (true) {
                // Step 1: Capture basic info + Department
                String[][] selectOptions = new String[fields.length][];
                selectOptions[0] = null; selectOptions[1] = null; selectOptions[2] = null; selectOptions[3] = null;
                selectOptions[4] = new String[]{"Male", "Female", "Prefer not to say"};
                selectOptions[5] = null; selectOptions[6] = null; selectOptions[7] = null;

                Department[] allDepts = Department.values();
                selectOptions[8] = Arrays.stream(allDepts)
                                        .filter(d -> d != Department.UNASSIGNED)
                                        .map(Department::getCode)
                                        .toArray(String[]::new);

                selectOptions[9] = new String[0]; // Course empty for now
                selectOptions[10] = new String[]{"Freshman (1st Year)", "Sophomore (2nd Year)", "Junior (3rd Year)", "Senior (4th Year)"};

                boolean[] hiddenFields = new boolean[fields.length];
                Arrays.fill(hiddenFields, false);

                String[] inputs = ConsoleInput.captureFormInputs(yPosition, xPosition, maxLength, 9, hiddenFields, selectOptions);
                if (inputs == null) { displayLoginScreen(); return; }

                // Step 2: Populate Course based on selected Department
                String deptCode = inputs[8];
                Department dept = Department.fromCodeOrFullName(deptCode);
                String[] courseOptions = switch (dept) {
                    case CAS -> Arrays.stream(Courses.CASCourse.values())
                                    .map(c -> Course.generateCourseShortName(c.getFullName()))
                                    .toArray(String[]::new);
                    case CICT -> Arrays.stream(Courses.CICTCourse.values())
                                    .map(c -> Course.generateCourseShortName(c.getFullName()))
                                    .toArray(String[]::new);
                    case CBM -> Arrays.stream(Courses.CBMCourse.values())
                                    .map(c -> Course.generateCourseShortName(c.getFullName()))
                                    .toArray(String[]::new);
                    case COP -> Arrays.stream(Courses.COPCourse.values())
                                    .map(c -> Course.generateCourseShortName(c.getFullName()))
                                    .toArray(String[]::new);
                    case COM -> Arrays.stream(Courses.COMCourse.values())
                                    .map(c -> Course.generateCourseShortName(c.getFullName()))
                                    .toArray(String[]::new);
                    case COE -> Arrays.stream(Courses.COECourse.values())
                                    .map(c -> Course.generateCourseShortName(c.getFullName()))
                                    .toArray(String[]::new);
                    case CON -> Arrays.stream(Courses.CONCourse.values())
                                    .map(c -> Course.generateCourseShortName(c.getFullName()))
                                    .toArray(String[]::new);
                    case COC -> Arrays.stream(Courses.COCCourse.values())
                                    .map(c -> Course.generateCourseShortName(c.getFullName()))
                                    .toArray(String[]::new);
                    case COL -> Arrays.stream(Courses.COLCourse.values())
                                    .map(c -> Course.generateCourseShortName(c.getFullName()))
                                    .toArray(String[]::new);
                    case COD -> Arrays.stream(Courses.CODCourse.values())
                                    .map(c -> Course.generateCourseShortName(c.getFullName()))
                                    .toArray(String[]::new);
                    case ILS -> Arrays.stream(Courses.ILSCourse.values())
                                    .map(c -> Course.generateCourseShortName(c.getFullName()))
                                    .toArray(String[]::new);
                    default -> new String[0];
                };

                // Step 3: Capture Course + Year Level
                selectOptions[9] = courseOptions; // update course options
                String[] courseAndYear = ConsoleInput.captureFormInputs(
                    yPosition + 18, xPosition, maxLength, 2, hiddenFields, new String[][]{courseOptions, selectOptions[10]}
                );
                if (courseAndYear == null) { displayLoginScreen(); return; }

                // Merge inputs
                String[] finalInputs = Arrays.copyOf(inputs, fields.length);
                finalInputs[9] = courseAndYear[0]; // Course
                finalInputs[10] = courseAndYear[1]; // Year Level

                // Extract and validate
                String firstName = finalInputs[0], middleName = finalInputs[1], lastName = finalInputs[2];
                String dobStr = finalInputs[3], genderStr = finalInputs[4], address = finalInputs[5];
                String contact = finalInputs[6], enrollStr = finalInputs[7], courseName = finalInputs[9], yearLevelStr = finalInputs[10];

                if (firstName.isBlank() || lastName.isBlank() || dobStr.isBlank() || genderStr.isBlank() ||
                    enrollStr.isBlank() || deptCode.isBlank() || courseName.isBlank() || yearLevelStr.isBlank()) {
                    ConsoleUI.clearDialogBox(5);
                    ConsoleDisplay.dialogBox("error", "Please fill in all required fields!");
                    ConsoleUI.clearInputFields(yPosition, xPosition, maxLength, fields.length);
                    continue;
                }

                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                    LocalDate dob = LocalDate.parse(dobStr, formatter);
                    LocalDate enrollmentDate = LocalDate.parse(enrollStr, formatter);
                    Gender gender = Gender.valueOf(genderStr.toUpperCase());
                    Course course = Course.fromShortName(courseName);
                    YearLevel yearLevel = YearLevel.fromDisplayName(yearLevelStr);

                    Student student = new UndergraduateStudent(
                        firstName, middleName, lastName, dob, gender,
                        address, contact, email, enrollmentDate, dept, course,
                        AcademicStanding.GOOD, yearLevel
                    );

                    CSV.appendRow(Settings.STUDENTS_FILE, student.toCSVRow());
                    currentStudent = student;
                    ConsoleUI.clearDialogBox(5);
                    ConsoleDisplay.dialogBox("success", "Student information saved successfully!");
                    ConsoleInput.pressEnterToContinue();
                    displayStudentMenu(student);
                    return;

                } catch (Exception e) {
                    ConsoleUI.clearDialogBox(5);
                    ConsoleDisplay.dialogBox("error", "Invalid input: " + e.getMessage());
                    ConsoleUI.clearInputFields(yPosition, xPosition, maxLength, fields.length);
                }
            }
        }

    // [METHOD] Display "Setup Information" screen for Academic Staff
    public static void displayAcademicStaffSetupInformationScreen(String email, String userType) throws IOException {

        ConsoleUI.clearScreen();
        ConsoleDisplay.displayBorder(2, 3);
        ConsoleUI.goTo(4, 0);
        ConsoleDisplay.displayHeaderSubtitle("Setup Information");
        System.out.println();

        ConsoleUI.goTo(5, 0);
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("Please fill out all personal information fields to proceed.", Settings.CONSOLE_WIDTH - 3, true);

        String[] fields = {
            "First Name", "Middle Name", "Last Name", "Date of Birth (MM-dd-YYYY)",
            "Gender", "Address", "Contact Number",
            "Department", "Rank", "Hire Date (MM-dd-YYYY)", "Office Location"
        };

        int yPosition = 8;
        int xPosition = 41;
        int maxLength = 49;

        ConsoleUI.drawInputFields(80, fields);

        while (true) {
            // === Step 1: Basic Info + Department ===
            String[][] selectOptions = new String[fields.length][];
            selectOptions[0] = null; selectOptions[1] = null; selectOptions[2] = null;
            selectOptions[3] = null;
            selectOptions[4] = new String[]{"Male", "Female", "Prefer not to say"};
            selectOptions[5] = null; selectOptions[6] = null;

            selectOptions[7] = Arrays.stream(Department.values())
                                    .filter(d -> d != Department.UNASSIGNED)
                                    .map(Department::getCode)
                                    .toArray(String[]::new);

            selectOptions[8] = Arrays.stream(FacultyRank.values())
                                    .map(FacultyRank::getDisplayName)
                                    .toArray(String[]::new);

            selectOptions[9] = null;
            selectOptions[10] = null;

            boolean[] hiddenFields = new boolean[fields.length];
            Arrays.fill(hiddenFields, false);

            String[] inputs = ConsoleInput.captureFormInputs(
                yPosition, xPosition, maxLength, fields.length, hiddenFields, selectOptions
            );

            if (inputs == null) { displayLoginScreen(); return; }

            // Extract Inputs
            String firstName = inputs[0], middleName = inputs[1], lastName = inputs[2];
            String dobStr = inputs[3], genderStr = inputs[4], address = inputs[5];
            String contact = inputs[6], deptCode = inputs[7];
            String rankStr = inputs[8], hireDateStr = inputs[9], officeLocation = inputs[10];

            if (firstName.isBlank() || lastName.isBlank() || dobStr.isBlank() || genderStr.isBlank()
                    || deptCode.isBlank() || rankStr.isBlank() || hireDateStr.isBlank()
                    || officeLocation.isBlank()) {
                ConsoleUI.clearDialogBox(5);
                ConsoleDisplay.dialogBox("error", "Please fill in all required fields!");
                ConsoleUI.clearInputFields(yPosition, xPosition, maxLength, fields.length);
                continue;
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                LocalDate dob = LocalDate.parse(dobStr, formatter);
                LocalDate hireDate = LocalDate.parse(hireDateStr, formatter);

                Gender gender = Gender.valueOf(genderStr.toUpperCase());
                Department department = Department.fromCodeOrFullName(deptCode);
                FacultyRank rank = FacultyRank.fromString(rankStr);

                double salary = rank.getDefaultSalary();
                int teachingHours = rank.getDefaultTeachingHours();
                int maxTeachingHours = 18;

                // Generate a new unique Person ID for the staff
                String personId = CSV.generatePersonId();

                AcademicStaff staff = new AcademicStaff(
                    personId,
                    firstName, middleName, lastName, dob, gender,
                    address, contact, email,
                    department, rank, hireDate, officeLocation,
                    salary, false, new ArrayList<GraduateStudent>(),
                    teachingHours, maxTeachingHours
                );

                CSV.appendRow(Settings.ACADEMIC_STAFF_FILE, staff.toCSVRow());
                currentAcademicStaff = staff;
                ConsoleUI.clearDialogBox(5);
                ConsoleDisplay.dialogBox("success", "Academic staff information saved successfully!");
                ConsoleInput.pressEnterToContinue();
                displayAcademicStaffMenu(staff);
                return;

            } catch (Exception e) {
                ConsoleUI.clearDialogBox(5);
                ConsoleDisplay.dialogBox("error", "Invalid input: " + e.getMessage());
                ConsoleUI.clearInputFields(yPosition, xPosition, maxLength, fields.length);
            }
        }
    }

    // [METHOD] Display "Setup Information" screen for Non-Academic Staff
    public static void displayNonAcademicStaffSetupInformationScreen(String email, String userType) throws IOException {

        ConsoleUI.clearScreen();
        ConsoleDisplay.displayBorder(2, 3);
        ConsoleUI.goTo(4, 0);
        ConsoleDisplay.displayHeaderSubtitle("Setup Information");
        System.out.println();

        ConsoleUI.goTo(5, 0);
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered(
            "Please fill out all personal information fields to proceed.",
            Settings.CONSOLE_WIDTH - 3, true
        );

        String[] fields = {
            "First Name", "Middle Name", "Last Name", "Date of Birth (MM-dd-YYYY)",
            "Gender", "Address", "Contact Number",
            "Department", "Job Title / Position",
            "Hire Date (MM-dd-YYYY)", "Office Location"
        };

        int yPosition = 8;
        int xPosition = 41;
        int maxLength = 49;

        ConsoleUI.drawInputFields(80, fields);

        while (true) {

            String[][] selectOptions = new String[fields.length][];
            selectOptions[0] = null; selectOptions[1] = null; selectOptions[2] = null;
            selectOptions[3] = null;

            selectOptions[4] = new String[]{"Male", "Female", "Prefer not to say"};
            selectOptions[5] = null; selectOptions[6] = null;

            selectOptions[7] = Arrays.stream(Department.values())
                                    .filter(d -> d != Department.UNASSIGNED)
                                    .map(Department::getCode)
                                    .toArray(String[]::new);

            selectOptions[8] = null; // Free text job title
            selectOptions[9] = null;
            selectOptions[10] = null;

            boolean[] hiddenFields = new boolean[fields.length];
            Arrays.fill(hiddenFields, false);

            String[] inputs = ConsoleInput.captureFormInputs(
                yPosition, xPosition, maxLength, fields.length,
                hiddenFields, selectOptions
            );

            if (inputs == null) { displayLoginScreen(); return; }

            String firstName = inputs[0], middleName = inputs[1], lastName = inputs[2];
            String dobStr = inputs[3], genderStr = inputs[4], address = inputs[5];
            String contact = inputs[6], deptCode = inputs[7], jobTitle = inputs[8];
            String hireDateStr = inputs[9], officeLocation = inputs[10];

            if (firstName.isBlank() || lastName.isBlank() || dobStr.isBlank() || genderStr.isBlank()
                || deptCode.isBlank() || jobTitle.isBlank()
                || hireDateStr.isBlank() || officeLocation.isBlank()) {
                ConsoleUI.clearDialogBox(5);
                ConsoleDisplay.dialogBox("error", "Please fill in all required fields!");
                ConsoleUI.clearInputFields(yPosition, xPosition, maxLength, fields.length);
                continue;
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                LocalDate dob = LocalDate.parse(dobStr, formatter);
                LocalDate hireDate = LocalDate.parse(hireDateStr, formatter);

                Gender gender = Gender.valueOf(genderStr.toUpperCase());
                Department department = Department.fromCodeOrFullName(deptCode);

                double salary = 35000;
                int workHours = 40;

                NonAcademicStaff admin = new NonAcademicStaff(
                    firstName, middleName, lastName, dob, gender,
                    address, contact, email,
                    department, jobTitle, hireDate, officeLocation,
                    salary, workHours
                );

                CSV.appendRow(Settings.NON_ACADEMIC_STAFF_FILE, admin.toCSVRow());
                currentNonAcademicStaff = admin;
                ConsoleUI.clearDialogBox(5);
                ConsoleDisplay.dialogBox("success", "Administrator information saved successfully!");
                ConsoleInput.pressEnterToContinue();
                displayNonAcademicStaffMenu(admin);
                return;

            } catch (Exception e) {
                ConsoleUI.clearDialogBox(5);
                ConsoleDisplay.dialogBox("error", "Invalid input: " + e.getMessage());
                ConsoleUI.clearInputFields(yPosition, xPosition, maxLength, fields.length);
            }
        }
    }

    // [METHOD] Display "Login" screen
    public static void displayLoginScreen() throws IOException {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(17, 0);
        ConsoleDisplay.displayHeaderSubtitle("LOGIN");

        // Setup input fields
        int yPosition = 19;
        int xPosition = 38;
        int maxLength = 37;

        String email, password;

        // Display input fields
        ConsoleUI.goTo(18, 0);
        ConsoleUI.drawInputFields(50, "Email", "Password");

        // This loop runs indefinitely until user enters valid credentials
        while (true) {
            boolean[] hiddenFields = {false, true}; // Hide password input
            String[] inputs = ConsoleInput.captureFormInputs(yPosition, xPosition, maxLength, 2, hiddenFields);

            // If user pressed ESC, navigate to 'Register' screen
            if (inputs == null) {
                displayRegisterScreen();
                return;
            }

            email = inputs[0];
            password = inputs[1];

            // ! [ERROR] Invalid email input
            if (!Auth.isValidEmail(inputs[0])) {
                ConsoleUI.clearDialogBox(5);
                ConsoleDisplay.dialogBox("error", "Please enter a valid email address (e.g. example@domain.com).");
            } else if (!Auth.isValidPassword(password)) {
                // ! [ERROR] Invalid password
                ConsoleUI.clearDialogBox(5);
                ConsoleDisplay.dialogBox("error", "Password must be 8 to 20 characters long and include at least one number.");
            } else if (!Auth.accountExists(email)) {
                // ! [ERROR] Non-existing account
                ConsoleUI.clearDialogBox(5);
                ConsoleDisplay.dialogBox("error", "Account not found. Please register first.");
            } else if (!Auth.matchingUsernamePassword(email, password)) {
                    // ! [ERROR] Non-matching credentials
                    ConsoleUI.clearDialogBox(5);
                    ConsoleDisplay.dialogBox("error", "Incorrect email or password. Try again.");
            } else break;

            // Clear input fields
            ConsoleUI.clearInputFields(yPosition, xPosition, maxLength, 2);
        }
        
    // Get user type
    String userType = Auth.getUserType(email, password);

    // ! [ERROR] Invalid user type
    if (userType == null) {
        ConsoleUI.clearDialogBox(5);
        ConsoleDisplay.dialogBox("error", "Unable to determine user type. Login failed.");
        return;
    }

    // ? [SUCCESS] Successful login
    ConsoleUI.clearDialogBox(5);
    ConsoleDisplay.dialogBox("success", "Login successful! Press [Enter] to continue...");

    // Wait until user presses [ENTER] on an empty line and ignore any non-empty input
    while (!ConsoleInput.getString("").equals("")) {}

    // Redirect based on user type
    switch (userType) {
        // ? Type: Student
        case "Student":
            Student student = Auth.getUndergraduateByEmail(email);
            if (student == null) {
                // First-time login - redirect to setup information screen
                displayStudentSetupInformationScreen(email, userType);
                return;
            }

            currentStudent = student; // Set current student
            displayStudentMenu(student);
            break;

        // ? Type: AcademicStaff
        case "Academic Staff":
            AcademicStaff staff = Auth.getAcademicStaffByEmail(email);
            if (staff == null) {
                displayAcademicStaffSetupInformationScreen(email, userType);
                return;
            }
            currentAcademicStaff = staff;
            displayAcademicStaffMenu(staff);
            break;

        // ? Type: NonAcademicStaff
        case "Non-Academic Staff":
            NonAcademicStaff admin = Auth.getNonAcademicStaffByEmail(email);
            if (admin == null) {
                displayNonAcademicStaffSetupInformationScreen(email, userType);
                return;
            }
            currentNonAcademicStaff = admin;
            displayNonAcademicStaffMenu(admin);
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
        ConsoleUI.goTo(17, 0);
        ConsoleDisplay.displayHeaderSubtitle("REGISTER");

        ConsoleUI.drawInputFields(60, "Email", "Password", "Confirm Password", "User Type");

        int yPosition = 19;
        int xPosition = 41;
        int maxLength = 39;

        String[] inputs; // To store user input
        String email, password, confirmPassword, userType;

        // This loop runs indefinitely until user enters valid credentials
        while (true) {
            boolean[] hiddenFields = {false, true, true, false}; // Hide password inputs
            String[][] selectOptions = new String[4][];
            selectOptions[0] = null;
            selectOptions[1] = null;
            selectOptions[2] = null;
            selectOptions[3] = new String[]{
                "Student",
                "Academic Staff",
                "Non-Academic Staff"}; // User type
            inputs = ConsoleInput.captureFormInputs(yPosition, xPosition, maxLength, 4, hiddenFields, selectOptions);

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
                ConsoleUI.clearDialogBox(5);
                ConsoleDisplay.dialogBox("error", "Please enter a valid email address (e.g. example@domain.com).");
            } else if (!Auth.isValidPassword(password)) {
                // ! [ERROR] Invalid password input
                ConsoleUI.clearDialogBox(5);
                ConsoleDisplay.dialogBox("error", "Password must be 8 to 20 characters long and at least one number.");
            } else if (!password.equals(confirmPassword)) {
                // ! [ERROR] Mismatching password
                ConsoleUI.clearDialogBox(5);
                ConsoleDisplay.dialogBox("error", "Passwords do not match.");
            } else if (!Auth.isValidUserType(userType)) {
                // ! [ERROR] Invalid user type
                ConsoleUI.clearDialogBox(5);
                ConsoleDisplay.dialogBox("error", "Invalid user type. Please enter a valid student or faculty type.");
            } else break;

            // Clear input fields
            ConsoleUI.clearInputFields(yPosition, xPosition, maxLength, 4);
        }

        // If successful registration, save credentials to backend
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{email, password, userType});
        
        // If successful registration, save credentials to backend
        CSV.appendRow(Settings.CREDENTIALS_FILE, new String[]{email, password, userType});

        // Display success message
        ConsoleUI.clearDialogBox(5);
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