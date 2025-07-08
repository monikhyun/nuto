package goorm.nuto.Nuto.Exception;

public class InvalidTokenException extends BusinessException {

  public InvalidTokenException(String message) {
    super(ErrorCode.INVALID_TOKEN, message);
  }
}
