package Presentation;

import BussinesLogic.Days;
import BussinesLogic.Driver;
import BussinesLogic.Employee;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Scanner;

/**
 * this class is in charge of all things regarding employee constraints
 * gets their constraints and can update them
 * this class is used as an interface between the system and the employee
 */
public class EmployeeConstraints {

    /**
     * This function check if the driver can take the transit
     * @param date of the transit
     * @param d Driver
     * @return true or false depends on his answer
     */
    public static boolean checkDriverAvailabilityForDate(LocalDate date, Driver d){
        return d.isAvailableOnDate(date);
    }

    /**
     * Ask every employee about their constraints and updates them
     * if they are available, updates to true and if not then false
     */
    public static void askForConstraints(Employee e)
    {
        Scanner scanner = new Scanner(System.in);
        String choice = "0";
        int counter = 0; // 7 days a week, include 0
        Days[] days = Days.values();

        System.out.println("Hello " + e.getName());
        while(counter < 7)
        {
            System.out.println("Please set your choices for " + days[counter]);
            System.out.println("1. Morning shift");
            System.out.println("2. Evening shift");
            System.out.println("3. Both");
            System.out.println("4. Neither");

            // Get user input
            System.out.print("Enter your choice: ");
            choice = scanner.nextLine();

            // Process the user's choice
            switch (choice) {
                case "1":
                    e.setConstrains(counter,0, true); // 0 means morning
                    e.setConstrains(counter,1, false);
                    break;
                case "2":
                    e.setConstrains(counter,1, true); // 1 means evening
                    e.setConstrains(counter,0, false);
                    break;
                case "3":
                    e.setConstrains(counter,0, true); // both OK
                    e.setConstrains(counter,1, true);
                    break;
                case "4":
                    e.setConstrains(counter,0, false);
                    e.setConstrains(counter,1, false);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    counter --;
            }
            counter ++;
        }

    }

    /**
     * gets an employee and asks them how to update their constraints
     * @param employee : employee to update
     */
    public static void updateConstraints(Employee employee)
    {
        Scanner scanner = new Scanner(System.in);
        String answer = "y";
        System.out.println("Hello "+employee.getName()+" enter the following to update your constraints");
        while (Objects.equals(answer, "y"))
        {
            try{
                System.out.println("Enter day you want to update (e.g Saturday): ");
                String day = scanner.nextLine();

                System.out.println("Enter which shift you want to update (morning - 0, evening - 1): ");
                String shift = scanner.nextLine();

                System.out.println("Enter availability (available - 0, unavailable - 1): ");
                String availability = scanner.nextLine();

                employee.setConstrains(Days.valueOf(day).ordinal(),Integer.parseInt(shift),(Integer.parseInt(availability) == 0));

                answer=scanner.nextLine();
                System.out.println("Would you like to change another constraint? (enter y/n)");
                answer = scanner.nextLine();
            }
            catch (Exception e)
            {
                System.out.println("Invalid input, please try again");
            }

        }
    }
}
