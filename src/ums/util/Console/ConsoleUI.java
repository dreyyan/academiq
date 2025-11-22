package ums.util.console;

// [IMPORT] Standard
import java.io.IOException;
import java.util.List;
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

// [UTILITY] Draw one or multiple input boxes with a vertical divider between label and value.
// boxInnerWidth = desired width inside the outer borders (characters between ╔ and ╗)
public static void drawInputFields(int boxInnerWidth, String... labels) {
    if (boxInnerWidth < 10) boxInnerWidth = 10; // minimum sane width

    // box drawing chars
    final String TL = "╔", TR = "╗", BL = "╚", BR = "╝";
    final String H  = "═";
    final String V  = "║";
    final String DL = "╠", DR = "╣";
    final String SEP = "║"; // inner separator between label and value

    // longest label
    int maxLabelLength = 0;
    for (String label : labels) {
        if (label != null) maxLabelLength = Math.max(maxLabelLength, label.length());
    }

    // Ensure inner width can contain label + sep + at least small value area
    int innerWidth = Math.max(boxInnerWidth, maxLabelLength + 6); // +6 safe padding
    // label block will be right-aligned into maxLabelLength and surrounded by one space each side:
    // " " + <rightAlignedLabel(maxLabelLength)> + " "  => length = maxLabelLength + 2
    int labelBlockLen = maxLabelLength + 2;

    // Remaining space for value block after we reserve 1 char for inner separator
    int valueBlockLen = innerWidth - labelBlockLen - 1; // -1 for SEP char
    if (valueBlockLen < 1) valueBlockLen = 1; // at least 1 char for value area

    // Top border
    String top = TL + H.repeat(innerWidth) + TR;
    String divider = DL + H.repeat(innerWidth) + DR;
    String bottom = BL + H.repeat(innerWidth) + BR;

    ConsoleUI.moveCursor(5);
    ConsoleInput.printCentered(top, Settings.CONSOLE_WIDTH - 9);

    for (int i = 0; i < labels.length; i++) {
        String rawLabel = labels[i] == null ? "" : labels[i];

        // right-align label inside maxLabelLength
        String labelRightAligned = String.format("%" + maxLabelLength + "s", rawLabel);

        // label block: " " + labelRightAligned + " "  => length = maxLabelLength + 2
        String labelBlock = " " + labelRightAligned + " ";

        // value block: single string of exactly valueBlockLen characters (initially spaces)
        String valueBlock = String.format("%-" + valueBlockLen + "s", "");

        // inner content must be exactly innerWidth characters:
        // labelBlock (labelBlockLen) + SEP (1) + valueBlock (valueBlockLen) == innerWidth
        String innerContent = labelBlock + SEP + valueBlock;

        // full row = outer vertical + innerContent + outer vertical
        String fullRow = V + innerContent + V;

        ConsoleUI.moveCursor(5);
        ConsoleInput.printCentered(fullRow, Settings.CONSOLE_WIDTH - 9);

        // draw divider between rows (except after last)
        if (i < labels.length - 1) {
            ConsoleUI.moveCursor(5);
            ConsoleInput.printCentered(divider, Settings.CONSOLE_WIDTH - 9);
        }
    }

    ConsoleUI.moveCursor(5);
    ConsoleInput.printCentered(bottom, Settings.CONSOLE_WIDTH - 9);
}


    // [UTILITY] Clear input fields
    public static void clearInputFields(int startY, int fieldX, int maxLength, int totalFields) throws IOException {
        int currentY = startY;

        for (int i = 0; i < totalFields; i++) {
            // Move cursor to the start of the current field
            ConsoleUI.goTo(currentY, fieldX);

            // Overwrite the field with spaces
            System.out.print(" ".repeat(maxLength));

            // Move cursor back to start of the field
            ConsoleUI.goTo(currentY, fieldX);

            // Increment Y for next field
            currentY += 2;
        }
    }

    // [UTILITY] Display information in input fields
    public static void displayInputFields(List<String> values, int startY, int fieldX, int maxLength) throws IOException {
        int currentY = startY;

        for (String rawValue : values) {

            // Move cursor to the field position
            ConsoleUI.goTo(currentY, fieldX);

            // Prepare displayed value
            String value = rawValue;

            // Truncate if too long
            if (value.length() > maxLength) {
                value = value.substring(0, maxLength);
            }

            // Pad for clean display
            if (value.length() < maxLength) {
                value = String.format("%-" + maxLength + "s", value);
            }

            System.out.print(value);

            currentY += 2; // Auto-increment Y
        }
    }

    public static void clearDialogBox(int startPosition) {
        for (int i = startPosition; i > 2; --i) {
            ConsoleUI.goTo(Settings.CONSOLE_HEIGHT - i, 0);
            ConsoleUI.moveCursor(3);
            System.out.println((" ".repeat(Settings.CONSOLE_WIDTH - 6)));
        }
    }

    public static String truncate(String value, int maxLength) {
        if (value.length() <= maxLength) return value;
        if (maxLength <= 3) return "..."; // minimal space for ellipsis
        return value.substring(0, maxLength - 3) + "...";
    }
}
