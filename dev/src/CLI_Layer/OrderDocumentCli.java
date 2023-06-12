package CLI_Layer;

import BussinesLogic.TransitCoordinator;
import ControllerLayer.OrderDocumentController;
import ControllerLayer.ProductController;
import ControllerLayer.SupplierController;
import DomainLayer.OrderDocument;
import DomainLayer.Product;
import java.util.Scanner;

public class OrderDocumentCli {
    private final OrderDocumentController orderDocumentController;
    private final ProductController productController;
    private final SupplierController supplierController;
    private final TransitCoordinator transitCoordinator;


    public OrderDocumentCli(OrderDocumentController orderDocumentController, ProductController productController, SupplierController supplierController, TransitCoordinator transitCoordinator) {
        this.orderDocumentController = orderDocumentController;
        this.productController = productController;
        this.supplierController = supplierController;
        this.transitCoordinator = transitCoordinator;
    }
    /**
     * This function interacts with the user to collect information about a new orderdocs:
     * Once all the required information is gathered, it uses the OrderService and OrderRepository
     * interfaces to create and save a new Order object to a database.
     */

    public void createNewOrderDocument(Scanner scanner) {
        System.out.println("-----Create new Order-----");
        System.out.println("Enter source (supplier id) of the order: "); // assuming valid supplier id
        int supplierId = scanner.nextInt();
        scanner.nextLine();
        if(supplierController.findSupplierById(supplierId) == null)
        {
            System.out.println("That is not an existing id of a supplier! ");
            return;
        }
        System.out.println("Enter destination (store id)  of the order: "); //assuming valid store id
        int storeId = scanner.nextInt();
        scanner.nextLine();
        if(transitCoordinator.findStoreById(storeId) == null)
        {
            System.out.println("That is not an existing id of a store! ");
            return;
        }
        System.out.println("This order id is: "+ OrderDocument.documentNextId);
        OrderDocument newOrderDoc = orderDocumentController.createOrderDocDBD(supplierId,storeId);
        //TODO also print the docs info ?
        addProductToOrder(scanner);
    }
    /**
     * responsible on adding products to an existing orderDocument
     **/
    public void addProductToOrder(Scanner scanner) {
        int orderDocId = orderDocChoice(scanner);
        if (orderDocId ==-1) // order id not found
        {
            System.out.println("The order id does not exist! ");
            return;
        }
        //int orderDocId;

        OrderDocument orderDocument = orderDocumentController.findOrderDocById(orderDocId);

        double weight = orderDocument.getTotalWeight();
        boolean flag = false;
        while (!flag) {
            System.out.println("Please choose which products you want from the supplier: ");
            //function that prints all products instead of what there is now
            this.productController.showAllProducts();
            String input = scanner.nextLine(); //assuming valid input
            Product newProduct = productController.findProductByName(input);

            if(newProduct != null) //product exist
            {
                System.out.println("Please choose what amount of the product you would like: ");
                Double inAmount = scanner.nextDouble();
                scanner.nextLine();
                weight += inAmount;
                // TODO also add in DB
                orderDocument.getProductsList().put(newProduct, inAmount);
                orderDocumentController.addProductToOrderDocDB(orderDocId, newProduct.getProductId(), inAmount);
                // TODO also add in DB
                System.out.println("Would you like to add more products? ");
                System.out.println(("Y/N "));
                input = scanner.nextLine();

                if (input.equalsIgnoreCase("N")) {
                    flag = true;
                }
            }
            else { System.out.println("Not a valid product name! ");}
        }
        orderDocumentController.updateWeightDB(orderDocument,weight);
        //TODO orderDocumentController.updateOrderDocument(updatedOrderDocument)

    }
    public void updateProductAmount(Scanner scanner){
        int orderDocumentId;
        orderDocumentId = orderDocChoice(scanner);
        if (orderDocumentId ==-1)
        {
            System.out.println("The order id does not exist! ");
            return;
        }
        this.orderDocumentController.showAllProductsInDoc(orderDocumentId);
        System.out.println("Please enter which product you would like to change the Amount: ");
        String productName = scanner.nextLine();

        System.out.println("Please enter the new amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        orderDocumentController.updateAmountDBD(orderDocumentId,productName,amount);
        System.out.println("Amount has changed to: "+ amount);
    }
    public void removeProductFromOrder(Scanner scanner){
        int orderDocumentId;
        orderDocumentId = orderDocChoice(scanner);
        if (orderDocumentId ==-1)
        {
            System.out.println("The order id does not exist! ");
            return;
        }
        orderDocumentController.showAllProductsInDoc(orderDocumentId);
        System.out.println("Please enter which product you would like to remove: ");
        String productName = scanner.nextLine();
        orderDocumentController.removeProductFromOrderDocDBdP(orderDocumentId,productName);
        System.out.println("product: "+ productName + " has been removed");
    };
    public int orderDocChoice(Scanner scanner)
    {
        int orderId;
        System.out.println("What Order would you like to manage? ");
        orderId = scanner.nextInt();
        if (scanner.hasNextLine()) scanner.nextLine();
        if(orderDocumentController.orderDocumentChooser(orderId)){
            return orderId;}
        return -1;
    }
    public void printPendingOrderDocs() {
        orderDocumentController.showPendingOrderDocs();
    }
    public void printCompletedOrderDocs(){
        orderDocumentController.showCompletedOrderDocs();
    }
}
