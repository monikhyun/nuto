package goorm.nuto.Nuto.Exception;

public class DuplicateCardNumberException extends BusinessException {
    public DuplicateCardNumberException() {
        super(ErrorCode.DUPLICATE_CARDNUMBER, "이미 등록된 카드 번호입니다.");
    }
}

