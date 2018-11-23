package game.buildings.pawnShop;

import game.GameController;
import game.Item;
import game.Possession;
import game.buildings.Building;

import java.util.ArrayList;

import static game.GameWorld.player;

/*
This is the factory building, here you can work, if you have one of the jobs mentioned below
 */
public class PawnShop extends Building {
    private static final String name = "Pawn Shop";
    private static final String desc = "Buy or sell household items here";
    private static final int nItems = 10;
    //Singleton
    private static PawnShop instance = new PawnShop();

    public static PawnShop getInstance() {
        return instance;
    }

    private PawnShop() {
        super(name, desc);
        mainMenu = MainMenu.getInstance(this);

        entrances.remove(3);//remove the bottom door
    }

    static ArrayList<Possession> itemsForSale;

    //no jobs here
    public void init() {
        itemsForSale = new ArrayList<>();

        if (GameController.DEBUG.getBooleanValue()) {
            itemsForSale = new ArrayList<>();
            itemsForSale.add(Possession.getSaleItemsTemplate()[0]);
            itemsForSale.add(Possession.getSaleItemsTemplate()[Possession.getSaleItemsTemplate().length - 1]);
            itemsForSale.add(Possession.getSaleItemsTemplate()[Possession.getSaleItemsTemplate().length - 2]);
        }

        resetInventory();

        jobs = null;
    }

    static boolean hasItemsForSale() {
        return itemsForSale.size() != 0;
    }

    private void resetInventory() {
        //if there are less than a third of items left, refresh the inventory
        if (itemsForSale.size() < nItems / 3)
            itemsForSale = Possession.newSalePool(nItems);

        itemsForSale.forEach(Item::fluctuateValue);
    }

    static boolean sellItem(int itemNumber) {
        if (player.removeMoney(itemsForSale.get(itemNumber).getValue())) {
            Possession item = itemsForSale.remove(itemNumber);
            item.deflateValue();
            player.addPossession(item);
            return true;
        }
        return false;
    }

    static void buyItem(int itemNumber) {
        Possession item = player.sellItem(itemNumber);
        item.inflateValue();
        itemsForSale.add(item);
    }

    @Override
    public void onWeekChange() {
        resetInventory();
    }
}
