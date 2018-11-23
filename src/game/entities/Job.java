package game.entities;

import game.buildings.Building;

/*
Job class
 */
public class Job {
    public static final double HIGHEST_SALARY = 40;
    public static final double LOWEST_SALARY = 4;


    private String name;
    private double wage; //amount of money earned at every press of the "Work" button
    private Course courseNeeded;//university course required to get the job
    private int minExperience;//minimum experience needed to get the job
    private Building workPlace; //building where the job is offered

    public Job(String name, double wage, Course courseNeeded, int minExperience, Building workPlace) {
        this.name = name;
        this.wage = wage;
        this.courseNeeded = courseNeeded;
        this.minExperience = minExperience;
        this.workPlace = workPlace;
    }

    public void work(Player player) {
        player.addSalary(wage);
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public double getWage() {
        return wage;
    }

    public Course getCourseNeeded() {
        return courseNeeded;
    }

    public int getMinExperience() {
        return minExperience;
    }

    public Building getWorkPlace() {
        return workPlace;
    }
}
