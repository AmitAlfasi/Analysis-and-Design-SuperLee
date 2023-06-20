package ControllerLayer;

import BussinesLogic.Driver;
import BussinesLogic.License;
import BussinesLogic.TransitCoordinator;
import DataAccessLayer.TransitDAO;
import DomainLayer.OrderDocument;
import DomainLayer.Transit;
import DomainLayer.Truck;
import ExceptionsPackage.UiException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;

public class TransitControllerImpl implements TransitController {
    private final TransitDAO transitDAO;
    private final TruckController truckController;
    private final TransitCoordinator transitCoordinator;
    private final OrderDocumentController orderDocController;
    private final TransitRecordController transitRecordController;

    public TransitControllerImpl(TransitDAO transitDAO, TruckController truckController,
                                 TransitCoordinator transitCoordinator, OrderDocumentController orderDocController,
                                 TransitRecordController transitRecordController) {
        this.transitDAO = transitDAO;
        this.truckController = truckController;
        this.transitCoordinator = transitCoordinator;
        this.orderDocController = orderDocController;
        this.transitRecordController = transitRecordController;

    }

    @Override
    public Transit createTransit(String dateString, String truckPlateNumber, String driverId) throws UiException {
        LocalDate transitDate;
        try {
        transitDate = LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            throw new UiException("Invalid date format " + dateString + "\t" + "The correct format is: yyyy-MM-dd ");
        }

        Truck truckForTransit = truckController.findTruckByPlate(truckPlateNumber);
        if (truckForTransit == null) {
            throw new UiException("Truck's plate number not found: " + truckPlateNumber);
        }
        Driver driverForTransit = transitCoordinator.addDriverToTransit(transitDate,driverId,truckForTransit.getTruckLicenses());
        if (driverForTransit == null) {
            throw new UiException("Driver id not found: " + driverId);
        }
        Transit newTransit = new Transit(transitDate, truckForTransit, driverForTransit);
        driverForTransit.addTransitDate(transitDate);
        transitDAO.saveTransit(newTransit);
        transitCoordinator.addDriverToDriverTransitsDates(driverForTransit, newTransit.getTransitDate());
        return newTransit;
    }
    @Override
    public Transit findTransitByID(int transitId) {
        return transitDAO.findTransitByID(transitId);
    }
//    @Override
//    public Set<Transit> getTransitsSet() {
//        return this.transitDAO.getTransitsSet();
//    }
    @Override
    public Transit showTransitByID(int transitId) {
        return findTransitByID(transitId);
    }
    @Override
    public int replaceTransitTruck(int transitId, String newTruckPlate) {
        Transit transitToUpdate = findTransitByID(transitId);
        if (transitToUpdate == null) return -2; //transit fail
        Truck otherTruck = truckController.findTruckByPlate(newTruckPlate);
        if (otherTruck == null) return -1; //truck fail
        Driver currentDriver = transitToUpdate.getDriver();
        boolean qualifiedDriverFlag = isDriverAllowToDriveTruck(otherTruck,currentDriver);
        if (!qualifiedDriverFlag) return 0; //driver fail
        transitDAO.updateTruckAndDriverOfTransit(transitToUpdate, otherTruck, currentDriver);
        //updating transit object
        transitToUpdate.setTruck(otherTruck);
        transitToUpdate.setDriver(currentDriver);
        currentDriver.addTransitDate(transitToUpdate.getTransitDate());
        //transitToUpdate.setTruck(otherTruck);
        return 1;// successes
    }
    /**
     * replaces transit driver not on the fly, only if we changed a truck for a specific transit.
     * returns @int based on what the problem is
     */
    public int replaceTransitDriver(int transitId, String newDriverId, String truckPlate, String callingFlag) {
        Truck newTruck = truckController.findTruckByPlate(truckPlate);
        Transit transitToUpdate = findTransitByID(transitId);
        Driver otherDriver = transitCoordinator.SwitchDriverInTransit(transitToUpdate.getTransitDate(),newDriverId,newTruck.getTruckLicenses());
        if (otherDriver == null) return -1; //fail to find driver
        boolean qualifiedDriverFlag = isDriverAllowToDriveTruck(newTruck,otherDriver);
        if (!qualifiedDriverFlag) return 0; // driver is not qualified
        //driver is qualified
        if (callingFlag.equals("notOnTheFly"))
        {   transitCoordinator.removeDriverFromDriverTransitsDates(transitToUpdate.getDriver().getId(), transitToUpdate.getTransitDate());
            transitToUpdate.getDriver().removeTransitDate(transitToUpdate.getTransitDate());
            otherDriver.addTransitDate(transitToUpdate.getTransitDate());
            transitCoordinator.addDriverToDriverTransitsDates(otherDriver, transitToUpdate.getTransitDate());
        }
        else {
            transitToUpdate.getDriver().removeTransitDate(transitToUpdate.getTransitDate());
            otherDriver.addTransitDate(transitToUpdate.getTransitDate());
            transitCoordinator.addDriverToDriverTransitsDates(otherDriver, transitToUpdate.getTransitDate());
        }
        transitDAO.updateTruckAndDriverOfTransit(transitToUpdate, newTruck, otherDriver);
        //updating transit object
        transitToUpdate.setTruck(newTruck);
        transitToUpdate.setDriver(otherDriver);

        return 1;
    }

    @Override
    public boolean isDriverAllowToDriveTruck(Truck truck, Driver driver){
        Set <BussinesLogic.License> truckLSet = truck.getTruckLicenses();
        Set <License> driverLicenses = new HashSet<>(driver.getLicenses());
        Set <BussinesLogic.License> driverLicenseSet = driverLicenses; //might be a problem
        return (driverLicenseSet.containsAll(truckLSet));
    }
    public void moveTransitToFinishedDB(Transit completedTransit){
        transitDAO.moveToCompleted(completedTransit);
    }
    public boolean transferLoad(Truck smallTruck, Truck biggerTruck){
        boolean validTransfer = truckController.transferLoadV2(smallTruck, biggerTruck);
        if (validTransfer){
            //System.out.println("Transfer load form truck: " + smallTruck.getPlateNumber() + " to truck: " + biggerTruck.getPlateNumber());
            return true;
        }
        System.out.println("Chosen truck is too small, please try again.. ");
        return false;
    }

    @Override
    public boolean isValidWeight(Transit currentTransit, OrderDocument orderDocument) {
        Truck currentTruck = currentTransit.getTruck();
        double maxCarry = currentTruck.getMaxCarryWeight();
        double currentTransitWeight = currentTransit.calcOrdersWeight();
        double weightToAdd = orderDocument.getTotalWeight();
        double newCapacity = maxCarry-(currentTransitWeight+weightToAdd);
        if (newCapacity < 0){
            System.out.println("Exceeding truck's max weight");
            System.out.printf("available capacity is: %.2f %n", (maxCarry-currentTransitWeight));
            System.out.println("Order was not added to transit");
            return false;
        }
        return true;
    }

    public void updateOrderDocumentOfTransit(Transit transit, OrderDocument orderDocument, String addOrRemoveFlag)
    {
        if (addOrRemoveFlag.equals("+1"))
        {
            transit.addOrderDoc(orderDocument);
            transit.addDestinationStore(orderDocument.getDestination());
            transit.addDestinationSupplier(orderDocument.getSource());
            transitDAO.updateOrderDocumentOfTransit(transit,orderDocument,addOrRemoveFlag);
        }
        else{
            transitDAO.updateOrderDocumentOfTransit(transit,orderDocument,addOrRemoveFlag);
            transit.removeOrderDoc(orderDocument);
            transit.removeDestinationSupplier(orderDocument.getSource());
            transit.removeDestinationStore(orderDocument.getDestination());
        }
        transit.setETA();
        transitDAO.updateETA(transit);
    }


    public void removeTransitCompletely(Transit transit)
    {
        transitDAO.removeTransit(transit);
    }
}