package game.menus;

import game.*;
import game.requests.QuitGameRequest;
import game.requests.ShowStatisticsRequest;

import java.util.ArrayList;
import java.util.Arrays;

//Main abstract class for handling menus
public abstract class Menu {
    protected static final String BACK = "Back";
    protected static final String CANCEL = "Cancel";

    //adjustable variable for the minimum desired menu width
    public static final int MIN_WIDTH = Surface.displayWidth / 3;

    protected MenuController menuCtrl;
    protected String title;
    protected Menu nextMenu;
    protected Menu lastMenu;
    protected ArrayList<String> menuItems;//stores the options available in the menu
    protected String message;
    protected boolean canRequest = false;

    protected Menu() {
        this(null, null, (String[]) null);
    }

    public static void main(String[] args) {
        Menu menu = new Menu() {
            @Override
            public void run(int command) {
                return;
            }
        };
    }

    //standard constructor with no message
    protected Menu(MenuController menuCtrl, String title, String... menuItems) {
        this.menuCtrl = menuCtrl;
        this.title = title;
        if (menuItems != null) {
            this.menuItems = new ArrayList<>();
            this.menuItems.addAll(Arrays.asList(menuItems));
        } else
            this.menuItems = null;
        message = null;
    }

    //first method called on the menu by the controller loop
    public void onEnter() {
        nextMenu = this; //reset the reference to the next menu to null
        display();//display the menu
    }

    //next method called after onEnter
    public void go() throws QuitGameRequest, ShowStatisticsRequest {
        int input = getInput();
        if (input != -1)
            run(input);
    }

    //get user input, limited to the number of menu items available
    protected int getInput() throws QuitGameRequest, ShowStatisticsRequest {
        return UserInterface.intInput(1, menuItems != null ? menuItems.size() : 0, canRequest);
    }

    //functionality to be implemented by subclasses
    public abstract void run(int command);

    public void setMenuItems(ArrayList<String> menuItems) {
        this.menuItems = menuItems;
    }

    void setNextMenu(Menu nextMenu) {
        this.nextMenu = nextMenu;
    }

    //last method called by the controller loop
    public Menu onExit() {
        return nextMenu;
    }

    //present the menu to the user
    private void display() {
        refreshMenu();
        UserInterface.print(getOutput().toString());
    }

    // get the 2d representation of the menu
    private StringBuilder getOutput() {
        StringBuilder frame = new StringBuilder();

        //find optimal width
        int width = findWidth();

        //top line of the frame
        //for each add*Line method, its length will be width+2
        addTopLine(frame, width);

        //if there is any title, draw it
        if (hasTitle())
            //Menu Title
            addTitle(frame, width);

        //if there is any message, draw it
        if (hasMessage()) {
            if (hasTitle())
                //add middle frame line to separate the title from the message
                addMidLine(frame, width);

            //message
            addMessage(getMessage(), frame, width);
        }

        //if there are any menu items, draw them
        if (hasMenuItems()) {
            if (hasTitle() || hasMessage())
                //if there is any title or/and message, draw a midline to separate the items from the title/message
                addMidLine(frame, width);

            //menu items
            addMenuItems(frame, width);
        }

        //draw the bottom line
        addBotLine(frame, width);

        return frame;
    }

    //Method that converts the menu to a Surface representation of it
    //& calling the refreshMenu() method beforehand
    public Surface toSurface() {
        refreshMenu();

        StringBuilder preSurf = getOutput();

        int width = 0;
        for (int i = 0; i < preSurf.toString().length(); i++) {
            if (preSurf.charAt(i) == '\n') {
                if (width == 0)
                    width = i;
                preSurf.deleteCharAt(i);
            }
        }

        int height = preSurf.length() / width;

        return new Surface(preSurf, width, height);
    }

    protected void refreshMenu() {
    }

    @Override
    public String toString() {
        return this.toSurface().toString();
    }

    protected String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//helper methods for display()

    //compare the length of the title and that of each menu item
    private int findWidth() {
        //find the largest of 'minimum menu width - 2', 'title length' and 'each menu item's length'
        //start by assigning the minimum width to the variable
        //we substract 2 from MIN_WIDTH because MIN_WIDTH refers to menu width,
        //and we need the width without the 2 extra frame parts
        int longestItem = MIN_WIDTH - 2;

        //variable to store the biggest possible number of digits when numbering the menu items
        int nDigits;

        //if the menu has a title and its length is bigger than the current found width
        //replace the width with the length of the title
        if (hasTitle() && this.title.length() > longestItem)
            longestItem = this.title.length();
        //if the menu body has items, for each one of them, find the longest
        if (hasMenuItems()) {
            //find the width of the numbering system in drawing menuItems
            nDigits = findNDigits();
            for (String menuItem : menuItems) {
                //in each menu item line, the length equals the sum of
                //nDigits, the the string of the item and 1, for the dot separator
                int len = menuItem.length() + nDigits + 1;
                if (len > longestItem)
                    longestItem = len;
            }
        }

        //return the width
        return longestItem + 2;
    }

    //find the width of the numbering system in drawing menuItems
    private int findNDigits() {
        if (hasMenuItems())
            return (int) Math.log10(menuItems.size()) + 1;
        return 0;
    }

    //adds top, mid and bot lines to the specified frame, with leading and trailing characters + newline at the end
    private static void addTopLine(StringBuilder frame, int length) {
        frame.append(Graphics.FRAME_TOP_LEFT);
        frame.append(Graphics.hMenuLine(Graphics.FRAME_TOP, length));
        frame.append(Graphics.FRAME_TOP_RIGHT);
        frame.append(Graphics.NEW_LINE);
    }

    private static void addMidLine(StringBuilder frame, int length) {
        frame.append(Graphics.FRAME_V_RIGHT);
        frame.append(Graphics.hMenuLine(Graphics.FRAME_HORIZONTAL, length));
        frame.append(Graphics.FRAME_V_LEFT);
        frame.append(Graphics.NEW_LINE);
    }

    private static void addBotLine(StringBuilder frame, int length) {
        frame.append(Graphics.FRAME_BOT_LEFT);
        frame.append(Graphics.hMenuLine(Graphics.FRAME_BOT, length));
        frame.append(Graphics.FRAME_BOT_RIGHT);
        frame.append(Graphics.NEW_LINE);
    }

    //check if the menu has a title, a message and menu items, to avoid null pointer exceptions
    private boolean hasTitle() {
        return title != null && title.length() > 0;
    }

    private boolean hasMenuItems() {
        return menuItems != null && menuItems.size() > 0;
    }

    private boolean hasMessage() {
        return message != null && message.length() > 0;
    }

    //appends title, message and menu items to the frame
    private void addTitle(StringBuilder frame, int length) {
        frame.append(Graphics.FRAME_LEFT);
        frame.append(Graphics.centerString(title, length));
        frame.append(Graphics.FRAME_RIGHT);
        frame.append(Graphics.NEW_LINE);
    }

    private static void addMessage(String message, StringBuilder frame, int length) {
        Messaging.splitMessageCentered(message, frame, length, true);
        /*
        if(message.length() <= length)
        {
            //if the message length is smaller or equal to the specified length
            //it means that it fits into one line, so no need to split the message
            //into multiple lines
            frame.append(Graphics.FRAME_LEFT);
            //frame.append(String.format("%-" + length + 's', message));
            frame.append(Graphics.centerString(message, length));
            frame.append(Graphics.FRAME_RIGHT);
            frame.append(Graphics.NEW_LINE);
        }
        else
        {
            //the message does not fit in a single line, so measure the message string
            //until the last newLine character '\n' is found, and if not found, last space character ' ' within the specified length, append it to the frame
            //and send what's left back to this method recursively
            int lastNewlineIndex = message.substring(0, length + 1).lastIndexOf('\n');
            
            if(lastNewlineIndex == -1)
                lastNewlineIndex = message.substring(0, length + 1).lastIndexOf(' ');
            
            if(lastNewlineIndex == -1)
                throw new IllegalArgumentException("Message Box Length too small or a word from the message is too big (len:" + length + ")");
            
            //formatting
            frame.append(Graphics.FRAME_LEFT);
            //frame.append(String.format("%-" + length + 's', message.substring(0, lastSpaceIndex)));
            frame.append(Graphics.centerString(message.substring(0, lastNewlineIndex), length));
            frame.append(Graphics.FRAME_RIGHT);
            frame.append(Graphics.NEW_LINE);
            
            //get what's left of the message into a string
            String nextLine = message.substring(lastNewlineIndex+1);
            
            //recursive godliness
            addMessage(nextLine, frame, length);
        }*/
    }

    private void addMenuItems(StringBuilder frame, int length) {
        //variable to store the biggest possible number of digits when numbering the menu items
        int digits = findNDigits();

        //print the menu items
        for (int i = 0; i < menuItems.size(); i++) {
            frame.append(Graphics.FRAME_LEFT);
            String item = String.format("%" + digits + "d.%s", i + 1, menuItems.get(i));
            String format = "%-" + length + 's';
            frame.append(String.format(format, item));
            frame.append(Graphics.FRAME_RIGHT);
            frame.append(Graphics.NEW_LINE);
        }
    }
}