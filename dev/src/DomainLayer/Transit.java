package DomainLayer;

import BussinesLogic.BranchStore;
import BussinesLogic.Driver;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Transit {
    public static int nextId;

    // changed to static !
    private static final Site source = new Supplier("Logistical Warehouse", Area.Center, "Amitai", "031000778", 0);
    private final int transitId;
    private final LocalDate transitDate;
    private LocalTime departureTime;
    private Truck truck;
    private Driver driver;
    private Double ETA;
    private final Set<OrderDocument> ordersDocs;
    private  Set<Supplier> destinationSuppliers;
    private Set<BranchStore> destinationBranchStores;


    public Transit(LocalDate transitDate, Truck truck, Driver driver) {
        transitId = nextId;
        nextId++;
        this.transitDate = transitDate;
        this.truck = truck;
        this.driver = driver;
        this.ETA = 0.0;
        this.destinationBranchStores = new HashSet<>();
        this.destinationSuppliers = new HashSet<>();
        this.ordersDocs = new HashSet<>();
    }

    public Transit(int transitId, LocalDate transitDate, Truck truck, Driver driver){
        this.transitId = transitId;
        this.transitDate = transitDate;
        this.truck = truck;
        this.driver = driver;
        this.destinationBranchStores = new HashSet<>();
        this.destinationSuppliers = new HashSet<>();
        this.ordersDocs = new HashSet<>();
    }
    public Transit(int transitId, LocalDate transitDate, Truck truck, Driver driver, double eta, Set<Supplier> destinationSuppliers,
                   Set<BranchStore> destinationBranchStores,Set<OrderDocument> ordersDocs) {
        this.transitId = transitId;
        this.transitDate = transitDate;
        this.truck = truck;
        this.driver = driver;
        this.ETA = eta;
        this.destinationBranchStores = destinationBranchStores;
        this.destinationSuppliers = destinationSuppliers;
        this.ordersDocs = ordersDocs;}


    public Double getETA() {
        return ETA;
    }
    public void setETA() {
        this.ETA = calculateETA();
    }
    public double calculateETA() {

        Supplier previousSupplier = null;
        double eta = 0.0;

        if (destinationBranchStores.isEmpty()){
            eta = 0;
            return eta;
        }

// Iterate over the suppliers
        for (Supplier supplier : this.getDestinationSuppliers()) {
            // Check if the previous supplier is in the same area code as the current supplier
            if (previousSupplier != null && previousSupplier.getAreaCode().equals(supplier.getAreaCode())) {
                eta += 15.0;  // add 15 to the ETA if they are in the same area code
            } else {
                eta += 30.0;  // add 30 to the ETA if they are not in the same area code
            }
            previousSupplier = supplier;  // update the previous supplier to the current one
        }

// Check the first branch store and update the ETA accordingly

        BranchStore firstBranchStore = getDestinationStores().iterator().next();

        if (previousSupplier != null && previousSupplier.getAreaCode().equals(firstBranchStore.getAreaCode())) {
            eta += 15.0;  // add 15 to the ETA if the first branch store is in the same area code as the last supplier
        } else {
            eta += 30.0;  // add 30 to the ETA if the first branch store is not in the same area code as the last supplier
        }

        BranchStore previousBranchStore = firstBranchStore;
        for (BranchStore branchStore : this.getDestinationStores()) {
            if (branchStore.getBranchID() != firstBranchStore.getBranchID()) {
                if (previousBranchStore.getAreaCode().equals(branchStore.getAreaCode())) {
                    eta += 15.0;  // add 15 to the ETA if they are in the same area code
                } else {
                    eta += 30.0;  // add 30 to the ETA if they are not in the same area code
                }
                previousBranchStore = branchStore;
            }
        }
        return eta;
    }

    public int getTransitId() {
        return transitId;
    }
    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }
    public LocalDate getTransitDate() {
        return transitDate;
    }
    public Truck getTruck() {
        return truck;
    }
    public Driver getDriver() {
        return driver;
    }
    public Set<Supplier> getDestinationSuppliers() {
        return destinationSuppliers;
    }
    public Set<BranchStore> getDestinationStores() {
        return destinationBranchStores;
    }
    public Set<OrderDocument> getOrdersDocs() {
        return ordersDocs;
    }
    public void printTransit(){
        System.out.println("Transit id: " + transitId);
        System.out.println("Date: " + transitDate);
        System.out.println("Truck Details: ");
        getTruck().printTruck();
        System.out.println("Driver Details: ");
        getDriver().printEmployeeDetails();
        for (OrderDocument od : ordersDocs){
            od.printOrder();
        }
        System.out.println("ETA in minutes for the transit: " + getETA());
    }
    public void addDestinationStore(BranchStore dest){
        destinationBranchStores.add(dest);
    };
    public void addDestinationSupplier(Supplier dest){
        destinationSuppliers.add(dest);
    };
    public void removeDestinationSupplier(Supplier dest)
    {
        int count = 0;
        for (OrderDocument orderDoc : ordersDocs)
        {
            if (orderDoc.getSource().getSupplierId() == dest.getSupplierId())
            {
                count+=1;
            }
        }
        if (count > 1) return;
        destinationSuppliers.remove(dest);
    }
    public void removeDestinationStore(BranchStore dest)
    {
        int count = 0;
        for (OrderDocument orderDoc : ordersDocs)
        {
            if (orderDoc.getDestination().getBranchID() == dest.getBranchID())
            {
                count+=1;
            }
        }
        if (count > 1) return;
        destinationBranchStores.remove(dest);
    }
    public void addOrderDoc(OrderDocument orderDocument){
        ordersDocs.add(orderDocument);
    };
    public void setTruck(Truck truck) {
        this.truck = truck;
    }
    public void setDriver(Driver driver) {
        this.driver = driver;
    }
    public void removeOrderDoc(OrderDocument orderDocument) {
        for (OrderDocument od : ordersDocs){
            if(od.getOrderDocumentId() == orderDocument.getOrderDocumentId())
            {ordersDocs.remove(od);
                return;}

        }
    }
    public double calcOrdersWeight(){
        // TODO why there currentWeight is not an attribute?
        double currentWeight = 0;
        for (OrderDocument od : ordersDocs){
            currentWeight += od.getTotalWeight();
        }
        return currentWeight;
    }
    public static Site getSource() {
        return source;
    }
    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDestinationSuppliers(Set<Supplier> destinationSuppliers) {
        this.destinationSuppliers = destinationSuppliers;
    }

    public void setDestinationBranchStores(Set<BranchStore> destinationBranchStores) {
        this.destinationBranchStores = destinationBranchStores;
    }
}
