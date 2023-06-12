package DomainLayer;

import java.util.HashSet;
import java.util.Set;

/**
 * consider make this class "normal class" (not abstract)
 * need to add more methods
 **/

public class Truck {
    final String plateNumber;
    final TruckModel model;
    Set<BussinesLogic.License> lSet;
    final double truckWeight;
    double currentLoadWeight;
    final double maxCarryWeight;


    public Truck(String plateNumber, TruckModel model, double truckWeight, double maxWeight) {
        this.plateNumber = plateNumber;
        this.model = model;
        this.truckWeight = truckWeight;
        this.currentLoadWeight = 0;
        this.maxCarryWeight = maxWeight;
        this.lSet = new HashSet<BussinesLogic.License>();
    }

    public Truck(String plateNumber, TruckModel model, double truckWeight, double maxWeight, Set<BussinesLogic.License> lSet) {
        this.plateNumber = plateNumber;
        this.model = model;
        this.truckWeight = truckWeight;
        this.currentLoadWeight = 0;
        this.maxCarryWeight = maxWeight;
        this.lSet = lSet;
    }

    public double getTruckWeight() {
        return truckWeight;
    }

    public double getMaxCarryWeight() {
        return maxCarryWeight;
    }

    public double getCurrentWeight() {
        return currentLoadWeight;
    }

    public void loadTruck(double weight) {
        currentLoadWeight += weight;
    }

    ; // will be inside truck service

    public void unloadTruck(double weight) {
        currentLoadWeight -= weight;
    }

    public Set<BussinesLogic.License> getTruckLicenses() {
        return this.lSet;
    }

    public void addLToLSet(BussinesLogic.License lSet) {
        this.lSet.add(lSet);
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public TruckModel getModel() {
        return this.model;
    }

    public void setCurrentLoadWeight(double currentLoadWeight) {
        this.currentLoadWeight = currentLoadWeight;
    }

    public void printTruck() {
        System.out.println("Plate number: " + this.plateNumber);
        System.out.println("Model: " + this.model);
        System.out.println("Truck's weight: " + this.truckWeight);
        System.out.println("Current load weight: " + this.currentLoadWeight);
        System.out.println("Max carry weight: " + this.maxCarryWeight);
        if (!lSet.isEmpty()) {
            System.out.println("Licenses: ");
            for (BussinesLogic.License l : lSet) {
                System.out.println("\t" + l);
            }
        }
        System.out.println();
    }


}