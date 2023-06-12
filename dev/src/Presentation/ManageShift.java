package Presentation;

import BussinesLogic.Cancellation;
import BussinesLogic.DailyShift;
import BussinesLogic.ShiftManager;
import DataAccess.DAO_DailyShift;
import DataAccess.DAO_Generator;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * this class is made to manage a shift and keep track of all the cancellations
 * has user interactions
 */
public class ManageShift {

    //variables
    private ShiftManager shiftManager;
    private DailyShift currentShift;
    private int branchID;
    private LocalDate date;

    private DAO_DailyShift dailyShiftDAO;

    //constructor
    public ManageShift(ShiftManager shiftManager, DailyShift currentShift, LocalDate date, int branchID) throws SQLException, ClassNotFoundException {
        this.shiftManager = shiftManager;
        this.currentShift = currentShift;
        this.branchID = branchID;
        this.date = date;
        dailyShiftDAO = DAO_Generator.getDailyShiftDAO();
    }

    /**
     * only Shift manager can cancel items
     * saves item name and amount that are canceled
     */
    public void cancelItem() throws SQLException, ClassNotFoundException {
        System.out.println("CANCELLATION SYSTEM");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter item name: ");
        String itemName = scanner.nextLine();
        System.out.println("Enter item amount: ");
        int amount = scanner.nextInt();
        Cancellation cancellation = new Cancellation(itemName,amount);
        this.shiftManager.addToCancelations(cancellation);
        System.out.println("ITEM CANCELLED !!!\nCancellation ID: "+cancellation.getCancelID()+"\nCancellation Details:\nItem: "+cancellation.getItem()+" x"+cancellation.getAmount());
        String temp = scanner.nextLine();
        dailyShiftDAO.update(currentShift, branchID);

    }

    /**
     * upload an end of day file
     */
    public void uploadEndofDayReport() throws SQLException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a file path: ");
        File file = new File(scanner.nextLine());
        currentShift.setEndOfDayReport(file);
        dailyShiftDAO.update(currentShift, branchID);
        System.out.println("Done.");
    }

    /**
     * prints Cancellation information from id given
     */
    public void getCancellation()
    {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Please enter Cancellation ID: ");
            String id = scanner.nextLine();
            Cancellation cancellation = shiftManager.findCancellationInList(Integer.parseInt(id));
            if(cancellation == null)
                System.out.println("Cancellation not found");
            else {
                System.out.println("Cancellation Details:\nItem: "+cancellation.getItem()+" x"+cancellation.getAmount());
            }
        }
        catch (Exception e)
        {
            System.out.println("Sorry, invalid input");
        }


    }
}
