package game.buildings.factory;

import game.buildings.Building;
import game.entities.Job;

import static game.entities.CourseCollection.*;

/*
This is the factory building, here you can work, if you have one of the jobs mentioned below
 */
public class Factory extends Building {
    private static final String name = "Factory";
    private static final String desc = "Work your damning lower back here";
    //Singleton
    private static Factory instance = new Factory();

    public static Factory getInstance() {
        return instance;
    }

    private Factory() {
        super(name, desc);
        mainMenu = MainMenu.getInstance(this);

        entrances.remove(2);//remove the top door
    }

    //create the jobs for the building
    public void init() {
        jobs = new Job[]
                {
                        new Job("Janitor", 9, null, 0, this),
                        new Job("Assembly Worker", 10, null, 10, this),
                        new Job("Secretary", 11, null, 20, this),
                        new Job("Machinist's Helper", 13, preEngineering, 20, this),
                        new Job("Executive Secretary", 24, businessAdmin, 25, this),
                        new Job("Machinist", 26, engineering, 30, this),
                        new Job("Department Manager", 29, postDoctoral, 35, this),
                        new Job("Engineer", 31, engineering, 40, this),
                        new Job("General Manager", Job.HIGHEST_SALARY, postDoctoral, 50, this)
                };
    }
}
