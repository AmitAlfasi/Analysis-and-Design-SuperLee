package BussinesLogic;

import java.time.LocalDate;
import java.util.ArrayList;

public class Driver extends Employee{
    private ArrayList<License> licenses;
    private ArrayList<LocalDate> transitsDates;

    public Driver(String firstName, String lastName, String id, String bankAccount, double salary, String empTerms, String startDate) {
        super(firstName, lastName, id, bankAccount, salary, empTerms, startDate);
        licenses = new ArrayList<>();
        transitsDates = new ArrayList<>();
    }

    public void addLicense(License newLicense){licenses.add(newLicense);}
    public void removeLicense(License license){  licenses.remove(license);}

    //getters
    public ArrayList<License> getLicenses(){return this.licenses;}
    public ArrayList<LocalDate> getTransitsDates(){return transitsDates;}

    //functions on transit
    public void addTransitDate(LocalDate date){
        this.setCumulativeSalary(this.getCumulativeSalary() + getSalary());
        this.transitsDates.add(date);
    }
    public void removeTransitDate(LocalDate date){
        this.transitsDates.remove(date);
    }

    /**
     * if the driver is available on given date, if the driver doesn't have the date in the transit list
     * @param date date to check
     * @return true if available, and false if not
     */
    public boolean isAvailableOnDate(LocalDate date){return !this.transitsDates.contains(date);}

    /**
     * prints the drivers transits dates that are scheduled
     */
    public void printTransits()
    {
        System.out.print("Transit scheduled dates are: "+ this.getTransitsDates().toString()+"\n");
    }

    public void printEmployeeDetails()
    {
        System.out.println("- "+getName()+" -\nID: "+getId()+"\nStart Date: "+getStartDate()+
                "\nCumulative Salary: "+getCumulativeSalary()+ "\nShift Salary: "+getSalary()+"\nBank account: "+getBankAccount()+"\nQualifications: "+getQualifications().toString());
        printTransits();
    }
}
