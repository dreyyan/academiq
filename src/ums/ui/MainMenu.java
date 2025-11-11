package ums.ui;

// [IMPORT] Standard
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// [IMPORT] Utilities
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

    public static void displayMainMenu() throws IOException {
        while (true) {
            ConsoleDisplay.setupScreen();
            scanner.nextLine();
        }
    }

    public static void displayStudentMenu() {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Student Menu");

        // This loop runs indefinitely until the user enters a valid choice
        while (true) {
            // Display operations
            ConsoleFormatting.displayFormat(Settings.CONSOLE_WIDTH, '=', true);
            ConsoleUI.moveCursor(0, 0, 5, 0); ConsoleAnimation.lineDelayAnimation("1. Display Info", 50, true);
            ConsoleUI.moveCursor(0, 0, 5, 0); ConsoleAnimation.lineDelayAnimation("2. Generate Report", 50, true);
            ConsoleUI.moveCursor(0, 0, 5, 0); ConsoleAnimation.lineDelayAnimation("3. Update GPA", 50, true);
            ConsoleUI.moveCursor(0, 0, 5, 0); ConsoleAnimation.lineDelayAnimation("4. Add Credits", 50, true);
            ConsoleUI.moveCursor(0, 0, 5, 0); ConsoleAnimation.lineDelayAnimation("5. Enroll in Course", 50, true);
            ConsoleUI.moveCursor(0, 0, 5, 0); ConsoleAnimation.lineDelayAnimation("6. Drop Course", 50, true);
            ConsoleUI.moveCursor(0, 0, 5, 0); ConsoleAnimation.lineDelayAnimation("7. Change Department", 50, true);
            ConsoleUI.moveCursor(0, 0, 5, 0); ConsoleAnimation.lineDelayAnimation("8. Change Academic Standing", 50, true);
            ConsoleUI.moveCursor(0, 0, 5, 0); ConsoleAnimation.lineDelayAnimation("9. Show Year Level", 50, true);
            ConsoleUI.moveCursor(0, 0, 5, 0); ConsoleAnimation.lineDelayAnimation("10. Check Graduation Eligibility", 50, true);
            ConsoleFormatting.displayFormat(Settings.CONSOLE_WIDTH, '=', true);

            int input = ConsoleInput.getInt(">> ");
        }
    }

    public static void displayFacultyMenu() {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("Faculty Menu");
    }

    public static void displayLoginScreen() throws IOException {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("LOGIN");

        ConsoleUI.drawInputBoxes(40, "Email", "Password");

        int[] yPositions = { 17, 19 }; // y coordinates of each input line
        int[] xPositions = { 39, 42 }; // cursor start position
        int maxLength = 22;

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
        int maxLength = 22;

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