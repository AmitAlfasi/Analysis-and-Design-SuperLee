package ControllerLayer;

import DataAccessLayer.TransitRecordDAO;
import DomainLayer.Transit;
import DomainLayer.TransitRecord;
import java.util.Set;

public class TransitRecordControllerImpl implements TransitRecordController {
    protected final TransitRecordDAO transitRecordDAO;


    public TransitRecordControllerImpl(TransitRecordDAO transitRecordDAO) {
        this.transitRecordDAO = transitRecordDAO;
    }
    @Override
    public TransitRecord createTransitRecord(Transit transit) {
        TransitRecord newTransitRecord = new TransitRecord(transit);
        return newTransitRecord;
    }
    @Override
    public void saveTransitRecordDB(TransitRecord transitRecord) {
        transitRecordDAO.saveTransitRecord(transitRecord);
    }
    @Override
    public void showTransitRecords(){
        System.out.println("-----Transit Records-----");
        Set<TransitRecord> transitRecordSet = transitRecordDAO.getTransitRecordsSet();
        for (TransitRecord transitRecord: transitRecordSet){
            transitRecord.printTransitRecord();
        }
    }

    public void removeTransitRecordCompletely(TransitRecord transitRecord){
        transitRecordDAO.removeTransitRecord(transitRecord);
    }
}
