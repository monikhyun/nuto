package goorm.nuto.Nuto.Exception;

public class NotFoundConsumeException extends BusinessException {
    public NotFoundConsumeException() {
        super(ErrorCode.NOT_FOUND_CONSUME,"소비를 찾지 못했습니다.");
    }
}
