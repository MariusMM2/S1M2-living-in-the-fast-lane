package game.buildings.university;

import game.Course;
import game.CourseCollection;
import game.buildings.Building;
import game.buildings.BuildingMenu;

import java.util.ArrayList;

import static game.GameWorld.player;

public class EnrollMenu extends BuildingMenu {
    //Singleton
    private static EnrollMenu instance;

    public static EnrollMenu getInstance(Building bld) {
        if (instance == null)
            instance = new EnrollMenu(bld, BACK);
        return instance;
    }

    private EnrollMenu(Building bld, String... menuItems) {
        super(bld.getName(), bld.getDescription(), bld, menuItems);
    }

    private boolean notEnoughMoney = false;
    private Course[] liableCourses;

    @Override
    public void run(int command) {
        if (command != menuItems.size())
            enroll(command - 1);

        runMainMenu();
    }

    private void enroll(int courseNumber) {
        if (player.removeMoney(University.getEnrollFee()))
            University.setCurrentCourse(liableCourses[courseNumber]);
        else {
            notEnoughMoney = true;
            return;
        }
    }

    @Override
    protected void refreshMenu() {
        message = String.format("A small fee of %.2f$ will be applied upon enrolling", University.getEnrollFee());

        if (notEnoughMoney) {
            message = String.format("Not enough money,\nyou need %.2f$", University.getEnrollFee());
            notEnoughMoney = false;
        }

        liableCourses = findLiableCourses();

        ArrayList<String> menuItems = new ArrayList<>();

        for (Course course : liableCourses)
            menuItems.add(course.name);
        menuItems.add(BACK);

        setMenuItems(menuItems);
    }

    private Course[] findLiableCourses() {
        //create a list to hold the liable courses
        ArrayList<Course> liableCourses = new ArrayList<>();

        //iterate through each Course of the of the Collection
        for (Course course : CourseCollection.getCourseList()) {
            //if the player has not graduated this course
            //and has the required previous course for this course,
            //add it to the list
            if (!player.hasCourse(course) &&
                    player.hasCourse(course.courseRequired))
                liableCourses.add(course);

        }
        return liableCourses.toArray(new Course[liableCourses.size()]);
    }
}
