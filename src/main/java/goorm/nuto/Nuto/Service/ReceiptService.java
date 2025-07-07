package goorm.nuto.Nuto.Service;

import goorm.nuto.Nuto.Dto.ReceiptRequestDto;

public interface ReceiptService {
    void saveReceipt(Long memberId, ReceiptRequestDto dto);
}
