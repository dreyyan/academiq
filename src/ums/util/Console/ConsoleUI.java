package ums.util.console;

// [IMPORT] Standard
import java.io.IOException;

// [IMPORT] Utilities
import ums.util.Settings;

public class ConsoleUI {
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
    public static void moveCursor(int up, int down, int right, int left) {
        if (up > 0)
            System.out.print("\033[" + up + "A");
        if (down > 0)
            System.out.print("\033[" + down + "B");
        if (right > 0)
            System.out.print("\033[" + right + "C");
        if (left > 0)
            System.out.print("\033[" + left + "D");
        System.out.flush();
    }

    // [UTILITY] Move cursor to specific row (y) and column (x)
    public static void goTo(int y, int x) {
        System.out.print("\033[" + y + ";" + x + "H");
        System.out.flush();
    }

    // [METHOD] Draw one or multiple input boxes dynamically
    public static void drawInputBoxes(int width, String... labels) {
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

            ConsoleInput.printCentered(top, Settings.CONSOLE_WIDTH);
            ConsoleInput.printCentered(labelLine, Settings.CONSOLE_WIDTH);
            ConsoleInput.printCentered(bottom, Settings.CONSOLE_WIDTH);
            return;
        }

        // Otherwise, build a stacked multi-input box
        String top = topLeft + horizontal.repeat(innerWidth) + topRight;
        String bottom = bottomLeft + horizontal.repeat(innerWidth) + bottomRight;
        ConsoleUI.moveCursor(0, 0, 5, 0);
        ConsoleInput.printCentered(top, Settings.CONSOLE_WIDTH - 9);

        for (int i = 0; i < labels.length; i++) {
            String labelLine = String.format("%s %-" + (innerWidth - 1) + "s%s", vertical, labels[i] + ":", vertical);
            ConsoleUI.moveCursor(0, 0, 5, 0);
            ConsoleInput.printCentered(labelLine, Settings.CONSOLE_WIDTH - 9);

            // Add divider if not the last field
            if (i < labels.length - 1) {
                String divider = dividerLeft + horizontal.repeat(innerWidth) + dividerRight;
                ConsoleUI.moveCursor(0, 0, 5, 0);
                ConsoleInput.printCentered(divider, Settings.CONSOLE_WIDTH - 9);
            }
        }

        ConsoleUI.moveCursor(0, 0, 5, 0);
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
}
