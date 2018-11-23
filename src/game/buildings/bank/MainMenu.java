package game.buildings.bank;

import game.buildings.Building;
import game.buildings.BuildingMenu;

import static game.GameWorld.player;

/*
Main menu for the Bank, handles Withdrawing, depositing and leaving the building
since the only thing you can do here is work or go to another building
 */
public class MainMenu extends BuildingMenu {
    //Singleton
    private static MainMenu instance;

    public static MainMenu getInstance(Bank bld) {
        if (instance == null)
            instance = new MainMenu(bld, LEAVE);
        return instance;
    }

    private MainMenu(Building bld, String... menuItems) {
        super(bld.getName(), bld.getDescription(), bld, menuItems);
    }

    //constant String to help build the menu options
    private static final String DEPOSIT_CASH = "Deposit Cash";
    private static final String WITHDRAW_CASH = "Withdraw Cash";

    //input branch executed if either the player chose not to work at the building
    //or if he doesn't have a job at the building
    @Override
    protected void runNoWork(int command) {
        if (menuItems.contains(DEPOSIT_CASH)) {
            //if the menu contains the deposit option and the user chose it,
            //run addMoneyToBank()
            //if not, subtract 1 from the command and execute the next input branch
            if (command == 1)
                addMoneyToBank();
            else
                runNoDeposit(command - 1);
        } else
            //if there is no deposit option, pass the input to the next input branch
            runNoDeposit(command);
    }

    private void runNoDeposit(int command) {
        //if the menu contains the withdraw option and the user chose it,
        //run getMoneyFromBank()
        if (menuItems.contains(WITHDRAW_CASH) && command == 1)
            getMoneyFromBank();
        else
            //if there is no deposit option, leave the building
            runLeaveMenu();
    }

    private void addMoneyToBank() {
        if (player.removeMoney(Bank.DEPOSIT_AMOUNT))
            getBank().deposit(Bank.DEPOSIT_AMOUNT);
    }

    private void getMoneyFromBank() {
        player.addMoney(getBank().withdraw(Bank.DEPOSIT_AMOUNT));
    }

    @Override
    protected void refreshMenu() {
        if (player.hasMoney(Bank.DEPOSIT_AMOUNT)) {
            message = getBank().hasDepositedMoney() ?
                    String.format("Money deposited: %.2f", getBank().getDepositedMoney()) :
                    "No money deposited";
        } else if (!getBank().hasDepositedMoney())
            message = String.format("You can only deposit in bulks of %.2f$", Bank.DEPOSIT_AMOUNT);
        
        /*
        if the player has enough money to deposit, add the Deposit Cash option
        if the player doesn't have enough, remove the option
        */
        //check if the Deposit Cash option is in the menu
        if (this.menuItems.contains(DEPOSIT_CASH)) {
            //if the deposit option is available, but the player doesn't have enough money to deposit
            //remove the option
            if (!player.hasMoney(Bank.DEPOSIT_AMOUNT))
                this.menuItems.remove(DEPOSIT_CASH);
        }
        //if the deposit option is not in the menu BUT
        //the player has enough money to deposit,
        //add the option
        else if (player.hasMoney(Bank.DEPOSIT_AMOUNT))
            this.menuItems.add(getMenuItemIndex(WORK) + 1, DEPOSIT_CASH);
    
        /*
        if the bank has money deposited, add the Withdraw Cash option
        if the bank doesn't have any money inside, remove the option
        */
        //check if the Withdraw Cash option is in the menu
        if (this.menuItems.contains(WITHDRAW_CASH)) {
            //if the withdraw cash option is available, but the bank doesn't have any money inside,
            //remove the option
            if (!getBank().hasDepositedMoney())
                this.menuItems.remove(WITHDRAW_CASH);
        }
        //if the withdraw option is not in the menu BUT
        //the bank has money deposited,
        //add the option
        else if (getBank().hasDepositedMoney())
            this.menuItems.add(getMenuItemIndex(LEAVE), WITHDRAW_CASH);

        super.refreshMenu();
    }

    private Bank getBank() {
        return (Bank) building;
    }
}
