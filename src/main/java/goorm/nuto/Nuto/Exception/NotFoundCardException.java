package goorm.nuto.Nuto.Exception;

public class NotFoundCardException extends BusinessException {
    public NotFoundCardException() {
        super(ErrorCode.NOT_FOUND_CARD, "카드를 찾지 못했습니다");
    }
}
