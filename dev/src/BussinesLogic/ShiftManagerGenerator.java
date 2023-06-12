package BussinesLogic;

import java.time.LocalDate;

/**
 * this class only generates shift manager objexts
 */
public class ShiftManagerGenerator {

    //generator of shift managers
    public static ShiftManager CreateShiftManager(String name, String id, LocalDate shiftDate, int shiftSlot){
        return new ShiftManager(name,id,shiftDate, shiftSlot);
    }
}
