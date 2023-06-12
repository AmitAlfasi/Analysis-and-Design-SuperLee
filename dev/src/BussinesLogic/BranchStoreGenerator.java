package BussinesLogic;

import DomainLayer.Area;

import java.sql.SQLException;

public class BranchStoreGenerator {

    public static BranchStore CreateBranchStore(String name, Area area, String address, String phoneNum, String openingtime, int id) throws SQLException, ClassNotFoundException {
        return new BranchStore(name, area, address, phoneNum, openingtime, id);
    }

}
