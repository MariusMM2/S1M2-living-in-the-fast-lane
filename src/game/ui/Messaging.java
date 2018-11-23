package game.ui;

import game.entities.Course;
import game.menus.Menu;

/*
Static class used for obtaining different kinds of message boxes
 */
public class Messaging {
    //popup window to inform the user the rent is due
    private static final String RENT_IS_DUE_STRING =
            "Your rent is due;\n if you do not pay it this week, starting from " +
                    "the next week your landlord will take half your earnings until you pay it.";

    public static Window wRentIsDue() {
        return MessageBox(RENT_IS_DUE_STRING, Menu.MIN_WIDTH);
    }

    private static final String DID_NOT_EAT_STRING =
            "You didn't eat last week;\n your steps have been reduced by a quarter for this week";

    public static Window wDidNotEat() {
        return MessageBox(DID_NOT_EAT_STRING, Menu.MIN_WIDTH);
    }

    private static final String EXIT_CONFIRMATION_STRING = "Are you sure you want to abort the current game?";
    public static final Window EXIT_CONFIRMATION = MenuBox(Menu.MIN_WIDTH, false, true,
            EXIT_CONFIRMATION_STRING, "Yes", "No");

    //Return a Window with a single message String
    public static Window MessageBox(String message, int length, boolean leftJustified) {
        //subtract 2 from the length to accommodate frames
        length -= 2;
        //create a new StringBuilder to hold multiple, centered string
        //separated by newlines, with regards to the length parameter
        StringBuilder formattedMsg = new StringBuilder();
        if (leftJustified)
            splitMessageLeftAligned(message, formattedMsg, length);
        else
            splitMessageCentered(message, formattedMsg, length);

        //return a new Window representation of the message, with added frames
        return new Window(formattedMsg, length, formattedMsg.length() / length).addFrame();
    }

    public static Window MessageBox(String message, int length) {
        return MessageBox(message, length, false);
    }

    //Return a Window with two message Strings
    public static Window DoubleMessageBox(String message1, String message2, int length, boolean leftJustified1, boolean leftJustified2) {
        //get message boxes of the two messages
        Window wMessage1 = MessageBox(message1, length, leftJustified1);
        Window wMessage2 = MessageBox(message2, length, leftJustified2);

        //create a new window to hold both message boxes
        Window message = new Window(length, wMessage1.getHeight() + wMessage2.getHeight() - 1);

        //merge both message boxes to the new window
        message.addSurface(wMessage1);
        message.addSurface(0, wMessage1.getHeight() - 1, wMessage2);

        //place the horizontal line that separates the two messages
        message.addSingle(0, wMessage1.getHeight() - 1, Graphics.FRAME_V_RIGHT);
        message.addSurface(1, wMessage1.getHeight() - 1, Graphics.hLine(Graphics.FRAME_HORIZONTAL, length - 2));
        message.addSingle(length - 1, wMessage1.getHeight() - 1, Graphics.FRAME_V_LEFT);

        return message;
    }

    public static Window DoubleMessageBox(String message1, String message2, int length) {
        return DoubleMessageBox(message1, message2, length, false, false);
    }

    public static Window MenuBox(int length, boolean leftJustified1, boolean leftJustified2, String message1, String... message2Items) {
        StringBuilder formattedMsgList = new StringBuilder();
        int lastIndex = message2Items.length - 1;

        for (int i = 0; i < lastIndex; i++)
            formattedMsgList.append(i + 1).append('.').append(message2Items[i]).append(Graphics.NEW_LINE);
        formattedMsgList.append(lastIndex + 1).append('.').append(message2Items[lastIndex]);

        return DoubleMessageBox(message1, formattedMsgList.toString(), length, leftJustified1, leftJustified2);
    }

    private static void splitMessage(String message, StringBuilder frame, int length, boolean preFramed, boolean centered) {
        int lastLineIndex;
        if (message.length() <= length)
            lastLineIndex = message.indexOf(Graphics.NEW_LINE);
        else
            lastLineIndex = message.substring(0, length + 1).lastIndexOf(Graphics.NEW_LINE);
        boolean hasNewLine = lastLineIndex != -1;

        if (message.length() <= length && !hasNewLine) {
            //if the message length is smaller than or equal to the specified length
            //it means that the message fits into one line, so no need to split the message
            //across multiple lines
            if (preFramed)
                frame.append(Graphics.FRAME_LEFT);
            if (centered)
                frame.append(Graphics.centerString(message, length));
            else
                frame.append(String.format("%-" + length + 's', message));
            if (preFramed) {
                frame.append(Graphics.FRAME_RIGHT);
                frame.append(Graphics.NEW_LINE);
            }
        } else {
            //the message does not fit in a single line, so measure the message string
            //until the last newLine character '\n' is found, and if not found,
            //look for the last space character ' ' within the specified length, append it to the frame
            //and send what's left back to this method recursively
            if (!hasNewLine)
                lastLineIndex = message.substring(0, length + 1).lastIndexOf(' ');

            if (lastLineIndex == -1)
                throw new IllegalArgumentException(String.format("Message Box Length too small or a word from the message is too big (len:%d)", length));

            //formatting
            if (preFramed)
                frame.append(Graphics.FRAME_LEFT);
            if (centered)
                frame.append(Graphics.centerString(message.substring(0, lastLineIndex), length));
            else
                frame.append(String.format("%-" + length + 's', message.substring(0, lastLineIndex)));
            if (preFramed) {
                frame.append(Graphics.FRAME_RIGHT);
                frame.append(Graphics.NEW_LINE);
            }
            //get what's left of the message into a string
            String nextLine = message.substring(lastLineIndex + 1);
            //recursive godliness
            splitMessage(nextLine, frame, length, preFramed, centered);
        }
    }

    public static void splitMessageLeftAligned(String message, StringBuilder frame, int length, boolean preFramed) {
        splitMessage(message, frame, length, preFramed, false);
    }

    public static void splitMessageLeftAligned(String message, StringBuilder frame, int length) {
        splitMessage(message, frame, length, false, false);
    }

    public static void splitMessageCentered(String message, StringBuilder frame, int length, boolean preFramed) {
        splitMessage(message, frame, length, preFramed, true);
    }

    public static void splitMessageCentered(String message, StringBuilder frame, int length) {
        splitMessage(message, frame, length, false, true);
    }

    public static Surface getGraduatedMessage(Course currentCourse) {
        return DoubleMessageBox(
                "CONGRATULATIONS! You have earned a degree in:",
                currentCourse.name,
                Surface.displayWidth / 3);
    }

    private static class ConfirmationMenu extends Menu {
        ConfirmationMenu(String message) {
            super(null, message, "Yes", "No");

        }

        @Override
        public void run(int command) {
        }
    }

    private static Surface ConfirmationWindow(String message) {
        return new ConfirmationMenu(message).toSurface();
    }
}