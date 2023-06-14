package ControllerLayer;
import DomainLayer.Truck;

public interface TruckController {
    Truck createTruck(String plateNumber, int iModel, int[] iQArr,
                      double truckWeight, double maxWeight);
    boolean removeTruckByPlateNumber(String tPlateNumber);
    Truck findTruckByPlate(String tPlateNumber);
    void showAllTrucks();
    boolean transferLoadV2(Truck smallerTruck, Truck biggerTruck);

}
