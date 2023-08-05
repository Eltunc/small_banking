package az.azercell.smallbanking.exception;

public class UnknownTypeTransactionException extends CustomException {

    public UnknownTypeTransactionException() {
        super("400", "Transaction type does not find (DEPOSIT, PURCHASE)");
    }
}
