package game.buildings.rentOffice;

import game.buildings.Building;
import game.entities.Item;
import game.entities.Job;

import static game.entities.CourseCollection.businessAdmin;

/*
This is the Rent Office building, here you can work or pay your rent when time comes
 */
public class RentOffice extends Building {
    private static final String name = "Rent Office";
    private static final String desc = "Pay your rent here";
    //Singleton
    private static RentOffice instance = new RentOffice();

    public static RentOffice getInstance() {
        return instance;
    }

    private RentOffice() {
        super(name, desc);
        mainMenu = MainMenu.getInstance(this);

        entrances.remove(3);//remove the bottom door
        entrances.remove(0);//remove the left door
    }

    private static final double INITIAL_RENT_PRICE = 325.15;

    private static boolean rentIsPaid;
    private static boolean rentIsDue;
    private static Item rent;

    public void init() {
        rentIsDue = false;
        rentIsPaid = true;
        //create an Item to hold the rent
        rent = new Item("Rent", INITIAL_RENT_PRICE, INITIAL_RENT_PRICE / 20.0);

        //create the jobs for the building
        jobs = new Job[]
                {
                        new Job("Groundskeeper", 7, null, 0, this),
                        new Job("Apartment Manager", 10, businessAdmin, 10, this)
                };
    }

    static double getRent() {
        return rent.getValue();
    }

    //trigger for when the rent is due
    public static void rentIsDue() {
        rentIsDue = true;
        rentIsPaid = false;
    }

    //trigger for when the rent has been paid
    static void rentIsPaid() {
        rentIsDue = false;
        rentIsPaid = true;
    }


    public static boolean isRentDue() {
        return rentIsDue;
    }

    public static boolean isRentPaid() {
        return rentIsPaid;
    }

    @Override
    public void onDayChange() {
        rent.fluctuateValue();
    }
}
