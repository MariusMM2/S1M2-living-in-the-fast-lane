package game.buildings;

import game.GameController;
import game.UserInterface;
import game.menus.Menu;
import game.requests.QuitGameRequest;
import game.requests.ShowStatisticsRequest;

import static game.GameWorld.player;

/*
General menu class related to a building, with customised behaviour
 */
public class BuildingMenu extends Menu {
    protected static final String WORK = "Work";
    protected static final String LEAVE = "Leave";
    //reference to the building the menu belongs to
    public Building building;

    //constructor
    public BuildingMenu(String title, String message, Building building, String... menuItems) {
        super(null, title, menuItems);
        this.message = message;
        this.building = building;
        this.canRequest = true;
    }

    //Main execution method
    //The method checks if the player has a job at this building
    //If they do, check if user chooses the "Work" option
    //else, execute runNoWork with default command
    //this method should be overridden by menus belonging to buildings without jobs
    @Override
    public void run(int command) {
        if (playerWorksHere()) {
            if (command == 1)
                player.work();
            else
                runNoWork(command - 1);
        } else
            runNoWork(command);
    }

    //input branch executed if either the player chose not to work at the building
    //or if he doesn't have a job at the building
    protected void runNoWork(int command) {
        switch (command) {
            case 1:
                runLeaveMenu();
                break;
        }
    }

    protected void runMainMenu() {
        building.setMenuToMainMenu();
    }

    //branch of run() executed when user chose the "Leave" option
    protected void runLeaveMenu() {
        if (GameController.FREEROAM.getBooleanValue()) {
            BuildingController.getInstance().playerLeft();
            building.onPlayerExit();
        } else
            building.setMenuToNextLocationMenu();
    }

    //Check if the player has a job belonging to this building
    private boolean playerWorksHere() {
        return player.isWorkingAt(this.building);
    }

    protected int getMenuItemIndex(String item) {
        if (this.menuItems.contains(item))
            return this.menuItems.indexOf(item);
        else
            return -1;
    }

    //refresh the menu to show whether the player can work
    //at this building
    protected void refreshMenu() {
        if (this.menuItems == null)
            return;

        //remove or add the "Work" option depending on the player
        if (playerWorksHere()) {
            //if the "Work" option is not already there, add it
            if (!this.menuItems.get(0).equals(WORK))
                this.menuItems.add(0, WORK);
        } else {
            //if the "Work" options is there, remove it
            if (this.menuItems.get(0).equals(WORK))
                this.menuItems.remove(0);
        }
    }
}
