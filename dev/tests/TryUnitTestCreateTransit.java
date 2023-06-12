import BussinesLogic.TransitCoordinator;
import ControllerLayer.*;
import DataAccess.DAO_Employee;
import DomainLayer.Transit;
import ExceptionsPackage.UiException;
import org.junit.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TryUnitTestCreateTransit {
    private TruckController truckController;
    private ProductController productController;
    private SupplierController supplierController;
    private OrderDocumentController orderDocumentController;
    private TransitController transitController;
    private TransitRecordController transitRecordController;
    private DAO_Employee daoEmployee;
    private Transit transit;


    @Before
    public void setUp() throws Exception {
        truckController = ControllerGen.getTruckController();
        productController =  ControllerGen.getProductController();
        supplierController = ControllerGen.getSupplierController();
        orderDocumentController = ControllerGen.getOrderDocumentController();
        transitRecordController = ControllerGen.getTransitRecordController();
        transitController = ControllerGen.getTransitController();
    }

    @After
    public void tearDown() throws Exception {
        transitController.removeTransitCompletely(transit);
    }

    @Test
    public void testCreateTransit() throws UiException {
        transit = transitController.createTransit("2023-05-21", "123", "888888");
        assertNotNull(transitController.findTransitByID(transit.getTransitId()));
        transitController.removeTransitCompletely(transit);
        assertNull(transitController.findTransitByID(transit.getTransitId()));
    }

}
