package goorm.nuto.Nuto.Exception;

public class NotFoundIncomeException extends BusinessException {
  public NotFoundIncomeException() {
    super(ErrorCode.NOT_FOUND_INCOME,"수입을 찾지 못했습니다.");
  }
}
