package BussinesLogic;

import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

/**
 * Daily Shift class
 * creates to set shifts for the system
 */
public class DailyShift {

    private LocalDate date; // date of the shift
    private File endOfDayReport; // file of "end-of-day-report" for the shift manager's ability to create/update/get
    private Map<Role, ArrayList<Employee>> morningShift; // list of employees which work at morning shift
    private Map<Role, ArrayList<Employee>> eveningShift; // list of employees which work at evening shift
    private List<ShiftManager> shiftManagers; // list of shift managers of a specific shift

    //constructor
    public DailyShift(LocalDate date) {
        this.date = date;
        this.morningShift = new HashMap<Role,ArrayList<Employee>>();
        this.eveningShift = new HashMap<Role,ArrayList<Employee>>();
        this.shiftManagers= new ArrayList<ShiftManager>();
        this.endOfDayReport = new File("");
    }

    //getters
    public LocalDate getDate() {
        return date;
    }
    public Map<Role, ArrayList<Employee>> getMorningShift() {
        return morningShift;
    }
    public Map<Role, ArrayList<Employee>> getEveningShift() {
        return eveningShift;
    }
    public File getEndOfDayReport() {
        return endOfDayReport;
    }
    public List<ShiftManager> getShiftManagers() {return shiftManagers;}

    //setters
    public void setMorningShift(Map<Role, ArrayList<Employee>> morningShift) {
        this.morningShift = morningShift;
    }
    public void setEveningShift(Map<Role, ArrayList<Employee>> eveningShift) {
        this.eveningShift = eveningShift;
    }
    public void setEndOfDayReport(File endOfDayReport) {
        this.endOfDayReport = endOfDayReport;
    }
    public void addShiftManager(ShiftManager shiftManager) {
        this.shiftManagers.add(shiftManager);
    }
    public void removeShiftManager(ShiftManager shiftManager) {
        this.shiftManagers.remove(shiftManager);
    }


    /*change shift
     * remove from shift
     * remove from morning shift
     */
    public boolean removeEmployeeFromMorning(Employee employee, Role role)
    {
        boolean removed = this.morningShift.get(role).remove(employee);

        if (this.morningShift.get(role).size() == 0)
            this.morningShift.remove(role);
        return removed;
    }

    //remove from evening shift
    public boolean removeEmployeeFromEvening(Employee employee, Role role)
    {
        boolean removed =this.eveningShift.get(role).remove(employee);

        if (this.eveningShift.get(role).size() == 0)
            this.eveningShift.remove(role);
        return removed;
    }
    //according to shift this function refers to the right helper

    /**
     * Remove employee from shift - a specific shift
     * @param employee to remove
     * @param role he acts
     * @param shift he presents
     */
    public void removeEmployeeFromShift(Employee employee, Role role, int shift)
    {
        //if the employee is a shift manager
        if(role == Role.SHIFTMANAGER)
        {
            ShiftManager shiftManager = findEmployeeInShiftManager(employee.getId());
            if(shiftManager != null)
                removeShiftManager(shiftManager);
        }

        boolean removedFromShift;
        // means the employee is in morning shift
        if(shift == 0)
            removedFromShift =removeEmployeeFromMorning(employee,role);
        else
            removedFromShift = removeEmployeeFromEvening(employee, role);

        //only if removed
        if(removedFromShift)
        {
            //update limit
            employee.setShiftsLimit(employee.getShiftsLimit()+1);
            //update salary
            employee.setCumulativeSalary(employee.getCumulativeSalary() - employee.getSalary());
        }

    }

    /* add to shift
     * remove from morning shift
     */
    public boolean addEmployeeToMorning(Employee employee, Role role)
    {
        if(!this.morningShift.containsKey(role))
            this.morningShift.put(role, new ArrayList<>());
        return this.morningShift.get(role).add(employee);

    }

    //remove from evening shift
    public boolean addEmployeeToEvening(Employee employee, Role role)
    {
        if(!this.eveningShift.containsKey(role))
            this.eveningShift.put(role, new ArrayList<>());
        return this.eveningShift.get(role).add(employee);
    }

    //according to shift this function refers to the right helper
    public boolean addEmployeeToShift(Employee employee, Role role, int shift)
    {
        if(employee.canDoRole(role) && !isExistEvening(employee) && !isExistMorning(employee))
        {
            //if the employee is a shift manager
            if(role == Role.SHIFTMANAGER)
            {
                ShiftManager shiftManager = ShiftManagerGenerator.CreateShiftManager(employee.getName(), employee.getId(), LocalDate.now(),shift);
                addShiftManager(shiftManager);
            }

            boolean addToShift;
            if(shift == 0)
                addToShift = addEmployeeToMorning(employee,role);
            else
                addToShift=addEmployeeToEvening(employee, role);

            //only if added
            if(addToShift)
            {
                //update limit
                employee.setShiftsLimit(employee.getShiftsLimit()-1);
                //update salary
                employee.setCumulativeSalary(employee.getCumulativeSalary() + employee.getSalary());
            }
            return addToShift;
        }
        return false;
    }

    /**
     * Search a shift manager in a specific shift
     * @param ID of the employee (who is shift manager)
     * @return the shift manager if existed, else just null
     */
    public ShiftManager findEmployeeInShiftManager(String ID)
    {
        for (ShiftManager shiftManager : shiftManagers) {
            if (Objects.equals(shiftManager.getId(), ID)) {
                return shiftManager;
            }
        }
        return null;
    }

    /**
     * Print the shift by slots (morning/evening) and roles with employees
     */
    public void showMeSchedualing()
    {
        int count=0;
        DayOfWeek dayOfWeek=this.date.getDayOfWeek();
        System.out.println("Daily shift - "+ dayOfWeek + " " + this.date+"\nMORNING:");
        Map<Role, ArrayList<Employee>> sortedMap = new TreeMap<>(morningShift);
        print(sortedMap,count);
        System.out.println("\nEVENING:");
        sortedMap = eveningShift;
        print(sortedMap,count);
    }
    private void print(Map<Role, ArrayList<Employee>> sortedMap, int count)
    {
        for (Map.Entry<Role, ArrayList<Employee>> entry : sortedMap.entrySet()) {
            Role key = entry.getKey();
            count = 1;
            System.out.println(key.name()+":");
            for (Employee employee : sortedMap.get(key))
            {
                System.out.println(count+++". "+employee.getName());
            }
        }
    }

    /**
     * checks if employee is in morning shift
     * @param e : employee to search for
     * @return true if in shift
     */
    public boolean isExistMorning(Employee e)
    {
        for(Map.Entry<Role,ArrayList<Employee>> employees : morningShift.entrySet())
            for (Employee employee : employees.getValue())
            {
                if(employee == e)
                    return true;
            }
        return false;
    }

    /**
     * checks if employee is in evening shift
     * @param e : employee to search for
     * @return true if in shift
     */
    public boolean isExistEvening(Employee e)
    {
        for(Map.Entry<Role,ArrayList<Employee>> employees : eveningShift.entrySet())
            for (Employee employee : employees.getValue())
            {
                if(employee == e)
                    return true;
            }
        return false;
    }

    /**
     * find employee in shift
     */
    public Employee isEmployeeInShift(String employeeID)
    {
        //go over morning shift and find employee
        for(Map.Entry<Role,ArrayList<Employee>> employees : morningShift.entrySet())
            for (Employee employee : employees.getValue())
            {
                if(employee.getId().equals(employeeID))
                    return employee;
            }
        //go over evening shift and find employee
        for(Map.Entry<Role,ArrayList<Employee>> employees : eveningShift.entrySet())
            for (Employee employee : employees.getValue())
            {
                if(employee.getId().equals(employeeID))
                    return employee;
            }
        return null;
    }

    /**
     * find employees role in shift
     * @param e : employee
     */
    public Role getEmployeeRoleInShift(Employee e)
    {
        //go over morning shift
        for(Role role: this.morningShift.keySet())
        {
            for(Employee employee: this.morningShift.get(role))
                if(employee == e)
                    return role;
        }
        for(Role role: this.eveningShift.keySet())
        {
            for(Employee employee: this.eveningShift.get(role))
                if(employee == e)
                    return role;
        }
        return null;
    }

    /**
     *
     * @return if there is a storekeeper in each shift then returns true
     */
    public boolean storeKeepersInDailyShift()
    {
        return (morningShift.containsKey(Role.STORAGE)&&eveningShift.containsKey(Role.STORAGE));
    }
}
