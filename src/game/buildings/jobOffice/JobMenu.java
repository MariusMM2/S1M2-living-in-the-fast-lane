package game.buildings.jobOffice;

import game.Job;
import game.buildings.Building;
import game.buildings.BuildingMenu;

import java.util.ArrayList;

import static game.GameWorld.player;

/*
This menu presents the jobs available at a specific building
 */
public class JobMenu extends BuildingMenu {
    private Job[] liableJobs;
    private Building targetBuilding;

    public JobMenu(Building bld, Building targetBld) {
        super("Jobs at the " + targetBld.getName(), null, bld);
        targetBuilding = targetBld;
    }

    @Override
    public void run(int command) {
        if (command < menuItems.size())
            setPlayerJob(command);
        else
            runMainMenu();
    }

    @Override
    protected void refreshMenu() {
        //store a new list of possible jobs
        liableJobs = findLiableJobs(targetBuilding.getJobs());

        //the message
        message = liableJobs.length == 0 ?
                "No jobs available" :
                String.format("%d %s available", liableJobs.length, liableJobs.length == 1 ? "job" : "jobs");

        //add the list of jobs to the menu
        ArrayList<String> menuItems = new ArrayList<>();
        for (Job job : liableJobs)
            menuItems.add(String.format("%5.2f$:%s", job.getWage(), job.getName()));
        menuItems.add(BACK);
        setMenuItems(menuItems);
    }

    //give the player a job
    private void setPlayerJob(int command) {
        player.setJob(liableJobs[command - 1]);
    }

    //return a list of jobs the player is qualified for,
    //from the given list of jobs
    private Job[] findLiableJobs(Job[] availableJobs) {
        //create a list to hold the liable jobs
        ArrayList<Job> liableJobs = new ArrayList<>();

        //iterate through each job in the given list
        for (Job job : availableJobs) {
            //player has this job already
            if (job.equals(player.getJob()))
                continue;

            //booleans to course requirement
            boolean playerHasCourse = player.hasCourse(job.getCourseNeeded());

            //if the player's working experience is at least equal
            //to the experience required by the job,
            //set the playerHasExperience flag to true
            boolean playerHasExperience = player.getWorkExperience() >= job.getMinExperience();

            //if the player meets both conditions for the job,
            //add it to the list of liable jobs
            if (playerHasCourse && playerHasExperience)
                liableJobs.add(job);
        }

        //return the list of liable jobs as an array
        return liableJobs.toArray(new Job[liableJobs.size()]);
    }
}
