package DataAccessLayer;

import DomainLayer.TransitRecord;

import java.util.Set;

public interface TransitRecordDAO {
    void saveTransitRecord(TransitRecord transitRecord);
    Set<TransitRecord> getTransitRecordsSet();
    TransitRecord findTransitRecordByID(int transitRecordId);
    void removeTransitRecord(TransitRecord transitRecord);
}
