package game.ui;

import java.awt.*;

/*
Surface subclass used mainly for screen drawing and framing of the surface
 */
public class Window extends Surface {
    //default constructor
    Window() {
        this(displayWidth - 2, displayHeight - 2);
    }

    //specific constructors
    public Window(int width, int height) {
        rect.width = width;
        rect.height = height;
        reset();
    }

    Window(StringBuilder surface, int width, int height) {
        super(surface, width, height);
    }

    Window(String surface, int width, int height) {
        super(surface, width, height);
    }

    //copy constructor
    Window(Surface that) {
        this.surface = new StringBuilder(that.surface);
        this.rect.width = that.rect.width;
        this.rect.height = that.rect.height;
    }

    //replace all the surface elements with a predefined element
    @Override
    public void reset() {
        reset(' ');
    }

    void reset(char blankChar) {
        for (int i = 0; i < rect.height; i++)
            for (int j = 0; j < rect.width; j++)
                setChar(j, i, blankChar);
    }

    //add enclosing frames to the window
    public Window addFrame() {
        //create a bigger copy of this window object
        Window framedWindow = new Window(this.rect.width + 2, this.rect.height + 2);

        //add frame borders
        framedWindow.addSurface(0, 1, Graphics.vLine(Graphics.FRAME_LEFT, this.rect.height));
        framedWindow.addSurface(framedWindow.rect.width - 1, 1, Graphics.vLine(Graphics.FRAME_RIGHT, this.rect.height));
        framedWindow.addSurface(1, 0, Graphics.hLine(Graphics.FRAME_TOP, this.rect.width));
        framedWindow.addSurface(1, framedWindow.rect.height - 1, Graphics.hLine(Graphics.FRAME_BOT, this.rect.width));

        //add frame corners
        framedWindow.setChar(0, 0, Graphics.FRAME_TOP_LEFT);
        framedWindow.setChar(framedWindow.rect.width - 1, 0, Graphics.FRAME_TOP_RIGHT);
        framedWindow.setChar(0, framedWindow.rect.height - 1, Graphics.FRAME_BOT_LEFT);
        framedWindow.setChar(framedWindow.rect.width - 1, framedWindow.rect.height - 1, Graphics.FRAME_BOT_RIGHT);

        //add the original contents inside the frame
        framedWindow.addSurface(1, 1, this);

        return framedWindow;
    }

    //display the Window to the user
    void showFramed() {
        UserInterface.print(this.addFrame());
    }

    public void show() {
        UserInterface.print(this);
    }

    //add a surface or a single char to a predefined position on the window
    public void addSurface(Surface surf) {
        if (surf != null)
            addSurface(0, 0, surf);
    }

    public void addSurface(Point pos, Surface surf) {
        addSurface(pos.x, pos.y, surf);
    }

    public void addSurface(int x, int y, Surface surf) {
        for (int i = 0; i < surf.rect.height; i++)
            for (int j = 0; j < surf.rect.width; j++) {
                setCharClipped(j + x, y + i, surf.getChar(j, i));
            }
    }

    public void addText(int x, int y, String text) {
        addSurface(x, y, new Surface(text));
    }

    public void addSingle(int x, int y, char single) {
        this.setCharClipped(x, y, single);
    }
}
