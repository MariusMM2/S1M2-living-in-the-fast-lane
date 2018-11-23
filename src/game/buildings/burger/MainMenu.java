package game.buildings.burger;

import game.buildings.Building;
import game.buildings.BuildingMenu;
import game.entities.Food;

/*
Main menu for the Burger House, where you can enter the food menu, order a random food item,
or leave the building
 */
public class MainMenu extends BuildingMenu {
    //string constants used for messaging the player
    private static final String STD_MSG = "Choose a food from the menu, or get a random food.";
    private static final String NO_MONEY_MSG = "You can't afford anything, get some cash first.";

    private static String getBoughtMessage(Food foodBought) {
        return String.format("You bought %s for %.2f$", foodBought.name, foodBought.getValue());
    }

    //Singleton
    private static MainMenu instance;

    public static MainMenu getInstance(Building bld) {
        if (instance == null)
            instance = new MainMenu(bld, "Menu", "Eat randomly", LEAVE);
        return instance;
    }

    private MainMenu(Building bld, String... menuItems) {
        super(bld.getName(), bld.getDescription(), bld, menuItems);
    }

    @Override
    protected void runNoWork(int command) {
        //reset the message
        message = STD_MSG;

        //process the input
        switch (command) {
            case 1:
                runFoodMenu();
                break;
            case 2:
                getRandomFood();
                break;
            case 3:
                runLeaveMenu();
                break;
        }

    }

    //go to the Food Menu
    private void runFoodMenu() {
        building.setNextMenu(Burger.foodMenu);
    }

    //get a random food item
    private void getRandomFood() {
        Food foodBought = Burger.giveRandomFood();

        message = (foodBought == null) ?
                NO_MONEY_MSG :
                getBoughtMessage(foodBought);
    }
}
