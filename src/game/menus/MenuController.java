package game.menus;

import game.Graphics;
import game.requests.QuitGameRequest;
import game.requests.ShowStatisticsRequest;

/*
Controller responsible for handling the menus outside the main game
 */
public class MenuController {
    //Singleton
    private static MenuController instance = new MenuController();

    public static MenuController getInstance() {
        return instance;
    }

    //Creates a new menu controller that instantiates the menus
    private MenuController() {
        mainGameMenu = MainGameMenu.getInstance(this);
        settingsMenu = SettingsMenu.getInstance(this);
        nullMenu = ExitMenu.getInstance(this);
        mainMenu = MainMenu.getInstance(this);
        //Set the initial menu at the start of the app
        currentMenu = mainMenu;
    }

    //Store references to all the menus in the app
    final Menu mainMenu,
            mainGameMenu,
            settingsMenu,
            nullMenu;

    //Store the current menu to be operated on
    private Menu currentMenu;

    //starts the controller
    public void go() {
        //while the current menu is a valid menu, execute it
        while (currentMenu != nullMenu) {
            currentMenu.onEnter();
            try {
                currentMenu.go();
            }
            //ignore the in game requests (exceptions), as we are not in any game
            catch (QuitGameRequest | ShowStatisticsRequest ignore) {
            }
            //get the next menu
            currentMenu = currentMenu.onExit();
        }
    }
}
