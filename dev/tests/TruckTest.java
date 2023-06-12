import BussinesLogic.License;
import ControllerLayer.ControllerGen;
import ControllerLayer.TruckController;
import DataAccess.DAO_Generator;
import DataAccessLayer.TruckDAO;
import DomainLayer.Truck;
import DomainLayer.TruckModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TruckTest {
    private TruckDAO truckDAO;
    private Truck truck;
    private TruckController truckController;
    @Before
    public void setUp() throws Exception {
        truckDAO = DAO_Generator.getTruckDAO();
        // Generate a random plate number for the test truck
        truckController = ControllerGen.getTruckController();
        int[] iLarr = {0,2};

        //truck = truckController.createTruck("IT54432AI", 1, iLarr ,9000, 30000);
        truck = new Truck("IT54432AI", TruckModel.LARGETRUCK ,9000, 30000);
        truck.addLToLSet(License.C);
        truck.addLToLSet(License.COOLER);
    }
    @After
    public void tearDown() throws Exception {
        truckDAO.removeTruck(truck);
    }

    @Test
    public void testAddLicense()
    {
        truck.addLToLSet(License.C1);
        assertEquals(3, truck.getTruckLicenses().size());
    }

    @Test
    public void testLoadTruck()
    {
        truck.loadTruck(777);
        assertEquals(777, truck.getCurrentWeight(), 0.01);
        truck.setCurrentLoadWeight(999);
        assertEquals(999, truck.getCurrentWeight(), 0.01);
    }

    @Test
    public void testFindTruck() {
        truckDAO.saveTruck(truck);
        Truck savedTruck = truckDAO.findTruckByPlateNumber(truck.getPlateNumber());
        assertNotNull(savedTruck);
        assertEquals(truck.getPlateNumber(), savedTruck.getPlateNumber());
        assertEquals(truck.getModel(), savedTruck.getModel());
        assertTrue(truck.getTruckLicenses().containsAll(savedTruck.getTruckLicenses()));
        assertTrue(savedTruck.getTruckLicenses().containsAll(truck.getTruckLicenses()));
        assertEquals(truck.getTruckWeight(), savedTruck.getTruckWeight(), 0.01);
        assertEquals(truck.getMaxCarryWeight(), savedTruck.getMaxCarryWeight(), 0.01);
    }

    @Test
    public void testRemoveTruck() {
        truckDAO.saveTruck(truck);
        truckDAO.removeTruck(truck);
        Truck removedTruck = truckDAO.findTruckByPlateNumber(truck.getPlateNumber());
        assertNull(removedTruck);
    }

}
