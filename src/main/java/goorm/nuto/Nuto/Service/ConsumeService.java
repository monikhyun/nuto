package goorm.nuto.Nuto.Service;

import goorm.nuto.Nuto.Dto.ConsumeRequestDto;

public interface ConsumeService {
    void saveReceipt(Long memberId, ConsumeRequestDto dto);
}
