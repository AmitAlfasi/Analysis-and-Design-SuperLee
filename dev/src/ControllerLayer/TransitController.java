package ControllerLayer;

import BussinesLogic.Driver;
import DomainLayer.OrderDocument;
import DomainLayer.Transit;
import DomainLayer.Truck;
import ExceptionsPackage.UiException;

public interface TransitController {
    Transit createTransit(String dateString, String truckPlateNumber, String driverId) throws UiException;
    Transit findTransitByID(int transitId);
    Transit showTransitByID(int transitId);
    int replaceTransitTruck(int transitId, String truckPlate);
    int replaceTransitDriver(int transitId, String driverId, String truckPlate, String callingFlag);
    void moveTransitToFinishedDB(Transit transit);
    boolean transferLoad(Truck smallTruck, Truck biggerTruck);
    boolean isDriverAllowToDriveTruck(Truck truck, Driver driver);
    void updateOrderDocumentOfTransit(Transit transit, OrderDocument orderDocument, String addOrRemoveFlag);

    void removeTransitCompletely(Transit transit);
//    Set<Transit> getTransitsSet();
    //TODO belongs to check weight before starting transit.
    boolean isValidWeight(Transit currentTransit, OrderDocument orderDocument);
}
