package goorm.nuto.Nuto.Exception;

public class NotVerifiedEmailException extends BusinessException {
  public NotVerifiedEmailException(String message) {
    super(ErrorCode.NOT_VERIFIED_EMAIL, message);
  }
}
