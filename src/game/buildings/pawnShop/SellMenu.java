package game.buildings.pawnShop;

import game.buildings.Building;
import game.buildings.BuildingMenu;
import game.entities.Possession;

import java.util.ArrayList;

import static game.GameWorld.player;
import static game.buildings.pawnShop.MainMenu.ITEMS_PER_PAGE;

/*
menu to handle selling items from the player's inventory
 */

public class SellMenu extends BuildingMenu {
    private static final String SELL_MESSAGE = "Choose item to sell:";

    private static String getSoldMessage(Possession itemSold) {
        return String.format("You have sold your %s for %.2f$.", itemSold.name, itemSold.getValue());
    }

    SellMenu(Building bld, String... menuItems) {
        super(bld.getName(), bld.getDescription(), bld, menuItems);
    }

    @Override
    public void run(int command) {
        message = SELL_MESSAGE;

        if (command != menuItems.size())
            sellItem(command - 1);
        else
            runMainMenu();
    }

    private void sellItem(int itemNumber) {
        //in contrast to the Buy Menu, here we don't need a clone of the item,
        //as the Pawn Shop will always be able to pay the price of an item
        Possession itemSold = player.getPossessions().get(itemNumber);
        message = getSoldMessage(itemSold);
        PawnShop.buyItem(itemNumber);
    }

    //menu items refresh
    @Override
    protected void refreshMenu() {
        ArrayList<String> menuItems = new ArrayList<>();
        ArrayList<Possession> playerItems = player.getPossessions();
        for (int i = 0; i < playerItems.size() && i < ITEMS_PER_PAGE; i++)
            menuItems.add(playerItems.get(i).toString());
        menuItems.add(BACK);
        setMenuItems(menuItems);
    }

}
