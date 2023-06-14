package Presentation;

import CLI_Layer.PresentationCli;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String choice = "";
        while (choice != "3") {
            System.out.println("-----Main Menu-----");
            System.out.println("1. HR Menu");
            System.out.println("2. Transits Menu");
            System.out.println("3. Exit");
            choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    HR_Main.system();
                    break;
                case "2":
                    PresentationCli.transitSystemCli();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }
}
