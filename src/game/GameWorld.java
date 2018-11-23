package game;

import game.buildings.Building;
import game.buildings.BuildingController;
import game.entities.Player;
import game.requests.QuitGameRequest;
import game.requests.ShowStatisticsRequest;
import game.ui.Messaging;
import game.ui.Surface;
import game.ui.UserInterface;
import game.ui.Window;

import java.awt.*;
import java.util.Arrays;

import static game.GameController.DEBUG;
import static game.GameController.FREEROAM;
import static game.entities.Player.MAX_GOAL;
import static game.entities.Player.MIN_GOAL;

/*
Class holding the main game world behaviour
 */
public class GameWorld {
    private static game.ui.Window gameMap; // 2D representation of the world
    public static Player player;//player reference
    private static int days;//days passed
    private static int weeks;//weeks passed
    private boolean playerEnteredABuilding;
    public final Rectangle rect;

    public GameWorld() {
        rect = new Rectangle(Surface.displayWidth - 2, Surface.displayHeight - 2);
        //-2 for each dimension to accommodate adding a frame before drawing
        gameMap = new game.ui.Window(rect.width, rect.height);
        //reset();
    }

    void reset() {
        days = 0;
        weeks = 1;
        playerEnteredABuilding = false;
        player = createPlayer();
    }

    //getter for the gameMap
    public game.ui.Window getMap() {
        return gameMap;
    }

    //display the game world to the user, with the current menu centered
    private static void show(Surface currentBuildingMenu, boolean skipReset) {
        //add every building to the map
        Arrays.stream(BuildingController.getBuildings()).forEach(building -> building.addToWindow(gameMap));

        if (FREEROAM.getBooleanValue()) {
            gameMap.replaceAll(game.ui.Graphics.ENTITY_WALL, game.ui.Graphics.getFrameList());
            gameMap.addSingle(player.getPos().x, player.getPos().y, game.ui.Graphics.PLAYER_CHAR);
        }

        if (currentBuildingMenu != null) {
            //get the coordinates for the top-left corner of the menu, relative to the centerString of the map
            int xMenu = gameMap.getHalfWidth() - currentBuildingMenu.getHalfWidth();
            int yMenu = gameMap.getHalfHeight() - currentBuildingMenu.getHalfHeight();
            //add the menu at those coordinates
            gameMap.addSurface(xMenu, yMenu, currentBuildingMenu);
        }

        //text representing the current week
        String weekText = String.format("Week %d", weeks);
        //text representing the number of steps left for the week
        String stepsText = String.format("Steps left:%d", player.getStepsLeft());
        //join the two texts together
        StringBuilder weeksAndStepsText = new StringBuilder(weekText).append(game.ui.Graphics.FRAME_BOT).append(stepsText);

        //text representing the total amount of money the player has
        String moneyText = String.format("Money:%.2f$", player.getMoney());

        //text representing the current game progress
        String goalsText = String.format(
                "Wealth:%.0f%%" + game.ui.Graphics.FRAME_TOP +
                        "Happiness:%.0f%%" + game.ui.Graphics.FRAME_TOP +
                        "Education:%.0f%%" + game.ui.Graphics.FRAME_TOP +
                        "Career:%.0f%%",
                player.getWealthPercentage(), player.getHappinessPercentage(), player.getEducationPercentage(), player.getCareerPercentage());

        //text representing information about the current job
        String jobInfoText;
        if (player.getJob() != null)
            jobInfoText = String.format("%s @ %s 4 %2.2f$", player.getJob(), player.getJob().getWorkPlace(), player.getSalary());
        else
            jobInfoText = "You are unemployed";

        //text representing the player's name
        String playerText = String.format("*%S*", player.name);

        //add the frame to the display
        Window display = gameMap.addFrame();
        //add the current week and stepsLeft text to the bottom left corner of the display
        display.addText(1, display.getHeight() - 1, weeksAndStepsText.toString());
        //add the money text to the top left corner of the display
        display.addText(1, 0, moneyText);
        //add the goals text to the top right corner of the display
        display.addText((display.getWidth() - 1) - goalsText.length(), 0, goalsText);
        //add the job info text to the bottom right corner of the display
        display.addText((display.getWidth() - 1) - jobInfoText.length(), display.getHeight() - 1, jobInfoText);
        //add the player name to the bottom center of the display
        display.addText((display.getWidth() / 2) - (playerText.length() / 2), display.getHeight() - 1, playerText);

        //present the display
        display.show();
        if (!skipReset)
            //reset the map surface
            gameMap.reset();
    }

    public static void show(Surface currentBuildingMenu) {
        show(currentBuildingMenu, false);
    }

    private static void show(boolean skipReset) {
        show(null, skipReset);
    }

    public static void showPrompt(Surface message) {
        show(message);
        UserInterface.advance();
    }

    static boolean showExitConfirmation() {
        show(Messaging.EXIT_CONFIRMATION);
        return UserInterface.confirm();
    }

    public Building walkPlayer() throws QuitGameRequest, ShowStatisticsRequest {
        String input;
        boolean getInput = true;
        while (getInput) {
            //show the display and skip resetting it, to avoid player noClip issues
            show(true);
            input = UserInterface.keyInput().toLowerCase();
            switch (input) {
                case "left":
                case "right":
                case "up":
                case "down":
                    player.move(input);
                    getInput = false;
                    break;
                case "quit":
                    throw new QuitGameRequest();
                case "stats":
                    throw new ShowStatisticsRequest();
            }
            //reset the map after the player moved
            gameMap.reset();
        }

        if (playerEnteredABuilding) {
            playerEnteredABuilding = false;
            return checkEnteredBuilding();
        }

        return null;
    }

    public boolean canWalkAt(int x, int y) {
        char aChar = gameMap.getChar(x, y);
        if (aChar != game.ui.Graphics.BLANK_CHAR && aChar != game.ui.Graphics.ENTITY_FLOOR)
            return false;
        if (aChar == game.ui.Graphics.ENTITY_FLOOR)
            playerEnteredABuilding = true;

        return true;
    }

    private Building checkEnteredBuilding() {
        for (Building building : BuildingController.getBuildings())
            for (Point entrance : building.getEntrances())
                if (player.getPos().equals(entrance))
                    return building;
        return null;
    }


    //end the week
    void endWeek() {
        //increase the number of weeks
        weeks++;
        resetDays();
    }

    //reset the current day of the week to 0
    private void resetDays() {
        days = 0;
    }

    //create the player
    private Player createPlayer() {
        //if debugging is enabled, create a default player, with default values
        if (DEBUG.getBooleanValue())
            return new Player(this, "Debugger", 400, 400, 400, 400);
        else {
            String name = UserInterface.stringInput("Enter your name", 2, 20, "name");
            int wealth = UserInterface.intInput("Target wealth", MIN_GOAL, MAX_GOAL);
            int happiness = UserInterface.intInput("Target happiness", MIN_GOAL, MAX_GOAL);
            int education = UserInterface.intInput("Target education", MIN_GOAL, MAX_GOAL);
            int career = UserInterface.intInput("Target career", MIN_GOAL, MAX_GOAL);

            return new Player(this, name, wealth, happiness, education, career);
        }
    }

    int getWeeks() {
        return weeks;
    }

    //increment the days
    void incrementDays() {
        days++;
    }

    public void movePlayerTo(Building bld) {
        if (GameController.FREEROAM.getBooleanValue())
            player.setPos(bld.getRandEntrance());
    }
}
