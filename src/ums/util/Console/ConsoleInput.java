package ums.util.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
// [IMPORT] Standard
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// [IMPORT] Utilities
import ums.util.Logger;
import ums.util.Settings;

public class ConsoleInput {
    static Scanner scanner = new Scanner(System.in);

    // [UTILITY] Prompts the user to press the 'Enter' key to continue execution
    public static void pressEnterToContinue() {
        System.out.println("Press [ENTER] to continue...");
        scanner.nextLine();
    }

    // [UTILITY] Prints the given text centered within the specified console width
    public static void printCentered(String text, int consoleWidth) {
        if (text == null)
            return;
        int padding = (consoleWidth - text.length()) / 2;
        if (padding < 0)
            padding = 0; // ? [HANDLE] Text longer than console
        ConsoleAnimation.lineDelayAnimation(" ".repeat(padding) + text, Settings.MS_DELAY, true);
    }

    // [UTILITY] Prints the given text centered within the specified console width
    public static void printCentered(String text, int consoleWidth, boolean newline) {
            printCentered(text, consoleWidth);
        if (newline) { System.out.println(); }
    }

    // [UTILITY] Get an integer input from the user
    public static int getInt(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                int input = scanner.nextInt();
                scanner.nextLine();
                return input;
            } catch (InputMismatchException e) {
                Logger.errorMessage("Invalid input, please enter a valid number");
            }
        }
    }

    // [UTILITY] Get a floating number input from the user
    public static float getFloat(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                float input = scanner.nextFloat();
                scanner.nextLine();
                return input;
            } catch (InputMismatchException e) {
                Logger.errorMessage("Invalid input, please enter a valid decimal number");
            }
        }
    }

    // [UTILITY] Get a double input from the user
    public static double getDouble(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                double input = scanner.nextDouble();
                scanner.nextLine();
                return input;
            } catch (InputMismatchException e) {
                Logger.errorMessage("Invalid input, please enter a valid decimal number");
            }
        }
    }

    // [UTILITY] Get a char input from the user
    public static char getChar(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                char input = scanner.nextLine().charAt(0);
                return input;
            } catch (InputMismatchException e) {
                Logger.errorMessage("Invalid input, please enter a valid string");
            }
        }
    }

    // [UTILITY] Get a string input from the user
    public static String getString(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                String input = scanner.nextLine();
                return input;
            } catch (InputMismatchException e) {
                Logger.errorMessage("Invalid input, please enter a valid string");
            }
        }
    }

    // [UTILITY] Get a boolean input from the user
    public static boolean getBool(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                boolean input = scanner.nextBoolean();
                return input;
            } catch (InputMismatchException e) {
                Logger.errorMessage("Invalid input, please enter a valid boolean");
            }
        }
    }

// [METHOD] Navigate input fields using ENTER key, with max characters per field
// and individual X/Y positions. Returns null if user types "\"
public static String[] navigateInputs(int[] fieldYPositions, int[] fieldXPositions, int maxLength) throws IOException {
    // ! [ERROR] Mismatching Y and X length
    if (fieldYPositions.length != fieldXPositions.length) {
        throw new IllegalArgumentException("Y and X positions arrays must have the same length.");
    }

    String[] inputs = new String[fieldYPositions.length];
    Arrays.fill(inputs, ""); // initialize

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    for (int current = 0; current < fieldYPositions.length; current++) {
        ConsoleUI.goTo(fieldYPositions[current], fieldXPositions[current]);

        String input = "";
        while (true) {
            // Move cursor to the start of the current field
            ConsoleUI.goTo(fieldYPositions[current], fieldXPositions[current]);

            // Display current input
            System.out.print(input);

            // Read the line
            String line = reader.readLine();
            if (line == null) line = "";

            // Check if user wants to cancel by entering "\"
            if (line.equals("\\")) {
                return null; // return null to indicate cancellation
            }

            // Enforce max length
            if (line.length() > maxLength) {
                line = line.substring(0, maxLength); // enforce max length
            }

            input = line;
            break; // done with this field, go to next
        }
        inputs[current] = input;
    }

    return inputs;
}

}