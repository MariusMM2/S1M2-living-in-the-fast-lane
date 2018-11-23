package game.buildings.home;

import game.buildings.Building;

/*
This is the home, where you can take a rest, increasing your happiness by 1 + nPossessions every time
 */
public class Home extends Building {
    private static final String name = "Home";
    private static final String desc = "Get some rest here";

    //Singleton
    private static Home instance = new Home();

    public static Home getInstance() {
        return instance;
    }

    private Home() {
        super(name, desc);
        mainMenu = MainMenu.getInstance(this);
        setInitialMenu();

        entrances.remove(1);//remove the right door
    }

    //Empty, since no jobs are available at home
    public void init() {
    }
}
