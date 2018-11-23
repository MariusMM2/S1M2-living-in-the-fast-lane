package game.buildings.rentOffice;

import game.GameWorld;
import game.buildings.Building;
import game.buildings.BuildingMenu;

import static game.GameWorld.player;

/*
Main menu for the factory, nothing else other than the constructor is needed,
since the only thing you can do here is work or go to another building
 */
public class MainMenu extends BuildingMenu {
    //Singleton
    private static MainMenu instance;

    public static MainMenu getInstance(Building bld) {
        if (instance == null)
            instance = new MainMenu(bld, "Pay Rent", "Leave");
        return instance;
    }

    private MainMenu(Building bld, String... menuItems) {
        super(bld.getName(), bld.getDescription(), bld, menuItems);
    }

    private boolean notEnoughMoney = false;

    @Override
    protected void runNoWork(int command) {
        switch (command) {
            case 1:
                payRent();
                break;
            case 2:
                runLeaveMenu();
        }
    }

    private void payRent() {
        if (RentOffice.isRentDue()) {
            if (player.payRent(RentOffice.getRent()))
                RentOffice.rentIsPaid();
            else
                notEnoughMoney = true;
        }
    }

    @Override
    protected void refreshMenu() {
        if (notEnoughMoney) {
            message = String.format("Not enough Money, you need %.2f$", RentOffice.getRent());
            notEnoughMoney = false;
        } else {
            if (RentOffice.isRentDue())
                message = String.format("It's time to pay the rent: %.2f$", RentOffice.getRent());
            else
                message = "No need to pay your rent... yet";
        }

        super.refreshMenu();
    }
}
