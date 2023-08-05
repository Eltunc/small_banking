package az.azercell.smallbanking.exception;

public class InsufficientBalanceException extends CustomException {
    public InsufficientBalanceException() {
        super("403 Forbidden","You don't have enough funds in your balance");
    }
}