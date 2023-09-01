package banking;

import java.sql.*;
import java.util.Scanner;

class Account extends Database {

    Account (String url) {
        super(url);
    }

    // log into account (select row)
    private int checkCredentials() throws BankingSystemError {
        Scanner sc = new Scanner(System.in);
        System.out.println("\nEnter your card number:");
        String cardNumber = sc.next();

        System.out.println("Enter your PIN:");
        String pin = sc.next();

        String sql = "SELECT * FROM card WHERE number = ? AND pin = ?";

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cardNumber);
            pstmt.setString(2, pin);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.isBeforeFirst()) {
                    System.out.println("\nYou have successfully logged in!\n");
                    return rs.getInt("id");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        throw new BankingSystemError("Wrong card number or PIN!\n");
    }

     boolean logIn() {
        Scanner sc = new Scanner(System.in);
        int id = 0;
        try {
            id = checkCredentials();
        } catch (BankingSystemError e) {
            System.out.println(e.getMessage());
        }

        boolean loggedIn = false;
        boolean systemOn = true;

         if (id > 0) {
             loggedIn = true;
         }

        while (loggedIn) {
            System.out.println("\n1. Balance\n" +
                                "2. Add income\n" +
                                "3. Do transfer\n" +
                                "4. Close account\n" +
                                "5. Log out\n" +
                                "0. Exit");
            int input = sc.nextInt();
            switch(input) {
                case 1:
                    System.out.println("\nBalance: " + getBalance(id));
                    break;
                case 2:
                    System.out.println("\nEnter income:");
                    int income = sc.nextInt();
                    addIncome(income, id);
                    break;
                case 3:
                    try {
                        doTransfer(id);
                    } catch (BankingSystemError e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    closeAccount(id);
                    loggedIn = false;
                    break;
                case 5:
                    System.out.println("\nYou have successfully logged out!\n");
                    loggedIn = false;
                    break;
                case 0:
                    System.out.println("\nBye!");
                    loggedIn = false;
                    systemOn = false;
                    break;
                default:
                    System.out.println("\nIncorrect input. Try again.\n");
                    break;
                }
        }
        return systemOn;
    }
}
