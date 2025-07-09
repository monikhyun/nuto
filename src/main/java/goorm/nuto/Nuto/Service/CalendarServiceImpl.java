package goorm.nuto.Nuto.Service;

import goorm.nuto.Nuto.Dto.CalendarResponseDto;
import goorm.nuto.Nuto.Dto.CalendarSummaryDto;
import goorm.nuto.Nuto.Entity.Member;
import goorm.nuto.Nuto.Entity.Receipt;
import goorm.nuto.Nuto.Repository.ConsumeRepository;
import goorm.nuto.Nuto.Repository.ReceiptRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {
    private final ReceiptRepository receiptRepository;
    private final ConsumeRepository consumeRepository;

    @Override
    public List<CalendarResponseDto> getDailyConsumes(Member member, int year, int month){
        List<Receipt> receipts = receiptRepository.findExpenseReceiptsByMemberAndYearMonth(member, year, month);

        Map<LocalDate, List<Receipt>> grouped = receipts.stream()
                .collect(Collectors.groupingBy(Receipt::getDate));

        return grouped.entrySet().stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<Receipt> dailyReceipts = entry.getValue();
                    Long total = dailyReceipts.stream().mapToLong(Receipt::getPrice).sum();
                    List<String> shopNames = dailyReceipts.stream()
                            .map(Receipt::getShop_name)
                            .filter(Objects::nonNull)
                            .distinct()
                            .collect(Collectors.toList());

                    return new CalendarResponseDto(date, total, shopNames);
                })
                .sorted(Comparator.comparing(CalendarResponseDto::getDate))
                .collect(Collectors.toList());
    }

    @Override
    public CalendarSummaryDto getCalendarSummary(Long memberId, int year, int month) {
        Long thisMonthTotal = consumeRepository.findTotalAmountByMonth(memberId, year, month);
        Long lastMonthTotal = consumeRepository.findTotalAmountByMonth(memberId,
                month == 1 ? year - 1 : year,
                month == 1 ? 12 : month - 1);

        Long totalAmount = consumeRepository.findMaxDailyConsumeAmount(memberId, year, month);
        Long category = consumeRepository.findMaxDailyConsumeCategory(memberId, year, month);
        List<LocalDate> topDays = consumeRepository.findTopSpendDaysByAmount(memberId, year, month,totalAmount);
        List<String> topCategories = consumeRepository.findTopSpendDaysByCategory(memberId, year, month, category);

        return new CalendarSummaryDto(
                thisMonthTotal == null ? 0L : thisMonthTotal,
                lastMonthTotal == null ? 0L : lastMonthTotal,
                topDays.isEmpty() ? null : topDays,
                topCategories.isEmpty() ? null : topCategories
        );
    }
}
