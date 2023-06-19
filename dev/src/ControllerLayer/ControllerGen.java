package ControllerLayer;
import BussinesLogic.TransitCoordinator;
import DataAccess.DAO_Generator;
import java.sql.SQLException;

public class ControllerGen {
    private static TruckController truckController;
    private static ProductController productController;
    private static SupplierController supplierController;
    private static OrderDocumentController orderDocumentController;
    private static TransitController transitController;
    private static TransitRecordController transitRecordController;
    private static TransitCoordinator transitCoordinator;
    // --- Gui section ---
    private static TruckController truckControllerGui;
    private static TransitController transitControllerGui;
    private static OrderDocumentController orderDocumentControllerGui;
    private static TransitRecordController transitRecordControllerGui;


    public static TransitCoordinator getTransitCoordinator() throws SQLException, ClassNotFoundException {
        if (transitCoordinator == null)
        {
            transitCoordinator = new TransitCoordinator();
        }
        return transitCoordinator;
    }

    public static TruckController getTruckController() throws SQLException, ClassNotFoundException {
        if (truckController == null)
        {
            truckController = new TruckControllerImpl(DAO_Generator.getTruckDAO());
        }
        return truckController;
    }

    public static ProductController getProductController() throws SQLException, ClassNotFoundException {
        if (productController == null)
        {
            productController = new ProductControllerImpl(DAO_Generator.getProductDAO());
        }
        return productController;
    }

    public static SupplierController getSupplierController() throws SQLException, ClassNotFoundException {
        if (supplierController == null)
        {
            supplierController = new SupplierControllerImpl(DAO_Generator.getSupplierDAO());
        }
        return supplierController;
    }

    public static OrderDocumentController getOrderDocumentController() throws SQLException, ClassNotFoundException {
        if (orderDocumentController == null)
        {
            orderDocumentController = new OrderDocumentControllerImpl(DAO_Generator.getOrderDocumentDAO(),
                    getSupplierController(),getTransitCoordinator(),getProductController());
        }
        return orderDocumentController;
    }



    public static TransitRecordController getTransitRecordController() throws SQLException, ClassNotFoundException {
        if (transitRecordController == null)
        {
            transitRecordController = new TransitRecordControllerImpl(DAO_Generator.getTransitRecordDAO());
        }
        return transitRecordController;
    }

    public static TransitController getTransitController() throws SQLException, ClassNotFoundException {
        if (transitController == null)
        {
            transitController = new TransitControllerImpl(DAO_Generator.getTransitDAO(), getTruckController(),
                    getTransitCoordinator(), getOrderDocumentController(), getTransitRecordController());
        }
        return transitController;
    }

    // ----- GUI - Controllers -----
    public static TruckController getTruckControllerGUI() throws SQLException, ClassNotFoundException {
        if (truckControllerGui == null)
        {
            truckControllerGui = new TruckControllerImplGui(DAO_Generator.getTruckDAO());
        }
        return truckControllerGui;
    }

    public static TransitController getTransitControllerGUI() throws SQLException, ClassNotFoundException {
        if (transitControllerGui == null)
        {
            transitControllerGui = new TransitControllerImplGui(DAO_Generator.getTransitDAO(), getTruckController(),
                    getTransitCoordinator(), getOrderDocumentController(), getTransitRecordController());
        }
        return transitControllerGui;
    }

    public static OrderDocumentController getOrderDocumentControllerGui() throws SQLException, ClassNotFoundException {
        if (orderDocumentControllerGui == null)
        {
            orderDocumentControllerGui = new OrderDocumentControllerImplGui(DAO_Generator.getOrderDocumentDAO(),
                    getSupplierController(),getTransitCoordinator(),getProductController());
        }
        return orderDocumentControllerGui;
    }
    public static TransitRecordController getTransitRecordControllerGui() throws SQLException, ClassNotFoundException {
        if (transitRecordControllerGui == null)
        {
            transitRecordControllerGui = new TransitRecordControllerImplGui(DAO_Generator.getTransitRecordDAO());
        }
        return transitRecordControllerGui;
    }
}
