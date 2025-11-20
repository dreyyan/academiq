package ums.util.console;

public class ConsoleFormatting {
    // * Methods
    // [UTILITY] Prints a formatted line of 'length' characters using 'symbol'
    public static void displayFormat(int length, char symbol) {
        System.out.print(String.valueOf(symbol).repeat(length));
    }

    // [UTILITY] Prints a formatted line of 'length' characters using 'symbol' with optional newline
    public static void displayFormat(int length, char symbol, boolean newline) {
        if (newline) {
            System.out.println(String.valueOf(symbol).repeat(length));
        } else {
            System.out.print(String.valueOf(symbol).repeat(length));
        }
    }

    // [UTILITY] Prints a formatted line block of 'length' characters using 'symbol'
    public static void displayBlockFormat(int width, int height, char symbol) {
        for (int i = 1; i < height; ++i) {
            ConsoleAnimation.lineDelayAnimation(String.valueOf(symbol).repeat(width), symbol, true);
        } ConsoleAnimation.lineDelayAnimation(String.valueOf(symbol).repeat(width), symbol);
    }

    // [UTILITY] Prints a formatted line block of 'length' characters using 'symbol' with optional newline
    public static void displayBlockFormat(int width, int height, char symbol, boolean newline) {
        for (int i = 0; i < height; i++) {
            if (i == height - 1 && !newline) {
                ConsoleAnimation.lineDelayAnimation(String.valueOf(symbol).repeat(width), symbol, false);
            } else {
                ConsoleAnimation.lineDelayAnimation(String.valueOf(symbol).repeat(width), symbol);
            }
        }
    }
}