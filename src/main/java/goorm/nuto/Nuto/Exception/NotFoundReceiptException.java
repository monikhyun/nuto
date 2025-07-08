package goorm.nuto.Nuto.Exception;

public class NotFoundReceiptException extends RuntimeException {
  public NotFoundReceiptException() {
    super("해당 영수증을 찾을 수 없습니다.");
  }
}
