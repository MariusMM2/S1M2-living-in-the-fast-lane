package game.menus;

import game.GameController;
import game.GlobalVar;
import game.Graphics;

import java.util.ArrayList;

public class SettingsMenu extends Menu {
    private static final String TITLE = "Settings";

    private static SettingsMenu instance;

    public static SettingsMenu getInstance(MenuController menuCtrl) {
        if (instance == null)
            instance = new SettingsMenu(menuCtrl, TITLE);
        return instance;
    }

    private SettingsMenu(MenuController menuCtrl, String title, String... menuItems) {
        super(menuCtrl, title, menuItems);
    }

    private GlobalVar[] variables = new GlobalVar[]
            {
                    Graphics.GFX_STYLE,
                    GameController.FREEROAM,
                    GameController.DEBUG
            };

    @Override
    public void run(int command) {
        if (command == menuItems.size())
            setNextMenu(menuCtrl.mainMenu);
        else
            variables[command - 1].incrementValue();
    }

    @Override
    protected void refreshMenu() {
        ArrayList<String> menuItems = new ArrayList<>();
        for (GlobalVar variable : variables)
            menuItems.add(String.format("%s : %s", variable.type, variable.getLiteralValue()));
        menuItems.add(BACK);

        message = "freeroam is unstable";

        setMenuItems(menuItems);
    }
}
