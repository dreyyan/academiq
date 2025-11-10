package ums.ui;

import java.util.Scanner;

import ums.util.console.ConsoleAnimation;
// [IMPORT] Utilities
import ums.util.console.ConsoleDisplay;
import ums.util.console.ConsoleInput;
import ums.util.console.ConsoleUI;

public class MainMenu {
    static Scanner scanner = new Scanner(System.in);

    public static void displayMainMenu() {
        while (true) {
            ConsoleDisplay.displaySplashScreen();
            ConsoleAnimation.delayS(2);
            ConsoleUI.clearScreen();

            ConsoleDisplay.displayBorder(2, 3);
            // ConsoleUI.goTo(3, 4);
            // ConsoleDisplay.displayHeaderTitle();

            // ConsoleInput.pressEnterToContinue();
            scanner.nextLine();
        }
    }
}