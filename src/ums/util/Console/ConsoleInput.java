package ums.util.Console;

import java.util.InputMismatchException;
import java.util.Scanner;

import ums.util.Logger;

public class ConsoleInput {
    static Scanner scanner = new Scanner(System.in);

    // [UTILITY] Prompts the user to press the 'Enter' key to continue execution
    public static void pressEnterToContinue() {
        System.out.println("Press [ENTER] to continue...");
        scanner.nextLine();
    }
    
    // [UTILITY] Prints the given text centered within the specified console width
    public static void printCentered(String text, int consoleWidth) {
        if (text == null) return;
        int padding = (consoleWidth - text.length()) / 2;
        if (padding < 0) padding = 0; // ? [HANDLE] Text longer than console
        System.out.println(" ".repeat(padding) + text);
    }

    // [UTILITY] Get an integer input from the user
    public static int getInt(String prompt) {
        while (true) { 
            System.out.print(prompt);
            
            try {
                int input = scanner.nextInt();
                scanner.nextLine();
                return input;
            } catch (InputMismatchException e) {
                Logger.errorMessage("Invalid input, please enter a valid number");
            } 
        }
    }

    // [UTILITY] Get a floating number input from the user
    public static float getFloat(String prompt) {
        while (true) { 
            System.out.print(prompt);
            
            try {
                float input = scanner.nextFloat();
                scanner.nextLine();
                return input;
            } catch (InputMismatchException e) {
                Logger.errorMessage("Invalid input, please enter a valid decimal number");
            } 
        }
    }

    // [UTILITY] Get a double input from the user
    public static double getDouble(String prompt) {
        while (true) { 
            System.out.print(prompt);
            
            try {
                double input = scanner.nextDouble();
                scanner.nextLine();
                return input;
            } catch (InputMismatchException e) {
                Logger.errorMessage("Invalid input, please enter a valid decimal number");
            } 
        }
    }

    // [UTILITY] Get a char input from the user
    public static char getChar(String prompt) {
        while (true) { 
            System.out.print(prompt);
            
            try {
                char input = scanner.nextLine().charAt(0);
                return input;
            } catch (InputMismatchException e) {
                Logger.errorMessage("Invalid input, please enter a valid string");
            } 
        }
    }

    // [UTILITY] Get a string input from the user
    public static String getString(String prompt) {
        while (true) {
            System.out.print(prompt);
            
            try {
                String input = scanner.nextLine();
                return input;
            } catch (InputMismatchException e) {
                Logger.errorMessage("Invalid input, please enter a valid string");
            } 
        }
    }

    // [UTILITY] Get a boolean input from the user
    public static boolean getBool(String prompt) {
        while (true) {
            System.out.print(prompt);
            
            try {
                boolean input = scanner.nextBoolean();
                return input;
            } catch (InputMismatchException e) {
                Logger.errorMessage("Invalid input, please enter a valid boolean");
            } 
        }
    }
}
