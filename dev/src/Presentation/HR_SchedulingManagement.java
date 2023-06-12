package Presentation;

import BussinesLogic.*;
import DataAccess.DAO_BranchStore;
import DataAccess.DAO_DailyShift;
import DataAccess.DAO_Employee;
import DataAccess.DAO_Generator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * This class will help HR user to manage the system:
 * - schedule shifts by the ShiftOrganizer class
 * - ask for employees' constraints by the EmployeeConstraints class
 * We assume there is one user at this point.
 */
public class HR_SchedulingManagement {

    //variables
    private DAO_Employee employeesDAO;
    private DAO_BranchStore branchStoreDAO;

    private DAO_DailyShift dailyShiftDAO;

    //constructor
    public HR_SchedulingManagement() throws SQLException, ClassNotFoundException {
        employeesDAO = DAO_Generator.getEmployeeDAO();
        branchStoreDAO = DAO_Generator.getBranchStoreDAO();
        dailyShiftDAO=DAO_Generator.getDailyShiftDAO();
    }

    /**
     * once a week the system asks all the network employees for their schedule constraints
     */
    public void schedulingFromEmployees() throws SQLException, ClassNotFoundException {
        /*
         * First function ask all the employees in all branches to give constraints
         */
        System.out.println("TIME TO SCHEDULE CONSTRAINTS \u231B");
        for(int j = 0; j<branchStoreDAO.getNetworkBranches().size(); j ++) // note that it's not all
        {
            branchStoreDAO.getNetworkBranches().get(j).printOpenHours();
        }
        for(int i = 0; i< employeesDAO.getNetworkEmployees().size(); i ++) // note that it's not all
        {
            EmployeeConstraints.askForConstraints(employeesDAO.getNetworkEmployees().get(i));
            Employee e = employeesDAO.getNetworkEmployees().get(i);
            employeesDAO.update(e); // update DB
        }
        System.out.println("DONE.");

    }

    /**
     * daily the system build the shifts for the next week in each branch
     */
    public void setShift() throws SQLException, ClassNotFoundException {
        for(int j=0; j<2 ; j++) // two days
        {
            Days day = Days.values()[j];
            System.out.println("\n- Set Shift Schedule for "+ LocalDate.now().plusDays(day.ordinal()+2)+" -");
            List<BranchStore> branchStores = branchStoreDAO.getNetworkBranches();
            DailyShift[] newShift = new DailyShift[branchStores.size()];
            /*
             * Second function set all branches shifts for one day.
             */
            List<Employee> listEmployees;
            for(int i = 0; i<branchStores.size(); i++) // note that it's not all
            {
                System.out.println("\nSetting shift for Branch No. "+ branchStores.get(i).getBranchID());
                newShift[i] = new DailyShift(LocalDate.now().plusDays(day.ordinal()+2));
                // get the employees from each branch and set them a new scheduling
                listEmployees = branchStores.get(i).getEmployees();
                BranchStore branchStore = branchStores.get(i);
                //schedule morning shift
                newShift[i] = ShiftOrganizer.DailyShifts(listEmployees,branchStore.storekeeperStatusByDate,branchStore.getOpenHours(), 0, newShift[i],day);
                if(newShift[i] != null)
                    System.out.println("This shift is set for: "+ newShift[i].getDate().toString()+" in the "+ShiftOrganizer.Shift.Morning);

                //schedule evening shift
                newShift[i] = ShiftOrganizer.DailyShifts(listEmployees,branchStore.storekeeperStatusByDate, branchStore.getOpenHours(), 1, newShift[i],day);

                if(newShift[i] != null)
                {
                    System.out.println("This shift is set for: "+newShift[i].getDate().toString()+" in the "+ShiftOrganizer.Shift.Evening+"\n");
                    dailyShiftDAO.insert(newShift[i],branchStore.getBranchID());
                    newShift[i].showMeSchedualing();
                }
            }

        }

    }

    /**
     * HR manager can change a shift schedule.
     * this function asks all the required information to do so
     */
    public void changeShiftSchedule()
    {
        System.out.println("-Update Shift Schedule-");
        String answer = "y";
        while (Objects.equals(answer, "y"))
        {
            try{
                System.out.println("Hello, please answer the following questions to update a shift:");
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter Branch Store ID that you want to update ");
                int branchID = scanner.nextInt();

                //find branch
                BranchStore branch = (BranchStore) branchStoreDAO.findByID(branchID);
                while (branch == null)
                {
                    System.out.println("Invalid Branch Store ID, please try again ");
                    branchID = scanner.nextInt();
                    branch = (BranchStore) branchStoreDAO.findByID(branchID);
                }

                String dateString=scanner.nextLine();
                System.out.println("What date? ");
                dateString=scanner.nextLine();
                LocalDate date = LocalDate.parse(dateString);

                System.out.println("Morning (0) or Evening (1) shift? (enter number)");
                int shift = scanner.nextInt();

                System.out.println("Add (0) or Remove (1) employee? (enter number)");
                int choice = scanner.nextInt();

                String employeeID = scanner.nextLine();
                System.out.println("Enter employees ID");
                employeeID = scanner.nextLine();

                //find employee
                Employee employee = branch.findEmployeeInBranch(employeeID);
                while (employee == null)
                {
                    System.out.println("Invalid Employee ID, please try again ");
                    employeeID = scanner.nextLine();
                    employee = branch.findEmployeeInBranch(employeeID);
                }

                System.out.println("If you are: ");
                System.out.println("- Adding an employee, enter role you want to change to. ");
                System.out.println("- Removing an employee, enter current role. ");
                System.out.println("Enter the number of the role from the following ");
                Role[] roles = Role.values();
                for (int i = 0; i < roles.length; i++) {
                    System.out.println(i + " - " + roles[i]);
                }
                int qualification = scanner.nextInt();
                ShiftOrganizer.changeShift(branch,date , shift, choice, employee, roles[qualification]);
                //update DB
                if(choice == 0)
                    dailyShiftDAO.addToShift(date, shift, employeeID, roles[qualification].ordinal(), branchID);
                else if(choice == 1)
                    dailyShiftDAO.removefromShift(date, shift, employeeID, roles[qualification].ordinal(), branchID);

                dailyShiftDAO.update(branch.getShiftByDate(String.valueOf(date)),branchID);
                branchStoreDAO.update(branch);
                answer = scanner.nextLine();
                System.out.println("SHIFT UPDATED\n\nDo you wish to update another shift? (enter y/n)");
                answer = scanner.nextLine();
            }
            catch (Exception e)
            {
                System.out.println("Invalid choice. Please try again.");
            }

        }


    }

    /**
     * updates employees constrains by id
     */
    public void updateEmployeeConstrainsByID() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("-Update Employee Constraints-");
        while (true)
        {
            System.out.println("Hello, Enter your ID to update your shift constraints: ");
            String employeeId = scanner.nextLine();
            Employee employee = (Employee) employeesDAO.findByID(employeeId);
            if (employee == null){
                System.out.println("Invalid ID.");
                continue;
            }
            EmployeeConstraints.updateConstraints(employee);
            employeesDAO.update(employee);
            System.out.println("Constraints updated ");
            break;
        }

    }

    /**
     * this function reset the employees limits to 6
     */
    public void resetEmployeesLimits() throws SQLException {
        LocalDate currentDate = LocalDate.now();
        /* Reset employee's limit of shifts if the week is over */
        if(currentDate.toString().equals("Saturday"))
            for (Employee employee : employeesDAO.getNetworkEmployees()){
                employee.setShiftsLimit(6);
                employeesDAO.update(employee);
            }
    }

    /**
     * add permission to Shift Manager for Daily Shift today
     */
    public void addPermissionToShiftManagerForDailyShiftToday()
    {
        System.out.println("-Add permission to Shift Manager of TODAY shift-");
        String answer = "y";
        Scanner scanner = new Scanner(System.in);

        while (Objects.equals(answer, "y"))
        {
            try{
                System.out.println("Enter permission name: ");
                String name =scanner.nextLine();
                System.out.println("Enter permission description");
                String description = scanner.nextLine();

                //create new permission to add to shift manager
                ShiftM_Permissions permission = new ShiftM_Permissions(name,description);

                System.out.println("Enter branch ID: ");
                int branchNum = scanner.nextInt();

                //find branch in network
                BranchStore branch = (BranchStore) branchStoreDAO.findByID(branchNum);
                if(branch== null)
                {
                    System.out.println("ID entered does not exist, please try again: ");
                }
                else
                {
                    answer=scanner.nextLine();
                    System.out.println("Enter shift managers ID: ");
                    String ID = scanner.nextLine();
                    Employee employee = branch.findEmployeeInBranch(ID);
                    if(employee != null)
                    {
                        DailyShift dailyShift = branch.getShiftByDate(LocalDate.now().plusDays(2).toString());
                        if(dailyShift == null)
                            System.out.println("NO SHIFT YET");
                        else {
                            ShiftManager shiftManager= dailyShift.findEmployeeInShiftManager(ID);
                            if(shiftManager == null)
                            {
                                System.out.println("Shift Manager not found, maybe wrong branch, please try again");
                                continue;
                            }
                            else {
                                shiftManager.addPermission(permission);
                                dailyShiftDAO.update(dailyShift,branchNum); // connect to DB
                            }
                        }
                    }
                    else {
                        System.out.println("ID not found, maybe wrong branch, please try again");
                        continue;
                    }

                    System.out.println("Do you want to add another permission to a shift manager? (enter y/n): ");
                    answer = scanner.nextLine();
                }

            }
            catch (Exception e)
            {
                System.out.println("Invalid choice. Please try again.");
            }


        }
    }

    /**
     * remove a permission from Shift Manager for Daily Shift today
     */
    public void removePermissionToShiftManagerForDailyShiftToday()
    {
        System.out.println("-Remove permission from Shift Manager of TODAY shift-");
        String answer = "y";
        Scanner scanner = new Scanner(System.in);

        while (Objects.equals(answer, "y"))
        {
            try{
                System.out.println("Enter permission name");
                String name = scanner.nextLine();

                System.out.println("Enter branch ID: ");
                int branchNum = scanner.nextInt();

                //find branch in network
                BranchStore branch = (BranchStore) branchStoreDAO.findByID(branchNum);
                if(branch== null)
                {
                    System.out.println("ID entered does not exist, please try again: ");
                }
                else
                {
                    answer=scanner.nextLine();
                    System.out.println("Enter shift managers ID: ");
                    String ID = scanner.nextLine();
                    Employee employee = branch.findEmployeeInBranch(ID);
                    if(employee != null)
                    {
                        DailyShift dailyShift = branch.getShiftByDate(LocalDate.now().plusDays(2).toString());
                        if(dailyShift == null)
                            System.out.println("NO SHIFT YET");
                        else {
                            ShiftManager shiftManager= dailyShift.findEmployeeInShiftManager(ID);
                            if(shiftManager == null)
                            {
                                System.out.println("Shift Manager not found, maybe wrong branch, please try again");
                                continue;
                            }
                            else {
                                shiftManager.removePermission(shiftManager.findPermission(name));
                                dailyShiftDAO.update(dailyShift,branchNum); // connect to DB
                            }
                        }
                    }
                    else {
                        System.out.println("ID not found, maybe wrong branch, please try again");
                        continue;
                    }

                    System.out.println("Do you want to remove another permission from a shift manager? (enter y/n): ");
                    answer = scanner.nextLine();
                }
            }
            catch (Exception e)
            {
                System.out.println("Invalid choice. Please try again.");
            }

        }
    }
}
