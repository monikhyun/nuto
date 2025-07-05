package goorm.nuto.Nuto.Exception;

public class NotAuthorizedCardAccessException extends RuntimeException {
    public NotAuthorizedCardAccessException(String message) {
        super(message);
    }
}
