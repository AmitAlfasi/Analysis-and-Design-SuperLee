package Presentation;

import DataAccess.DAO_BranchStore;
import DataAccess.DAO_Employee;
import DataAccess.DAO_Generator;
import BussinesLogic.*;
import DomainLayer.Area;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;


/**
 * This class will help HR user to manage the system:
 * - create new employees and branches
 * - update employees and branches
 */
public class HR_EntityManagement {

    //variables
    private DAO_Employee employeesDAO;
    private DAO_BranchStore branchStoreDAO;

    //constructor
    public HR_EntityManagement() throws SQLException, ClassNotFoundException {
        employeesDAO = DAO_Generator.getEmployeeDAO();
        branchStoreDAO = DAO_Generator.getBranchStoreDAO();
    }

    /**
     * @param employee: gets employee and adds a qualification to the employee
     */
    public void addQualificationToEmployee(Employee employee) {
        Scanner scanner = new Scanner(System.in);
        String answer = "y";
        System.out.println("-Add qualification to Employee-");
        //add qualification to employee - only one when it is a new employee
        while (!Objects.equals(answer, "n"))
        {
            try{
                System.out.println("Choose from the following, please enter the number of the role");
                Role[] roles = Role.values();
                for (int i = 0; i < roles.length; i++) {
                    if(!roles[i].equals(Role.DRIVER))
                        System.out.println(i + " - " + roles[i]);
                }
                int qualification = scanner.nextInt();
                if(!employee.getQualifications().contains(roles[qualification])){
                    employee.addRole(roles[qualification]);
                    employeesDAO.update(employee);
                }

                else
                    System.out.println("This employee is already a " + roles[qualification].toString());
                answer=scanner.nextLine();
                System.out.println("Would you like to add more qualifications? (Enter y/n): ");
                answer = scanner.nextLine();
            }
            catch (Exception e)
            {
                System.out.println("Invalid choice. Please try again.");
            }

        }
    }

    /**
     * @param employee: gets employee and removes a role qualification
     */
    public void removeQualificationToEmployee(Employee employee) {
        Scanner scanner = new Scanner(System.in);
        String answer = "y";
        System.out.println("-Remove qualification from Employee-");
        //add qualification to employee - only one when it is a new employee
        while (Objects.equals(answer, "y"))
        {
            try{
                System.out.println("Choose from the following, please enter the number of the role");
                Role[] roles = Role.values();
                for (int i = 0; i < roles.length; i++) {
                    System.out.println(i + " - " + roles[i]);
                }
                int qualification = scanner.nextInt();
                if(employee.getQualifications().contains(roles[qualification])){
                    employee.removeRole(roles[qualification]);
                    employeesDAO.update(employee);
                }

                else
                    System.out.println("This employee isn't a " + roles[qualification].toString());
                answer=scanner.nextLine();
                System.out.println("Would you like to remove another qualification? (Enter y/n): ");
                answer = scanner.nextLine();
            }
            catch (Exception e)
            {
                System.out.println("Invalid choice. Please try again.");
            }

        }
    }

    /**
     * add a licence to a driver
     * @param driver to update
     */
    public void addLicenceToDriver(Driver driver) {
        Scanner scanner = new Scanner(System.in);
        String answer = "y";
        System.out.println("-Add licence to Driver-");
        //add qualification to employee - only one when it is a new employee
        while (Objects.equals(answer, "y"))
        {
            try{
                System.out.println("Choose from the following, please enter the number of the licence");
                License[] licenses = License.values();
                for (int i = 0; i < licenses.length; i++) {
                    System.out.println(i + " - " + licenses[i]);
                }
                int type = scanner.nextInt();
                if(!driver.getLicenses().contains(licenses[type])){
                    driver.addLicense(licenses[type]);
                    employeesDAO.update(driver);
                }

                else
                    System.out.println("This driver is already qualified for " + licenses[type].toString());
                answer=scanner.nextLine();
                System.out.println("Would you like to add more licences? (Enter y/n): ");
                answer = scanner.nextLine();
            }
            catch (Exception e)
            {
                System.out.println("Invalid choice. Please try again.");
            }

        }
    }

    /**
     * @param driver: gets driver and removes a licence
     */
    public void removeLicenceFromDriver(Driver driver) {
        Scanner scanner = new Scanner(System.in);
        String answer = "y";
        System.out.println("-Remove Licence from Driver-");
        //add qualification to employee - only one when it is a new employee
        while (Objects.equals(answer, "y"))
        {
            try{
                System.out.println("Choose from the following, please enter the number of the licence");
                License[] licenses = License.values();
                for (int i = 0; i < licenses.length; i++) {
                    System.out.println(i + " - " + licenses[i]);
                }
                int qualification = scanner.nextInt();
                if(driver.getLicenses().contains(licenses[qualification]))
                {
                    driver.removeLicense(licenses[qualification]);
                    employeesDAO.update(driver);
                }
                else
                    System.out.println("This employee doesn't have a " + licenses[qualification].toString()+ " licence.");
                answer=scanner.nextLine();
                System.out.println("Would you like to remove another licence? (Enter y/n): ");
                answer = scanner.nextLine();
            }
            catch (Exception e)
            {
                System.out.println("Invalid choice. Please try again.");
            }

        }
    }

    /**
     * function asks the user what branch to add to
     * @param employee: employee to add
     */
    public void addEmployeeToBranch(Employee employee)
    {
        System.out.println("-Add Employee to Branch-");
        //add employee to branch
        String answer = "y";
        Scanner scanner = new Scanner(System.in);
        while (Objects.equals(answer, "y"))
        {
            try{
                System.out.println("Enter branch ID: ");
                String branchNum = scanner.nextLine();
                //find branch in network
                BranchStore branch = (BranchStore) branchStoreDAO.findByID(branchNum); // check in DAO
                if(branch== null){
                    System.out.println("ID entered does not exist, please try again: ");
                }
                else {
                    if(!branch.getEmployees().contains(employee))
                        branch.addEmployee(employee);
                    branchStoreDAO.update(branch); // connect to DB
                    System.out.println("Do you want to add the employee to another branch? (enter y/n): ");
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
     * remove employee form branch
     * @param employee: employee to remove
     */
    public void removeEmployeeFromBranch(Employee employee)
    {
        System.out.println("-Remove Employee from Branch-");
        //remove employee to branch
        String answer = "y";
        Scanner scanner = new Scanner(System.in);
        while (Objects.equals(answer, "y"))
        {
            try
            {
                System.out.println("Enter branch ID: ");
                String branchNum = scanner.nextLine();

                //find branch in network
                BranchStore branch = (BranchStore) branchStoreDAO.findByID(branchNum);
                if(branch== null){
                    System.out.println("ID entered does not exist, please try again: ");
                }
                else {
                    branch.removeEmployee(employee);
                    branchStoreDAO.update(branch); // connect to DB
                    answer=scanner.nextLine();
                    System.out.println("Do you want to remove the employee from another branch? (enter y/n): ");
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
     * add a new employee to system
     */
    public void newEmployeeInNetwork() throws SQLException {
        System.out.println("-Add Employee to Network-");
        //get from HR manager all the details to create a new employee in the system
        System.out.println("Hello HR manager, to add a new employee please enter the following details:");
        Scanner scanner = new Scanner(System.in);
        String startDate;
        while(true){
            System.out.println("Enter employee's start Date (enter as yyyy-MM-dd): ");
            try {
                startDate = scanner.nextLine();
                LocalDate.parse(startDate);
                break;
            }
            catch (Exception e)
            {
                System.out.println("Invalid format");
            }
        }
        System.out.println("Enter employee's First name: ");
        String first = scanner.nextLine();
        System.out.println("Enter employee's Last name: ");
        String last = scanner.nextLine();
        System.out.println("Enter employee's ID: ");
        String id = scanner.nextLine();
        System.out.println("Enter employee's bank account: ");
        String bankAccount = scanner.nextLine();
        String tmp;
        double salary;
        while(true){
            System.out.println("Enter employee's salary: ");
            try {
                tmp = scanner.nextLine();
                salary = Double.parseDouble(tmp);
                break;
            }
            catch (Exception e)
            {
                System.out.println("Invalid input. Enter a number please");
            }
        }
        System.out.println("Enter employee's terms of employment: ");
        String filePath = scanner.nextLine();

        //create an employee generator
        EmployeeGenerator employeeGenerator = new EmployeeGenerator();
        //create new employee
        Employee employee = employeeGenerator.CreateEmployee(first,last,id,bankAccount,salary,filePath,startDate);
        //add employee to list of all employees in network and update the DB
        employeesDAO.insert(employee);

        System.out.println("Is the new employee a Driver? (y/n)");
        String ans = scanner.nextLine();
        while (Objects.equals(ans, "y") || Objects.equals(ans, "n"))
        {
            if (ans.equals("y"))// driver
            {
                Driver driver = employeeGenerator.CreateDriver(employee);
                addLicenceToDriver(driver);
                break;
            }
            else if (ans.equals("n")) //employee
            {
                addEmployeeToBranch(employee);
                //add qualifications - call update DAO
                addQualificationToEmployee(employee);
                break;
            }
            else System.out.print("Invalid input. Try again");
        }

        //add employee to branch and update the DB

        System.out.println("Employee successfully added to system");
    }

    /**
     * gets from user a branch that they want to change its open hours
     * inside this function it calls a helper function that changes a specific day
     * @param branchStore : store to update
     */
    public void updateBranchOpenHours(BranchStore branchStore) throws SQLException {
        System.out.println("-Update Branch opening hours-");
        Scanner scanner = new Scanner(System.in);
        String answer = "y";
        while (Objects.equals(answer, "y"))
        {
            System.out.println("Enter day you want to update (e.g Saturday): ");
            String day = scanner.nextLine();

            System.out.println("Enter which shift you want to update (morning - 0, evening - 1): ");
            int shift = scanner.nextInt();

            System.out.println("Do you want the shift to be open or closed (open - 0, closed - 1): ");
            int availability = scanner.nextInt();

            branchStore.setOpenHours(Days.valueOf(day).ordinal(),shift,availability);

            answer=scanner.nextLine();
            System.out.println("Would you like to change another days open hours? (enter y/n)");
            answer = scanner.nextLine();

        }
        System.out.println("Enter a summery for this branch's opening time");
        answer = scanner.nextLine();
        branchStore.setOpeningTime(answer);
        branchStoreDAO.update(branchStore); // connect to DB

    }
    /**
     * create new branch in system
     */
    public void newBranchInNetwork() throws SQLException, ClassNotFoundException {
        System.out.println("-Add Branch to Network-");
        //get from HR manager all the details to create a new employee in the system
        System.out.println("Hello HR manager, to add a new branch please enter the following details:");
        Scanner scanner = new Scanner(System.in);
        int id = branchStoreDAO.nextID();
        System.out.println("Enter branch's name: ");
        String name = scanner.nextLine();
        System.out.println("Enter branch's address: ");
        String address = scanner.nextLine();
        System.out.println("Enter branch's Area: ");
        Area area = Area.valueOf(scanner.nextLine());
        System.out.println("Enter branch's phone number: ");
        String phone = scanner.nextLine();
        BranchStore branchStore = BranchStoreGenerator.CreateBranchStore(name,area,address,phone, "24/7",id);
        branchStoreDAO.insert(branchStore);
        System.out.println("Please update the open hours in according to the opening time for scheduling purposes\nthe default is that the branch store is open 24/7");
        updateBranchOpenHours(branchStore);
        System.out.println("Branch successfully added to system, ID number is: "+ branchStore.getBranchID());
    }

    /**
     * gets an employee and asks HR manger what details to update
     * @param employee: employee to update
     */
    public void updateEmployeesDetails(Employee employee) {
        int choice = 0;
        Scanner scanner = new Scanner(System.in);
        while (choice != 7) {
            System.out.println("-Update Employees details-");
            System.out.println("1. Bank Account");
            System.out.println("2. Salary");
            System.out.println("3. Employment Terms");
            System.out.println("4. Add qualification");
            System.out.println("5. Remove qualification");
            System.out.println("6. Add Bonus");
            System.out.println("--DRIVERS ONLY--\n7. Add licence");
            System.out.println("8. Remove licence");
            System.out.println("9. Exit");
            try{
                choice = scanner.nextInt();
                String temp = scanner.nextLine();
                switch (choice) {
                    case 1:
                        System.out.println("You chose Option 1.");
                        System.out.println("Enter new bank account number");
                        String bank = scanner.nextLine();
                        employee.setBankAccount(bank);
                        break;

                    case 2:
                        System.out.println("You chose Option 2.");
                        while (true){
                            System.out.println("Enter new salary");
                            try{
                                String salary = scanner.nextLine();
                                employee.setSalary(Double.parseDouble(salary));
                                break;
                            }
                            catch (Exception e){
                                System.out.println("Invalid input. Please enter a number");
                            }
                        }
                        break;

                    case 3:
                        System.out.println("You chose Option 3.");
                        System.out.println("Enter new employment terms");
                        String empTerms = scanner.nextLine();
                        employee.setEmpTerms(empTerms);
                        break;

                    case 4:
                        System.out.println("You chose Option 4.");
                        addQualificationToEmployee(employee);
                        break;

                    case 5:
                        System.out.println("You chose Option 5.");
                        removeQualificationToEmployee(employee);
                        break;

                    case 6 :
                        System.out.println("You chose Option 6.");
                        while (true){
                            System.out.println("How much do you wish to add as a bonus?");
                            try{
                                String bonus = scanner.nextLine();
                                employee.setCumulativeSalary(employee.getCumulativeSalary() + Double.parseDouble(bonus));
                                System.out.println("Employees cumulative salary now is: " + employee.getCumulativeSalary());
                                break;
                            }
                            catch (Exception e){
                                System.out.println("Invalid input. Please enter a number");
                            }
                        }
                        break;
                    case 7:
                        System.out.println("You chose Option 7.");
                        if(employee.canDoRole(Role.DRIVER))
                            addLicenceToDriver((Driver) employee);
                        else System.out.println("Employee is not a driver");
                        break;
                    case 8:
                        System.out.println("You chose Option 8.");
                        if(employee.canDoRole(Role.DRIVER))
                            removeLicenceFromDriver((Driver) employee);
                        else System.out.println("Employee is not a driver");
                        break;
                    case 9:
                        System.out.println("Existing menu....");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
                employeesDAO.update(employee);
                break;
            }
            catch (Exception e)
            {
                System.out.println("Invalid choice. Please try again.");
            }

        }
    }

    /**
     * gets an employee and asks HR manger what details to update
     * @param employee: employee to present
     */
    public void getEmployeesDetails(Employee employee)
    {
        int choice = 0;
        Scanner scanner = new Scanner(System.in);
        while (choice != 8) {
            System.out.println("-Get Employees details-");
            System.out.println("1. Bank Account");
            System.out.println("2. Salary");
            System.out.println("3. Employment Terms");
            System.out.println("4. Qualifications");
            System.out.println("5. Cumulative Salary");
            System.out.println("6. Start Date");
            System.out.println("--DRIVERS ONLY--\n7. View upcoming Transit dates");
            System.out.println("8. Exit");
            try{
                choice = scanner.nextInt();
                String temp = scanner.nextLine();
                switch (choice) {
                    case 1:
                        System.out.println("You chose Option 1.");
                        System.out.println(employee.getName()+" bank account is: "+employee.getBankAccount());
                        break;

                    case 2:
                        System.out.println("You chose Option 2.");
                        System.out.println(employee.getName()+" salary is: "+employee.getSalary());
                        break;
                    case 3:
                        System.out.println("You chose Option 3.");
                        System.out.println(employee.getName()+" employment terms are:\n "+employee.getSalary());
                        break;
                    case 4:
                        System.out.println("You chose Option 4.");
                        System.out.println(employee.getName()+" qualifications are:\n "+employee.getQualifications().toString());
                        break;
                    case 5:
                        System.out.println("You chose Option 5.");
                        System.out.println(employee.getName()+" cumulative salary is:\n "+employee.getCumulativeSalary());
                        break;
                    case 6:
                        System.out.println("You chose Option 6.");
                        System.out.println(employee.getName()+" start date was:\n "+employee.getStartDate());
                        break;
                    case 7:
                        System.out.println("You chose Option 7.");
                        if(employee.canDoRole(Role.DRIVER))
                            ((Driver)employee).printTransits();
                        else System.out.println("Employee is not a driver");
                        break;
                    case 8:
                        System.out.println("Existing menu....");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }

            }
            catch (Exception e)
            {
                System.out.println("Invalid choice. Please try again.");
            }

        }
    }

    /**
     * update branch information
     * @param branch : branch to get details from
     */
    public void updateBranchDetails(BranchStore branch)
    {
        int choice = 0;
        Scanner scanner = new Scanner(System.in);
        while (choice != 4) {
            try{
                System.out.println("-Update Branch details-");
                System.out.println("1. Name");
                System.out.println("2. Phone number");
                System.out.println("3. Open Hours");
                System.out.println("4. Exit");

                choice = scanner.nextInt();
                String temp = scanner.nextLine();
                switch (choice) {
                    case 1:
                        System.out.println("You chose Option 1.");
                        System.out.println("Enter new branch name");
                        String name = scanner.nextLine();
                        branch.setName(name);
                        break;

                    case 2:
                        System.out.println("You chose Option 2.");
                        System.out.println("Enter new phone number");
                        String phoneNum = scanner.nextLine();
                        branch.setPhoneNum(phoneNum);
                        break;
                    case 3:
                        System.out.println("You chose Option 3.");
                        updateBranchOpenHours(branch);
                        break;
                    case 4:
                        System.out.println("Existing menu....");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
                branchStoreDAO.update(branch);
                break;
            }
            catch (Exception e)
            {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * print all employees cumulative salary
     */
    public void calculateSalary() throws SQLException {
        System.out.println("- Cumulative Salary -");

        for (Employee employee: employeesDAO.getNetworkEmployees())
            System.out.println(employee.getName()+ ": "+ employee.getCumulativeSalary());

        System.out.println("Do you want to nullify employees cumulative salary? (y/n)");
        Scanner scanner = new Scanner(System.in);
        String ans = scanner.nextLine();
        if(Objects.equals(ans, "y"))
            for (Employee employee: employeesDAO.getNetworkEmployees()){
                employee.setCumulativeSalary(0);
                employeesDAO.update(employee);
            }
        System.out.println("Complete");
    }

}