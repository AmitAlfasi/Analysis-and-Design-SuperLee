package BussinesLogic;

import DataAccess.DAO_BranchStore;
import DataAccess.DAO_Employee;
import DataAccess.DAO_Generator;
import DataAccessLayer.TransitDAO;
import DomainLayer.Transit;
import Presentation.EmployeeConstraints;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class TransitCoordinator {
    private DAO_BranchStore branchStoreDAO;
    private DAO_Employee driversDAO;
    private TransitDAO transitDAO;

    /**
     * Using DAO's of branchStore and drivers
     * this class will let you manage a transit include add drivers
     */
    public TransitCoordinator() throws SQLException, ClassNotFoundException {
        branchStoreDAO = DAO_Generator.getBranchStoreDAO();
        driversDAO = DAO_Generator.getEmployeeDAO();
        transitDAO = DAO_Generator.getTransitDAO();
    }

    /**
     * This function help to transit-module to schedule the drivers
     * @param transitDate to schedule
     * @param license of the needed driver
     * @return list of available drivers
     */

    public List<Driver> getAvailableDrivers(LocalDate transitDate, Set<License> license) {
        List<Driver> availableDrivers = new ArrayList<>();
        List<Driver> netWorkDrivers = null;
        try {
            netWorkDrivers = driversDAO.getNetworkDrivers();
        } catch (SQLException s) {
            s.printStackTrace();
        }
        if (netWorkDrivers == null) return null;
        for(Driver driver : netWorkDrivers){
            if(driver.getLicenses().containsAll(license))
                if(EmployeeConstraints.checkDriverAvailabilityForDate(transitDate, driver))
                    availableDrivers.add(driver);}
        return availableDrivers;
    }

    /**
     * Add transit's date to branch
     * @param date of the potential transit
     * @param branchID to add the transit
     */
    // TODO - fails when trying to add order to transit
    public void addTransitInDate(LocalDate date, int branchID){
        try {
            BranchStore branchStore = (BranchStore) branchStoreDAO.findByID(branchID);
            if (branchStore != null) {
                branchStore.storekeeperStatusByDate.put(date, false); // default value until validate there is a storekeeper
                branchStoreDAO.update(branchStore);}
            else
                System.out.println("Invalid branch ID");
            } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    /**
     * Add a driver to a transit
     * @param localDate of the transit
     * @param driverID which is going to be added
     * @param licenses of the truck that the driver needs to know
     */
    public Driver addDriverToTransit(LocalDate localDate, String driverID, Set<License> licenses){
//        System.out.println("d: addDriverToTransit ");
        List<Driver> driversList = getAvailableDrivers(localDate,licenses);
        for (Driver driver : driversList) {
//            driver.printEmployeeDetails();
//            System.out.println(" ");
//            driver.printEmployeesConstraints();
            if (driver.getId().equalsIgnoreCase(driverID)) {
                return driver;
            }
        }
        return null;
    }


    /**
     * switch a new driver to a transit instead of the old driver
     * used both in begin transit (on the fly) and in update transit - switch truck
     * in case that the old driver is not eligible to drive the new truck
     * @param localDate of the transit
     * @param newDriverID which is going to be added
     * @param licenses of the truck that the driver must have
     */

    public Driver SwitchDriverInTransit(LocalDate localDate, String newDriverID, Set<License> licenses) {
        List<Driver> driversList = getAvailableDrivers(localDate,licenses);
        for (Driver driver : driversList) {
            if (driver.getId().equalsIgnoreCase(newDriverID)) {
                return driver;
            }
        }
        return null;
    }

    /**
     *
     * @param stores the stores that we need to check if there are workers there
     * @param date date of transit
     * @return true if there are 2 storageWorkers, else false
     */
    public boolean StorageWorkersExist(Set<BranchStore> stores, LocalDate date)
    {
        for(BranchStore store : stores)
            if(store.storekeeperStatusByDate.containsKey(date))
                if(!store.storekeeperStatusByDate.get(date))
                    return false;
        return true;
    }


    /**
     * Get the map of date and driver's transits from a branch
     * @param branchID of the branch
     * @return the transits in the branch
     */
    // use before start transit
    public Map<LocalDate, Boolean> getTransitsInBranch(int branchID){
        try{
            BranchStore branchStore = (BranchStore) branchStoreDAO.findByID(branchID);
            if(branchStore!= null)
                return branchStore.storekeeperStatusByDate;
            else
                System.out.println("Invalid branch ID");
        }catch (SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }


    /**
     *
     * @param storeId the store id from the DAO
     * used in orderDocumentController
     * @return branchStore
     */
    public BranchStore findStoreById(int storeId) {
        BranchStore branchStore = null;
        try {
            branchStore = (BranchStore) branchStoreDAO.findByID(storeId);
        } catch (SQLException | ClassNotFoundException s)
        {
            s.printStackTrace();
        }
        return branchStore;
    }

    /**
     * Static function to alert of any change in transit's status
     */
    public static void Alert(String message){
        System.out.println(message);
    }


    public void removeDriverFromDriverTransitsDates(String driverId, LocalDate transitDate) {
        try{
            Driver tmpDriver = (Driver) driversDAO.findByID(driverId);
            driversDAO.update(tmpDriver);
        }catch (SQLException s){
            s.printStackTrace();
        }
    }

    public void addDriverToDriverTransitsDates(Driver driver, LocalDate transitDate) {
        try{
            driversDAO.update(driver);
        }catch (SQLException s){
            s.printStackTrace();
        }
    }

    /**
     * prints all transits in a specific date for the manager of the store to know which transits arrive today
     * @param branchStore the store that you want to know the incoming transits to
     * @param date the date of the manager shift
     */
    public void printArrivalDay(BranchStore branchStore,LocalDate date)
    {
        Set<Transit> transitSet = transitDAO.getTransitsSet(false);
        for (Transit transit :transitSet )
        {
            if(date.isEqual(transit.getTransitDate())) {
                for (BranchStore store : transit.getDestinationStores()) {
                    if (store.getBranchID() == branchStore.getBranchID()) {
                        transit.printTransit();
                    }
                }
            }
        }
    }

}
