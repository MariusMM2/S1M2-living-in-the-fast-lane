package game;

import game.buildings.Building;
import game.buildings.bank.Bank;
import game.buildings.rentOffice.RentOffice;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.IntStream;

/*
Player class
 */
public class Player {
    //Goal Boundaries
    static final int MIN_GOAL = 50;
    static final int MAX_GOAL = 400;

    //initial constant values
    private static final double INITIAL_MONEY = 300.0;
    private static final double WORK_EXPERIENCE_PER_DAY = 1;
    private final int INITIAL_STEPS;

    /*
    this ratio shows how much money the player needs to have in relation to the wealth goal
    eg: for any wealth amount, player needs to have wealth * TARGET_MONEY_TO_WEALTH_RATIO dollars
    to reach 100% wealth goal
     */
    private static final int TARGET_MONEY_TO_WEALTH_RATIO = 30;

    //this ratio determines the value of money stored in bank
    private static final double BANK_MONEY_TO_TOTAL_MONEY_RATIO = 1.0;

    //this ratio determines the value of money in pocket, so your pocket money only have a third of their value
    //taken into account for the wealth goal
    private static final double POCKET_MONEY_TO_TOTAL_MONEY_RATIO = 1.0 / 3.0;

    private static final double MAX_CAREER_TO_BEST_JOB_RATIO = MAX_GOAL / Job.HIGHEST_SALARY;

    //goals
    private final int TARGET_WEALTH;
    private final int TARGET_HAPPINESS;
    private final int TARGET_EDUCATION;
    private final int TARGET_CAREER;

    //attributes
    private double wealth;
    private double happiness;
    private double education;
    private double career;

    //player name
    public final String name;

    //if the player doesn't pay his monthly rent until the end of the week,
    //his next week's wage will be affected by this
    private double wageModifier = 1.0;

    //if the player did not eat this week, his next week's steps will be affected by this
    private double stepsModifier = 1;

    private double money;//money in pocket
    private int stepsLeft;//steps left for the current week
    private double workExperience;//
    private Job job; //current job
    private boolean ateThisWeek;
    private ArrayList<Course> graduatedCourses;//list of courses graduated
    private ArrayList<Possession> possessions;//list of possessions
    private Point pos;
    private GameWorld gameWorld;

    Player(GameWorld gameWorld, String name, int w, int h, int e, int c) {
        INITIAL_STEPS = GameController.ticksPerWeek;

        TARGET_WEALTH = w;
        TARGET_HAPPINESS = h;
        TARGET_EDUCATION = e;
        TARGET_CAREER = c;

        this.wealth = 0;
        this.happiness = 0;
        this.education = 0;
        this.career = 0;

        this.gameWorld = gameWorld;
        this.name = name;
        this.money = INITIAL_MONEY;
        this.stepsLeft = INITIAL_STEPS;
        this.workExperience = 0;
        this.job = null;
        this.ateThisWeek = false;
        this.graduatedCourses = new ArrayList<>();
        this.possessions = new ArrayList<>();
        pos = new Point();

        if (GameController.DEBUG.getBooleanValue()) {
            workExperience = Integer.MAX_VALUE;
            money = 1000.00;
            Collections.addAll(possessions, Possession.getSaleItemsTemplate());
            Collections.addAll(graduatedCourses, CourseCollection.getCourseList());
        }
    }

    //eat
    public boolean eat(Food item) {
        if (removeMoney(item.getValue())) {
            ateThisWeek = true;
            increaseHappiness(item.getHappiness());
            return true;
        }

        return false;
    }

    //work
    public void work() {
        job.work(this);
        workExperience += WORK_EXPERIENCE_PER_DAY / GameController.ticksPerDay;

        if (GameController.FREEROAM.getBooleanValue()) {
            if (stepsLeft < GameController.TICKS_PER_DAY_MODIFIER) {
                removeMoney(job.getWage() - job.getWage() / GameController.TICKS_PER_DAY_MODIFIER * stepsLeft);
                step(stepsLeft - 1);
            } else
                step(GameController.TICKS_PER_DAY_MODIFIER - 1);
        }
    }

    //relax, used at home, where the happiness is increased by 1 + the number of current possessions
    public void relax() {
        increaseHappiness(1 + possessions.size());
    }

    //check if player reached their goals
    boolean hasAchievedGoals() {
        return wealth >= TARGET_WEALTH &&
                happiness >= TARGET_HAPPINESS &&
                education >= TARGET_EDUCATION &&
                career >= TARGET_CAREER;
    }

    //check if the player's job belongs to the specified building
    public boolean isWorkingAt(Building bld) {
        //if the player has no job, return false
        if (this.job == null || bld.getJobs() == null)
            return false;

        //search the list of jobs of the building
        //check for each one of them if they match player's job
        for (Job job : bld.getJobs())
            if (this.job == job)
                return true;

        return false;
    }

    //one step
    void step() {
        step(1);
    }

    //n steps
    private void step(int n) {
        if (stepsLeft >= n) {
            stepsLeft -= n;
        }
    }

    //check if player ran out of steps
    boolean isOutOfSteps() {
        return stepsLeft == 0;
    }

    //get remaining steps
    int getStepsLeft() {
        return stepsLeft;
    }

    //resets the steps to INITIAL_STEPS
    private void resetSteps() {
        stepsLeft = (int) (INITIAL_STEPS * stepsModifier);
    }

    double getWealthPercentage() {
        return (wealth / TARGET_WEALTH * 100.0);
    }

    double getHappinessPercentage() {
        return (happiness / TARGET_HAPPINESS * 100.0);
    }

    double getEducationPercentage() {
        return (education / TARGET_EDUCATION * 100.0);
    }

    double getCareerPercentage() {
        return (career / TARGET_CAREER * 100.0);
    }

    //increase happiness by n
    private void increaseHappiness(double n) {
        setHappiness(happiness + n);
    }

    private void increaseEducation(double n) {
        setEducation(education + n);
    }

    private void setWealth(double newValue) {
        wealth = newValue;
        if (wealth > TARGET_WEALTH)
            wealth = TARGET_WEALTH;
        else if (wealth < 0)
            wealth = 0;
    }

    private void setHappiness(double newValue) {
        happiness = newValue;
        if (happiness > TARGET_HAPPINESS)
            happiness = TARGET_HAPPINESS;
        else if (happiness < 0)
            happiness = 0;
    }

    private void setEducation(double newValue) {
        education = newValue;
        if (education > TARGET_EDUCATION)
            education = TARGET_EDUCATION;
        else if (education < 0)
            education = 0;
    }

    private void setCareer(double newValue) {
        career = newValue;
        if (career > TARGET_CAREER)
            career = TARGET_CAREER;
        else if (career < 0)
            career = 0;
    }

    private void decreaseHappiness(double n) {
        happiness -= n;
        if (happiness < 0)
            happiness = 0;
    }

    double getMoney() {
        return money;
    }

    double getSalary() {
        return job.getWage() * wageModifier;
    }


    //Check if player has money
    public boolean hasMoney(double value) {
        return money >= value;
    }

    //add an amount of money
    public void addMoney(double value) {
        money += value;
    }

    void addSalary(double value) {
        addMoney(value * wageModifier);
    }

    //check if the player can afford to remove an amount of money
    //if he can, remove it and return true, else false
    public boolean removeMoney(double value) {
        if (money >= value) {
            money -= value;
            return true;
        }
        return false;
    }

    //if able to pay rent, pay it and reset the wageModifier to 1.0
    //(if it was previously reduced to 0.5)
    public boolean payRent(double rent) {
        if (removeMoney(rent)) {
            wageModifier = 1.0;
            return true;
        }
        return false;
    }

    //setter for the job
    public void setJob(Job job) {
        this.job = job;
        setCareer(job.getWage() * MAX_CAREER_TO_BEST_JOB_RATIO);
    }

    public Job getJob() {
        return job;
    }

    public void addCourse(Course graduatedCourse) {
        graduatedCourses.add(graduatedCourse);
        increaseEducation(graduatedCourse.educationalValue);
    }

    public ArrayList<Course> getCourses() {
        return graduatedCourses;
    }

    public boolean hasCourse(Course thatCourse) {
        //return true if this method was called by a Course
        //with no previous course required
        return thatCourse == null || graduatedCourses.contains(thatCourse);

    }

    public boolean hasAnyPossessions() {
        return possessions.size() != 0;
    }

    public int getWorkExperience() {
        return (int) workExperience;
    }

    void onTick() {
        updateWealth();
        if (GameController.DEBUG.getBooleanValue())
            printDebug();
    }

    void onWeekChange() {
        checkIfAte();
        checkRent();
        resetSteps();

        //decrease the values of the possessions
        possessions.forEach(Possession::fadeValue);
    }

    private void updateWealth() {
        double valueOfCashInBank = Bank.getInstance().getDepositedMoney() * BANK_MONEY_TO_TOTAL_MONEY_RATIO;
        double valueOfCashInPocket = money * POCKET_MONEY_TO_TOTAL_MONEY_RATIO;
        setWealth((valueOfCashInBank + valueOfCashInPocket) / TARGET_MONEY_TO_WEALTH_RATIO);
    }

    //if the player didn't eat this week, his next week he will only be able to do
    //3/4 of his normal steps
    private void checkIfAte() {
        if (!ateThisWeek) {
            stepsModifier = 3.0 / 4.0;
            decreaseHappiness(10);
        } else
            stepsModifier = 1.0;
        ateThisWeek = false;
    }

    //check if the rent is due, and if the player paid it
    //if they didn't, they will get half their salary next week
    private void checkRent() {
        if (RentOffice.isRentPaid())
            wageModifier = 1.0;

        if (RentOffice.isRentDue()) {
            if (RentOffice.isRentPaid())
                wageModifier = 1.0;
            else
                wageModifier = 0.5;
        }
    }

    void move(String direction) {
        switch (direction) {
            case "left":
                moveLeft();
                break;
            case "right":
                moveRight();
                break;
            case "up":
                moveUp();
                break;
            case "down":
                moveDown();
                break;
            default:
        }
    }

    private void moveLeft() {
        if (canMove(pos.x - 1, this.pos.y)) {
            pos.x--;
        }
    }

    private void moveRight() {
        if (canMove(pos.x + 1, this.pos.y)) {
            pos.x++;
        }
    }

    private void moveUp() {
        if (canMove(pos.x, this.pos.y - 1)) {
            pos.y--;
        }
    }

    private void moveDown() {
        if (canMove(pos.x, pos.y + 1)) {
            pos.y++;
        }
    }

    private boolean canMove(int x, int y) {
        return isClippedTo(x, y, gameWorld.rect) && gameWorld.canWalkAt(x, y);
    }

    private boolean isClippedTo(int x, int y, Rectangle rect) {
        return rect.contains(x, y);
    }

    Point getPos() {
        return pos;
    }

    void setPos(Point newPos) {
        pos.x = newPos.x;
        pos.y = newPos.y;
    }

    boolean didNotEatThisWeek() {
        return !ateThisWeek;
    }

    public void addPossession(Possession newItem) {
        possessions.add(newItem);
    }

    //used for debugging the player statistics
    private void printDebug() {
        UserInterface.println("player.wealth:     " + wealth);
        UserInterface.println("player.happiness:  " + happiness);
        UserInterface.println("player.education:  " + education);
        UserInterface.println("player.career:     " + career);
        UserInterface.println("player.money:      " + money);
        UserInterface.println("player.experience: " + workExperience);
        UserInterface.println("player.job:        " + job);
        UserInterface.println("player.courses:    " + graduatedCourses);
        UserInterface.println("player.possessions:" + possessions);
    }

    public ArrayList<Possession> getPossessions() {
        return possessions;
    }

    public Possession sellItem(int itemNumber) {
        Possession item = possessions.remove(itemNumber);
        addMoney(item.getValue());
        return item;
    }

    Surface getStatistics() {
        int winWidth = Surface.displayWidth / 2;

        String goalsText1 = String.format("Wealth: %d, Happiness: %d",
                (int) wealth, (int) happiness);
        String goalsText2 = String.format("Education: %d, Career: %d",
                (int) education, (int) career);
        String moneyText = String.format("Bank: %.2f$, Pocket: %.2f$", Bank.getInstance().getDepositedMoney(), money);
        String jobText = String.format("Job: %s, Wage: %.2f$", (job != null) ? job.getName() : "None", (job != null) ? job.getWage() : 0.0);

        StringBuilder coursesText = new StringBuilder();
        coursesText.append("Courses Graduated: ");
        IntStream.range(0, graduatedCourses.size() - 1).forEach(i -> coursesText.append(graduatedCourses.get(i).name).append("; "));
        coursesText.append(graduatedCourses.size() > 0 ? graduatedCourses.get(graduatedCourses.size() - 1) : "None");

        StringBuilder possessionsText = new StringBuilder();
        possessionsText.append("Possessions: ");
        IntStream.range(0, possessions.size() - 1).forEach(i -> possessionsText.append(possessions.get(i).name).append("; "));
        possessionsText.append(possessions.size() > 0 ? possessions.get(possessions.size() - 1).name : "None");

        String output = goalsText1 + Graphics.NEW_LINE +
                goalsText2 + Graphics.NEW_LINE +
                moneyText + Graphics.NEW_LINE +
                jobText + Graphics.NEW_LINE +
                coursesText + Graphics.NEW_LINE +
                possessionsText + Graphics.NEW_LINE;

        return Messaging.MessageBox(output, winWidth, true);
    }
}