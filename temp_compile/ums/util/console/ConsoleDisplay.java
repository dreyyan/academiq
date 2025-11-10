package ums.util.console;

// [IMPORT] Utilities
import ums.util.Settings;
import ums.util.console.ConsoleInput;
import ums.util.console.ConsoleUI;
import ums.util.console.ConsoleAnimation;

public class ConsoleDisplay {
    // [UTILITY] Display a bordered frame with dynamic border thickness
    public static void displayBorder(int borderH, int borderV) {

        // Use settings for console size
        int width = Settings.CONSOLE_WIDTH;
        int height = Settings.CONSOLE_HEIGHT;

        // Top border
        for (int i = 0; i < borderH; i++) {
            System.out.println(String.valueOf(Settings.SYMBOL).repeat(width));
        }

        // Middle section
        for (int i = 0; i < height - (borderH * 2); i++) {
            System.out.print(String.valueOf(Settings.SYMBOL).repeat(borderV)); // Left border
            System.out.print(" ".repeat(width - (borderV * 2)));      // Inside space
            System.out.println(String.valueOf(Settings.SYMBOL).repeat(borderV)); // Right border
        }

        // Bottom border
        for (int i = 1; i < borderH; i++) {
            System.out.println(String.valueOf(Settings.SYMBOL).repeat(width));
        } System.out.print(String.valueOf(Settings.SYMBOL).repeat(width));
    }

    public static void displaySplashScreen() {
        final int WIDTH  = Settings.CONSOLE_WIDTH;
        final int HEIGHT = Settings.CONSOLE_HEIGHT;
        final String VERSION = Settings.SYSTEM_VERSION;

        // Clear console screen
        ConsoleUI.clearScreen();

        // ASCII art for title
        String[] bigTitle = {
            "    █████╗  ██████╗ █████╗ ██████╗ ███████╗███╗   ███╗██╗ ██████╗ ",
            "   ██╔══██╗██╔════╝██╔══██╗██╔══██╗██╔════╝████╗ ████║██║██╔═══██╗",
            "   ███████║██║     ███████║██║  ██║█████╗  ██╔████╔██║██║██║   ██║",
            "   ██╔══██║██║     ██╔══██║██║  ██║██╔══╝  ██║╚██╔╝██║██║██║   ██║",
            "   ██║  ██║╚██████╗██║  ██║██████╔╝███████╗██║ ╚═╝ ██║██║╚██████╔╝",
            "   ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚═════╝ ╚══════╝╚═╝     ╚═╝╚═╝ ╚═════╝ "
        };

        // For building the header
        java.util.List<String> headerLines = new java.util.ArrayList<>();

        // ASCII title
        for (String line : bigTitle) {
            headerLines.add(line);
        }

        // Subtitle + Version
        headerLines.add("University Management System");
        headerLines.add(VERSION);

        int usedLines = headerLines.size();
        int totalPadding = HEIGHT - usedLines;
        int topPadding   = totalPadding / 2;
        int bottomPadding = totalPadding - topPadding;

        // Display top empty lines
        for (int i = 0; i < topPadding; i++) {
            System.out.println();
        }

        // Display centered header
        for (String line : headerLines) {
            ConsoleInput.printCentered(line, WIDTH);
        }

        // Display bottom empty lines
        for (int i = 1; i < bottomPadding; i++) {
            System.out.println();
        }
    }

    public static void displayHeaderTitle() {
        final int WIDTH  = Settings.CONSOLE_WIDTH;
        final int HEIGHT = Settings.CONSOLE_HEIGHT;
        final String VERSION = Settings.SYSTEM_VERSION;
        
        // ASCII art for title
        String[] bigTitle = {
            "    █████╗  ██████╗ █████╗ ██████╗ ███████╗███╗   ███╗██╗ ██████╗ ",
            "   ██╔══██╗██╔════╝██╔══██╗██╔══██╗██╔════╝████╗ ████║██║██╔═══██╗",
            "   ███████║██║     ███████║██║  ██║█████╗  ██╔████╔██║██║██║   ██║",
            "   ██╔══██║██║     ██╔══██║██║  ██║██╔══╝  ██║╚██╔╝██║██║██║   ██║",
            "   ██║  ██║╚██████╗██║  ██║██████╔╝███████╗██║ ╚═╝ ██║██║╚██████╔╝",
            "   ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚═════╝ ╚══════╝╚═╝     ╚═╝╚═╝ ╚═════╝ "
        };

        // For building the header
        java.util.List<String> headerLines = new java.util.ArrayList<>();
        

        // ASCII title
        for (String line : bigTitle) {
            headerLines.add(line);
        }

        // Subtitle + Version
        headerLines.add("University Management System");
        headerLines.add(VERSION);

        int usedLines = headerLines.size();
        int totalPadding = HEIGHT - usedLines;
        int topPadding   = totalPadding / 2;
        int bottomPadding = totalPadding - topPadding;

        // Display top empty lines
        for (int i = 0; i < topPadding; i++) {
            System.out.println();
        }

        // Display centered header
        for (String line : headerLines) {
            ConsoleUI.moveCursor(0, 0, 5, 0);
            ConsoleInput.printCentered(line, WIDTH - 18);
        }

        // Display bottom empty lines
        for (int i = 0; i < bottomPadding; i++) {
            System.out.println();
        }
    }
}
