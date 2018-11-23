package game.buildings.bank;

import game.buildings.Building;
import game.entities.Job;

import static game.entities.CourseCollection.businessAdmin;
import static game.entities.CourseCollection.research;

/*
This is the Bank building, here you can work, deposit or withdraw cash
Each day, the deposited amount will increase by 10% of the raw deposited amount (raw, as in without added interest)
 */
public class Bank extends Building {
    private static final String name = "Bank";
    private static final String desc = "Deposit or withdraw money here";

    //Singleton
    private static Bank instance = new Bank();

    public static Bank getInstance() {
        return instance;
    }

    private Bank() {
        super(name, desc);
        mainMenu = MainMenu.getInstance(this);

        //for Freeroam mode
        entrances.remove(0);//remove the left door
    }

    private static final double INTEREST_RATE_PER_WEEK = 0.1;
    private static final double INTEREST_RATE_PER_DAY = INTEREST_RATE_PER_WEEK / 7;
    protected static final double DEPOSIT_AMOUNT = 100.0; //fixed amount for deposit and withdrawal

    private double depositedRawMoney; //deposited money, without daily interest taken into account
    private double depositedMoney;    //deposited money plus daily interest
    private boolean playerHasMoneyInBank;//if player has any money in bank

    //create the jobs for the building
    public void init() {
        jobs = new Job[]
                {
                        new Job("Janitor", 5.5, null, 0, this),
                        new Job("Teller", 11, null, 10, this),
                        new Job("Assistant Manager", 16, null, 20, this),
                        new Job("Manager", 22, businessAdmin, 20, this),
                        new Job("Investment Broker", 25, research, 40, this)
                };
    }

    //deposit an amount in the bank
    void deposit(double value) {
        //increase the two storages and set the flag to true
        depositedRawMoney += value;
        depositedMoney += value;
        playerHasMoneyInBank = true;
    }

    //withdraw an amount from the bank
    double withdraw(double value) {
        //if the deposited cash is bigger than the value,
        //withdraw the value amount from the bank
        if (depositedMoney >= value) {
            depositedMoney -= value;
            depositedRawMoney -= value;
        }
        //if not, withdraw what's left in the bank and flip the flag
        else {
            value = depositedMoney;
            depositedMoney = 0.0;
            depositedRawMoney = 0.0;
            playerHasMoneyInBank = false;
        }
        return value;
    }

    //check if player has money in the bank
    public boolean hasDepositedMoney() {
        return playerHasMoneyInBank;
    }

    //getter
    public double getDepositedMoney() {
        return depositedMoney;
    }

    //on each day, add the interest to deposited money
    @Override
    public void onDayChange() {
        if (playerHasMoneyInBank)
            depositedMoney += depositedRawMoney * INTEREST_RATE_PER_DAY;
    }
}
