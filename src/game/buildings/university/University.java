package game.buildings.university;

import game.GameController;
import game.GameWorld;
import game.buildings.Building;
import game.buildings.BuildingMenu;
import game.entities.Course;
import game.entities.CourseCollection;
import game.entities.Item;
import game.entities.Job;
import game.ui.Messaging;

import static game.GameWorld.player;
import static game.entities.CourseCollection.academic;
import static game.entities.CourseCollection.postDoctoral;

/*
Here you can enroll in courses, so you can increase your Education
and also be able to access better paid jobs.
You can also work here
 */
public class University extends Building {
    private static final String name = "University";
    private static final String desc = "Up your education here";
    private static final double ENROLL_FEE = 75.40;
    //Singleton pattern
    private static University instance = new University();

    public static University getInstance() {
        return instance;
    }

    private University() {
        super(name, desc);
        mainMenu = MainMenu.getInstance(this);
        enrollMenu = EnrollMenu.getInstance(this);

        entrances.remove(2);//remove the top door
        entrances.remove(0);//remove the left door
    }

    static BuildingMenu enrollMenu;

    //number of days the player must attend the classes for the course
    private static final int DAYS_PER_COURSE = 10;
    //how many times the player has to attend classes for the course
    private static int courseAttendance;

    private static Course currentCourse;
    private static int currentCourseProgress;
    private static Item enrollPrice;

    //Initializer for the jobs
    @Override
    public void init() {
        currentCourse = null;
        currentCourseProgress = 0;
        enrollPrice = new Item("Enroll Fee", ENROLL_FEE);

        courseAttendance = DAYS_PER_COURSE * GameController.ticksPerDay;

        jobs = new Job[]
                {
                        new Job("Janitor", 5, null, 0, this),
                        new Job("Teacher", 13, academic, 10, this),
                        new Job("Professor", 27, postDoctoral, 30, this)
                };
    }

    static void setCurrentCourse(Course newCourse) {
        currentCourse = newCourse;
    }

    static Course getCurrentCourse() {
        return currentCourse;
    }

    static void attendCourse() {
        currentCourseProgress++;
        if (currentCourseProgress == courseAttendance) {
            currentCourseProgress = 0;
            player.addCourse(currentCourse);
            GameWorld.showPrompt(Messaging.getGraduatedMessage(currentCourse));
            currentCourse = null;
        }
    }

    static boolean hasCourse() {
        return currentCourse != null;
    }

    static boolean isOutOfCourses() {
        return player.getCourses().size() == CourseCollection.getCourseList().length;
    }

    static double getEnrollFee() {
        return enrollPrice.getValue();
    }

    static double getProgressPercentage() {
        return (double) currentCourseProgress / courseAttendance * 100;
    }

    @Override
    public void onDayChange() {
        enrollPrice.fluctuateValue();
    }
}
