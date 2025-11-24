package ums.util.console;

// [IMPORT] Utilities
import ums.util.Settings;
import ums.util.console.ConsoleInput;
import ums.util.console.ConsoleUI;
import ums.util.console.ConsoleAnimation;

// [IMPORT] Exceptions
import java.io.IOException;

// [IMPORT] UI
import ums.ui.MainMenu;

// [IMPORT] Utilities
import ums.util.Sound;

public class ConsoleDisplay {
    // * Methods
    // [UTILITY] Display a bordered frame with dynamic border thickness
    public static void displayBorder(int borderH, int borderV) {
        // Set default border thickness
        int width = Settings.CONSOLE_WIDTH;
        int height = Settings.CONSOLE_HEIGHT;

        // Display top border
        for (int i = 0; i < borderH; i++) {
            System.out.println(String.valueOf(Settings.SYMBOL).repeat(width));
        }

        // Display middle section with left and right borders
        for (int i = 0; i < height - (borderH * 2); i++) {
            System.out.print(String.valueOf(Settings.SYMBOL).repeat(borderV)); // Left border
            System.out.print(" ".repeat(width - (borderV * 2))); // Middle space
            System.out.println(String.valueOf(Settings.SYMBOL).repeat(borderV)); // Right border
        }

        // Display bottom border
        for (int i = 1; i < borderH; i++) {
            System.out.println(String.valueOf(Settings.SYMBOL).repeat(width));
        } System.out.print(String.valueOf(Settings.SYMBOL).repeat(width));
    }

    // [UTILITY] Display a splash screen with title and version
    public static void displaySplashScreen() throws IOException {
        // Constants for width, height, and version
        final int WIDTH = Settings.CONSOLE_WIDTH;
        final int HEIGHT = Settings.CONSOLE_HEIGHT;
        final String VERSION = Settings.SYSTEM_VERSION;

        // Clear the console screen
        ConsoleUI.clearScreen();

        // ASCII art for title
        String[] bigTitle = {
                "MMP\"\"\"\"\"\"\"MM                         dP                     M\"\"M MM'\"\"\"\"\"`MMM ",
                "M' .mmmm  MM                         88                     M  M M  .mmm,  MM ",
                "M         `M .d8888b. .d8888b. .d888b88 .d8888b. 88d8b.d8b. M  M M  MMMMM  MM ",
                "M  MMMMM  MM 88'  `\"\" 88'  `88 88'  `88 88ooood8 88'`88'`88 M  M M  MM  M  MM ",
                "M  MMMMM  MM 88.  ... 88.  .88 88.  .88 88.  ... 88  88  88 M  M M  `MM    MM ",
                "M  MMMMM  MM `88888P' `88888P8 `88888P8 `88888P' dP  dP  dP M  M MM.    .. `M ",
                "MMMMMMMMMMMM                                                MMMM MMMMMMMMMMMM "
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

        // Calculate padding for centering the header
        int usedLines = headerLines.size();
        int totalPadding = HEIGHT - usedLines;
        int topPadding = totalPadding / 2;
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

        // After displaying splash screen for 2 seconds, clear the console screen and navigate to login screen
        ConsoleAnimation.delayS(2);
        ConsoleUI.clearScreen();
        MainMenu.displayLoginScreen();
    }

    // [UTILITY] Display header title with ASCII art and version
    public static void displayHeaderTitle() {
        final int WIDTH = Settings.CONSOLE_WIDTH;
        final int HEIGHT = Settings.CONSOLE_HEIGHT;
        final String VERSION = Settings.SYSTEM_VERSION;

        // ASCII art for title
        String[] bigTitle = {
                "MMP\"\"\"\"\"\"\"MM                         dP                     M\"\"M MM'\"\"\"\"\"`MMM ",
                "M' .mmmm  MM                         88                     M  M M  .mmm,  MM ",
                "M         `M .d8888b. .d8888b. .d888b88 .d8888b. 88d8b.d8b. M  M M  MMMMM  MM ",
                "M  MMMMM  MM 88'  `\"\" 88'  `88 88'  `88 88ooood8 88'`88'`88 M  M M  MM  M  MM ",
                "M  MMMMM  MM 88.  ... 88.  .88 88.  .88 88.  ... 88  88  88 M  M M  `MM    MM ",
                "M  MMMMM  MM `88888P' `88888P8 `88888P8 `88888P' dP  dP  dP M  M MM.    .. `M ",
                "MMMMMMMMMMMM                                                MMMM MMMMMMMMMMMM "
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

        // Calculate padding for centering the header
        int usedLines = headerLines.size();
        int totalPadding = HEIGHT - usedLines;
        int topPadding = totalPadding / 2;
        int bottomPadding = totalPadding - topPadding;

        // Display centered header
        for (String line : headerLines) {
            ConsoleUI.moveCursor(5);
            ConsoleInput.printCentered(line, WIDTH - 10);
        }

        // Display bottom empty lines
        for (int i = 0; i < bottomPadding; i++) {
            System.out.println();
        }
    }

    // [UTILITY] Display header subtitle
    public static void displayHeaderSubtitle(String subtitle) {
        ConsoleUI.moveCursor(5);
        ConsoleInput.printCentered("[ " + subtitle + " ]", Settings.CONSOLE_WIDTH - 9);
    }

    // [UTILITY] Setup the console screen with a border and header
    public static void setupScreen() {
        ConsoleUI.clearScreen();
        ConsoleDisplay.displayBorder(2, 3);
        ConsoleUI.goTo(5, 0);
        ConsoleDisplay.displayHeaderTitle();
    }

    // [UTILITY] Display a dialog box with a message
    public static void dialogBox(String dialogType, String dialogMessage) {
        String type = ""; // Initialize type variable

        ConsoleUI.goTo(Settings.CONSOLE_HEIGHT - 4, 0);
        // Set the type and color based on dialogType
        switch (dialogType) {
            case "success":
                type = "[SUCCESS]";
                ConsoleUI.green(type);
                break;
            case "error":
                type = "[ERROR]";
                ConsoleUI.red(type);
                break;
            case "info":
                type = "[INFO]";
                ConsoleUI.blue(type);
                break;
        }

        switch (type) {
            case "[SUCCESS]":
                Sound.playSFX("success-sfx.wav");
                break;
            case "[ERROR]":
                Sound.playSFX("error-sfx.wav");
                break;
            case "[INFO]":
                Sound.playSFX("info-sfx.wav");
                break;
        }

        // Print the dialog message with the type
        ConsoleUI.moveCursor(3);
        ConsoleInput.printCentered(type + ConsoleUI.RESET + " " + dialogMessage, Settings.CONSOLE_WIDTH - 3);
    }

    // [UTILITY] Display ASCII art for the school
    public static void displaySchool() {
        String[] ASCIIArtSchool = {
            "                         %                          ",
            "                         #####                      ",
            "                         %                          ",
            "                         %%                         ",
            "                      %%---#%                       ",
            "                    %=--: .---%                     ",
            "             %%%%%%%%---%:#---*%%%%%%%              ",
            "            %%%%%%%%%-========*%%%%%%%%             ",
            "           %%@@@%%%%%-....... #%%%%%%%%%            ",
            " %%%%%%%%%%%=========%%%%%%%%%+*%#=*===+%%%%%%%%%%% ",
            "%%%%%%%%%%%%==========---------=*##=*===+%%%%%%%%%%%",
            " +++==++==++=========--#####=-=*%#=*====+==++==+++  ",
            " +++==++==++=========--#=*===-=*%#=*====+==++==+++  ",
            " +++==++==++=========--#=*===-=*##=*====+==++==+++  ",
            " +++==++==++=========--#=*===-=*##=*====+==++==+++  "
        };

        ConsoleUI.goTo(24, 0);
        // Display the ASCII art centered
        for (String line : ASCIIArtSchool) {
            ConsoleUI.moveCursor(3);
            ConsoleInput.printCentered(line, Settings.CONSOLE_WIDTH - 3);
        }
    }
}