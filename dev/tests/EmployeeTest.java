import BussinesLogic.Employee;
import BussinesLogic.Role;
import org.junit.Test;

import static org.junit.Assert.*;

public class EmployeeTest {

    Employee e = new Employee("Yossi", "Cohen", "1111", "098762",500,"Salary: 500 per shift", "2022-11-12");

    /**
     * Set the constraints for sunday morning for an employee
     * Check its false
     */
    @Test
    public void setConstrains() {

        assertTrue(e.getConstrains()[0][0]);
        e.setConstrains(0,0,false);
        assertFalse(e.getConstrains()[0][0]);

    }

    /**
     * Set the employee's weekly limit to 5
     * At start its 6
     */
    @Test
    public void setShiftsLimit() {

        assertEquals(6,e.getShiftsLimit());
        e.setShiftsLimit(5);
        assertEquals(5,e.getShiftsLimit());
    }

    /**
     * Set the employee salary
     */
    @Test
    public void setSalary() {

        assertEquals(500, e.getSalary(), 0);
        e.setSalary(560);
        assertEquals(560, e.getSalary(), 0);
    }

    /**
     * Add a role to employee
     */
    @Test
    public void addRole() {

        assertFalse(e.canDoRole(Role.SHIFTMANAGER));
        e.addRole(Role.SHIFTMANAGER);
        assertTrue(e.canDoRole(Role.SHIFTMANAGER));
    }

}