package ums.util.Console;

public class ConsoleFormatting {
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
            System.out.println(String.valueOf(symbol).repeat(width));
        } System.out.print(String.valueOf(symbol).repeat(width));
    }

    // [UTILITY] Prints a formatted line block of 'length' characters using 'symbol' with optional newline
    public static void displayBlockFormat(int width, int height, char symbol, boolean newline) {
        for (int i = 0; i < height; i++) {
            if (i == height - 1 && !newline) {
                System.out.print(String.valueOf(symbol).repeat(width));
            } else {
                System.out.println(String.valueOf(symbol).repeat(width));
            }
        }
    }
}
