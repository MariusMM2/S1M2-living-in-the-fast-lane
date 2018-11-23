package game;

public class Food extends Item {
    private int happiness;

    public Food(String name, double value, int happiness) {
        super(name, value);
        this.happiness = happiness;
    }

    public Food(Food that) {
        super(that);
    }

    int getHappiness() {
        return happiness;
    }
}
