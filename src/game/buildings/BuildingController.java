package game.buildings;

import game.GameController;
import game.GameWorld;
import game.buildings.bank.Bank;
import game.buildings.burger.Burger;
import game.buildings.factory.Factory;
import game.buildings.home.Home;
import game.buildings.jobOffice.JobOffice;
import game.buildings.pawnShop.PawnShop;
import game.buildings.rentOffice.RentOffice;
import game.buildings.university.University;
import game.requests.QuitGameRequest;
import game.requests.ShowStatisticsRequest;

import java.util.Arrays;

/*
Main buildings controller
This class is responsible for handling the buildings and their respective menus
 */
public class BuildingController {
    private static BuildingController instance;

    public static BuildingController createInstance(GameWorld gameWorld) {
        instance = new BuildingController(gameWorld);
        return instance;
    }

    public static BuildingController getInstance() {
        return instance;
    }

    private BuildingController(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        setBuildingRelationships();
        reset();
    }

    //The buildings
    private static final Building university = University.getInstance();
    private static final Building factory = Factory.getInstance();
    private static final Building jobOffice = JobOffice.getInstance();
    private static final Building bank = Bank.getInstance();
    private static final Building home = Home.getInstance();
    private static final Building rentOffice = RentOffice.getInstance();
    private static final Building pawnShop = PawnShop.getInstance();
    private static final Building burger = Burger.getInstance();

    private static final Building START_BUILDING = home;

    //array holding references to the buildings, for less redundancy during initialization
    private static final Building[] buildings = {university, factory, jobOffice, bank, home, rentOffice, pawnShop, burger};

    private Building currentBuilding; // reference to the current building being operated
    private Building previousBuilding;// memory reference to the last building accessed

    //the game world itself
    private GameWorld gameWorld;

    //method to handle delayed initialisation of various static variables
    public void init() {
        //The Job class depends on the Course class, so we have to make sure the Jobs are created after the Courses are
        //the Courses are initialised in static code blocks, because they are constants, which are executed before main()
        //so initialize the Jobs when main() is run
        Arrays.stream(buildings).forEach(Building::init);

        //explicitly cast as JobOffice, as the init(Buildings[] buildings) overloaded method is part of JobOffice, not Building
        ((JobOffice) jobOffice).init(buildings);

        setBuildingsMapPosition();
    }

    public void reset() {
        currentBuilding = START_BUILDING;

        Arrays.stream(buildings).forEach(Building::reset);
    }

    public void go() throws QuitGameRequest, ShowStatisticsRequest {
        //player is outside, so have him walk to the next building of choice
        if (currentBuilding == null) {
            if (!GameController.FREEROAM.getBooleanValue())
                throw new NullPointerException("player was in overworld, while freeroaming was false");
            currentBuilding = gameWorld.walkPlayer();
        } else {
            //this statement is true if the player changed the building in the last frame
            //so execute the onPlayerEnter() method
            if (currentBuilding != previousBuilding) {
                previousBuilding = currentBuilding;
                currentBuilding.onPlayerEnter();
                gameWorld.movePlayerTo(currentBuilding);
            }

            //redo the map coordinates for the buildings
            setBuildingsMapPosition();


            //show the map, adding the current building menu to the center of the display
            GameWorld.show(currentBuilding.getCurrentMenu().toSurface());

            //execute the main method of the current building
            currentBuilding.run();

            if (!playerIsInside())
                return;

            //true if player changed buildings
            //get the next building from the onPlayerExit() method and store its reference in currentBuilding
            if (previousBuilding != currentBuilding.nextBuilding)
                currentBuilding = currentBuilding.onPlayerExit();
        }
    }

    //method called by the game controller every time a day passes
    public void onDayChange() {
        Arrays.stream(buildings).forEach(Building::onDayChange);
    }

    public void onWeekChange() {
        Arrays.stream(buildings).forEach(Building::onWeekChange);

        if (currentBuilding != null)
            currentBuilding.onPlayerExit();
        currentBuilding = START_BUILDING;
        currentBuilding.onPlayerEnter();
        setBuildingsMapPosition();
    }

    //set the locations of the buildings on the game map
    //terribly inefficient, since only one building would require its position modified every frame
    private void setBuildingsMapPosition() {
        //redo the boxes for the buildings
        //if the player left a building, the box will no
        //longer have to contain the "You are here" message,
        //so the dimensions of the box will have to be modified
        //same if the player entered a building
        Arrays.stream(buildings).forEach(Building::refreshDisplay);

        //All the coordinates reference the top left corners of the surfaces

        //top left
        university.setMapPosition(
                0,
                0);

        //top middle
        factory.setMapPosition(
                gameWorld.getMap().getHalfWidth() - factory.worldBox.getHalfWidth(),
                0);

        //top right
        jobOffice.setMapPosition(
                gameWorld.getMap().getWidth() - jobOffice.worldBox.getWidth(),
                0);

        //middle left
        bank.setMapPosition(
                0,
                gameWorld.getMap().getHalfHeight() - bank.worldBox.getHalfHeight());

        //middle right
        home.setMapPosition(
                gameWorld.getMap().getWidth() - home.worldBox.getWidth(),
                gameWorld.getMap().getHalfHeight() - home.worldBox.getHalfHeight());

        //bottom left
        rentOffice.setMapPosition(
                0,
                gameWorld.getMap().getHeight() - rentOffice.worldBox.getHeight());

        //bottom middle
        pawnShop.setMapPosition(
                gameWorld.getMap().getHalfWidth() - pawnShop.worldBox.getHalfWidth(),
                gameWorld.getMap().getHeight() - pawnShop.worldBox.getHeight());

        //bottom right
        burger.setMapPosition(
                gameWorld.getMap().getWidth() - burger.worldBox.getWidth(),
                gameWorld.getMap().getHeight() - burger.worldBox.getHeight());
    }

    //set the relationships between the buildings, according to Daniel's map
    //ex: from the university you can go to either the factory, the bank or the rent office
    private void setBuildingRelationships() {
        university.setNextLocations(factory, bank, rentOffice);
        factory.setNextLocations(university, jobOffice, rentOffice, pawnShop);
        jobOffice.setNextLocations(factory, pawnShop, burger, home);
        bank.setNextLocations(university, rentOffice);
        home.setNextLocations(jobOffice, burger);
        rentOffice.setNextLocations(university, factory, bank, pawnShop);
        pawnShop.setNextLocations(factory, jobOffice, rentOffice, burger);
        burger.setNextLocations(jobOffice, home, pawnShop);
    }

    public static Building[] getBuildings() {
        return buildings;
    }

    private boolean playerIsInside() {
        return currentBuilding != null;
    }

    void playerLeft() {
        currentBuilding = null;
    }
}
