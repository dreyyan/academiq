package ums.ui;

// [IMPORT] Standard
import java.io.IOException;
import java.util.Scanner;

// [IMPORT] Utilities
import ums.util.Logger;
import ums.util.Settings;
import ums.util.console.ConsoleDisplay;
import ums.util.console.ConsoleInput;
import ums.util.console.ConsoleUI;
import ums.util.console.ConsoleFormatting;

public class MainMenu {
    static Scanner scanner = new Scanner(System.in);

    // [METHOD] Display a dialog box with a specified message
    public static void dialogBox(String dialogType, String dialogMessage) {
        String type = "";

        ConsoleUI.goTo(26, 0);
        switch (dialogType) {
            case "success":
                type = "[SUCCESS]";
                break;
            case "error":
                type = "[ERROR]";
                break;
            case "info":
                type = "[INFO]";
                break;
        }
        ConsoleUI.moveCursor(0, 0, 5, 0);
        ConsoleInput.printCentered(type + " " + dialogMessage + ".", Settings.CONSOLE_WIDTH - 9);
    }

    public static void displayMainMenu() throws IOException {
        while (true) {
            ConsoleDisplay.setupScreen();
            scanner.nextLine();
        }
    }

    public static void displayLoginScreen() throws IOException {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleUI.drawInputBoxes(40, "Email", "Password");

        int[] yPositions = { 16, 18 }; // y coordinates of each input line
        int[] xPositions = { 43, 43, 43 }; // cursor appears here
        int maxLength = 20;

        String inputs[] = ConsoleInput.navigateInputs(yPositions, xPositions, maxLength);
    }

    public static void displayRegisterScreen() throws IOException {
        // Display UI
        ConsoleDisplay.setupScreen();
        ConsoleUI.goTo(15, 0);
        ConsoleDisplay.displayHeaderSubtitle("REGISTER");

        ConsoleUI.drawInputBoxes(40, "Email", "Password", "Confirm Password");

        int[] yPositions = { 17, 19, 21 }; // y coordinates of each input line
        int[] xPositions = { 39, 42, 50 }; // cursor appears here
        int maxLength = 20;

        // This loop runs indefinitely until user enters valid credentials
        while (true) {
            String inputs[] = ConsoleInput.navigateInputs(yPositions, xPositions, maxLength);

            // ! [ERROR] Invalid email input
            if (!ConsoleInput.isValidEmail(inputs[0])) {
                dialogBox("error", "Please enter a valid email address (e.g. example@domain.com)");
            } else if (!ConsoleInput.isValidPassword(inputs[1])) {
                // ! [ERROR] Invalid password input
                dialogBox("error", "Password must be 8–20 characters long and include at least one uppercase letter, one lowercase letter, and one number");
            } else if (!inputs[1].equals(inputs[2])) { // ! [ERROR] Mismatching password
                dialogBox("error", "Passwords do not match");
            } else break;

            // Clear input fields
            ConsoleUI.clearInputFields(yPositions, xPositions, maxLength);
        }
    }
}