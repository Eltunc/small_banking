package az.azercell.smallbanking.exception;

public class RefundExceedsPurchaseException extends CustomException {
    public RefundExceedsPurchaseException(String message) {
        super("400", message);
    }
}
