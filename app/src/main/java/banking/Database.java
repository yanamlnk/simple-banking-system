package banking;

import java.sql.*;
import java.util.Scanner;

class Database {
    String url;

    Database (String url) {
        this.url = url;
    }

    // create table
    void createNewTable() {

        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "	id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "	number TEXT,\n"
                + "	pin TEXT, \n"
                + "	balance INTEGER DEFAULT 0 \n"
                + ");";

        try (Connection conn = DriverManager.getConnection(this.url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // create account
    void createAccount() {
        String sql = "INSERT INTO card(number,pin) VALUES(?,?)";
        String number;
        String pin = Card.randomNumber(4);
        do {
            number = Card.generateCard();
        } while(isDuplicateCard(number));

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, number);
            pstmt.setString(2, pin);
            pstmt.executeUpdate();

            System.out.println("\nYour card has been created\nYour card number:\n" + number
                                + "\nYour card PIN:\n" + pin);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // true - there are duplicates, false - no duplicates
    boolean isDuplicateCard(String cardNumber) {
        String sql = "SELECT number FROM card WHERE number = ?";

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cardNumber);

            try (ResultSet rs = pstmt.executeQuery()) {
                //returns false if there are no rows in the ResultSet = no duplicate (nothing was selected)
                return rs.isBeforeFirst();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return true;
    }

    int getBalance(int id) {
        String sql = "SELECT balance FROM card WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.getInt("balance");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return -1;
    }

    void addIncome(int income, int id) {
        String sql = "UPDATE card SET balance = ? WHERE id = ?";
        int currentBalance = getBalance(id);
        if (currentBalance == -1) {
            return;
        }
        int newBalance = currentBalance + income;

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newBalance);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

            System.out.println("Income was added!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void doTransfer(int id) throws BankingSystemError {
        Scanner sc = new Scanner(System.in);
        System.out.println("\nTransfer\nEnter card number:");
        String recipientCard = sc.nextLine();
        String myCard = selectCard(id);
        if (myCard == null) {
            return;
        }

        if (myCard.equals(recipientCard)) {
            throw new BankingSystemError("You can't transfer money to the same account!");
        } else if (!Card.checkByLuhn(recipientCard)) {
            throw new BankingSystemError("Probably you made a mistake in the card number. Please try again!");
        } else if (!isDuplicateCard(recipientCard)) {
            throw new BankingSystemError("Such a card does not exist.");
        }

        System.out.println("Enter how much money you want to transfer:");
        int transfer = sc.nextInt();
        int currentBalance = getBalance(id);
        if (currentBalance == -1) {
            return;
        }

        if (transfer > currentBalance) {
            throw new BankingSystemError("Not enough money!");
        }

        if (transfer <= 0) {
            throw new BankingSystemError("Transfer must be higher than 0");
        }

        performTransfer(id, recipientCard, transfer);
        System.out.println("Success!");
    }

    void performTransfer(int id, String recipientCard, int transferAmount) {
        String sql = "SELECT id FROM card WHERE number = ?";
        int recipientId = 0;
        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, recipientCard);

            try (ResultSet rs = pstmt.executeQuery()) {
                recipientId =  rs.getInt("id");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        int recipientBalanceAfterTransfer = getBalance(recipientId) + transferAmount;
        int ownerBalanceAfterTransfer = getBalance(id) - transferAmount;

        if (recipientId != 0) {
            String transferSQL = "UPDATE card SET balance = ? WHERE id = ?";
            try (Connection conn = DriverManager.getConnection(this.url)) {
                conn.setAutoCommit(false);

                try (PreparedStatement transfer = conn.prepareStatement(transferSQL);
                     PreparedStatement withdrawal = conn.prepareStatement(transferSQL)) {

                    transfer.setInt(1, recipientBalanceAfterTransfer);
                    transfer.setInt(2, recipientId);
                    transfer.executeUpdate();

                    withdrawal.setInt(1, ownerBalanceAfterTransfer);
                    withdrawal.setInt(2, id);
                    withdrawal.executeUpdate();

                    conn.commit();
                } catch (SQLException e) {
                    try {
                        conn.rollback();
                    } catch (SQLException excep) {
                        excep.printStackTrace();
                    }
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    String selectCard(int id) {
        String sql = "SELECT number FROM card WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.getString("number");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    void closeAccount(int id) {
        String sql = "DELETE FROM card WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            System.out.println("\nThe account has been closed!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
