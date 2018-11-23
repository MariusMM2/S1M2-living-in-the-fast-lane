package game.buildings.jobOffice;

import game.GameController;
import game.Job;
import game.buildings.Building;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static game.buildings.university.University.*;

/*
This is the Job Office building, here you can apply for a job, if you meet the requirements
 */
public class JobOffice extends Building {
    private static final String name = "Employment Office";
    private static final String desc = "Find a place to work, here";
    static Building[] workPlaces;
    //Singleton
    private static JobOffice instance = new JobOffice();

    public static JobOffice getInstance() {
        return instance;
    }

    private JobOffice() {
        super(name, desc);
        mainMenu = MainMenu.getInstance(this);

        entrances.remove(2);//remove the top door
        entrances.remove(1);//remove the right door
    }

    //no jobs here
    public void init() {
        jobs = null;
    }

    public void init(Building[] buildings) {
        //get a copy of the buildings
        ArrayList<Building> workPlaces = new ArrayList<>(Arrays.asList(buildings));

        //loop through the buildings and remove the ones without jobs
        for (Building building : buildings)
            if (!building.hasJobs())
                workPlaces.remove(building);

        JobOffice.workPlaces = workPlaces.toArray(new Building[workPlaces.size()]);

        //create a new list to hold the menu items
        /*ex:
        1.Building 1
        2.Building 2
        ...
        n.Leave
         */
        ArrayList<String> menuItems = new ArrayList<>();
        for (Building building : JobOffice.workPlaces)
            menuItems.add(building.getName());
        menuItems.add("Leave");

        //add the newly created menuItems to the main menu
        mainMenu.setMenuItems(menuItems);
    }
}
