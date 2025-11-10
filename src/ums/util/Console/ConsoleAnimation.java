package ums.util.console;

public class ConsoleAnimation {
    // [UTILITY] Delays execution for given 'seconds'
    public static void delayS(int s) {
        try {
            Thread.sleep(s * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[ERROR] Delay interrupted.");
        }
    }

    // [UTILITY] Delays execution for given 'milliseconds'
    public static void delayMs(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[ERROR] Delay interrupted.");
        }
    }

    // [UTILITY] Prints a string with a character-by-character delay (typing animation) with optional newline
    public static void characterDelayAnimation(String str, int ms, boolean newline) {
        // ! [ERROR]: Missing string input
        if (str == null) return;

        for (int i = 0; i < str.length(); ++i) {
            System.out.print(str.charAt(i));
            System.out.flush(); // print character immediately
            try {
                Thread.sleep(Math.max(ms, 0)); // prevent negative delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[ERROR] Delay interrupted.");
                break;
            }
        }

        if (newline) System.out.println();
    }
    
    // [UTILITY] Prints a string with a character-by-character delay (typing animation)
    public static void characterDelayAnimation(String str, int ms) {
        characterDelayAnimation(str, ms, false);
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
