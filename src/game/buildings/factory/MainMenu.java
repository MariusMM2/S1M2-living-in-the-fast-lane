package game.buildings.factory;

import game.buildings.Building;
import game.buildings.BuildingMenu;

/*
Main menu for the factory, nothing else other than the constructor is needed,
since the only thing you can do here is work or go to another building
 */
public class MainMenu extends BuildingMenu {
    //Singleton
    private static MainMenu instance;

    public static MainMenu getInstance(Building bld) {
        if (instance == null)
            instance = new MainMenu(bld, "Leave");
        return instance;
    }

    private MainMenu(Building bld, String... menuItems) {
        super(bld.getName(), bld.getDescription(), bld, menuItems);
    }
}
