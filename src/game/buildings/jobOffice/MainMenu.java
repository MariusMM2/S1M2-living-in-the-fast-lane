package game.buildings.jobOffice;

import game.buildings.Building;
import game.buildings.BuildingMenu;

/*
Main menu for the rent office, where you can choose a building to see the available
jobs at that building
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

    @Override
    public void run(int command) {
        if (command == menuItems.size())
            runLeaveMenu();
        else
            runJobMenu(command - 1);
    }

    //run the job menu for the selected building
    private void runJobMenu(int command) {
        building.setNextMenu(new JobMenu(building, JobOffice.workPlaces[command]));
    }

    //empty, as you can't work here
    @Override
    protected void refreshMenu() {
    }
}
