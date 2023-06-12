package BussinesLogic;

/**
 * Cancellation class
 * every shift-manager is allowed to use this class.
 * He can cancel item, get the item that already cancelled, and it's amount.
 */
public class Cancellation {


    private static int serialNumCounter=0; // every item gets a unique id
    private int cancelID;
    private String item;
    private int amount;

    //constructor
    public Cancellation(String itemName, int amount)
    {
        this.item = itemName;
        this.amount = amount;
        serialNumCounter++;
        this.cancelID = serialNumCounter;
    }

    //getters
    public int getCancelID() {return cancelID;}
    public String getItem() {return item;}
    public int getAmount() {return amount;}
}
