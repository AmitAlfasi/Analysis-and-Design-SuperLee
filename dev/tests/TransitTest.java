//import BussinesLogic.BranchStore;
//import BussinesLogic.Driver;
//import BussinesLogic.License;
//import BussinesLogic.TransitCoordinator;
//import ControllerLayer.TransitController;
//import ControllerLayer.TransitControllerImpl;
//import DataAccess.DAO_BranchStore;
//import DataAccess.DAO_Employee;
//import DataAccess.DAO_Generator;
//import DataAccessLayer.*;
//import DomainLayer.*;
//import org.junit.Before;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class TransitTest {
//
//    private TransitDAO transitDAO;
//    private OrderDocumentDAO orderDocumentDAO;
//    private TruckDAO truckDAO;
//    private DAO_Employee daoEmployee;
//    private ProductDAO productDAO;
//    private SupplierDAO supplierDAO;
//    private DAO_BranchStore daoBranchStore;
//    private TransitCoordinator transitCoordinator;
//    private TransitController transitController;
//    private Transit tt1;
//    private Transit tt2;
//    private Truck tk1;
//    private Truck tk2;
//    private Driver d1;
//    private Driver d2;
//    private Supplier s1;
//    private Supplier s2;
//    private BranchStore bs1;
//    private BranchStore bs2;
//    private Product p1;
//    private Product p2;
//    private Product p3;
//    private OrderDocument od1;
//    private OrderDocument od2;
//
//    @Before
//    public void setUp() throws Exception {
//        truckDAO = DAO_Generator.getTruckDAO();
//        daoEmployee = DAO_Generator.getEmployeeDAO();
//        supplierDAO = DAO_Generator.getSupplierDAO();
//        daoBranchStore = DAO_Generator.getBranchStoreDAO();
//        orderDocumentDAO = DAO_Generator.getOrderDocumentDAO();
//        productDAO = DAO_Generator.getProductDAO();
//        transitDAO = DAO_Generator.getTransitDAO();
//        transitCoordinator = new TransitCoordinator();
//        transitController = new TransitControllerImpl(truckDAO, )
//
//        //truck = truckController.createTruck("IT54432AI", 1, iLarr ,9000, 30000);
//        tk1 = new Truck("LargeCCRT123", TruckModel.LARGETRUCK ,9000, 15000);
//        tk1.addLToLSet(License.C);
//        tk1.addLToLSet(License.COOLER);
//
//        tk2 = new Truck("SmallC1T321", TruckModel.SMALLTRUCK ,4000, 9000);
//        tk2.addLToLSet(License.C1);
//
//        d1 = (Driver) daoEmployee.findByID(888888);
//        d2 = (Driver) daoEmployee.findByID(7778889);
//
//        s1 = supplierDAO.findSupplierById(1);
//        s2 = supplierDAO.findSupplierById(2);
//
//        bs1 = (BranchStore) daoBranchStore.findByID(1);
//        bs2 = (BranchStore) daoBranchStore.findByID(2);
//
//        p1 = productDAO.findProductById(1);
//        p2 = productDAO.findProductById(2);
//        p3 = productDAO.findProductById(3);
//
//        od1 = new OrderDocument(s1, bs1);
//        od1.getProductsList().put(p1, 8000.0);
//        od1.getProductsList().put(p3, 2000.0);
//        od1.setTotalWeight(8000+2000);
//
//        od2 = new OrderDocument(s2, bs2);
//        od2.getProductsList().put(p2, 3000.0);
//        od2.getProductsList().put(p3, 3000.0);
//        od2.setTotalWeight(3000 + 3000);
//
//        Date tt1Date = createDateObj("20-06-2023");
//        tt1 = new Transit(1,tt1Date,t1,d1,
//
//    }
//
//
//
//    public Date createDateObj(String dateString) throws ParseException {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        Date transitDate = dateFormat.parse(dateString);
//        return transitDate;
//    }
//
//}
