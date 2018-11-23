import game.Surface;
import game.menus.MenuController;

public class Main {
    private static final byte CONSOLE_WIDTH = 80;
    private static final byte CONSOLE_HEIGHT = 25;

    public static void main(String[] args) {
        //if running through window command prompt,
        //subtract 1 from the width, to avoid having an
        //extra line between the output lines
        //for running windows cmd prompt through .bat
//Surface.displayWidth = CONSOLE_WIDTH-1;
//for running through intellij
//Surface.displayWidth = CONSOLE_WIDTH;
        Surface.WINDOWS_CMD = args.length > 0 && args[0].equals("cmd");

        //subtract 1 from the console height to allocate
        //space for user input
        Surface.displayWidth = CONSOLE_WIDTH;
        Surface.displayHeight = CONSOLE_HEIGHT - 1;

        MenuController mCtrl = MenuController.getInstance();
        mCtrl.go();
    }
}
