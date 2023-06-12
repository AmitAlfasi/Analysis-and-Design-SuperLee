package Presentation;

import BussinesLogic.*;
import DataAccess.DAO_BranchStore;
import DataAccess.DAO_Employee;
import DataAccess.DAO_Generator;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * this class is the main user interface
 */
public class HR_Main {

    private static DAO_Employee employeesDAO;
    private static DAO_BranchStore branchStoreDAO;

    /**
     * Search an employee by id
     * @return the required employee
     */
    public static Employee searchAnEmployee() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        while(true)
        {
            System.out.println("Enter the employees ID please");
            String id = scanner.nextLine();
            Employee e = (Employee) employeesDAO.findByID(id);
            if(e == null)
                System.out.println("Invalid ID. Please try again");
            else
                return e;
        }
    }

    /**
     * Search a branch by id
     * @return the required branch
     */
    public static BranchStore searchABranchStore() throws SQLException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int id;
        while(true) {
            System.out.println("Enter the Branch ID please");
            try {
                id = scanner.nextInt();
            }catch (Exception e){
                System.out.println("Invalid input. Please try again");
                continue;
            }
            BranchStore b = (BranchStore) branchStoreDAO.findByID(id);
            if (b == null)
                System.out.println("Invalid ID. Please try again");
            else
                return b;
        }
    }

    /**
     * Main
     */
    public static void system() throws SQLException, ClassNotFoundException {
        DAO_Generator generator = new DAO_Generator();
        employeesDAO = generator.getEmployeeDAO();
        branchStoreDAO = generator.getBranchStoreDAO();
        /* The main object "HR" control */
        HR_EntityManagement entityManagement = new HR_EntityManagement();
        HR_SchedulingManagement schedulingManager = new HR_SchedulingManagement();
        Scanner scanner = new Scanner(System.in);
        String choice = "";
        while (choice != "7") {
            System.out.println("Hello HR manager. Welcome to Super-li system:");
            System.out.println("1. Employees");
            System.out.println("2. Branches");
            System.out.println("3. Constraints");
            System.out.println("4. Shifts");
            System.out.println("5. History");
            System.out.println("6. ManageShift");
            System.out.println("7. Exit");

            choice = scanner.nextLine();
            String c = "";
            int id;
            switch (choice) {
                case "1":
                    System.out.println("You chose Employees:");
                    System.out.println("1. Add new employee");
                    System.out.println("2. Update an existing employee");
                    System.out.println("3. Get employees information");
                    System.out.println("4. Calculate salary");
                    System.out.println("5. Print all network Employees");
                    System.out.println("6. Go Back");
                    c = scanner.nextLine();
                    switch (c)
                    {
                        case "1":
                            entityManagement.newEmployeeInNetwork();
                            break;
                        case "2":
                            entityManagement.updateEmployeesDetails(searchAnEmployee());
                            break;
                        case "3":
                            entityManagement.getEmployeesDetails(searchAnEmployee());
                            break;
                        case "4":
                            entityManagement.calculateSalary();
                            break;
                        case "5":
                            ArrayList<Employee> employees = new ArrayList<>(employeesDAO.getNetworkEmployees());
                            for (Employee employee : employees)
                            {
                                employee.printEmployeeDetails();
                                System.out.println("");
                            }
                            ArrayList<Driver> drivers = new ArrayList<>(employeesDAO.getNetworkDrivers());
                            for (Driver driver : drivers) {
                                driver.printEmployeeDetails();
                                System.out.println("");
                            }

                        case "6":
                            continue;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                            break;
                    }
                    break;
                case "2":
                    System.out.println("You chose Branches:");
                    System.out.println("1. Add new branch");
                    System.out.println("2. Update an existing branch");
                    System.out.println("3. Add employee to branch");
                    System.out.println("4. Remove employee from branch");
                    System.out.println("5. View transits");
                    System.out.println("6. Print all Branches");
                    System.out.println("7. Go Back");
                    c = scanner.nextLine();
                    switch (c)
                    {
                        case "1":
                            entityManagement.newBranchInNetwork();
                            break;
                        case "2":
                            entityManagement.updateBranchDetails(searchABranchStore());
                            break;
                        case "3":
                            entityManagement.addEmployeeToBranch(searchAnEmployee());
                            break;
                        case "4":
                            entityManagement.removeEmployeeFromBranch(searchAnEmployee());
                            break;
                        case "5":
                            Employee employee = searchAnEmployee();
                            BranchStore branch = searchABranchStore();
                            LocalDate date;
                            while(true)
                            {
                                try{
                                    System.out.println("Enter the required date (YYYY-MM-DD):");
                                    String dateString=scanner.nextLine();
                                    date = LocalDate.parse(dateString);
                                    branch.viewTransit(date, employee.getId());
                                    break;
                                }
                                catch (Exception e)
                                {
                                    System.out.println("Invalid date, Try again");
                                }
                            }
                            break;
                        case "6":
                            ArrayList<BranchStore> branchStores = new ArrayList<>(branchStoreDAO.getNetworkBranches());
                            for (BranchStore branchStore :branchStores )
                            {
                                branchStore.printBranchDetails();
                                System.out.println("");
                            }
                        case "7":
                            continue;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                            break;
                    }

                    break;
                case "3":
                    System.out.println("You chose Constraints:");
                    System.out.println("1. Ask constraints from all employees");
                    System.out.println("2. Update constraints to an employee");
                    System.out.println("3. Get Employees constraints");
                    System.out.println("4. Go Back");
                    c = scanner.nextLine();
                    switch (c)
                    {
                        case "1":
                            schedulingManager.schedulingFromEmployees();
                            break;
                        case "2":
                            schedulingManager.updateEmployeeConstrainsByID();
                            break;
                        case "3":
                            Employee employee = searchAnEmployee();
                            System.out.println(employee.getName()+ "constraints are: ");
                            employee.printEmployeesConstraints();

                        case "4":
                            continue;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                            break;
                    }
                    break;
                case "4":
                    System.out.println("You chose Shifts:");
                    System.out.println("1. Plan shifts for the day after tomorrow (Two day schedule)");
                    System.out.println("2. Change shift");
                    System.out.println("3. Add shift managers permissions");
                    System.out.println("4. Remove shift managers permissions");
                    System.out.println("5. Reset employees limit for next week");
                    System.out.println("6. Go Back");
                    c = scanner.nextLine();
                    switch (c) {
                        case "1":
                            schedulingManager.setShift();
                            break;
                        case "2":
                            schedulingManager.changeShiftSchedule();
                            break;
                        case "3":
                            schedulingManager.addPermissionToShiftManagerForDailyShiftToday();
                            break;
                        case "4":
                            schedulingManager.removePermissionToShiftManagerForDailyShiftToday();
                            break;
                        case "5":
                            schedulingManager.resetEmployeesLimits();
                            break;
                        case "6":
                            continue;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                            break;
                    }
                    break;
                case "5":
                    System.out.println("You chose History:");
                    System.out.println("1. Get shift by a date");
                    System.out.println("2. Clear last moth history");
                    c = scanner.nextLine();
                    switch (c) {

                        case "1":
                            BranchStore b = searchABranchStore();
                            while (true)
                            {
                                try
                                {
                                    System.out.println("Enter the required date (YYYY-MM-DD):");
                                    String ans = scanner.nextLine();
                                    b.showShiftByDate(ans);
                                    break;
                                }
                                catch (Exception e)
                                {
                                    System.out.println("Wrong format or date");
                                }
                            }
                            break;
                        case "2":
                            for( BranchStore branch: branchStoreDAO.getNetworkBranches()){branch.deleteHistory();}
                            System.out.println("All history is reset.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                            break;
                    }

                    break;
                case "6":
                    BranchStore branch_ = searchABranchStore();
                    DailyShift s = branch_.getShiftByDate(LocalDate.now().plusDays(2).toString());

                    if(s == null)
                        System.out.println("NO SHIFT YET");
                    else
                    {
                        System.out.println("Enter an employee ID");
                        String ans = scanner.nextLine();
                        ShiftManager shiftm = s.findEmployeeInShiftManager(ans);
                        ManageShift manageShift = new ManageShift(shiftm, s, LocalDate.now().plusDays(2), branch_.getBranchID());
                        System.out.println("Choose an option:");
                        System.out.println("1. Cancel an item");
                        System.out.println("2. Get Cancellation details");
                        System.out.println("3. Upload end-of-day report");
                        System.out.println("4. Get end-of-day report");
                        c = scanner.nextLine();
                        switch (c) {
                            case "1":
                                manageShift.cancelItem();
                                break;
                            case "2":
                                manageShift.getCancellation();
                                break;
                            case "3":
                                manageShift.uploadEndofDayReport();
                                break;
                            case "4":
                                File file = s.getEndOfDayReport();
                                System.out.println(file);
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                                break;
                        }
                    }
                    break;
                case "7":
                    System.out.println("Exiting menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }

    }

}