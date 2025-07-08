package goorm.nuto.Nuto.Exception;

public class NotAuthorizedReceiptAccessException extends BusinessException {
    public NotAuthorizedReceiptAccessException(String message) {
        super(ErrorCode.NOT_AUTHORIZED_RECEIPTACCESS, message);
    }
}
