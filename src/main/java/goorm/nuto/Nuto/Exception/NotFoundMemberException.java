package goorm.nuto.Nuto.Exception;

public class NotFoundMemberException extends BusinessException {
    public NotFoundMemberException() {
        super(ErrorCode.NOT_FOUND_MEMBER, "멤버를 찾지 못했습니다");
    }
}