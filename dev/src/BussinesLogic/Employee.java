package BussinesLogic;

import java.util.*;

/**
 * this class holds everything regarding the employee
 */
public class Employee {

    //variables
    private String firstName;
    private String lastName;
    private String id;
    private String bankAccount;
    private double salary;
    private String empTerms;
    private String startDate;
    private boolean[][] constrains; // [0,0] sunday morning, [0,1] sunday eve...
    private int shiftsLimit;
    private Collection<Role> qualifications;
    private double cumulativeSalary;

    //constructor
    public Employee(String firstName, String lastName, String id, String bankAccount, double salary, String empTerms, String startDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.bankAccount = bankAccount;
        this.salary = salary;
        this.cumulativeSalary =0;
        this.empTerms = empTerms;
        this.startDate = startDate;
        this.shiftsLimit = 6; // worker can only work 6 shifts a week
        this.constrains = new boolean[7][2]; //if worker can work in the morning or evening shift of each day
        this.qualifications=new ArrayList<Role>(); //employees qualifications to perform a role, description can be saved in details if required
        for(int i=0; i<7; i++)
        {
            for(int j=0; j<2; j++)
                this.constrains[i][j] = true;
        }
    }

    //getters
    public String getId() {return id;}
    public String getName() {return firstName+" "+lastName;}
    public String getFirstName(){return firstName;}
    public String getLastName() {return lastName;}
    public boolean[][] getConstraints() {return constrains;}
    public List<Role> getQualifications() {return (List<Role>) qualifications;}
    public int getShiftsLimit() {return shiftsLimit;}
    public String getBankAccount() {return bankAccount;}
    public double getSalary() {return salary;}
    public String getEmpTerms() {return empTerms;}
    public boolean[][] getConstrains() {return constrains;}
    public double getCumulativeSalary() {return cumulativeSalary;}
    public String getStartDate() {return startDate;}

    //setters
    public void setConstrains(int day, int shift,boolean limit)
    {
        if(limit){this.constrains[day][shift] = true;}
        else {this.constrains[day][shift] = false;}
    }
    public void setConstrains(boolean [][] constrains){this.constrains = constrains;}
    public void setShiftsLimit(int shiftsLimit) {this.shiftsLimit = shiftsLimit;}
    public void setBankAccount(String bankAccount) {this.bankAccount = bankAccount;}
    public void setSalary(double salary) {this.salary = salary;}
    public void setEmpTerms(String empTerms) {this.empTerms = empTerms;}
    public void setCumulativeSalary(double cumulativeSalary) {this.cumulativeSalary = cumulativeSalary;}

    //Employee Qualifications
    /**
     * add qualifications to this employee
     * @param newRole: new role that this employee can do in a shift
     */
    public void addRole(Role newRole){qualifications.add(newRole);}

    /**
     * as a result from removing the qualification, the employee cannot do this role in a shift
     * @param oldRole: remove this qualification from employee
     */
    public void removeRole(Role oldRole){qualifications.remove(oldRole);}

    /**
     * @param role: role to see if employee is qualified
     * @return true if employee can perform this role
     */
    public boolean canDoRole(Role role)
    {
        //go over employees roles
        for (Role qualification : this.qualifications)
            if (qualification.equals(role))
                return true;
        return false;
    }

    /**
     * prints all employees details without constraints
     */
    public void printEmployeeDetails()
    {
        System.out.println("- "+getName()+" -\nID: "+getId()+"\nStart Date: "+getStartDate()+
                "\nCumulative Salary: "+getCumulativeSalary()+ "\nShift Salary: "+getSalary()+"\nBank account: "+getBankAccount()+"\nQualifications: "+getQualifications().toString());
    }

    /**
     * prints employees constraints
     */
    public void printEmployeesConstraints()
    {
        for(Days days : Days.values())
        {
            System.out.println("- "+days.toString()+" -");
            System.out.println("Morning: "+constrains[days.ordinal()][0]+", Evening: "+constrains[days.ordinal()][1]);
        }

    }
}
