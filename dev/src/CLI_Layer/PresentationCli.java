package CLI_Layer;
import ControllerLayer.*;

import java.sql.SQLException;
import java.util.*;

public class PresentationCli {
    public static void transitSystemCli() throws SQLException, ClassNotFoundException {
        PresentationCli ps = new PresentationCli();
        Scanner scanner = new Scanner(System.in);

        TruckCli truckCli = new TruckCli(ControllerGen.getTruckController());
        OrderDocumentCli orderDocumentCli = new OrderDocumentCli(ControllerGen.getOrderDocumentController(),
                ControllerGen.getProductController(), ControllerGen.getSupplierController(), ControllerGen.getTransitCoordinator());
        TransitCli transitCli = new TransitCli(ControllerGen.getTransitController(), ControllerGen.getTruckController(),
                ControllerGen.getTransitCoordinator(), ControllerGen.getOrderDocumentController(), ControllerGen.getTransitRecordController());


        ps.switchMenu(scanner, truckCli, orderDocumentCli, transitCli);
    }


    public void switchMenu(Scanner scanner, TruckCli truckCli, OrderDocumentCli orderDocumentCli, TransitCli transitCli){
        int choice;
        do {
            displayMainMenu();
            choice = scanner.nextInt();
            if (scanner.hasNextLine()) scanner.nextLine();
            switch (choice) {
                case 1: // create new transit
                    transitCli.createNewTransit(scanner);
                    break;
                case 2: // open update transit menu
                    int ch2;
                    do{
                        displayUpdateTransitMenu();
                        ch2 = scanner.nextInt();
                        if (scanner.hasNextLine()) scanner.nextLine();
                        handleUpdateTransitMenu(ch2, scanner, transitCli, orderDocumentCli);
                    } while (ch2<0 || ch2>6);
                    break;
                case 3: // open manage truck menu
                    int ch3;
                do {
                    displayTruckManagerMenu();
                    ch3 = scanner.nextInt();
                    if (scanner.hasNextLine()) scanner.nextLine();
                    handleTruckManagerMenu(ch3, scanner, truckCli);
                } while (ch3<0 || ch3>3);
                    break;
                case 4: //manage documents
                    int ch4;
                    do{
                        displayDocumentManagerMenu();
                        ch4 = scanner.nextInt();
                        if (scanner.hasNextLine()) scanner.nextLine();
                        handleDocumentManagerMenu(ch4, transitCli, orderDocumentCli);
                    } while (ch4<0 || ch4>3);
                    break;
                case 5: //manage orders
                    int ch5;
                    do {
                        displayOrderManagerMenu();
                        ch5 = scanner.nextInt();
                        if (scanner.hasNextLine()) scanner.nextLine();
                        handleOrderManagerMenu(scanner, ch5, orderDocumentCli);
                    } while (ch5<0 || ch5>3);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        } while (choice != 0);
        scanner.close();
    }
    public void displayMainMenu(){
        System.out.println("-----Delivery System Menu-----");
        System.out.println("Please select an option: ");
        System.out.println("1. Create new transit ");
        System.out.println("2. Update transit ");
        System.out.println("3. Manage trucks ");
        System.out.println("4. Manage documents ");
        System.out.println("5. Manage orders ");
        System.out.println("0. Back To main menu");
    }
    public void displayTruckManagerMenu(){
        System.out.println("-----Truck Manager-----");
        System.out.println("1. Add a new truck ");
        System.out.println("2. Remove truck ");
        System.out.println("3. Show all trucks ");
        System.out.println("0. Back to Delivery System Menu ");
    }
    public void displayOrderManagerMenu(){
        System.out.println("-----Manage Orders-----");
        System.out.println("1. Create a new Order ");
        System.out.println("2. Show pending orders (by area) ");
        System.out.println("3. Edit order ");
        System.out.println("0. Back to Delivery System Menu");
    }
    public void displayUpdateTransitMenu(){
        System.out.println("-----Update Transit-----");
        System.out.println("1. print transit details ");
        System.out.println("2. print pending orders ");
        System.out.println("3. Add order to transit ");
        System.out.println("4. Remove order from transit ");
        System.out.println("5. Replace truck ");
        System.out.println("6. Start transit ");
        System.out.println("7. Print store availability dates for receiving transit ");
        System.out.println("0. Back to Delivery System Menu ");
    }
    public void displayEditOrderMenu(){
        System.out.println("-----Edit Order-----");
        System.out.println("1. Add products to an order ");
        System.out.println("2. Update product amount ");
        System.out.println("3. Remove products ");
        System.out.println("0. Back to Delivery System Menu ");
    }
    public void displayDocumentManagerMenu(){
        System.out.println("-----Manage Documents-----");
        System.out.println("1. Show pending orders (by area) ");
        System.out.println("2. Show completed orders ");
        System.out.println("3. Show Transit records ");
        System.out.println("0. Back to Delivery System Menu");
    }
    public void handleDocumentManagerMenu(int ch4, TransitCli transitCli, OrderDocumentCli orderDocumentCli){
        switch (ch4) {
            case 1:
                orderDocumentCli.printPendingOrderDocs();
                break;
            case 2:
                orderDocumentCli.printCompletedOrderDocs();
                break;
            case 3:
                transitCli.printAllTransitRecords();
                break;
            case 0:
                System.out.println("Going back...");
                break;
            default:
                System.out.println("Invalid input");
                break;
        }
    }
    public void handleTruckManagerMenu(int ch3, Scanner scanner, TruckCli truckCli){
        switch (ch3) {
            case 1:
                truckCli.createNewTruck(scanner);
                break;
            case 2:
                truckCli.removeTruckByPlate(scanner);
                break;
            case 3:
                truckCli.printAllTrucks();
                break;
            case 0:
                System.out.println("Going back...");
                break;
            default:
                System.out.println("Invalid input");
                break;
        }
    }
    public void handleUpdateTransitMenu(int ch2, Scanner scanner, TransitCli transitCli, OrderDocumentCli orderDocumentCli){
        switch (ch2){
            case 1:
                transitCli.printTransitDetails(scanner);
                break;
            case 2:
                orderDocumentCli.printPendingOrderDocs();
                break;
            case 3: transitCli.addOrderToTransit(scanner);
                break;
            case 4:
                transitCli.removeOrderFromTransit(scanner);
                break;
            case 5:
                transitCli.replaceTransitTruck(scanner);
                break;
            case 6:
                transitCli.beginTransit(scanner);
                break;
            case 7:
                transitCli.printStoreExistingStorageDates(scanner);
                break;
            case 0:
                System.out.println("Going back...");
                break;
            default:
                System.out.println("Invalid input");
                break;
        }
    }
    public void handleOrderManagerMenu(Scanner scanner, int ch5, OrderDocumentCli orderDocumentCli){
        switch (ch5) {
            case 1: // create new order
                orderDocumentCli.createNewOrderDocument(scanner);
                break;
            case 2: //prints all orders
                orderDocumentCli.printPendingOrderDocs();
                break;
            case 3: //open edit order menu
                int ch53;
                do {
                    displayEditOrderMenu();
                    ch53 = scanner.nextInt();
                    if (scanner.hasNextLine()) scanner.nextLine();
                    handleEditOrderMenu(scanner, ch53, orderDocumentCli);
                } while (ch53<0 || ch53>3);
                break;
            case 0:
                System.out.println("Going back...");
                break;
            default:
                System.out.println("Invalid input");
                break;
        }
    }
    public void handleEditOrderMenu(Scanner scanner, int ch53, OrderDocumentCli orderDocumentCli){
        switch (ch53) {
            case 1:
                orderDocumentCli.addProductToOrder(scanner);
                break;
            case 2:
                orderDocumentCli.updateProductAmount(scanner);
                break;
            case 3:
                orderDocumentCli.removeProductFromOrder(scanner);
                break;
            case 0:
                System.out.println("Going back...");
                break;
        }
    }
}


