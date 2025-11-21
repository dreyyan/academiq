package ums.util.console;

// [IMPORT] Standard
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Key;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// [IMPORT] JLine
import org.jline.reader.EndOfFileException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;
import org.jline.terminal.Attributes;


// [IMPORT] Utilities
import ums.util.Logger;
import ums.util.Settings;

public class ConsoleInput {
    // * Attributes
    static Scanner scanner = new Scanner(System.in); // Create an instance of 'Scanner'

    // JLine console reader
    private static Terminal terminal;
    private static NonBlockingReader reader;
    static {
        try {
            terminal = TerminalBuilder.builder()
                    .system(true)
                    .jna(true)
                    .jansi(true)
                    .build();
            reader = terminal.reader(); // don't enter raw mode yet
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // * Methods
    // [UTILITY] Prompts the user to press the 'Enter' key to continue execution
    public static void pressEnterToContinue() {
        ConsoleUI.moveCursor(3); ConsoleInput.printCentered("Press [ENTER] to continue...", Settings.CONSOLE_WIDTH - 3);
        scanner.nextLine();
    }

    // [UTILITY] Prompts the user to press the 'ESC' key to navigate back
    public static boolean pressESCToReturn() {
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered("Press [ESC] to return...", Settings.CONSOLE_WIDTH - 3);

        try {
            while (true) {
                int key = System.in.read();
                if (key == 27) { // ESC key ASCII
                    return true; // indicate ESC was pressed
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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

    // [METHOD] Navigate input fields using [ENTER] key, with max characters per field and individual X/Y positions. Returns null if user presses [ESC] key
public static String[] captureFormInputs(int[] fieldYPositions, int[] fieldXPositions, int maxLength) throws IOException {
    if (fieldYPositions.length != fieldXPositions.length) {
        throw new IllegalArgumentException("Y and X positions arrays must have the same length.");
    }

    String[] inputs = new String[fieldYPositions.length];
    Arrays.fill(inputs, ""); // initialize

    for (int current = 0; current < fieldYPositions.length; current++) {
        StringBuilder input = new StringBuilder();

        while (true) {
            // Move cursor to the start of the current field
            ConsoleUI.goTo(fieldYPositions[current], fieldXPositions[current]);

            // Display current input and clear leftover characters
            String value = input.toString();
            System.out.print(value + " ".repeat(maxLength - value.length()));

            // Move cursor to the end of current input
            ConsoleUI.goTo(fieldYPositions[current], fieldXPositions[current] + value.length());

            int key = readKey(); // use JLine or fallback to System.in

            if (key == 27) { // ESC key
                return null; // user cancelled
            } else if (key == 10 || key == 13) { // ENTER key (LF or CR)
                break; // done with this field
            } else if (key == 8 || key == 127) { // Backspace
                if (input.length() > 0) input.deleteCharAt(input.length() - 1);
            } else if (key >= 32 && key <= 126) { // Printable ASCII
                if (input.length() < maxLength) input.append((char) key);
            }
        }

        // Save the captured input
        inputs[current] = input.toString();
    }

    return inputs;
}

    // [METHOD] Read keyboard input with JLine
    public static int readKey() throws IOException {
        if (terminal == null) return System.in.read();

        Attributes original = terminal.getAttributes();
        try {
            // Enter temporary raw mode
            Attributes raw = new Attributes(original);
            raw.setLocalFlag(Attributes.LocalFlag.ECHO, false);   // disable echo
            raw.setLocalFlag(Attributes.LocalFlag.ICANON, false); // disable line-buffered input
            terminal.setAttributes(raw);

            int key = reader.read();

        // Handle arrow keys
        if (key == 27) { // ESC
            if (reader.ready()) {
                int next1 = reader.read();
                int next2 = reader.read();
                switch (next2) {
                    case 65: return Settings.UP_KEY; // Up arrow
                    case 66: return Settings.DOWN_KEY; // Down arrow
                    case 67: return Settings.RIGHT_KEY; // Right arrow
                    case 68: return Settings.LEFT_KEY; // Left arrow
                }
            }
        }

            return key;
        } finally {
            // Restore original attributes
            terminal.setAttributes(original);
        }
    }

    // [METHOD] Converts an ASCII value to its corresponding character
    public static char asciiToChar(int asciiValue) {
        return (char) asciiValue;
    }
}