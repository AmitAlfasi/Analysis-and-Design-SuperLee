package DataAccess;
import BussinesLogic.*;
import BussinesLogic.Driver;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static BussinesLogic.Role.DRIVER;

/**
 * This DAO is for the employees
 */
public class DAO_Employee implements IDAO_Entity {

    private Map<String ,Employee> networkEmployees;

    private List<Employee> employeeList;
    private Map<String, Driver> newtworkDrivers;

    private List<Driver> driverList;

    private Connection conn;

    // constructor
    public DAO_Employee(Connection connection) throws SQLException, ClassNotFoundException {
        conn = connection;
        networkEmployees = new HashMap<>();
        newtworkDrivers = new HashMap<>();
        employeeList = new ArrayList<>();
        driverList = new ArrayList<>();
    }

    /**
     * find an Employee (or Driver) by their ID
     * @param ID of the employee
     * @return the employee or null if not exist
     * @throws SQLException in case of error
     */
    @Override
    public Object findByID(Object ID) throws SQLException {
        if (networkEmployees != null && networkEmployees.containsKey(ID))
            return networkEmployees.get(ID);
        else if(newtworkDrivers != null && newtworkDrivers.containsKey(ID))
            return newtworkDrivers.get(ID);
//             if the employee not in the MAP
        else
        {
            PreparedStatement stmt = conn.prepareStatement("SELECT firstName, lastName, bankAccount, salary, empTerms, startDate, shiftsLimit, cumulativeSalary FROM Employees WHERE employeeID = ?");
            stmt.setString(1, (String) ID);
            ResultSet rs = stmt.executeQuery();
            Employee employee;
            EmployeeGenerator generator;
            if (rs.next()) {
                // get the details
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String bankAccount = rs.getString("bankAccount");
                double salary = rs.getDouble("salary");
                String empTerms = rs.getString("empTerms");
                String startDate = rs.getString("startDate");
                // insert to new employee
                generator = new EmployeeGenerator();
                employee = generator.CreateEmployee(firstName, lastName, (String) ID, bankAccount, salary, empTerms, startDate);
                // update salary and limits
                employee.setCumulativeSalary(rs.getDouble("cumulativeSalary"));
                employee.setShiftsLimit(rs.getInt("shiftsLimit"));
            }
            else
                return null;
            // update qualifications
            stmt = conn.prepareStatement("SELECT eq.qualificationId FROM EmployeeQualifications eq JOIN Qualifications q ON eq.qualificationId = q.qualificationId WHERE eq.employeeID = ?");
            stmt.setString(1, (String) ID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int roleID = rs.getInt("qualificationId");
                employee.addRole(Role.values()[roleID]);
            }
            stmt = conn.prepareStatement("SELECT * FROM EmployeeConstraints ec WHERE ec.employeeID = ?");
            stmt.setString(1, (String) ID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int day = rs.getInt("dayOfWeek");
                int morning = rs.getInt("morningShift");
                int evening = rs.getInt("eveningShift");
                employee.setConstrains(day, 0, morning == 0);
                employee.setConstrains(day, 1, evening == 0);
            }
            // check if it's driver
            if (employee.canDoRole(DRIVER))
            {
                //add licences
                stmt = conn.prepareStatement("SELECT d.licenseId FROM Drivers d WHERE d.employeeID = ?");
                stmt.setString(1, (String) ID);
                rs = stmt.executeQuery();
                Driver driver = generator.CreateDriver(employee);
                while (rs.next()) {
                    int license = rs.getInt("licenseId");
                    driver.addLicense(License.values()[license]);
                }

                //add transit dates
                stmt = conn.prepareStatement("SELECT transitDate FROM DriversTransitsDates WHERE driverID = ?");
                stmt.setString(1, (String) ID);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    LocalDate date = LocalDate.parse(rs.getString("transitDate"));
                    driver.addTransitDate(date);
                }
                //add to map
                stmt.close();
                rs.close();
                newtworkDrivers.put((String) ID,driver);
                return driver;
            }
            else {
                //add to map
                stmt.close();
                rs.close();
                networkEmployees.put((String) ID,employee);
                return employee;
            }
        }
    }

    /**
     * adds an employee to the DB and also the matching map
     * NO QUALIFICATIONS AT THIS POINT
     * @param o - new employee
     * @throws SQLException incase of an error
     */
    @Override
    public void insert(Object o) throws SQLException
    {
        Employee e = (Employee)o;
        if(e != null)
        {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Employees (firstName, lastName, employeeID, bankAccount, salary, empTerms, startDate, shiftsLimit, cumulativeSalary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, e.getFirstName());
            stmt.setString(2, e.getLastName());
            stmt.setString(3, e.getId());
            stmt.setString(4, e.getBankAccount());
            stmt.setDouble(5, e.getSalary());
            stmt.setString(6, e.getEmpTerms());
            stmt.setString(7, e.getStartDate());
            stmt.setInt(8, e.getShiftsLimit());
            stmt.setDouble(9, e.getCumulativeSalary());
            stmt.executeUpdate();
            // add to constraints table
            for(int i=0; i<7; i++) {
                stmt = conn.prepareStatement("INSERT INTO EmployeeConstraints (employeeId, dayOfWeek)" +
                        "VALUES (? , ?)");
                stmt.setString(1, e.getId());
                stmt.setInt(2, i);
                stmt.executeUpdate();
            }
        }
    }

    /**
     * update employees details
     * @param o employee to update
     * @throws SQLException in case of an error
     */
    @Override
    public void update(Object o) throws SQLException {
        Employee e = (Employee)o;
        if(e != null)
        {
            //set details
            PreparedStatement stmt = conn.prepareStatement("UPDATE Employees SET firstName = ?, lastName = ?, bankAccount = ?, salary = ?, empTerms = ?, startDate = ?, shiftsLimit = ?, cumulativeSalary = ? WHERE employeeID = ?");
            stmt.setString(1, e.getFirstName());
            stmt.setString(2, e.getLastName());
            stmt.setString(3, e.getBankAccount());
            stmt.setDouble(4, e.getSalary());
            stmt.setString(5, e.getEmpTerms());
            stmt.setString(6, (e.getStartDate()));
            stmt.setInt(7, e.getShiftsLimit());
            stmt.setDouble(8, e.getCumulativeSalary());
            stmt.setString(9, e.getId());
            stmt.executeUpdate();

            //set constraints
            for (Days day : Days.values())
            {
                stmt = conn.prepareStatement("UPDATE EmployeeConstraints SET morningShift = ?, eveningShift = ? WHERE employeeID = ? AND dayOfWeek = ?");
                stmt.setInt(1, e.getConstraints()[day.ordinal()][0]? 1:0);
                stmt.setInt(2, e.getConstraints()[day.ordinal()][1]? 1:0);
                stmt.setString(3, e.getId());
                stmt.setInt(4,day.ordinal());
                stmt.executeUpdate();
            }

            //set qualifications
            List<Role> roles = e.getQualifications();
            stmt = conn.prepareStatement("SELECT * FROM EmployeeQualifications WHERE employeeID = ?");
            stmt.setString(1,e.getId());
            ResultSet rs = stmt.executeQuery();
            int amount = 0;
            while(rs.next()) {
                amount++;
            }
            rs = stmt.executeQuery();

            //remove role from DB
            if(amount > roles.size()){
                while(rs.next()){
                    int index = rs.getInt("qualificationId");
                    if(!roles.contains(Role.values()[index])) {
                        stmt = conn.prepareStatement("DELETE FROM EmployeeQualifications WHERE employeeID = ? AND qualificationId = ?");
                        stmt.setString(1, e.getId());
                        stmt.setInt(2,index);
                        int rowsAffected = stmt.executeUpdate();
                        if (rowsAffected == 0) {
                            System.out.println("No rows were deleted.");
                        }
                        break;
                    }
                }
            }

            //add role to DB
            else if(amount < roles.size()){
                ArrayList<Integer> rolesInDB = new ArrayList<>();
                while(rs.next()) {
                    rolesInDB.add(rs.getInt("qualificationId"));
                }
                for(Role role: roles){
                    if(!rolesInDB.contains(role.ordinal())) {
                        stmt = conn.prepareStatement("INSERT INTO EmployeeQualifications (employeeID,qualificationId) VALUES (?,?)");
                        stmt.setString(1, e.getId());
                        stmt.setInt(2,role.ordinal());
                        stmt.executeUpdate();
                        break;
                    }
                }
            }

            //if it is a driver
            if(e.canDoRole(DRIVER))
            {
                //set licences
                List<License> licenses = ((Driver) e).getLicenses();
                stmt = conn.prepareStatement("SELECT * FROM Drivers WHERE employeeID = ?");
                stmt.setString(1,e.getId());
                rs = stmt.executeQuery();
                amount = 0;
                while(rs.next()) {
                    amount++;
                }
                rs = stmt.executeQuery();

                //remove licence from DB
                if(amount > licenses.size()){
                    while(rs.next()){
                        int index = rs.getInt("licenseId");
                        if(!licenses.contains(License.values()[index])) {
                            stmt = conn.prepareStatement("DELETE FROM Drivers WHERE employeeID = ? AND licenseId = ?");
                            stmt.setString(1, e.getId());
                            stmt.setInt(2,index);
                            int rowsAffected = stmt.executeUpdate();
                            if (rowsAffected == 0) {
                                System.out.println("No rows were deleted.");
                            }
                            break;
                        }
                    }
                }

                //add licence to DB
                else if(amount < licenses.size()){
                    ArrayList<Integer> rolesInDB = new ArrayList<>();
                    while(rs.next()) {
                        rolesInDB.add(rs.getInt("licenseId"));
                    }
                    for(License license: licenses){
                        if(!rolesInDB.contains(license.ordinal())) {
                            stmt = conn.prepareStatement("INSERT INTO Drivers (employeeID,licenseId) VALUES (?,?)");
                            stmt.setString(1, e.getId());
                            stmt.setInt(2,license.ordinal());
                            stmt.executeUpdate();
                            break;
                        }
                    }
                }

                //transit dates
                List<LocalDate> transits = ((Driver) e).getTransitsDates();
                stmt = conn.prepareStatement("SELECT * FROM DriversTransitsDates WHERE driverID = ?");
                stmt.setString(1,e.getId());
                rs = stmt.executeQuery();
                amount = 0;
                while(rs.next()) {
                    amount++;
                }
                rs = stmt.executeQuery();

                //remove transit date from DB
                if(amount > transits.size()){
                    while(rs.next()){
                        LocalDate date = LocalDate.ofEpochDay(rs.getInt("transitDate"));
                        if(!transits.contains(date)) {
                            stmt = conn.prepareStatement("DELETE FROM DriversTransitsDates WHERE driverID = ? AND transitDate = ?");
                            stmt.setString(1, e.getId());
                            stmt.setString(2, String.valueOf(date));
                            int rowsAffected = stmt.executeUpdate();
                            if (rowsAffected == 0) {
                                System.out.println("No rows were deleted.");
                            }
                            break;
                        }
                    }
                }
                //add transit date to DB
                else if(amount < transits.size()){
                    ArrayList<LocalDate> dates = new ArrayList<>();
                    while(rs.next()) {
                        dates.add(LocalDate.parse(rs.getString("transitDate")));
                    }
                    for(LocalDate date: transits){
                        if(!dates.contains(date)) {
                            stmt = conn.prepareStatement("INSERT INTO DriversTransitsDates (driverID,transitDate) VALUES (?,?)");
                            stmt.setString(1, e.getId());
                            stmt.setString(2, String.valueOf(date));
                            stmt.executeUpdate();
                            break;
                        }
                    }
                }

                //add to map
                newtworkDrivers.put(e.getId(), (Driver) e);
            }
            else //if not a driver add to other map
                networkEmployees.put(e.getId(),e);
        }
    }

    /**
     *
     * @param o employee to remove from database
     * @throws SQLException
     */
    @Override
    public void delete(Object o) throws SQLException {
        Employee e = (Employee)o;
        if(e != null) {
            //delete from employees table
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Employees WHERE employeeID = ?");
            stmt.setString(1, e.getId());
            stmt.executeUpdate();

            //delete from qualification tables
            stmt = conn.prepareStatement("DELETE FROM EmployeeQualifications WHERE employeeID = ?");
            stmt.setString(1, e.getId());
            stmt.executeUpdate();

            //delete from constraints tables
            stmt = conn.prepareStatement("DELETE FROM EmployeeConstraints WHERE employeeID = ?");
            stmt.setString(1, e.getId());
            stmt.executeUpdate();;

            //delete from drivers
            if(e.canDoRole(DRIVER))
            {
                stmt = conn.prepareStatement("DELETE FROM Drivers WHERE id = ?");
                stmt.setString(1, e.getId());
                stmt.executeUpdate();

                //DriversTransitsDates
                stmt = conn.prepareStatement("DELETE FROM DriversTransitsDates WHERE id = ?");
                stmt.setString(1, e.getId());
                stmt.executeUpdate();
                //remove from map
                newtworkDrivers.remove(e.getId());
            }
            else
                networkEmployees.remove(e.getId());
        }
    }

    /**
     *
     * @return list of all employees-no drivers
     * @throws SQLException
     */
    public List<Employee> getNetworkEmployees() throws SQLException {
        //if(networkEmployees.isEmpty())
        ifEmptyMaps();
        employeeList.clear();
        employeeList.addAll(networkEmployees.values());
        return employeeList;
    }

    /**
     *
     * @return list of all network drivers
     * @throws SQLException
     */
    public List<Driver> getNetworkDrivers() throws SQLException {
        //if(newtworkDrivers.isEmpty())
        ifEmptyMaps();
        driverList.clear();
        driverList.addAll(newtworkDrivers.values());
        return driverList;
    }

    /**
     * uploads the data to the list if they are required
     * @throws SQLException
     */
    private void ifEmptyMaps() throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT employeeID FROM Employees");
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            findByID(rs.getString("employeeID"));
        }
    }
}