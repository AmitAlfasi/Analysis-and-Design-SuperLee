import BussinesLogic.DailyShift;
import BussinesLogic.Employee;
import BussinesLogic.Role;
import BussinesLogic.ShiftManager;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class DailyShiftTest {

    private static DailyShift dailyShift;
    private static LocalDate date = LocalDate.now();
    private static ShiftManager shiftManager1, shiftManager2;
    private static Employee employee1, employee2;

    @BeforeClass
    public static void setUp()
    {
        shiftManager1 = new ShiftManager("Jason Bordello","1233",date,0 );
        shiftManager2 = new ShiftManager("Ninnet Tayeb", "11111", date, 1);
        employee1 = new Employee("Jason", "Bordello","1233", "651-12334",34.77,"Student", "2023-11-13");
        employee2 = new Employee("Alex", "Blue","5112", "781-7723",100.77,"General worker", "2023-09-13");
        dailyShift = new DailyShift(date);
        employee1.addRole(Role.CASHIER);
        employee2.addRole(Role.GENERAL);
        dailyShift.addShiftManager(shiftManager1);
        dailyShift.addShiftManager(shiftManager2);
    }
    @Test
    public void addShiftManager() {

        assertEquals(2,dailyShift.getShiftManagers().size());
    }

    @Test
    public void removeShiftManager() {

        dailyShift.removeShiftManager(shiftManager1);
        assertEquals(1,dailyShift.getShiftManagers().size());
        dailyShift.addShiftManager(shiftManager1);
    }

    @Test
    public void removeEmployeeFromShift() {

        dailyShift.addEmployeeToShift(employee1, Role.CASHIER,1);//evening
        dailyShift.addEmployeeToShift(employee2, Role.GENERAL, 0);//morning
        dailyShift.removeEmployeeFromShift(employee1,Role.CASHIER,1);//evening
        assertEquals(0,dailyShift.getEveningShift().size());
        assertEquals(1,dailyShift.getMorningShift().size());
    }

    @Test
    public void addEmployeeToShift() {
        dailyShift.addEmployeeToShift(employee1,Role.CASHIER,0);
        dailyShift.addEmployeeToShift(employee2, Role.GENERAL, 0);
        assertEquals(2,dailyShift.getMorningShift().size());
    }

    @Test
    public void isExistMorning() {
        dailyShift.addEmployeeToShift(employee1,Role.CASHIER,0);
        dailyShift.addEmployeeToShift(employee2, Role.GENERAL, 0);
        assertTrue(dailyShift.isExistMorning(employee1));
        dailyShift.removeEmployeeFromShift(employee1,Role.CASHIER,0);
        assertFalse(dailyShift.isExistMorning(employee1));
    }
}