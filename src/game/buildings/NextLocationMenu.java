package game.buildings;

/*
This menu is responsible to handle leaving a building and choosing the next building
This menu is the same across all buildings
 */
public class NextLocationMenu extends BuildingMenu {
    private Building building;

    public NextLocationMenu(Building building) {
        super(null, "Where do you want to go?", building);
        this.building = building;
    }

    //Get the player input ("command") and set the next building at the "command-1"
    //index of the nextLocations array
    @Override
    public void run(int command) {
        building.nextBuilding = building.nextLocations[command - 1];
    }

    /*
    Overriding this method is mandatory, otherwise the game will
    try to add the "Work" option to the menu, which is not something we want.
     */
    @Override
    protected void refreshMenu() {
    }
}
