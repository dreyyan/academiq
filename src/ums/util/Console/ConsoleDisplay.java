package ums.util.console;

// [IMPORT] Utilities
import ums.util.Settings;
import ums.util.console.ConsoleInput;
import ums.util.console.ConsoleUI;
import ums.util.console.ConsoleAnimation;

import java.io.IOException;

import ums.ui.MainMenu;

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
            System.out.print(" ".repeat(width - (borderV * 2))); // Inside space
            System.out.println(String.valueOf(Settings.SYMBOL).repeat(borderV)); // Right border
        }

        // Bottom border
        for (int i = 1; i < borderH; i++) {
            System.out.println(String.valueOf(Settings.SYMBOL).repeat(width));
        }
        System.out.print(String.valueOf(Settings.SYMBOL).repeat(width));
    }

    public static void displaySplashScreen() throws IOException {
        final int WIDTH = Settings.CONSOLE_WIDTH;
        final int HEIGHT = Settings.CONSOLE_HEIGHT;
        final String VERSION = Settings.SYSTEM_VERSION;

        // Clear console screen
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

        // After displaying splash screen for 2 seconds, clear the console screen and
        // navigate to login screen
        ConsoleAnimation.delayS(2);
        ConsoleUI.clearScreen();
        MainMenu.displayLoginScreen();
    }

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

    public static void displayHeaderSubtitle(String subtitle) {
        ConsoleUI.moveCursor(5);
        ConsoleInput.printCentered("[ " + subtitle + " ]", Settings.CONSOLE_WIDTH - 9);
    }

    public static void setupScreen() {
        ConsoleUI.clearScreen();
        ConsoleDisplay.displayBorder(2, 3);
        ConsoleUI.goTo(5, 0);
        ConsoleDisplay.displayHeaderTitle();
    }

    // [METHOD] Display a dialog box with a specified message
    public static void dialogBox(String dialogType, String dialogMessage) {
        String type = "";

        ConsoleUI.goTo(Settings.CONSOLE_HEIGHT - 4, 0);
        switch (dialogType) {
            case "success":
                type = "[SUCCESS]";
                break;
            case "error":
                type = "[ERROR]";
                break;
            case "info":
                type = "[INFO]";
                break;
        }
        ConsoleUI.moveCursor(5);
        ConsoleInput.printCentered(type + " " + dialogMessage, Settings.CONSOLE_WIDTH - 9);
    }

    // * ASCII Art
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

        // Display ASCII art
        for (String line : ASCIIArtSchool) {
            ConsoleUI.moveCursor(3);
            ConsoleInput.printCentered(line, Settings.CONSOLE_WIDTH - 3);
        }
    }
}