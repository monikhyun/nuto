package goorm.nuto.Nuto.Exception;

public class NotFoundReceiptException extends BusinessException {
    public NotFoundReceiptException() {
        super(ErrorCode.NOT_FOUND_RECEIPT,"영수증을 찾지 못했습니다.");
    }
}
