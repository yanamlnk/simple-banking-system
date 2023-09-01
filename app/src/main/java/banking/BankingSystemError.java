package banking;

class BankingSystemError extends Exception {
    BankingSystemError (String errorMessage) {
        super(errorMessage);
    }
}
