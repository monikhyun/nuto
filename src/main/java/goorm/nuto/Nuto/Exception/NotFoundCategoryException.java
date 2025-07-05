package goorm.nuto.Nuto.Exception;

public class NotFoundCategoryException extends BusinessException {
    public NotFoundCategoryException() {
        super(ErrorCode.NOT_FOUND_CATEGORY,"카테고리를 찾지 못했습니다.");
    }
}
