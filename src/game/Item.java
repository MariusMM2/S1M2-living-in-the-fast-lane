package game;

import java.util.Random;

public class Item {
    protected static final Random rand = new Random();

    protected final String name;
    protected final double VALUE_OFFSET;
    protected final double INITIAL_VALUE;
    protected double value;

    public Item(String name, double value) {
        this(name, value, value / 10.0);
    }

    public Item(String name, double value, double customOffset) {
        this.name = name;
        this.VALUE_OFFSET = customOffset;
        this.INITIAL_VALUE = value;
        this.value = value;
    }

    Item(Item that) {
        this(that.name, that.INITIAL_VALUE);
    }

    //increase or decrease the current item value by a fraction of its initial value
    public void fluctuateValue() {
        //if the current item value is at least three quarters of its initial value, fluctuate its value in the normal manner
        //if not, then add a value in the range of 0 to VALUE_OFFSET to it
        //if we were to only use the first formula, after some time the item value will eventually reach
        //minuscule values, if not even negative values, and we don't want that
        if (value >= (INITIAL_VALUE * 3.0 / 4.0))
            value += rand.nextDouble() * VALUE_OFFSET * 2.0 - VALUE_OFFSET;
        else
            value += rand.nextDouble() * VALUE_OFFSET;
    }

    @Override
    public String toString() {
        return String.format("%7.2f$: %s", value, name);
    }

    //getters
    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public double getInitialValue() {
        return INITIAL_VALUE;
    }
}
