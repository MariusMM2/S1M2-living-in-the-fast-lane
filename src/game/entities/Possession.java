package game.entities;

import java.util.ArrayList;
import java.util.Random;


/*
Represents items that are owned by the player and the pawn shop
In the end, this class is redundant, but was kept to improve code readability
 */
public class Possession extends Item {
    public static ArrayList<Possession> newSalePool(int nItems) {
        return PossessionGenerator.makePool(nItems, "saleItem");
    }

    private Possession(String name, double value) {
        super(name, value);
    }

    public Possession(Possession that) {
        super(that);
    }

    //just like in real life, the value of an item fades over time
    //in the game, the value of the item is decreased every week by a
    //random number from 0 to a tenth of its initial value
    //until it reaches a quarter of the initial value
    public void fadeValue() {
        if (value > initialValue / 4.0)
            value -= rand.nextDouble() * initialValue / 10.0;
    }


    //increase the value an item when sold to the pawn shop
    public void inflateValue() {
        value += initialValue / 20.0;
    }

    //decrease the value of an item when bought from the pawn shop
    public void deflateValue() {
        value -= initialValue / 20.0;
    }

    private static class PossessionGenerator {
        private static final Possession[] saleItemsTemplate;

        static {
            saleItemsTemplate = new Possession[]
                    {
                            new Possession("Refrigerator", 820),
                            new Possession("Freezer", 480),
                            new Possession("Stove", 530),
                            new Possession("Color TV", 500),
                            new Possession("VCR", 310),
                            new Possession("Stereo", 380),
                            new Possession("Microwave", 310),
                            new Possession("Hot Tub", 1170),
                            new Possession("PS Vita", 350),
                            new Possession("Razer Headphones", 300),
                            new Possession("Xbox One", 800),
                            new Possession("Laptop", 1000),
                            new Possession("Computer", 1500)
                    };
        }

        private static Random rand = new Random();

        private static ArrayList<Possession> makePool(int nItems, String type) {
            switch (type) {
                case "saleItem":
                    return PossessionGenerator.newSalePool(nItems, saleItemsTemplate);
                default:
                    return null;
            }
        }

        private static ArrayList<Possession> newSalePool(int nItems, Possession[] itemList) {
            ArrayList<Possession> pool = new ArrayList<>();

            for (int i = 0; i < nItems; i++)
                pool.add(new Possession(itemList[rand.nextInt(itemList.length)]));

            return pool;
        }
    }

    //returns the template for the sale items; used for debugging
    public static Possession[] getSaleItemsTemplate() {
        return PossessionGenerator.saleItemsTemplate;
    }
}