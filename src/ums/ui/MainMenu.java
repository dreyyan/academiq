package ums.ui;

// [IMPORT] Standard
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    // * Menu operations
    static MenuOperation[] studentMenuOperations = {
        new MenuOperation(new String[]{"1", "profile"}, "Profile", () -> displayStudentProfileMenu()),
        new MenuOperation(new String[]{"2", "academics"}, "Academics", () -> displayStudentAcademicsMenu()),
        new MenuOperation(new String[]{"3", "courses"}, "Courses", () -> displayStudentCoursesMenu())
    };

    static MenuOperation[] studentCoursesMenuOperations = {
        new MenuOperation(new String[]{"1", "enroll"}, "Enroll in Course Offering", () -> displayStudentEnrollInCourseOfferingMenu()),
        new MenuOperation(new String[]{"2", "drop"}, "Drop Course Offering", () -> displayStudentDropCourseOfferingMenu())
    };
    
    // * UI: Student Menu
    // [METHOD] Display "Student" menu
    public static void displayStudentMenu() {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(16, 0);
        ConsoleDisplay.displayHeaderSubtitle("Student Menu");

        // This loop runs indefinitely until the user enters a valid choice
        while (true) {
            // Display top border
            ConsoleUI.moveCursor(0, 0, 3, 0);
            ConsoleInput.printCentered("=".repeat(16), Settings.CONSOLE_WIDTH - 6);
            
            // Display operations
            for (MenuOperation operation : studentMenuOperations) {
                ConsoleUI.moveCursor(0, 0, 42, 0);
                ConsoleAnimation.lineDelayAnimation(operation.inputKeys[0] + ". " + operation.getDisplayName(), 50); System.out.println();
            }

            // Display bottom border
            ConsoleUI.moveCursor(0, 0, 3, 0);
            ConsoleInput.printCentered("=".repeat(16), Settings.CONSOLE_WIDTH - 6);

            // Display ASCII art of school
            ConsoleUI.moveCursor(0, 0, 42, 0);
            ConsoleDisplay.displaySchool();

            // Prompt user to enter choice
            ConsoleUI.goTo(22, 43);
            String input = ConsoleInput.getString(">> ");

            // Find operation to execute
            boolean found = false;
            for (MenuOperation operation : studentMenuOperations) {
                if (operation.matches(input)) {
                    operation.action.run(); // Call function
                    found = true;
                    break;
                }
            }

            // ! [ERROR] Invalid choice
            if (!found) {
                ConsoleUI.goTo(23, 43);
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // [METHOD] Display student "Profile" menu
    public static void displayStudentProfileMenu() {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(16, 0);
        ConsoleDisplay.displayHeaderSubtitle("Student: Profile");

        // This loop runs indefinitely until the user enters a valid choice
        while (true) {
            // Display top border
            ConsoleUI.moveCursor(0, 0, 3, 0);
            ConsoleInput.printCentered("=".repeat(28), Settings.CONSOLE_WIDTH - 6);

            // Display operations
            for (MenuOperation operation : studentCoursesMenuOperations) {
                ConsoleUI.moveCursor(0, 0, 36, 0);
                ConsoleAnimation.lineDelayAnimation(operation.inputKeys[0] + ". " + operation.getDisplayName(), 50); System.out.println();
            }

            // Display bottom border
            ConsoleUI.moveCursor(0, 0, 3, 0);
            ConsoleInput.printCentered("=".repeat(28), Settings.CONSOLE_WIDTH - 6);

            // Prompt user to enter choice
            ConsoleUI.moveCursor(0, 0, 36, 0);
            String input = ConsoleInput.getString(">> ");

            // Find operation to execute
            boolean found = false;
            for (MenuOperation operation : studentCoursesMenuOperations) {
                if (operation.matches(input)) {
                    operation.action.run(); // Call function
                    found = true;
                    break;
                }
            }

            // ! [ERROR] Invalid choice
            if (!found) {
                ConsoleUI.goTo(23, 43);
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // [METHOD] Display student "Academics" menu
    public static void displayStudentAcademicsMenu() {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Student: Academics");

        // This loop runs indefinitely until the user enters a valid choice
        while (true) {
            // Display operations
            ConsoleFormatting.displayFormat(Settings.CONSOLE_WIDTH, '=', true);
            ConsoleFormatting.displayFormat(Settings.CONSOLE_WIDTH, '=', true);

            int input = ConsoleInput.getInt(">> ");
        }
    }

    // [METHOD] Display student "Courses" menu
    public static void displayStudentCoursesMenu() {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Student: Courses");

        // This loop runs indefinitely until the user enters a valid choice
        while (true) {
            // Display operations
            ConsoleFormatting.displayFormat(Settings.CONSOLE_WIDTH, '=', true);
            ConsoleFormatting.displayFormat(Settings.CONSOLE_WIDTH, '=', true);

            int input = ConsoleInput.getInt(">> ");
        }
    }

    // [METHOD] Display student "Enroll in Course Offering" menu
    public static void displayStudentEnrollInCourseOfferingMenu() {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Student: Enroll in Course Offering");

        // This loop runs indefinitely until the user enters a valid choice
        while (true) {
            // Display operations
            ConsoleFormatting.displayFormat(Settings.CONSOLE_WIDTH, '=', true);
            ConsoleFormatting.displayFormat(Settings.CONSOLE_WIDTH, '=', true);

            int input = ConsoleInput.getInt(">> ");
        }
    }

    // [METHOD] Display student "Drop Course Offering" menu
    public static void displayStudentDropCourseOfferingMenu() {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Student: Drop Course Offering");

        // This loop runs indefinitely until the user enters a valid choice
        while (true) {
            // Display operations
            ConsoleFormatting.displayFormat(Settings.CONSOLE_WIDTH, '=', true);
            ConsoleFormatting.displayFormat(Settings.CONSOLE_WIDTH, '=', true);

            int input = ConsoleInput.getInt(">> ");
        }
    }

    // * UI: Faculty Menu
    // [METHOD] Display "Faculty" menu
    public static void displayFacultyMenu() {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Faculty Menu");

        // This loop runs indefinitely until the user enters a valid choice
        while (true) {
            // Display operations
            ConsoleFormatting.displayFormat(Settings.CONSOLE_WIDTH, '=', true);
            ConsoleFormatting.displayFormat(Settings.CONSOLE_WIDTH, '=', true);

            int input = ConsoleInput.getInt(">> ");
        }
    }

    // * UI: User Authentication
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

        // ! [ERROR] Undefined user type
        if (userType == null) {
            ConsoleDisplay.dialogBox("error", "Unable to determine user type. Login failed.");
            return;
        }

        // Convert to lowercase for normalizing input
        userType = userType.toLowerCase();

        // Display success message
        ConsoleDisplay.dialogBox("success", "Login successful! Press [Enter] to continue...");

        // This loop runs indefinitely until the user presses the 'Enter' key
        while (true) {
            String input = ConsoleInput.getString("");
            if (input.equals("")) break;
        }

        // Redirect to menu based on user type
        switch (userType) {
            case "freshman":
            case "sophomore":
            case "junior":
            case "senior":
            case "master":
            case "phd":
                displayStudentMenu();
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