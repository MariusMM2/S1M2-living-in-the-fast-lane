package game.menus;

public class ExitMenu extends Menu {
    private static final String NAME = "Exit";

    private static ExitMenu instance;

    public static ExitMenu getInstance(MenuController menuCtrl) {
        if (instance == null)
            instance = new ExitMenu(menuCtrl, NAME);
        return instance;
    }

    private ExitMenu(MenuController menuCtrl, String name) {
        super(menuCtrl, name);
    }

    @Override
    public void run(int command) {
        System.exit(0);
    }
}
