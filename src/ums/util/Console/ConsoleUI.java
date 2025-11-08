package ums.util.Console;

public class ConsoleUI {
    // [UTILITY] Clear the console screen
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
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
        if (up > 0) System.out.print("\033[" + up + "A");
        if (down > 0) System.out.print("\033[" + down + "B");
        if (right > 0) System.out.print("\033[" + right + "C");
        if (left > 0) System.out.print("\033[" + left + "D");
        System.out.flush();
    }

    // [UTILITY] Move cursor to specific row (y) and column (x)
    public static void goTo(int y, int x) {
        System.out.print("\033[" + y + ";" + x + "H");
        System.out.flush();
    }
}
