package game.buildings.pawnShop;

import game.buildings.Building;
import game.buildings.BuildingMenu;
import game.entities.Possession;

import java.util.ArrayList;

import static game.buildings.pawnShop.MainMenu.ITEMS_PER_PAGE;
import static game.buildings.pawnShop.PawnShop.itemsForSale;

/*
The buy menu for the pawn shop, where you can buy items from the seller
 */
public class BuyMenu extends BuildingMenu {
    //messages
    private static final String BUY_MESSAGE = "Choose item to buy:";

    private static String getNotBoughtMessage(Possession itemNotBought) {
        return String.format("Not enough money for %s, you need %.2f$.", itemNotBought.name, itemNotBought.getValue());
    }

    private static String getBoughtMessage(Possession itemBought) {
        return String.format("You have bought %s for %.2f$.", itemBought.name, itemBought.getValue());
    }

    BuyMenu(Building bld) {
        super(bld.getName(), bld.getDescription(), bld, BACK);
    }

    //overrides the main run(), as working here is not an option
    @Override
    public void run(int command) {
        message = BUY_MESSAGE;

        if (command != menuItems.size())
            purchaseItem(command - 1);
        else
            runMainMenu();
    }

    private void purchaseItem(int itemNumber) {
        //get a clone of the item going to be purchased, to hold its price before deflation,
        //so the message contains the actual price the player bought the item for, not the deflated price
        Possession itemGhost = new Possession(itemsForSale.get(itemNumber));
        //sell the item to the player
        message = PawnShop.sellItem(itemNumber) ?
                getBoughtMessage(itemGhost) :
                getNotBoughtMessage(itemGhost);
    }

    //refresh the list of items for sale
    @Override
    protected void refreshMenu() {
        ArrayList<String> menuItems = new ArrayList<>();
        ArrayList<Possession> shopItems = itemsForSale;
        for (int i = 0; i < shopItems.size() && i < ITEMS_PER_PAGE; i++)
            menuItems.add(shopItems.get(i).toString());
        menuItems.add(BACK);
        setMenuItems(menuItems);
    }
}
