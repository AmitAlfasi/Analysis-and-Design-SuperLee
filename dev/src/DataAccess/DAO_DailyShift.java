package DataAccess;

import BussinesLogic.*;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This DAO is for branchStore
 * All the dailyShifts saved in DB and BranchStore using it
 */
public class DAO_DailyShift implements IDAO_DailyShift {

    private Map<String,DailyShift> networkDailyShift;

    private DAO_BranchStore daoBranchStore;
    private Connection conn;


    // constructor
    public DAO_DailyShift(Connection connection) throws SQLException, ClassNotFoundException {
        conn = connection;
        networkDailyShift = new HashMap<>();
        daoBranchStore = DAO_Generator.getBranchStoreDAO();
    }

    /**
     * Search a dailyShift by key - date and branchId
     * @param date of the shift
     * @param id of the branch
     * @return the DailyShift object
     * @throws SQLException in case of a problem
     * @throws ClassNotFoundException in case of a problem
     */
    @Override
    public Object findByKey(Object date, Object id) throws SQLException, ClassNotFoundException {
        String dateString;
        try
        {
           dateString = ((LocalDate)date).toString();
        }
        catch (Exception e)
        {
            dateString= (String) date;
        }
        DailyShift dailyShift = null;
        PreparedStatement stmt = conn.prepareStatement("SELECT date, branchID, endOfDayReport FROM DailyShifts WHERE date = ? AND branchID = ?");
        stmt.setString(1, (dateString));
        stmt.setInt(2, (Integer) id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            LocalDate shiftDate = LocalDate.parse(rs.getString("date"));
            dailyShift = new DailyShift(shiftDate);
            dailyShift.setEndOfDayReport(new File(rs.getString("endOfDayReport")));

        }
        if(dailyShift != null)
        {
            // get the morning employees
            stmt = conn.prepareStatement("SELECT * FROM MorningShiftEmployees WHERE date = ?");
            stmt.setString(1, (dateString));
            rs = stmt.executeQuery();
            DAO_Employee employeesDAO = DAO_Generator.getEmployeeDAO();
            while(rs.next()){
                String employeeId = rs.getString("employeeID");
                Employee e = (Employee) employeesDAO.findByID(employeeId);
                Role r = Role.values()[rs.getInt("role")];
                assert dailyShift != null;
                dailyShift.addEmployeeToMorning(e, r);
            }

            // get the evening employees
            stmt = conn.prepareStatement("SELECT * FROM EveningShiftEmployees WHERE date = ?");
            stmt.setString(1, (dateString));
            rs = stmt.executeQuery();
            while(rs.next()){
                String employeeId = rs.getString("employeeID");
                Employee e = (Employee) employeesDAO.findByID(employeeId);
                Role r = Role.values()[rs.getInt("role")];
                dailyShift.addEmployeeToEvening(e, r);
            }

            // get shift managers
            stmt = conn.prepareStatement("SELECT * FROM ShiftManagers WHERE shiftDate = ?");
            stmt.setString(1, (dateString));
            rs = stmt.executeQuery();
            while(rs.next()) {
                String employeeId = rs.getString("shiftManagerID");
                Employee e = (Employee) employeesDAO.findByID(employeeId);
                int shift = rs.getInt("shiftSlot");
                ShiftManager manager = ShiftManagerGenerator.CreateShiftManager(e.getName(), employeeId, LocalDate.parse(dateString),shift);
                dailyShift.addShiftManager(manager);
            }
            networkDailyShift.put(date.toString()+id,dailyShift);
        }
        return dailyShift;
    }

    /**
     * Search and return the DailyShifts Map for a specific branchStore
     * @param ID of the branch
     * @return a Map of date and DailyShift
     * @throws SQLException in case of a problem
     * @throws ClassNotFoundException in case of a problem
     */
    public Map<LocalDate,DailyShift> findByBranchID(Object ID) throws SQLException, ClassNotFoundException {
        Map<LocalDate,DailyShift> dailyShifts = new HashMap<>();
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM DailyShifts WHERE branchID = ?");
        statement.setInt(1, (Integer) ID);
        ResultSet rs = statement.executeQuery();
        while (rs.next()){
            LocalDate date = LocalDate.parse(rs.getString("date"));
            dailyShifts.put(date,(DailyShift) findByKey(date, ID));
        }
        return dailyShifts;
    }

    /**
     * Insert new dailyShift to DB
     * @param o the DailyShift object
     * @param id of the branch
     * @throws SQLException in case of any problem
     */
    @Override
    public void insert(Object o, Object id) throws SQLException {
        DailyShift dailyShift = (DailyShift) o;
        if (dailyShift != null) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO DailyShifts (date, branchID, endOfDayReport)" +
                    "VALUES (?, ?, ?)");
            stmt.setString(1, dailyShift.getDate().toString());
            stmt.setInt(2, (Integer) id);
            stmt.setString(3, dailyShift.getEndOfDayReport().toString());
            stmt.executeUpdate();

            // add employees
            for (Map.Entry<Role, ArrayList<Employee>> shift : dailyShift.getMorningShift().entrySet()) {
                for (Employee e : shift.getValue()) {
                    stmt = conn.prepareStatement("INSERT INTO MorningShiftEmployees (date, employeeID,branchID, role)" + "VALUES(?,?,?,?)");
                    stmt.setString(1, dailyShift.getDate().toString());
                    stmt.setString(2, e.getId());
                    stmt.setInt(3,(Integer) id);
                    stmt.setInt(4, shift.getKey().ordinal());
                    stmt.executeUpdate();
                }
            }
            for (Map.Entry<Role, ArrayList<Employee>> shift : dailyShift.getEveningShift().entrySet()) {
                for (Employee e : shift.getValue()) {
                    stmt = conn.prepareStatement("INSERT INTO EveningShiftEmployees (date, employeeID,branchID, role)" + "VALUES(?,?,?,?)");
                    stmt.setString(1, dailyShift.getDate().toString());
                    stmt.setString(2, e.getId());
                    stmt.setInt(3,(Integer) id);
                    stmt.setInt(4, shift.getKey().ordinal());
                    stmt.executeUpdate();
                }
            }
            // add shift managers
            for (ShiftManager shiftManager : dailyShift.getShiftManagers()) {
                stmt = conn.prepareStatement("INSERT INTO ShiftManagers (shiftDate, shiftManagerID, branchID,fullName, shiftSlot)" + "VALUES(?,?,?,?,?)");
                stmt.setString(1, dailyShift.getDate().toString());
                stmt.setString(2, shiftManager.getId());
                stmt.setInt(3,(Integer) id);
                stmt.setString(4, shiftManager.getFullName());
                stmt.setInt(5, shiftManager.getShiftSlot());
                stmt.executeUpdate();

                // add permissions
                for (ShiftM_Permissions perm : shiftManager.getPermissions()) {
                    stmt = conn.prepareStatement("INSERT INTO ShiftM_Permissions (shiftDate,shiftSlot ,branchID, shiftManagerID, permissionName, permissionDescription)" + "VALUES(?,?,?,?,?,?)");
                    stmt.setString(1, dailyShift.getDate().toString());
                    stmt.setInt(2,shiftManager.getShiftSlot());
                    stmt.setInt(3,(Integer)id);
                    stmt.setString(4, shiftManager.getId());
                    stmt.setString(5, perm.getName());
                    stmt.setInt(6, shiftManager.getShiftSlot());
                    stmt.executeUpdate();
                }

                // add cancellations
                for (Cancellation cancel : shiftManager.getCancelations()) {
                    stmt = conn.prepareStatement("INSERT INTO Cancellations (shiftDate, shiftManagerID, cancelID, item, amount)" + "VALUES(?,?,?,?,?)");
                    stmt.setString(1, dailyShift.getDate().toString());
                    stmt.setString(2, shiftManager.getId());
                    stmt.setInt(3, cancel.getCancelID());
                    stmt.setString(4, cancel.getItem());
                    stmt.setInt(5, cancel.getAmount());
                    stmt.executeUpdate();
                }
            }
        }
    }

    /**
     * Update a DailyShift - in case of any change in details
     * @param o the DailyShift object
     * @param id of the branch
     * @throws SQLException in case of any problem
     * @throws ClassNotFoundException in case of any problem
     */
    @Override
    public void update(Object o, Object id) throws SQLException, ClassNotFoundException {
        DailyShift dailyShift = (DailyShift) o;

        if (dailyShift != null) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE DailyShifts SET endOfDayReport = ? WHERE branchID = ? AND date = ?");
            stmt.setString(1, dailyShift.getEndOfDayReport().toString());
            stmt.setInt(2, (Integer) id);
            stmt.setString(3, dailyShift.getDate().toString());
            stmt.executeUpdate();

            // update employees
            for (Map.Entry<Role, ArrayList<Employee>> shift : dailyShift.getMorningShift().entrySet()) {
                for (Employee e : shift.getValue()) {
                    stmt = conn.prepareStatement("UPDATE MorningShiftEmployees SET role = ? WHERE date = ?AND employeeID = ? AND branchID = ?");
                    stmt.setInt(1, shift.getKey().ordinal());
                    stmt.setString(2, dailyShift.getDate().toString());
                    stmt.setString(3, e.getId());
                    stmt.setInt(4, (Integer) id);
                    stmt.executeUpdate();
                }
            }
            for (Map.Entry<Role, ArrayList<Employee>> shift : dailyShift.getEveningShift().entrySet()) {
                for (Employee e : shift.getValue()) {
                    stmt = conn.prepareStatement("UPDATE EveningShiftEmployees SET role = ? WHERE date = ?AND employeeID = ? AND branchID = ?");
                    stmt.setInt(1, shift.getKey().ordinal());
                    stmt.setString(2, dailyShift.getDate().toString());
                    stmt.setString(3, e.getId());
                    stmt.setInt(4, (Integer) id);
                    stmt.executeUpdate();
                }
            }
            // update shift managers
            for (ShiftManager shiftManager : dailyShift.getShiftManagers()) {
                stmt = conn.prepareStatement("UPDATE ShiftManagers SET fullName = ?, shiftSlot = ? WHERE shiftDate = ? AND shiftManagerID = ? AND branchID = ?");
                stmt.setString(1, shiftManager.getFullName());
                stmt.setInt(2, shiftManager.getShiftSlot());
                stmt.setString(3, dailyShift.getDate().toString());
                stmt.setString(4, shiftManager.getId());
                stmt.setInt(5, (Integer) id);
                stmt.executeUpdate();

                // update permissions
                for (ShiftM_Permissions perm : shiftManager.getPermissions()) {
                    stmt = conn.prepareStatement("UPDATE ShiftM_Permissions SET permissionDescription = ? WHERE shiftDate = ? AND shiftSlot = ? AND branchID = ? AND shiftManagerID = ? AND permissionName = ?");
                    stmt.setString(1, perm.getDescription());
                    stmt.setString(2, dailyShift.getDate().toString());
                    stmt.setInt(3, shiftManager.getShiftSlot());
                    stmt.setInt(4, (Integer) id);
                    stmt.setString(5, shiftManager.getId());
                    stmt.setString(6, perm.getName());
                    stmt.executeUpdate();
                }

                // update cancellations
                for (Cancellation cancel : shiftManager.getCancelations()) {
                    stmt = conn.prepareStatement("INSERT INTO Cancellations (shiftDate, shiftManagerId, cancelID, item, amount) VALUES (?,?,?,?,?)");
                    stmt.setString(1, dailyShift.getDate().toString());
                    stmt.setString(2, shiftManager.getId());
                    stmt.setInt(3, cancel.getCancelID());
                    stmt.setString(4, cancel.getItem());
                    stmt.setInt(5, cancel.getAmount());
                    stmt.executeUpdate();
                }
            }
            networkDailyShift.put(dailyShift.getDate().toString()+id, dailyShift);
        }
    }

    public void addToShift(LocalDate date, int shiftSlot, String eID, int roleNum, int bID) {
        PreparedStatement stmt;
        try {
            if (shiftSlot == 0) // morning
            {
                stmt = conn.prepareStatement("INSERT INTO MorningShiftEmployees (date, employeeID, branchID, role) VALUES (?,?,?,?)");
                stmt.setString(1, date.toString());
                stmt.setString(2, eID);
                stmt.setInt(3, bID);
                stmt.setInt(4, roleNum);
                stmt.executeUpdate();

            }
            else if (shiftSlot == 1) // evening
            {
                stmt = conn.prepareStatement("INSERT INTO EveningShiftEmployees (date, employeeID, branchID, role) VALUES (?,?,?,?)");
                stmt.setString(1, date.toString());
                stmt.setString(2, eID);
                stmt.setInt(3, bID);
                stmt.setInt(4, roleNum);
                stmt.executeUpdate();
            }
        }catch (SQLException s) {
            s.printStackTrace();
        }


    }

    public void removefromShift(LocalDate date, int shiftSlot, String eID, int roleNum, int bID){
        PreparedStatement stmt;
        try {
            if (shiftSlot == 0) // morning
            {
                stmt = conn.prepareStatement("DELETE FROM MorningShiftEmployees WHERE date = ? AND employeeID = ? AND branchID = ? AND role = ?");
                stmt.setString(1, date.toString());
                stmt.setString(2, eID);
                stmt.setInt(3, bID);
                stmt.setInt(4, roleNum);
                stmt.executeUpdate();

            }
            else if (shiftSlot == 1) // evening
            {
                stmt = conn.prepareStatement("DELETE FROM EveningShiftEmployees WHERE date = ? AND employeeID = ? AND branchID = ? AND role = ?");
                stmt.setString(1, date.toString());
                stmt.setString(2, eID);
                stmt.setInt(3, bID);
                stmt.setInt(4, roleNum);
                stmt.executeUpdate();
            }
        }catch (SQLException s) {
            s.printStackTrace();
        }

    }

    /**
     * Delete a DailyShift from DB
     * @param date of the shift
     * @param id of the branch
     * @throws SQLException in case of any problem
     * @throws ClassNotFoundException in case of any problem
     */
    @Override
    public void delete(Object date, Object id) throws SQLException, ClassNotFoundException {
        DailyShift dailyShift = (DailyShift) findByKey(date,id);
        if (dailyShift != null) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM DailyShifts WHERE date = ? AND branchID = ?");
            stmt.setString(1, dailyShift.getDate().toString());
            stmt.setString(2, (String) id);
            stmt.executeQuery();

            // delete employees
            for (Map.Entry<Role, ArrayList<Employee>> shift : dailyShift.getMorningShift().entrySet()) {
                for (Employee e : shift.getValue()) {
                    stmt = conn.prepareStatement("DELETE FROM MorningShiftEmployees WHERE date = ? AND employeeID = ?");
                    stmt.setString(1, dailyShift.getDate().toString());
                    stmt.setString(2, e.getId());
                    stmt.executeQuery();
                }
            }
            for (Map.Entry<Role, ArrayList<Employee>> shift : dailyShift.getEveningShift().entrySet()) {
                for (Employee e : shift.getValue()) {
                    stmt = conn.prepareStatement("DELETE FROM EveningShiftEmployees WHERE date = ? AND employeeID = ?");
                    stmt.setString(1, dailyShift.getDate().toString());
                    stmt.setString(2, e.getId());
                    stmt.executeQuery();
                }
            }
            // delete shift managers
            for (ShiftManager shiftManager : dailyShift.getShiftManagers()) {
                stmt = conn.prepareStatement("DELETE FROM ShiftManagers WHERE shiftDate = ? AND shiftManagerID = ?");
                stmt.setString(1, dailyShift.getDate().toString());
                stmt.setString(2, shiftManager.getId());
                stmt.executeQuery();

                // delete permissions
                for (ShiftM_Permissions perm : shiftManager.getPermissions()) {
                    stmt = conn.prepareStatement("DELETE FROM ShiftM_Permissions WHERE shiftDate = ? AND shiftManagerID = ?");
                    stmt.setString(1, dailyShift.getDate().toString());
                    stmt.setString(2, shiftManager.getId());
                    stmt.executeQuery();
                }

            }
        }
        networkDailyShift.remove(date.toString() + id);
    }
}

