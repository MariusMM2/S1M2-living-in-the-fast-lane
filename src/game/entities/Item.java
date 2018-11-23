package game.entities;

import java.util.Random;

public class Item {
    protected static final Random rand = new Random();

    public final String name;
    public final double valueOffset;
    public final double initialValue;
    protected double value;

    public Item(String name, double value) {
        this(name, value, value / 10.0);
    }

    public Item(String name, double value, double customOffset) {
        this.name = name;
        this.valueOffset = customOffset;
        this.initialValue = value;
        this.value = value;
    }

    Item(Item that) {
        this(that.name, that.initialValue);
    }

    //increase or decrease the current item value by a fraction of its initial value
    public void fluctuateValue() {
        //if the current item value is at least three quarters of its initial value, fluctuate its value in the normal manner
        //if not, then add a value in the range of 0 to valueOffset to it
        //if we were to only use the first formula, after some time the item value will eventually reach
        //minuscule values, if not even negative values, and we don't want that
        if (value >= (initialValue * 3.0 / 4.0))
            value += rand.nextDouble() * valueOffset * 2.0 - valueOffset;
        else
            value += rand.nextDouble() * valueOffset;
    }

    @Override
    public String toString() {
        return String.format("%7.2f$: %s", value, name);
    }

    public double getValue() {
        return value;
    }
}
