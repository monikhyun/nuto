package goorm.nuto.Nuto.Service;

import goorm.nuto.Nuto.Dto.MonthlyReceiptDto;
import goorm.nuto.Nuto.Dto.ReceiptRequestDto;
import goorm.nuto.Nuto.Dto.ReceiptResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReceiptService {
    void saveReceipt(Long memberId, ReceiptRequestDto dto);

    ReceiptResponseDto getReceipt(Long memberId, Long receiptId);

    Page<ReceiptResponseDto> getReceiptListPage(int page, int size, Long memberId);

    Page<MonthlyReceiptDto> getMonthlyReceiptPage(int page, int size, Long memberId);
    Page<ReceiptResponseDto> getConsumeListPage(int page, int size, Long memberId, int year, int month);
    Page<ReceiptResponseDto> getIncomeListPage(int page, int size, Long memberId);

    void modifyReceipt(Long memberId, ReceiptRequestDto dto);

    void deleteReceipts(Long memberId,List<Long> receiptIds);
}
