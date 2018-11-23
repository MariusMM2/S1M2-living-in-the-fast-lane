package game.entities;

public class Food extends Item {
    private int happiness;

    public Food(String name, double value, int happiness) {
        super(name, value);
        this.happiness = happiness;
    }

    public Food(Food that) {
        super(that);
    }

    public int getHappiness() {
        return happiness;
    }
}
