package game.buildings.university;

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
            instance = new MainMenu(bld, LEAVE);
        return instance;
    }

    private MainMenu(Building bld, String... menuItems) {
        super(bld.getName(), bld.getDescription(), bld, menuItems);
    }

    private static final String ATTEND_COURSE = "Attend Course";
    private static final String ENROLL = "Enroll in a course";

    @Override
    protected void runNoWork(int command) {
        if (menuItems.contains(ATTEND_COURSE)) {
            if (command == 1)
                University.attendCourse();
            else
                runNoAttend(command - 1);
        } else
            runNoAttend(command);
    }

    private void runNoAttend(int command) {
        if (menuItems.contains(ENROLL) && command == 1)
            runEnrollMenu();
        else
            runLeaveMenu();

    }

    private void runEnrollMenu() {
        building.setNextMenu(University.enrollMenu);
    }

    @Override
    protected void refreshMenu() {
        if (University.hasCourse())
            message = String.format(" Progress: %.2f%%\nYou are currently studying:\n%s", University.getProgressPercentage(), University.getCurrentCourse().name);
        else if (University.isOutOfCourses())
            message = "Congratulations!\nYou have completed all courses!";
        else
            message = "You should enroll in a course";
    
        /*
        if University has a course in progress, add the Attend Course option
        if not, remove the option
        */
        //check if the Attend Course option is in the menu
        if (this.menuItems.contains(ATTEND_COURSE)) {
            //if the option is available, but the uni doesn't have any course in progress,
            //remove the option
            if (!University.hasCourse())
                this.menuItems.remove(ATTEND_COURSE);
        }
        //if the option is not available BUT
        //the uni has a course in progress,
        //add the option
        else if (University.hasCourse())
            this.menuItems.add(getMenuItemIndex(WORK) + 1, ATTEND_COURSE);
        
        /*
        if the uni has any courses left and no course in progress, add the Enroll option
        else, remove it
         */
        //check if the Enroll option is available
        if (this.menuItems.contains(ENROLL)) {
            //if the option is available, but the uni is out of courses, or has a course in progress.
            //remove the option
            if (University.isOutOfCourses() || University.hasCourse())
                this.menuItems.remove(ENROLL);
        }
        //if the option is not in the mnu BUT
        //the uni still has available courses AND no course in progress,
        //add the option
        else if (!University.isOutOfCourses() && !University.hasCourse())
            this.menuItems.add(getMenuItemIndex(LEAVE), ENROLL);


        super.refreshMenu();
    }
}
