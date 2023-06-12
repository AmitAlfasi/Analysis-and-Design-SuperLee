package DomainLayer;

import BussinesLogic.BranchStore;

import java.util.HashMap;
import java.util.Map;

public class OrderDocument {
    public static int documentNextId;
    private final int documentId;
    private final Supplier source;
    //private final Store destination;

    private final BranchStore destination;

    private double totalWeight; //detailed weight ??
    private Map<Product, Double> productsList;


    public OrderDocument(Supplier source, BranchStore destination) {
        this.documentId = documentNextId;
        documentNextId++;
        this.source = source;
        this.destination = destination;
        this.totalWeight=0;
        this.productsList = new HashMap<>();
    }

    public OrderDocument(int orderDocId, Supplier sourceSupplier, BranchStore destinationStore, double totalWeight, Map<Product, Double> productsList) {
        this. documentId = orderDocId;
        this.source = sourceSupplier;
        this.destination = destinationStore;
        this.totalWeight = totalWeight;
        this.productsList = productsList;
    }
    public int getOrderDocumentId() {
        return documentId;
    }
    public Supplier getSource() {
        return source;
    }
    public BranchStore getDestination() {
        return destination;
    }
    public double getTotalWeight() {
        return totalWeight;
    }
    public Map<Product, Double> getProductsList() {
        return productsList;
    }
    public void setTotalWeight(double weight) {
        totalWeight=weight;
    }
    public void printOrderProductList() {
        for (Map.Entry<Product, Double> entry : productsList.entrySet()) {
            String product = entry.getKey().getProductName();
            Double amount = entry.getValue();
            System.out.println("\t" + product + " : " + amount);
        }
    }
    public void printOrder(){
        System.out.println("Document id: " + this.documentId);
        System.out.println("Source: " + source.address);
        System.out.println("Destination: " + destination.getAddress());
        System.out.println("Total weight: " + totalWeight);
        System.out.println("Products in order: ");
        printOrderProductList();
    }
    public void removeProductFromOrder(Product product){
        double amountToReduce = 0.33;// = productsList.get(product);
        Product product1 = null;
        for (Map.Entry<Product, Double> entry : getProductsList().entrySet()) {
            if (entry.getKey().getProductId() == product.getProductId()) {
                amountToReduce = entry.getValue();
                product1 = entry.getKey();
                break;
            }
        }
        productsList.remove(product1);
        totalWeight-= amountToReduce;
    }
    public void printOrderDestination() {
        System.out.println("Destination is: " + destination.getAddress());
    }
    public void setProductsList(Map<Product, Double> newProductsList) {
        productsList = newProductsList;
    }
    public void printOrderSource(){
        System.out.println("Source is: " + source.address);
    }
    public void printOrderId() {
        System.out.println("Order Id is: " + documentId);
    }
    public void updateProductAmount(double amount,Product product)
    {
        double oldProductAmount = getProductsList().get(product);
        productsList.replace(product,amount);
        totalWeight = totalWeight - oldProductAmount + amount;

    }
}
