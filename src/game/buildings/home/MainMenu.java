package game.buildings.home;

import game.buildings.Building;
import game.buildings.BuildingMenu;

import static game.GameWorld.player;

/*
Main menu for the home, containing the "Relax" and "Leave" options
 */
public class MainMenu extends BuildingMenu {
    //Singleton
    private static MainMenu instance;

    public static MainMenu getInstance(Building bld) {
        if (instance == null)
            instance = new MainMenu(bld, "Relax", "Leave");
        return instance;
    }

    private MainMenu(Building bld, String... menuItems) {
        super(bld.getName(), bld.getDescription(), bld, menuItems);
    }

    /*
    Run through the menu, handling the user input
    Inputs:
    1 - rest
    2 - leave
     */
    @Override
    public void run(int command) {
        switch (command) {
            case 1:
                //player rests, happiness++, time--
                player.relax();
                //GameWorld.getPlayer().step();
                break;
            case 2:
                //player chooses to leave the building,
                //show the menu with the possible travel locations
                runLeaveMenu();
        }
    }

    /*
    Overriding this method is mandatory, otherwise the game will
    try to add the "Work" option to the menu, which is not something we want.
    
    Handling this behaviour from outside is not really necessary, since this is one of the few
    buildings where working is not an option
     */
    @Override
    protected void refreshMenu() {
    }
}
