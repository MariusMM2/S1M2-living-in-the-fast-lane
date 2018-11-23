package game;

import java.awt.Rectangle;

/*
Class used for representing and operating 2d arrays of chars
 */
public class Surface {
    private static final char BLANK = '.';//blank char for empty chars inside a surface

    //check if user is running through command prompt
    public static boolean WINDOWS_CMD;

    public static int displayWidth;  //width of the display area of the game
    public static int displayHeight; //height of the display area of the game

    public StringBuilder surface = new StringBuilder();//StringBuilder used to store the chars
    protected Rectangle rect = new Rectangle(); //rectangle object for the surface

    //default constructor
    Surface() {
        reset();
    }

    //standard constructor
    public Surface(StringBuilder surface, int width, int height) {
        this.surface = surface;
        this.rect.width = width;
        this.rect.height = height;
    }

    Surface(String surface, int width, int height) {
        this(new StringBuilder(surface), width, height);
    }

    Surface(int width, int height) {
        this(new StringBuilder(), width, height);
    }

    Surface(String text) {
        this(text, text.length(), 1);
    }

    Surface(StringBuilder surface, int width) {
        this(surface, width, surface.length() / width + 1);
    }

    //copy constructor
    Surface(Surface that) {
        this.surface = new StringBuilder(that.surface);
        this.rect.width = that.rect.width;
        this.rect.height = that.rect.height;
    }

    //return a Surface segment specified by the parameters
    public Surface subSurface(int x, int y, int width, int height) {
        Surface subSurface = new Surface(new StringBuilder(), width, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                subSurface.setChar(j, i, getCharClipped(x + j, y + i));
            }
        }

        return subSurface;
    }

    public Surface subSurface(Rectangle rect) {
        return subSurface(rect.x, rect.y, rect.width, rect.height);
    }

    //reset the surface
    void reset() {
        surface = new StringBuilder();
    }

    //return a String representation of the object, mainly using newlines to represent the Surface in a 2d fashion
    //unless running through the windows cmd, in which case the newlines are omitted and the width of the console
    //is taken into account and expected to be precise
    //this method should only be called when drawing to the screen
    @Override
    public String toString() {
        if (!WINDOWS_CMD) {
            Surface display = new Surface(this);
            for (int i = display.rect.width * display.rect.height; i >= display.rect.width; i -= display.rect.width)
                display.surface.insert(i, '\n');
            return display.surface.toString();
        } else
            return this.surface.toString();
    }

    //getters
    public int getWidth() {
        return rect.width;
    }

    public int getHeight() {
        return rect.height;
    }

    public int getHalfWidth() {
        return rect.width / 2;
    }

    public int getHalfHeight() {
        return rect.height / 2;
    }

    protected char getChar(int x, int y) {
        try {
            return this.surface.charAt(y * this.rect.width + x);
        } catch (StringIndexOutOfBoundsException e) {
            return Graphics.BLANK_CHAR;
        }
    }

    protected char getCharClipped(int x, int y) {
        if (x >= 0 && x < this.getWidth() &&
                y >= 0 && y < this.getHeight())
            return getChar(x, y);
        return BLANK;
    }

    //setters used for adding chars to the surface
    protected void setChar(int x, int y, char newChar) {
        int i = y * this.rect.width + x;
        this.surface.replace(i, i + 1, String.valueOf(newChar));
    }

    //clipped version which checks for boundaries, to avoid out of index errors
    protected void setCharClipped(int x, int y, char newChar) {
        if (x >= 0 && x < this.getWidth() &&
                y >= 0 && y < this.getHeight())
            setChar(x, y, newChar);
    }

    public void replaceAll(char newChar, char... oldChars) {
        for (int i = 0; i < surface.length(); i++)
            for (char oldChar : oldChars)
                if (surface.charAt(i) == oldChar)
                    surface.setCharAt(i, newChar);
    }
}

