import BussinesLogic.Driver;
import BussinesLogic.Employee;
import BussinesLogic.EmployeeGenerator;
import DataAccess.DAO_Employee;
import DataAccess.DAO_Generator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class DAO_EmployeeTest {

    private DAO_Employee testDAOemployees;
    private Employee e;
    private Employee e1;

    @Before
    // test also insert and find
    public void setUp() {
        e = new Employee("person","test","123456","943-17689",
                780,"Salary - 780","2023-01-03");
        try {
            testDAOemployees = DAO_Generator.getEmployeeDAO();
            testDAOemployees.insert(e);
            Employee tmp = (Employee) testDAOemployees.findByID(e.getId()); // also test findByID
            assertEquals(e.getId(),tmp.getId());
        }
        catch (SQLException | ClassNotFoundException s){
            System.out.println(s);
        }
    }

    @Test
    public void update() {
        try {
            Employee e = (Employee) testDAOemployees.findByID("123456");
            assertNotNull(e);
            e.setShiftsLimit(5);
            testDAOemployees.update(e);
            Employee tmp = (Employee) testDAOemployees.findByID("123456");
            assertEquals(5,tmp.getShiftsLimit());
        }
        catch (SQLException s){
            System.out.println(s);
        }
    }

    @After
    public void delete() {
        try{
            Employee emTodelete = (Employee)testDAOemployees.findByID("123456");
            assertNotNull(emTodelete);
            testDAOemployees.delete(emTodelete);
            emTodelete = (Employee) testDAOemployees.findByID("123456");
            assertNull(emTodelete);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getNetworkEmployees() {

        try{
            testDAOemployees.update(e);
            assertTrue(testDAOemployees.getNetworkEmployees().contains(e));
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getNetworkDrivers() {
        try{
            e1 = new Employee("person","test","444333","943-17689",
                    780,"Salary - 780","2023-01-03");
            testDAOemployees.insert(e1);
            e1.setSalary(800);
            testDAOemployees.update(e1);
            Employee tmpE = (Employee) testDAOemployees.findByID("444333");
            assertNotNull(tmpE);
            EmployeeGenerator employeeGenerator = new EmployeeGenerator();
            Driver driver = employeeGenerator.CreateDriver(tmpE);
            assertNotNull(driver);
            testDAOemployees.update(driver);
            assertTrue(testDAOemployees.getNetworkDrivers().contains(driver));
            testDAOemployees.delete(e1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}