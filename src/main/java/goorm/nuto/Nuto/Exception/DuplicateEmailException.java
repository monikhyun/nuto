package goorm.nuto.Nuto.Exception;

public class DuplicateEmailException extends BusinessException {
    public DuplicateEmailException(String email) {
        super(ErrorCode.DUPLICATE_EMAIL, email + "은 이미 존재하는 이메일입니다");
    }
}
