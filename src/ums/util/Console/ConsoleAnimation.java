package ums.util.console;

public class ConsoleAnimation {
    // * Methods
    // [UTILITY] Delays execution for given 'seconds'
    public static void delayS(int s) {
        try {
            Thread.sleep(s * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[ERROR] Delay interrupted.");
        }
    }

    // [UTILITY] Prints a string with a line delay with optional newline
    public static void lineDelayAnimation(String str, int ms, boolean newline) {
        // ! [ERROR]: Missing string input
        if (str == null) return;

        if (newline) {
            System.out.println(str);
        } else {
            System.out.print(str);
            System.out.flush();
        }

        try {
            Thread.sleep(Math.max(ms, 0)); // prevent negative delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[ERROR] Delay interrupted.");
        }
    }
    
    // [UTILITY] Prints a string with a line delay
    public static void lineDelayAnimation(String str, int ms) {
        lineDelayAnimation(str, ms, false);
    }
}
