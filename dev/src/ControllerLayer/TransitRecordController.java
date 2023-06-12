package ControllerLayer;

import DomainLayer.Transit;
import DomainLayer.TransitRecord;

public interface TransitRecordController {
    TransitRecord createTransitRecord(Transit transit);
    void saveTransitRecordDB(TransitRecord transitRecord);
    void removeTransitRecordCompletely(TransitRecord transitRecord);
    void showTransitRecords();

}
