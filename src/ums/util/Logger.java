package ums.util;

public class Logger {
    // [METHOD] Display success message
    public static void successMessage(String message) {
        System.out.printf("[SUCCESS] %s", message);
    }

    // [METHOD] Display error message
    public static void errorMessage(String message) {
        System.out.printf("[ERROR] %s", message);

        try {
            Thread.sleep(Math.max(3000, 0)); // prevent negative delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[ERROR] Delay interrupted.");
        }

        System.out.print("\033[1A"); // move cursor up 1 line
        System.out.print("\033[2K"); // clear entire line
    }

    // [METHOD] Display info message
    public static void infoMessage(String message) {
        System.out.printf("[INFO] %s", message);
    }
}
