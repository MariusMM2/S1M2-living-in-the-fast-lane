package game.buildings.burger;

import game.*;
import game.buildings.Building;
import game.buildings.BuildingMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static game.CourseCollection.*;
import static game.GameWorld.player;

/*
This is the factory building, here you can work, if you have one of the jobs mentioned below
 */
public class Burger extends Building {
    private static final String name = "Burger House";
    private static final String desc = "Eat here";

    private static final Random rand = new Random();

    //Singleton
    private static Burger instance = new Burger();

    public static Burger getInstance() {
        return instance;
    }

    private Burger() {
        super(name, desc);
        //get the menus
        mainMenu = MainMenu.getInstance(this);
        foodMenu = FoodMenu.getInstance(this);

        entrances.remove(3);//remove the bottom door
        entrances.remove(1);//remove the right door
    }

    //
    static BuildingMenu foodMenu;
    private static Food[] foodItems;

    //create the fast foods and the jobs for the building
    public void init() {
        foodItems = new Food[]
                {
                        new Food("Hamburger", 83, 2),
                        new Food("Cheeseburger", 94, 3),
                        new Food("Astro Chicken", 131, 4),
                        new Food("Fries", 68, 1),
                        new Food("Shakes", 108, 3),
                        new Food("Colas", 73, 1)
                };

        jobs = new Job[]
                {
                        new Job("Cook", Job.LOWEST_SALARY, null, 0, this),
                        new Job("Clerk", 6, null, 10, this),
                        new Job("Assistant Manager", 7, null, 15, this),
                        new Job("Manager", 9, businessAdmin, 20, this)
                };
    }

    //give food to player (if he can afford it)
    static boolean giveFood(int foodNumber) {
        return player.eat(foodItems[foodNumber]);
    }

    //give a random food item to the player
    static Food giveRandomFood() {
        //create a new arraylist containing every kind of food,
        //call giveFoodRecursive() method recursively, removing one random food item
        //from the list until one is found that the player can afford
        //if the player can't afford any food, return null
        ArrayList<Food> foods = new ArrayList<>();
        for (Food item : foodItems)
            foods.add(new Food(item));

        return giveFoodRecursive(foods);
    }

    //recursive helper for the random food method
    private static Food giveFoodRecursive(ArrayList<Food> foods) {
        int foodNumber;
        try {
            //try to get a random food number
            foodNumber = rand.nextInt(foods.size());
        } catch (IllegalArgumentException e) {
            //foods.size() is -1, so all the foods have been removed from the queue,
            //which means the player was unable to afford any of them
            return null;
        }

        //player can afford the random food item
        if (giveFood(foodNumber))
            return foods.get(foodNumber);
            //they can't, so remove that item from the list, and recall this method
        else {
            foods.remove(foodNumber);
            return giveFoodRecursive(foods);
        }
    }

    static Food[] getFoodItems() {
        return foodItems;
    }

    @Override
    public void onDayChange() {
        //fluctuate the values of the food items each day
        Arrays.stream(foodItems).forEach(Item::fluctuateValue);
    }
}
