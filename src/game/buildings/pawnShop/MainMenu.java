package game.buildings.pawnShop;

import game.GameController;
import game.GameWorld;
import game.UserInterface;
import game.buildings.Building;
import game.buildings.BuildingMenu;

import static game.GameWorld.player;

/*
Main menu for the factory, nothing else other than the constructor is needed,
since the only thing you can do here is work or go to another building
 */
public class MainMenu extends BuildingMenu {
    protected static final int ITEMS_PER_PAGE = 6;
    //Singleton
    private static MainMenu instance;

    public static MainMenu getInstance(Building bld) {
        if (instance == null)
            instance = new MainMenu(bld, "Leave");
        return instance;
    }

    private MainMenu(Building bld, String... menuItems) {
        super(bld.getName(), bld.getDescription(), bld, menuItems);
    }

    private static final String BUY = "Buy";
    private static final String SELL = "Sell";
    private static final String OUT_OF_ITEMS_MSG = "The shop is currently out of items, but check back next week!";

    @Override
    public void run(int command) {
        if (menuItems.contains(BUY)) {
            if (command == 1)
                runBuyMenu();
            else
                runNoBuy(command - 1);
        }
    }

    private void runNoBuy(int command) {
        if (menuItems.contains(SELL) && command == 1)
            runSellMenu();
        else
            runLeaveMenu();
    }

    private void runBuyMenu() {
        building.setNextMenu(new BuyMenu(building));
    }

    private void runSellMenu() {
        building.setNextMenu(new SellMenu(building));
    }

    @Override
    protected void refreshMenu() {
        if (GameController.DEBUG.getBooleanValue())
            UserInterface.println("PawnShop.itemsForSale:" + PawnShop.itemsForSale);
        
        /*
        if the pawn shop has items for sale, add the Buy option
        if the pawn shop is out of items, remove the option
        */
        //check if the Buy option is in the menu
        if (this.menuItems.contains(BUY)) {
            //if the buy option is available, but the shop doesn't have any items
            //remove the option
            if (!PawnShop.hasItemsForSale())
                this.menuItems.remove(BUY);
        }
        //if the Buy option is not in the menu BUT
        //the shop has items for sale,
        //add the option
        else if (PawnShop.hasItemsForSale())
            this.menuItems.add(getMenuItemIndex(WORK) + 1, BUY);
        
        /*
        if the player has any possessions, add the Sell option
        if the player doesn't have any possessions, remove the option
        */
        //check if the Sell option is in the menu
        if (this.menuItems.contains(SELL)) {
            //if the sell option is available, but the player doesn't have any possessions,
            //remove the option
            if (!player.hasAnyPossessions())
                this.menuItems.remove(SELL);
        }
        //if the Sell option is not in the menu BUT
        //the player has any possessions,
        //add the option
        else if (player.hasAnyPossessions())
            this.menuItems.add(getMenuItemIndex(LEAVE), SELL);

        if (!player.hasAnyPossessions() && !PawnShop.hasItemsForSale())
            message = "You and the Shop are out of items!\nThis is unusual, but check back next week anyway.";
        else if (!PawnShop.hasItemsForSale())
            message = OUT_OF_ITEMS_MSG;
        else
            message = building.getDescription();

        super.refreshMenu();
    }
}
