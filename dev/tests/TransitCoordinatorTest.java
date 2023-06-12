import BussinesLogic.*;
import DataAccess.DAO;
import DataAccess.DAO_BranchStore;
import DataAccess.DAO_Generator;
import DomainLayer.Area;
import DomainLayer.Transit;
import DomainLayer.Truck;
import DomainLayer.TruckModel;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.*;

public class TransitCoordinatorTest {

    private static Employee e;
    private static Driver e1;
    private static BranchStore b;
    private static DAO_BranchStore branchStoreDAO;
    private static TransitCoordinator t;

    @BeforeClass
    public static void setup() {
        e = new Driver("Yossi", "Cohen", "1111", "098762", 500, "Salary: 500 per shift", "2022-11-12");
        e1 = new Driver("Shula", "Cohen", "2222", "098763", 500, "Salary: 500 per shift", "2022-11-12");
        try
        {
            b = new BranchStore("Super", Area.East, "Hayarkon-17", "08-689534", "24/7", 11111117);
            t = new TransitCoordinator();
            branchStoreDAO = DAO_Generator.getBranchStoreDAO();

        }catch (SQLException| ClassNotFoundException s)
        {
            s.printStackTrace();
        }
        b.addEmployee(e);
        b.addEmployee(e1);

    }

    @Test
    public void testGetAvailableDrivers() {
        // Set up test data
        LocalDate transitDate = LocalDate.now();
        Set<License> licenses = new HashSet<>();
        licenses.add(License.C1);

        // Call the method under test
        List<Driver> availableDrivers = t.getAvailableDrivers(transitDate, licenses);

        // Verify the result
        assertNotNull(availableDrivers);
        assertEquals(5, availableDrivers.size());
    }

    @Test
    public void testAddDriverToTransit() {
        // Set up test data
        LocalDate transitDate = LocalDate.now();
        String driverID = "123";
        Set<License> licenses = new HashSet<>();
        licenses.add(License.C);
        Truck truck = new Truck("IT54432AI", TruckModel.LARGETRUCK ,9000, 30000);
        truck.addLToLSet(License.C);
        truck.addLToLSet(License.COOLER);
        e1 = new Driver("Shula", "Cohen", "2222", "098763", 500, "Salary: 500 per shift", "2022-11-12");
        e1.addLicense(License.C);
        e1.addLicense(License.COOLER);
        // Create a Transit instance
        Transit transit = new Transit(transitDate,truck, e1);


        // Verify the result
        assertEquals("2222", transit.getDriver().getId());
        assertEquals("Shula Cohen", transit.getDriver().getName());
    }

    @Test
    public void switchDriverInTransit() {
        // Set up test data
        LocalDate transitDate = LocalDate.now();
        String driverID = "123";
        Set<License> licenses = new HashSet<>();
        licenses.add(License.C);
        Truck truck = new Truck("IT54432AI", TruckModel.LARGETRUCK ,9000, 30000);
        truck.addLToLSet(License.C);
        truck.addLToLSet(License.COOLER);
        e1 = new Driver("Shula", "Cohen", "2222", "098763", 500, "Salary: 500 per shift", "2022-11-12");
        e1.addLicense(License.C);
        e1.addLicense(License.COOLER);
        // Create a Transit instance
        Transit transit = new Transit(transitDate,truck, e1);

        t.SwitchDriverInTransit(transitDate,"2222",licenses);
        // Verify the result
        assertEquals("2222", transit.getDriver().getId());
        assertEquals("Shula Cohen", transit.getDriver().getName());
    }

    @Test
    public void testStorageWorkersExist() {
        LocalDate date = LocalDate.now();

        Map<LocalDate,Boolean> m =new HashMap<>();
        b.storekeeperStatusByDate = new HashMap<>();
        b.storekeeperStatusByDate.put(date,true);

        Set<BranchStore> stores = new HashSet<>();
        Set<BranchStore> stores2 = new HashSet<>();
        stores.add(b);

        Employee e = new Employee("Yossi", "Cohen", "1111", "098762",500,"Salary: 500 per shift", "2022-11-12");
        Employee e1 = new Employee("Yossi", "gavi", "1211", "098962",500,"Salary: 500 per shift", "2022-11-12");
        b.addEmployee(e);
        b.addEmployee(e1);
        // Call the method under test
        boolean result = t.StorageWorkersExist(stores, date);
        boolean result2 = t.StorageWorkersExist(stores2,date);
        assertTrue(result);
        //assertFalse(result2);;
    }
}