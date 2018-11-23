package game.buildings.burger;

import game.buildings.Building;
import game.buildings.BuildingMenu;
import game.entities.Food;

import java.util.ArrayList;

import static game.buildings.burger.Burger.getFoodItems;

/*
Food SubMenu for the Burger House, where you choose a food item to buy
 */
public class FoodMenu extends BuildingMenu {
    //messages
    private static String BUY_MESSAGE = "Choose food to eat:";
    //Singleton
    private static FoodMenu instance;

    public static FoodMenu getInstance(Building bld) {
        if (instance == null)
            instance = new FoodMenu(bld);
        return instance;
    }

    private FoodMenu(Building bld, String... menuItems) {
        super(bld.getName(), bld.getDescription(), bld, menuItems);
    }


    @Override
    public void run(int command) {
        message = BUY_MESSAGE;

        if (command != menuItems.size())
            feedPlayer(command - 1);

        runMainMenu();
    }

    //give a food item to the player, if he can afford it
    void feedPlayer(int foodNumber) {
        Food item = getFoodItems()[foodNumber];

        message = Burger.giveFood(foodNumber) ?
                String.format("You bought %s for %.2f$", item.name, item.getValue()) :
                String.format("Not enough money for %s, you need %.2f$", item.name, item.getValue());
    }

    @Override
    protected void refreshMenu() {
        //refresh the menu items, after price fluctuation
        ArrayList<String> menuItems = new ArrayList<>();
        for (Food food : getFoodItems())
            menuItems.add(String.format("%.2f$: %s", food.getValue(), food.name));
        menuItems.add(BACK);
        setMenuItems(menuItems);
    }
}
