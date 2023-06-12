package DataAccessLayer;

import BussinesLogic.BranchStore;
import DataAccess.DAO_BranchStore;
import DataAccess.DAO_Employee;
import DataAccess.Database;
import DomainLayer.OrderDocument;
import DomainLayer.Supplier;
import DomainLayer.Transit;
import DomainLayer.Truck;
import BussinesLogic.Driver;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransitDAOImpl implements TransitDAO {
    final private Set<Transit> pendingTransitsSet = new HashSet<>();
    final private Set<Transit> completedTransitsSet = new HashSet<>();
    private final Connection connection;
    private final TruckDAO truckDAO;
    private final DAO_Employee driverDAO;
    private final OrderDocumentDAO orderDocumentDAO;
    private final SupplierDAO supplierDAO; // TODO when merge with suppliers module see also branch store
    private final DAO_BranchStore branchStoreDAO; //TODO check if can create here instead of passing as parameter
    public TransitDAOImpl(Connection connection, TruckDAO truckDAO, DAO_Employee daoEmployee,OrderDocumentDAO orderDocumentDAO ,SupplierDAO supplierDAO , DAO_BranchStore branchStoreDAO) throws SQLException, ClassNotFoundException {
        this.connection = connection;
        this.truckDAO = truckDAO;
        this.driverDAO = daoEmployee;
        this.orderDocumentDAO = orderDocumentDAO;
        this.supplierDAO = supplierDAO;
        this.branchStoreDAO = branchStoreDAO;
        Transit.nextId = getNextTransitId();
    }

    public void saveTransit(Transit transit) {
        String insertTransitQuery = "INSERT INTO Transits (transitId, transitDate, truckPlateNumber, driverId) VALUES (?, ?, ?, ?)";
        try {
            // Insert the transit data into the Transits table
            PreparedStatement insertTransitStmt = connection.prepareStatement(insertTransitQuery);
            insertTransitStmt.setInt(1, transit.getTransitId());
            // TODO change DATE format
//            insertTransitStmt.setDate(2, new java.sql.Date(transit.getTransitDate().getTime()));
            insertTransitStmt.setDate(2, java.sql.Date.valueOf(transit.getTransitDate()));
            insertTransitStmt.setString(3, transit.getTruck().getPlateNumber());
            //TODO driverID is String - decide on convention! - at Drivers table is int.
            // TODO also what was decided about driver? is chosen in the creation of a new transit?
            insertTransitStmt.setString(4, transit.getDriver().getId());
            insertTransitStmt.executeUpdate();
            pendingTransitsSet.add(transit);
        } catch (SQLException e) {
            System.out.println("Error saving transit to database: " + e.getMessage());
        }
    }


    @Override
    public void updateOrderDocumentOfTransit(Transit transit, OrderDocument orderDocument, String addOrRemoveFlag) {
        if (!addOrRemoveFlag.equals("+1") && !addOrRemoveFlag.equals("-1"))
        {
            System.out.println("Invalid addOrRemoveFlag value: " + addOrRemoveFlag + "Must be: +1 or -1");
            return;
        }
        String orderDocumentQuery;
        String supplierRouteQuery;
        String branchStoreRouteQuery;
        int transitId = transit.getTransitId();
        int orderDocumentId = orderDocument.getOrderDocumentId();

        if (addOrRemoveFlag.equals("+1"))
        {
            orderDocumentQuery =  "INSERT INTO TransitOrderDocuments (transitId, orderDocumentId) VALUES (?, ?)";
            supplierRouteQuery = "INSERT INTO TransitSuppliersRoute (transitId, orderDocumentId, supplierId) VALUES (?, ? ,?)";
            branchStoreRouteQuery = "INSERT INTO TransitBranchStoresRoute (transitId, orderDocumentId, branchStoreId) VALUES (?, ? ,?)";
            //transit.addOrderDoc(orderDocument);
        }
        else { // addOrRemoveFlag.equals("-1")
            orderDocumentQuery = "DELETE FROM TransitOrderDocuments WHERE transitId = ? AND orderDocumentId = ?";
            supplierRouteQuery = "DELETE FROM TransitSuppliersRoute WHERE transitId = ? AND orderDocumentId = ? AND supplierId = ?";
            branchStoreRouteQuery = "DELETE FROM TransitBranchStoresRoute WHERE transitId = ? AND orderDocumentId = ? AND branchStoreId = ?";
            //transit.removeOrderDoc(orderDocument);
        }

        try {
            PreparedStatement orderDocumentStmt = connection.prepareStatement(orderDocumentQuery);
            orderDocumentStmt.setInt(1, transitId);
            orderDocumentStmt.setInt(2, orderDocumentId);
            orderDocumentStmt.executeUpdate();

            PreparedStatement supplierRouteStmt = connection.prepareStatement(supplierRouteQuery);
            supplierRouteStmt.setInt(1, transitId);
            supplierRouteStmt.setInt(2, orderDocumentId);
            supplierRouteStmt.setInt(3, orderDocument.getSource().getSupplierId());
            supplierRouteStmt.executeUpdate();

            PreparedStatement branchStoreRouteStmt = connection.prepareStatement(branchStoreRouteQuery);
            branchStoreRouteStmt.setInt(1, transitId);
            branchStoreRouteStmt.setInt(2, orderDocumentId);
            branchStoreRouteStmt.setInt(3, orderDocument.getDestination().getBranchID());
            branchStoreRouteStmt.executeUpdate();


//            Set<Supplier> updatedSuppliersRoute = retrieveSupplierForTransit(transitId);
//            Set<BranchStore> updatedBranchStoreRoute = retrieveBranchStoresForTransit(transitId);
//            transit.setDestinationSuppliers(updatedSuppliersRoute);
//            transit.setDestinationBranchStores(updatedBranchStoreRoute);

        } catch (SQLException e)
        {
            System.out.println("Error related to order document of transit: " + e.getMessage());
        }
    }

    @Override
    public void updateETA(Transit transit) {
        String updateETAQuery = "UPDATE Transits SET ETA = ? WHERE transitId = ?";
        try {
            PreparedStatement updateETAStmt = connection.prepareStatement(updateETAQuery);
            updateETAStmt.setDouble(1, transit.getETA());
            updateETAStmt.setInt(2, transit.getTransitId());
            updateETAStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating ETA in the database: " + e.getMessage());
        }
    }


    @Override
    public void updateTruckAndDriverOfTransit(Transit transit, Truck newTruck, Driver driver) {
        // Update the transit in the database
        String updateTransitQuery = "UPDATE Transits SET truckPlateNumber = ?, driverId = ? WHERE transitId = ?";
        try {
            PreparedStatement updateTransitStmt = connection.prepareStatement(updateTransitQuery);
            updateTransitStmt.setString(1, newTruck.getPlateNumber());
            updateTransitStmt.setString(2,driver.getId());
            updateTransitStmt.setInt(3,transit.getTransitId());
            updateTransitStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating transit in the database: " + e.getMessage());
        }
    }
    @Override
    public Transit findTransitByID(int transitId) {
        Transit transit = lookForTransitInCache(transitId);
        if (transit != null) return transit;
        String selectTransitQuery = "SELECT * FROM Transits WHERE transitId = ?";
        try {
            PreparedStatement selectTransitStmt = connection.prepareStatement(selectTransitQuery);
            selectTransitStmt.setInt(1, transitId);
            ResultSet resultSet = selectTransitStmt.executeQuery();

            if (resultSet.next()) {
                // Retrieve the transit data from the result set
                int transitIdResult = resultSet.getInt("transitId");

                LocalDate transitDateResult = resultSet.getDate("transitDate").toLocalDate();
                String truckPlateNumberResult = resultSet.getString("truckPlateNumber");
                String driverIdResult = resultSet.getString("driverId");
                double etaResult = resultSet.getDouble("ETA");
                Time departureTimeResult = resultSet.getTime("departureTime");
                boolean isCompletedStatusResult = resultSet.getBoolean("isCompletedStatus");
                // Retrieve the truck and driver objects based on the retrieved IDs
                Truck truck = truckDAO.findTruckByPlateNumber(truckPlateNumberResult);
                Driver driver = (Driver) driverDAO.findByID(driverIdResult);

                // TODO pay-attention: high coupling with ETA
                if (etaResult == 0){
                    // that means that there are not orderDocuments related to this driver
                    // Create a new Transit object with the retrieved data
                    transit = new Transit(transitIdResult, transitDateResult, truck, driver);
                }
                else { //there is at least one orderDocument in this transit
                    Set<OrderDocument> orderDocumentSet = retrieveOrderDocumentsForTransit(transitId);
                    Set<Supplier> suppliersRoute = retrieveSupplierForTransit(transitId);
                    Set<BranchStore> branchStoresRoute = retrieveBranchStoresForTransit(transitId);
                    transit = new Transit(transitIdResult, transitDateResult, truck, driver, etaResult, suppliersRoute,
                            branchStoresRoute, orderDocumentSet);

                    if (departureTimeResult != null) {
                        LocalTime departureTime = departureTimeResult.toLocalTime();
                        transit.setDepartureTime(departureTime);
                    }
                }
                if (isCompletedStatusResult)
                {
                    completedTransitsSet.add(transit);
                }
                else {
                    pendingTransitsSet.add(transit);
                }
            }

            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Error finding transit in the database: " + e.getMessage());
        }
        return transit;
    }
    private Transit lookForTransitInCache(int transitId){
        for (Transit transit : pendingTransitsSet)
        {
            if (transit.getTransitId() == transitId)
                return transit;
        }
        for (Transit transit : completedTransitsSet)
        {
            if (transit.getTransitId() == transitId)
                return transit;
        }
        return null;
    }
    private Set<OrderDocument> retrieveOrderDocumentsForTransit(int transitId) {
        Set<OrderDocument> orderDocuments = new HashSet<>();
        String selectOrderDocumentsQuery = "SELECT * FROM TransitOrderDocuments WHERE transitId = ?";

        try {
            PreparedStatement selectOrderDocumentsStmt = connection.prepareStatement(selectOrderDocumentsQuery);
            selectOrderDocumentsStmt.setInt(1, transitId);
            ResultSet resultSet = selectOrderDocumentsStmt.executeQuery();

            while (resultSet.next()) {
                // Retrieve order document data from the result set
                int orderDocumentId = resultSet.getInt("orderDocumentId");
                // TODO: Retrieve order document object based on the retrieved ID
                OrderDocument orderDocument = orderDocumentDAO.findOrderDocumentById(orderDocumentId);

                if (orderDocument != null) {
                    orderDocuments.add(orderDocument);
                }
            }

            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving order documents for transit: " + e.getMessage());
        }
        return orderDocuments;
    }
    private Set<BranchStore> retrieveBranchStoresForTransit(int transitId) {
        Set<BranchStore> branchStores = new HashSet<>();
        String selectBranchStoresRouteQuery = "SELECT * FROM TransitBranchStoresRoute WHERE transitId = ?";

        try {
            PreparedStatement selectBranchStoresRouteStmt = connection.prepareStatement(selectBranchStoresRouteQuery);
            selectBranchStoresRouteStmt.setInt(1, transitId);
            ResultSet resultSet = selectBranchStoresRouteStmt.executeQuery();

            while (resultSet.next()) {
                // Retrieve branch store data from the result set
                int branchStoreId = resultSet.getInt("branchStoreId");
                // TODO: Use the BranchStoreDAO to retrieve the BranchStore object based on the retrieved ID
                BranchStore branchStore = (BranchStore) branchStoreDAO.findByID(branchStoreId);

                if (branchStore != null) {
                    branchStores.add(branchStore);
                }
            }

            resultSet.close();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error retrieving branch stores for transit: " + e.getMessage());
        }

        return branchStores;
    }
    private Set<Supplier> retrieveSupplierForTransit(int transitId) {
        Set<Supplier> suppliers = new HashSet<>();
        String selectSuppliersRouteQuery = "SELECT * FROM TransitSuppliersRoute WHERE transitId = ?";

        try {
            PreparedStatement selectSuppliersRouteStmt = connection.prepareStatement(selectSuppliersRouteQuery);
            selectSuppliersRouteStmt.setInt(1, transitId);
            ResultSet resultSet = selectSuppliersRouteStmt.executeQuery();

            while (resultSet.next()) {
                // Retrieve branch store data from the result set
                int supplierId = resultSet.getInt("supplierId");
                // TODO: Use the BranchStoreDAO to retrieve the BranchStore object based on the retrieved ID
                Supplier supplier = supplierDAO.findSupplierById(supplierId);

                if (supplier != null) {
                    suppliers.add(supplier);
                }
            }

            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving supplier for transit: " + e.getMessage());
        }

        return suppliers;
    }
    @Override
    public void moveToCompleted(Transit completedTransit) {
        String updateStatusQuery = "UPDATE Transits SET isCompletedStatus = true, departureTime = ? WHERE transitId = ?";
        try {
            PreparedStatement updateStatusStmt = connection.prepareStatement(updateStatusQuery);
            java.sql.Time sqlTime = java.sql.Time.valueOf(completedTransit.getDepartureTime());
            updateStatusStmt.setObject(1, sqlTime);
            updateStatusStmt.setInt(2, completedTransit.getTransitId());
            updateStatusStmt.executeUpdate();
            pendingTransitsSet.remove(completedTransit);
            completedTransitsSet.add(completedTransit);
        } catch (SQLException e) {
            System.out.println("Error moving transit to completed: " + e.getMessage());
        }
    }
    @Override
    public Set<Transit> getTransitsSet(boolean isCompleted) {
        Set<Transit> transits = new HashSet<>();
        String selectPendingTransits = "SELECT * FROM Transits WHERE isCompletedStatus = false";
        String selectCompletedTransits = "SELECT * FROM Transits WHERE isCompletedStatus = true";

        try {
            PreparedStatement statement;
            if (isCompleted) {
                statement = connection.prepareStatement(selectCompletedTransits);
            } else {
                statement = connection.prepareStatement(selectPendingTransits);
            }
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int transitId = resultSet.getInt("transitId");
                Transit transit = findTransitByID(transitId);
                transits.add(transit);
            }
        } catch (SQLException e) {
            System.out.println("Error getting transits from the database: " + e.getMessage());
        }
        return transits;
    }

    @Override
    public void removeTransit(Transit transit) {
        int transitId = transit.getTransitId();
        String deleteTransitQuery = "DELETE FROM Transits WHERE transitId = ?";

        try {
            PreparedStatement deleteTransitStmt = connection.prepareStatement(deleteTransitQuery);
            deleteTransitStmt.setInt(1, transitId);
            deleteTransitStmt.executeUpdate();
            pendingTransitsSet.remove(transit);
            completedTransitsSet.remove(transit);
            System.out.println("Transit removed successfully.");
        } catch (SQLException e) {
            System.out.println("Error removing transit from the database: " + e.getMessage());
        }
    }




    private int getNextTransitId() {
        int maxTransitId = 0;
        String query = "SELECT MAX(transitId) AS maxId FROM Transits";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                maxTransitId = resultSet.getInt("maxId");
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        maxTransitId++;
        return maxTransitId;
    }
}
