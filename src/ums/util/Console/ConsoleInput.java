package ums.util.console;

// [IMPORT] Standard
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;

// [IMPORT] Regex
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// [IMPORT] Exceptions
import java.io.IOException;
import java.util.InputMismatchException;

// [IMPORT] JLine
import org.jline.reader.EndOfFileException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;
import org.jline.terminal.Attributes;

// [IMPORT] Utilities
import ums.util.Settings;

public class ConsoleInput {
    // * Attributes
    static Scanner scanner = new Scanner(System.in); // Scanner for console input

    // * JLine terminal and reader for advanced input handling
    private static Terminal terminal;
    private static NonBlockingReader reader;

    // * Static initializer to set up the terminal and reader
    static {
        try {
            terminal = TerminalBuilder.builder()
                    .system(true)
                    .jna(true)
                    .jansi(true)
                    .build();
            reader = terminal.reader();
        } catch (IOException e) {
            // ! [ERROR]: Failed to initialize terminal
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
                if (key == 27) { // ESC key pressed
                    return true; // Return true to indicate navigation back
                }
            }
        } catch (IOException e) {
            // ! [ERROR]: Failed to read input
            e.printStackTrace();
        } return false;
    }

    // [UTILITY] Prints the given text centered within the specified console width
    public static void printCentered(String text, int consoleWidth) {
        // ? [HANDLE] Null or empty text
        if (text == null)
            return;

        // Padding calculation
        int padding = (consoleWidth - text.length()) / 2;

        // ? [HANDLE] Negative padding (text longer than console width)
        if (padding < 0)
            padding = 0; // Ensure padding is not negative

        // Print centered text with padding
        ConsoleAnimation.lineDelayAnimation(" ".repeat(padding) + text, Settings.MS_DELAY, true);
    }

    // [UTILITY] Prints the given text centered within the specified console width
    public static void printCentered(String text, int consoleWidth, boolean newline) {
        printCentered(text, consoleWidth);
        if (newline) { System.out.println(); }
    }

    // [UTILITY] Get a string input from the user
    public static String getString(String prompt) {

        // Loop until valid input is received
        while (true) {
            // Display prompt
            System.out.print(prompt);

            // Capture input
            try {
                String input = scanner.nextLine();
                return input;
            } catch (InputMismatchException e) {
                ConsoleDisplay.dialogBox("error", "Invalid input, please enter a valid string");
            }
        }
    }

    // [METHOD] Navigate input fields using [ENTER] key, with max characters per field and individual X/Y positions. Returns null if user presses [ESC] key
    public static String[] captureFormInputs(int startY, int fieldX, int maxLength, int totalFields) throws IOException {
        String[] inputs = new String[totalFields]; // Array to hold inputs
        Arrays.fill(inputs, ""); // Initialize inputs to empty strings
        int currentY = startY; // Current Y position for input fields

        // Loop through each input field
        for (int i = 0; i < totalFields; i++) {
            StringBuilder input = new StringBuilder(); // StringBuilder for current input

            // Input loop for current field
            while (true) {
                ConsoleUI.goTo(currentY, fieldX); // Move cursor to the current field

                String value = input.toString(); // Current input value

                // Draw value + wipe leftovers
                System.out.print(value + " ".repeat(maxLength - value.length()));

                // Move cursor back to end of text
                ConsoleUI.goTo(currentY, fieldX + value.length());

                int key = readKey();

                // Handle key inputs
                if (key == 27) { // ESC → cancel
                    return null;
                } else if (key == 10 || key == 13) { // ENTER
                    break;
                } else if (key == 8 || key == 127) { // Backspace
                    if (input.length() > 0) input.deleteCharAt(input.length() - 1); // Remove last character
                } else if (key >= 32 && key <= 126) { // Printable ASCII
                    if (input.length() < maxLength) input.append((char) key); // Append character
                }
            }

            inputs[i] = input.toString(); // Save actual value
            currentY += 2; // Auto-increment Y
        }

        return inputs;
    }

    // [METHOD] Navigate input fields using [ENTER] key, with max characters per field, individual X/Y positions, and hidden input option. Returns null if user presses [ESC] key
    public static String[] captureFormInputs(int startY, int fieldX, int maxLength, int totalFields, boolean[] hiddenFields) throws IOException {
        String[] inputs = new String[totalFields]; // Array to hold inputs
        Arrays.fill(inputs, ""); // Initialize inputs to empty strings
        int currentY = startY; // Current Y position for input fields

        // Loop through each input field
        for (int i = 0; i < totalFields; i++) {
            StringBuilder input = new StringBuilder(); // StringBuilder for current input
            boolean hidden = hiddenFields != null && i < hiddenFields.length && hiddenFields[i]; // Check if current field is hidden

            // Input loop for current field
            while (true) {
                ConsoleUI.goTo(currentY, fieldX); // Move cursor to the current field

                // Determine display value
                String displayValue = hidden ? "*".repeat(input.length()) : input.toString();

                // Draw value + wipe leftovers
                System.out.print(displayValue + " ".repeat(maxLength - displayValue.length()));

                // Move cursor back to end of text
                ConsoleUI.goTo(currentY, fieldX + displayValue.length());

                int key = readKey(); // Read key input

                // Handle key inputs
                if (key == 27) { // ESC → cancel
                    return null;
                } else if (key == 10 || key == 13) { // ENTER
                    break;
                } else if (key == 8 || key == 127) { // Backspace
                    if (input.length() > 0) input.deleteCharAt(input.length() - 1); // Remove last character
                } else if (key >= 32 && key <= 126) { // Printable ASCII
                    if (input.length() < maxLength) input.append((char) key); // Append character
                }
            }

            inputs[i] = input.toString(); // Save actual value
            currentY += 2; // Auto-increment Y
        }

        return inputs;
    }

    // [METHOD] Navigate input fields using [ENTER] key, with max characters per field, individual X/Y positions, hidden input option, and selectable options. Returns null if user presses [ESC] key
    public static String[] captureFormInputs(int startY, int fieldX, int maxLength, int totalFields, boolean[] hiddenFields, String[][] selectOptions) throws IOException {
        String[] inputs = new String[totalFields]; // Array to hold inputs
        Arrays.fill(inputs, ""); // Initialize inputs to empty strings
        int currentY = startY; // Current Y position for input fields

        // Loop through each input field
        for (int i = 0; i < totalFields; i++) {
            StringBuilder input = new StringBuilder(); // StringBuilder for current input
            boolean hidden = hiddenFields != null && i < hiddenFields.length && hiddenFields[i]; // Check if current field is hidden
            String[] options = (selectOptions != null && i < selectOptions.length) ? selectOptions[i] : null; // Get selectable options for current field
            int selectedIndex = 0; // Index of selected option

            // Input loop for current field
            while (true) {
                ConsoleUI.goTo(currentY, fieldX);

                // Determine display value
                String displayValue;
                if (options != null && options.length > 0) {
                    // Selectable options
                    String leftArrow = selectedIndex > 0 ? "<" : " ";
                    String rightArrow = selectedIndex < options.length - 1 ? ">" : " ";

                    // Get selected option text
                    String optionText = options[selectedIndex];

                    // Center option between arrows
                    int innerSpace = maxLength - 2; // space between arrows
                    int paddingLeft = (innerSpace - optionText.length()) / 2; // left padding
                    int paddingRight = innerSpace - optionText.length() - paddingLeft; // right padding

                    // Construct display value
                    displayValue = leftArrow + " ".repeat(Math.max(0, paddingLeft)) + optionText + " ".repeat(Math.max(0, paddingRight)) + rightArrow;
                } else {
                    // Free text input
                    displayValue = hidden ? "*".repeat(input.length()) : input.toString();
                    displayValue += " ".repeat(Math.max(0, maxLength - displayValue.length()));
                }

                // Draw the field
                System.out.print(displayValue);

                // Move cursor for text input only
                if (options == null || options.length == 0) {
                    ConsoleUI.goTo(currentY, fieldX + input.length());
                }

                int key = readKey(); // Read key input

                if (key == 27) { // ESC
                    return null;
                } else if (key == 10 || key == 13) { // ENTER
                    break;
                } else if (options != null && options.length > 0) { // Selectable options
                    // Navigate options
                    if (key == Settings.RIGHT_KEY && selectedIndex < options.length - 1) { // Right arrow
                        selectedIndex++;
                    } else if (key == Settings.LEFT_KEY && selectedIndex > 0) { // Left arrow
                        selectedIndex--;
                    }
                } else {
                    // Normal typing
                    if (key == 8 || key == 127) { // Backspace
                        if (input.length() > 0) input.deleteCharAt(input.length() - 1); // Remove last character
                    } else if (key >= 32 && key <= 126) { // Printable ASCII
                        if (input.length() < maxLength) input.append((char) key); // Append character
                    }
                }
            }

            // Save actual value
            if (options != null && options.length > 0) {
                inputs[i] = options[selectedIndex];
            } else { // Free text input
                inputs[i] = input.toString();
            }

            currentY += 2; // Auto-increment Y
        }

        return inputs;
    }


    // [METHOD] Read keyboard input with JLine
    public static int readKey() throws IOException {
        // ? [HANDLE] Terminal not initialized
        if (terminal == null) return System.in.read();

        Attributes original = terminal.getAttributes(); // Save original attributes

        try {
            // Enter temporary raw mode
            Attributes raw = new Attributes(original);
            raw.setLocalFlag(Attributes.LocalFlag.ECHO, false); // disable echo
            raw.setLocalFlag(Attributes.LocalFlag.ICANON, false); // disable line-buffered input
            terminal.setAttributes(raw); // apply raw attributes

            int key = reader.read(); // Read a single key

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
            } return key;
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