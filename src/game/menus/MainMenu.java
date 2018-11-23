package game.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/*

 */
public class MainMenu extends Menu {
    private static final String TITLE = "Living in the Fast Lane";
    //lazy singleton
    private static MainMenu instance;

    public static MainMenu getInstance(MenuController menuCtrl) {
        if (instance == null)
            instance = new MainMenu(menuCtrl, TITLE);
        return instance;
    }

    private MainMenu(MenuController menuCtrl, String name) {
        super(menuCtrl, name);

        this.subMenus = new Menu[]
                {
                        menuCtrl.mainGameMenu,
                        menuCtrl.settingsMenu,
                        menuCtrl.nullMenu
                };

        for (Menu menu : this.subMenus)
            menuItems.add(menu.title);
    }

    private Menu[] subMenus;

    //Main execution method
    public void run(int command) {
        nextMenu = subMenus[command - 1];
    }
}
