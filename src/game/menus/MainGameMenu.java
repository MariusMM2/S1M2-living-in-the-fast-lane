package game.menus;

import game.GameController;

//this is the menu that displays the game screen and computes user input
public class MainGameMenu extends Menu {
    private static MainGameMenu instance;

    ///dump this idea
    //lazy initialization, to avoid having the menu instantiated after the controller,
    //which leads to NullPointerException @ menuCtrl var
    static MainGameMenu getInstance(MenuController menuCtrl) {
        if (instance == null)
            instance = new MainGameMenu(menuCtrl);
        return instance;
    }

    private MainGameMenu(MenuController menuCtrl) {
        this.menuCtrl = menuCtrl;
        this.title = "Main Game";
    }

    public void onEnter() {
        nextMenu = null;
    }

    @Override
    public void go() {
        run(0);
    }

    @Override
    public void run(int command) {
        new GameController().go();

        nextMenu = menuCtrl.mainMenu;
    }
}
