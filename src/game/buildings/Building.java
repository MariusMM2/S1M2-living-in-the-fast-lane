package game.buildings;

import game.GameController;
import game.entities.Job;
import game.requests.QuitGameRequest;
import game.requests.ShowStatisticsRequest;
import game.ui.Graphics;
import game.ui.Messaging;
import game.ui.Window;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/*
Main abstract building class containing the general variables
used by the concrete (pun intended) buildings
 */
public abstract class Building {
    //prompt to let the player know their location
    private static final String PLAYER_IS_HERE = "You are here";

    private String name;        //Name of the building
    private String description; //Building description
    private boolean hasPlayer;   //Yeah
    protected Job[] jobs;        //the list of possible jobs
    private Rectangle rect; //Position of the building in the game map
    protected ArrayList<Point> entrances;
    protected BuildingMenu mainMenu;    //Main menu, each building will have one
    private BuildingMenu nextLocationMenu; //Menu for the possible destinations
    private BuildingMenu currentMenu;   //Current active menu
    Building nextBuilding;  //Next building, will either be this building or one of the next locations
    Building[] nextLocations; //possible locations to go to when leaving the building
    Window worldBox;      //In-game representation of the building in the game map

    //Constructor
    public Building(String name, String description) {
        this.name = name;
        this.description = description;
        //create the rectangle with an odd (2n+1) width
        rect = new Rectangle(((name.length() % 2 == 0) ? name.length() + 1 : name.length()) + 2, 3);
        nextLocationMenu = new NextLocationMenu(this);
        mainMenu = nextLocationMenu;
        reset();

        entrances = new ArrayList<>();
        entrances.add(new Point(0, rect.height / 2));// left
        entrances.add(new Point(rect.width - 1, rect.height / 2));// right
        entrances.add(new Point(rect.width / 2, 0));// up
        entrances.add(new Point(rect.width / 2, rect.height - 1));// down
    }

    //method called at the start of every game
    public abstract void init();

    void reset() {
        nextBuilding = null;
        setInitialMenu();
    }

    //add the world representation of this building at the specified Window object,
    //in the position specified by mapPosition
    public void addToWindow(Window wnd) {
        wnd.addSurface(this.rect.getLocation(), this.worldBox);
        if (GameController.FREEROAM.getBooleanValue())
            for (Point point : entrances)
                wnd.addSingle(rect.x + point.x, rect.y + point.y, Graphics.ENTITY_FLOOR);
    }

    //Refresh the world box, cycling between showing a normal box
    //and a box telling the player he's here
    void refreshDisplay() {
        if (!hasPlayer || GameController.FREEROAM.getBooleanValue())
            this.worldBox = Messaging.MessageBox(name, rect.width);
        else
            this.worldBox = Messaging.DoubleMessageBox(name, PLAYER_IS_HERE,
                    ((name.length() > PLAYER_IS_HERE.length()) ? name.length() : PLAYER_IS_HERE.length()) + 2);
    }

    //Method called when the player enters the building, before the run() method
    void onPlayerEnter() {
        hasPlayer = true;
        nextBuilding = this;//set the next building to this building, looping until the player decides to leave
        setInitialMenu();
        refreshDisplay(); // refresh the display
    }

    //Method called by the main loop of the building controller object
    void run() throws QuitGameRequest, ShowStatisticsRequest {
        currentMenu.go();
    }

    //Method called when the player exits the building, after the run() method
    Building onPlayerExit() {
        hasPlayer = false;
        refreshDisplay(); // refresh the display to not show the "PLAYER_IS_HERE" message
        return nextBuilding; // return the next building to the controller
    }

    //to be implemented by subclasses, where needed
    public void onDayChange() {
    }

    public void onWeekChange() {
    }

    //set the initial menu when entering the building
    protected void setInitialMenu() {
        currentMenu = mainMenu;
    }

    //various getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BuildingMenu getCurrentMenu() {
        return currentMenu;
    }

    public Job[] getJobs() {
        return jobs;
    }

    //various setters
    public void setNextMenu(BuildingMenu nextMenu) {
        currentMenu = nextMenu;
    }

    public void setMenuToMainMenu() {
        currentMenu = mainMenu;
    }

    public void setMenuToNextLocationMenu() {
        currentMenu = nextLocationMenu;
    }

    public void setNextLocations(Building... buildings) {
        //add the "buildings" parameter list to the array of possible next locations
        nextLocations = buildings;

        ArrayList<String> preMenuItems = new ArrayList<>();
        for (Building building : buildings)
            preMenuItems.add(building.name);

        nextLocationMenu.setMenuItems(preMenuItems);
    }

    public void setMapPosition(int x, int y) {
        rect.x = x;
        rect.y = y;
    }

    @Override
    public String toString() {
        return name;
    }

    public BuildingMenu getMainMenu() {
        return mainMenu;
    }

    public boolean hasJobs() {
        return jobs != null;
    }

    public Point getRandEntrance() {
        Point p = new Point(entrances.get(new Random().nextInt(entrances.size())));

        p.translate(rect.x, rect.y);
        return p;
    }

    public ArrayList<Point> getEntrances() {
        ArrayList<Point> points = new ArrayList<>();

        for (int i = 0; i < entrances.size(); i++)
            points.add(new Point(entrances.get(i)));

        for (Point point : points) {
            point.translate(rect.x, rect.y);
        }

        return points;
    }
}
