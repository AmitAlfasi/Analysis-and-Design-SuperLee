package ControllerLayer;

import DataAccessLayer.TruckDAO;
import DomainLayer.Truck;

import java.util.Set;

public interface TruckController {
    Truck createTruck(String plateNumber, int iModel, int[] iQArr,
                      double truckWeight, double maxWeight);
    boolean removeTruckByPlateNumber(String tPlateNumber);
    Truck findTruckByPlate(String tPlateNumber);
    TruckDAO getTruckDAO();
    void showAllTrucks();
    boolean showTruckByPlate(String tPlateNumber);


    void transferLoad(Truck smallerTruck, Truck biggerTruck);
    public boolean transferLoadV2(Truck smallerTruck, Truck biggerTruck);


}
