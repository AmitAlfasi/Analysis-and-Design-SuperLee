package DataAccessLayer;

import BussinesLogic.Driver;
import DomainLayer.OrderDocument;
import DomainLayer.Transit;
import DomainLayer.Truck;

import java.util.Set;

public interface TransitDAO {
    void saveTransit(Transit transit);
    void updateOrderDocumentOfTransit(Transit transit, OrderDocument orderDocument, String addOrRemoveFlag); // addOrRemoveFlag = "+1" "-1"
    Transit findTransitByID(int transitId);
    void moveToCompleted(Transit completedTransit);
    Set<Transit> getTransitsSet(boolean isCompleted);
    void removeTransit(Transit transit);
    void updateETA(Transit transit);

//    void addOrderDocumentToTransit(Transit transit, OrderDocument orderDocument);
//    void removeOrderDocumentFromTransit(Transit transit, OrderDocument orderDocument);
    void updateTruckAndDriverOfTransit(Transit transit, Truck newTruck, Driver driver);
}
