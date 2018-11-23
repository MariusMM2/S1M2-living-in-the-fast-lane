package game;

import game.buildings.BuildingController;
import game.buildings.factory.Factory;
import game.buildings.rentOffice.RentOffice;
import game.requests.QuitGameRequest;
import game.requests.ShowStatisticsRequest;

/*
The main game controller
 */
public class GameController {
    //constant used for debugging purposes
    public static GlobalVar DEBUG = new GlobalVar("debug", false);
    public static GlobalVar FREEROAM = new GlobalVar("freeroam", false);

    public GameController() {
        gameWorld = new GameWorld();
        bldCtrl = BuildingController.createInstance(gameWorld);
    }

    //amount of game tick available per day
    static final int TICKS_PER_DAY_MODIFIER = 4;
    private static final int DAYS_PER_WEEK = 7;
    public static int ticksPerDay;
    static int ticksPerWeek;


    private static boolean isGameOver;
    private GameWorld gameWorld;//reference to the game world
    private BuildingController bldCtrl;//reference to the building controller

    private Player player;//reference to the player

    //reset the game world
    private void resetGame() {
        ticksPerDay = FREEROAM.getBooleanValue() ?
                TICKS_PER_DAY_MODIFIER * TICKS_PER_DAY_MODIFIER :
                TICKS_PER_DAY_MODIFIER;
        ticksPerWeek = ticksPerDay * DAYS_PER_WEEK;
        isGameOver = false;
        gameWorld.reset();
        player = GameWorld.player;
        bldCtrl.reset();
    }

    //method that holds the main game loop
    //game exits the loop when the play is done
    public void go() {
        //reset the game
        resetGame();

        bldCtrl.init();

        if (DEBUG.getBooleanValue())
            player.setJob(Factory.getInstance().getJobs()[8]);

        //main game loop
        while (!isGameOver) {
            //this loops once every week
            int weekTicks = 0;

            do {
                if (isGameOver) {
                    endWeek();
                    return;
                }
                //this loop loops throughout the week
                if (!player.isOutOfSteps()) {
                    //run the controller
                    try {
                        //may throw one of two exceptions
                        bldCtrl.go();

                        //if no exception was thrown,
                        //step the player
                        player.step();
                    } catch (QuitGameRequest e) {
                        //player requested to exit the game, so return to the main menu
                        if (GameWorld.showExitConfirmation())
                            return;
                    } catch (ShowStatisticsRequest e) {
                        //player requested the statistics, so show the statistics
                        GameWorld.showPrompt(player.getStatistics());
                    }
                }

                //increase the week ticks
                weekTicks++;

                player.onTick();

                //each time this statement is true, a day has passed
                if (weekTicks % ticksPerDay == 0)
                    endDay();
            } while (weekTicks % ticksPerWeek != 0);

            endWeek();
        }
    }

    private void endDay() {
        gameWorld.incrementDays();
        bldCtrl.onDayChange();
    }

    //end the week, and also the game, if the player achieved their goal
    private void endWeek() {
        bldCtrl.onWeekChange();
        //if the player achieved their goals, end the game
        if (player.hasAchievedGoals())
            isGameOver = true;

        if (player.didNotEatThisWeek())
            GameWorld.showPrompt(Messaging.DID_NOT_EAT_MESSAGE);

        player.onWeekChange();

        //4 weeks have passed, rent is due
        if (gameWorld.getWeeks() % 4 == 0) {
            RentOffice.rentIsDue();
            GameWorld.showPrompt(Messaging.RENT_IS_DUE);
        }


        //call the endWeek() method of gameWorld
        gameWorld.endWeek();
    }
}
