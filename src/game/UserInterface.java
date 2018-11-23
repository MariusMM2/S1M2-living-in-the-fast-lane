package game;

import game.requests.QuitGameRequest;
import game.requests.ShowStatisticsRequest;

import java.util.Scanner;

//Class to interact with the user
//This class is used across multiple projects, so there might be unused methods
public class UserInterface {
    private static final int ERROR_MSG_DELAY = 100;//delay in milliseconds after an error message shows

    //possible inputs for movement
    private static final String[] KEY_WORDS = new String[]
            {
                    "left", "a",
                    "right", "d",
                    "up", "w",
                    "down", "s",
                    "quit", null,
                    "stats", "e"
            };

    //scanner object
    private static Scanner input = new Scanner(System.in);

    public static int intInput(String prompt, int min, int max, boolean canRequest) throws ShowStatisticsRequest, QuitGameRequest {
        if (min < 0)
            throw new ArithmeticException(String.format("min(%d) < 0", min));

        if (prompt != null && min < max)
            printf("%s(%d:%d):", prompt, min, max);

        while (true) {
            int option;

            print('>');

            String in = input.next();

            //Catch the new line character
            input.nextLine();

            if (canRequest) {
                if (in.charAt(0) == 'e')
                    throw new ShowStatisticsRequest();
                else if (in.equals("quit")) {
                    throw new QuitGameRequest();
                }
            }
            try {
                option = Integer.parseInt(in);
            } catch (NumberFormatException e) {
                printerrf("\"%s\" is not a number!%n", in);
                continue;
            }

            //If the input is out of range, show error and continue the loop
            //If not, return the user choice as the result
            if (option < min || option > max) {
                if (min == max)
                    printerrf("Input \"%d\" is out of range(%d)\n", option, min);
                else
                    printerrf("Input \"%d\" is out of range(%d:%d)\n", option, min, max);
            } else
                return option;
        }
    }

    public static int intInput(int min, int max, boolean canRequest) throws QuitGameRequest, ShowStatisticsRequest {
        return intInput(null, min, max, canRequest);
    }

    public static int intInput(String prompt, int min, int max) {
        int input = -1;
        try {
            input = intInput(prompt, min, max, false);
        } catch (QuitGameRequest | ShowStatisticsRequest ignore) {
        }
        return input;
    }

    public static int intInput(int min, int max) {
        return intInput(null, min, max);
    }

    //Get String input from user
    //prompt is used to let the user know what to input (just the text, no colons or leading white space)
    //minLen is the minimum length of the input, while maxLen is the maximum
    static String stringInput(String prompt, int minLen, int maxLen, String type) {
        if (minLen < 0)
            throw new ArithmeticException(String.format("minLen(%d) < 0", minLen));
        if (minLen > maxLen)
            throw new ArithmeticException(String.format("minLen(%d) > maxLen(%d)", minLen, maxLen));

        //show the prompt, if available
        if (prompt != null) {
            if (!type.equals("key"))
                printf("%s(%d:%d):", prompt, minLen, maxLen);
        }

        while (true) {
            print('>');
            switch (type) {
                case "any":
                    return input.nextLine();
                case "name":
                    //user is restricted to entering only alphabet characters
                    String text = input.nextLine();
                    if (text.length() < minLen) {
                        printerrf("Too short, minimum is %d characters.\n", minLen);
                        continue;
                    }
                    if (text.length() > maxLen) {
                        printerrf("Too long, maximum is %d characters.\n", minLen);
                        continue;
                    }
                    if (hasOnlyLetters(text))
                        return text;
                    else {
                        printerrln("Only letters, no numbers or special characters");
                        continue;
                    }
                case "key":
                    return input.nextLine();
                default:
                    break;
            }
        }
    }

    static String stringInput(String prompt) {
        return stringInput(prompt, 0, 255, "any");
    }

    //prompts the user to input something until it matches an item
    //in the KEY_WORDS array
    static String keyInput() {
        String message = null;
        while (true) {
            String choice = stringInput("WASD to move", 0, 255, "key");
            for (int i = 0; i < KEY_WORDS.length; i++)
                if (choice.equals(KEY_WORDS[i])) {
                    if (i % 2 == 0)
                        return KEY_WORDS[i];
                    else
                        return KEY_WORDS[i - 1];
                }
        }
    }

    //string input overload, having a single character before the user's input
    static String stringInput(char prompt) {
        return stringInput(String.valueOf(prompt));
    }

    static String stringInput() {
        return stringInput('>');
    }

    //Print functions
    public static void print(String message) {
        System.out.print(message);
    }

    static void print(Object obj) {
        System.out.print(obj);
    }

    public static void println(String message) {
        System.out.println(message);
    }

    static void println() {
        System.out.println();
    }

    static void println(Object obj) {
        System.out.println(obj);
    }

    static void printf(String message, Object... objects) {
        System.out.printf(message, objects);
    }

    //Error print functions
    static void printerr(String message) {
        System.err.print(message);
        wait(ERROR_MSG_DELAY);
    }

    static void printerr(Object obj) {
        System.err.print(obj);
        wait(ERROR_MSG_DELAY);
    }

    static void printerrln(String message) {
        wait(ERROR_MSG_DELAY);
        System.err.println(message);
        wait(ERROR_MSG_DELAY);
    }

    static void printerrln(Object obj) {
        System.err.println(obj);
        wait(ERROR_MSG_DELAY);
    }

    static void printerrf(String message, Object... objects) {
        wait(ERROR_MSG_DELAY);
        System.err.printf(message, objects);
        wait(ERROR_MSG_DELAY);
    }

    //used in a "press enter to continue" prompt
    //half broken, because if the player presses enter
    //before the wait time has passed, the scanner will automatically
    //get the nextLine char possibly before the player has finished reading the message
    static void advance() {
        wait(4000);
        print("Press Enter to continue...");
        input.nextLine();
        println();
    }

    //makes the user confirm with "yes" or "no"
    public static boolean confirm() {
        return intInput(1, 2) == 1;
    }

    //private helper functions
    //check if a string has only ascii letters in it
    private static boolean hasOnlyLetters(String text) {
        for (char character : text.toCharArray()) {
            if (!('A' <= character && character <= 'Z' ||
                    'a' <= character && character <= 'z'))
                return false;
        }
        return true;
    }

    //wait after error message
    private static void wait(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignored) {
        }
    }
}

