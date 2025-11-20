package ums.util.console;

// [IMPORT] Standard
import java.io.IOException;
import java.util.List;

// [IMPORT] Utilities
import ums.util.Settings;

public class ConsoleUI {
    // * Attributes
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String ORANGE = "\u001B[38;5;208m";
    public static final String YELLOW = "\u001B[33m";
    public static final String GREEN = "\u001B[32m";
    public static final String BLUE = "\u001B[34m";
    public static final String VIOLET = "\u001B[35m";

    // * Methods
    // [UTILITY] Change color via ANSI color codes
    public static void red(String text) { System.out.print(RED + text + RESET); }
    public static void orange(String text) { System.out.print(ORANGE + text + RESET); }
    public static void yellow(String text) { System.out.print(YELLOW + text + RESET); }
    public static void green(String text) { System.out.print(GREEN + text + RESET); }
    public static void blue(String text) { System.out.print(BLUE + text + RESET); }
    public static void violet(String text) { System.out.print(VIOLET + text + RESET); }

    // [UTILITY] Clear the console screen
    public static void clearScreen() {
        try {
            final String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                // Windows: Use cls command via process
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Linux / macOS / Unix
                System.out.print("\033[H\033[2J"); // ANSI clear
                System.out.print("\033[3J"); // Clear scrollback (optional)
                System.out.flush();
            }
        } catch (Exception e) {
            // Fallback: Print blank lines (works everywhere)
            for (int i = 0; i < Settings.CONSOLE_HEIGHT; i++) {
                System.out.println();
            }
        }

        // Ensure cursor is at top-left
        System.out.print("\033[H");
        System.out.flush();
    }

    // [UTILITY] Show mouse cursor in the console
    public static void showCursor() {
        System.out.print("\033[?25l");
        System.out.flush();
    }

    // [UTILITY] Hide mouse cursor in the console
    public static void hideCursor() {
        System.out.print("\033[?25l");
        System.out.flush();
    }

    // [UTILITY] Move cursor up/down/right/left by specified values
    public static void moveCursor(int right) {
        // if (up > 0)
        //     System.out.print("\033[" + up + "A");
        // if (down > 0)
        //     System.out.print("\033[" + down + "B");
        if (right > 0)
            System.out.print("\033[" + right + "C");
        // if (left > 0)
        //     System.out.print("\033[" + left + "D");
        System.out.flush();
    }

    // [UTILITY] Move cursor to specific row (y) and column (x)
    public static void goTo(int y, int x) {
        System.out.print("\033[" + y + ";" + x + "H");
        System.out.flush();
    }

    // [METHOD] Draw one or multiple input boxes dynamically
    public static void drawInputFields(int width, String... labels) {
        // Box drawing characters
        String topLeft = "╔", topRight = "╗";
        String bottomLeft = "╚", bottomRight = "╝";
        String horizontal = "═";
        String vertical = "║";
        String dividerLeft = "╠", dividerRight = "╣";

        // Compute box width based on longest label
        int maxLabelLength = 0;
        for (String label : labels) {
            maxLabelLength = Math.max(maxLabelLength, label.length());
        }
        int innerWidth = Math.max(width, maxLabelLength + 2);

        // If only one field, use the simple box
        if (labels.length == 1) {
            String top = topLeft + horizontal.repeat(innerWidth) + topRight;
            String labelLine = String.format("%s %-" + (innerWidth - 1) + "s%s", vertical, labels[0] + ":", vertical);
            String bottom = bottomLeft + horizontal.repeat(innerWidth) + bottomRight;

            ConsoleUI.moveCursor(5); ConsoleInput.printCentered(top, Settings.CONSOLE_WIDTH - 9);
            ConsoleUI.moveCursor(5); ConsoleInput.printCentered(labelLine, Settings.CONSOLE_WIDTH - 9);
            ConsoleUI.moveCursor(5); ConsoleInput.printCentered(bottom, Settings.CONSOLE_WIDTH - 9);
            return;
        }

        // Otherwise, build a stacked multi-input box
        String top = topLeft + horizontal.repeat(innerWidth) + topRight;
        String bottom = bottomLeft + horizontal.repeat(innerWidth) + bottomRight;
        ConsoleUI.moveCursor(5);
        ConsoleInput.printCentered(top, Settings.CONSOLE_WIDTH - 9);

        for (int i = 0; i < labels.length; i++) {
            String labelLine = String.format("%s %-" + (innerWidth - 1) + "s%s", vertical, labels[i] + ":", vertical);
            ConsoleUI.moveCursor(5);
            ConsoleInput.printCentered(labelLine, Settings.CONSOLE_WIDTH - 9);

            // Add divider if not the last field
            if (i < labels.length - 1) {
                String divider = dividerLeft + horizontal.repeat(innerWidth) + dividerRight;
                ConsoleUI.moveCursor(5);
                ConsoleInput.printCentered(divider, Settings.CONSOLE_WIDTH - 9);
            }
        }

        ConsoleUI.moveCursor(5);
        ConsoleInput.printCentered(bottom, Settings.CONSOLE_WIDTH - 9);
    }

    // [METHOD] Clear input fields
    public static void clearInputFields(int[] fieldYPositions, int[] fieldXPositions, int maxLength) throws IOException {
        if (fieldYPositions.length != fieldXPositions.length) {
            throw new IllegalArgumentException("Y and X positions arrays must have the same length.");
        }

        for (int i = 0; i < fieldYPositions.length; i++) {
            // Move cursor to start of the field
            ConsoleUI.goTo(fieldYPositions[i], fieldXPositions[i]);

            // Overwrite the field with spaces
            System.out.print(" ".repeat(maxLength));

            // Move cursor back to start of the field
            ConsoleUI.goTo(fieldYPositions[i], fieldXPositions[i]);
        }
    }

    // [METHOD] Display information in input fields
    public static void displayInputFields(List<String> values, int[] fieldYPositions, int[] fieldXPositions, int maxLength) throws IOException {
        if (values.size() != fieldYPositions.length || values.size() != fieldXPositions.length) {
            throw new IllegalArgumentException("Values and positions arrays must all have the same length.");
        }

        for (int i = 0; i < values.size(); i++) {
            // Move cursor to the position
            ConsoleUI.goTo(fieldYPositions[i], fieldXPositions[i]);

            // Print the value, truncated or padded to maxLength
            String output = values.get(i);
            if (output.length() > maxLength) {
                output = output.substring(0, maxLength); // truncate if too long
            } else {
                output = String.format("%-" + maxLength + "s", output); // pad with spaces
            }

            System.out.print(output);
        }
    }
}
