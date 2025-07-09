package goorm.nuto.Nuto.Exception;

public class NotAuthorizedCardAccessException extends BusinessException {
    public NotAuthorizedCardAccessException(String message) {
        super(ErrorCode.NOT_AUTHORIZED_CARDACCESS,message);
    }
}
