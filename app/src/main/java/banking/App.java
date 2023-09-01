package banking;

import java.util.Scanner;

public class App {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        String url = "jdbc:sqlite:banking.s3db";
        Account db = new Account(url);
        db.createNewTable();

        boolean systemOn = true;
        while(systemOn) {
            System.out.println("\n1. Create an account \n2. Log into account \n0. Exit");
            int input = sc.nextInt();
            switch(input) {
                case 1:
                    db.createAccount();
                    break;
                case 2:
                    systemOn = db.logIn();
                    break;
                case 0:
                    System.out.println("\nBye!");
                    systemOn = false;
                    break;
                default:
                    System.out.println("\nIncorrect input. Try again.\n");
                    break;
            }
        }
    }
}
