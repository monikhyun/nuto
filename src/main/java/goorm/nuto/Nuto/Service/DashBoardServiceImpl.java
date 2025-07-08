package goorm.nuto.Nuto.Service;

import goorm.nuto.Nuto.Dto.*;
import goorm.nuto.Nuto.Entity.Card;
import goorm.nuto.Nuto.Entity.Consume;
import goorm.nuto.Nuto.Entity.Member;
import goorm.nuto.Nuto.Repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DashBoardServiceImpl implements DashBoardService {
    private final CardRepository cardRepository;
    private final IncomeRepository incomeRepository;
    private final ConsumeRepository consumeRepository;

    // 카드 정보 조회
    @Override
    public List<CardResponseDto> getCards(Member member) {
        Optional<List<Card>> cards = cardRepository.findByMember(member);
        List<CardResponseDto> cardResponseDtos = new ArrayList<>();

        if (cards.isPresent()) {
            for (Card card : cards.get()) {
                CardResponseDto cardResponseDto = CardResponseDto.builder()
                        .cardNumber(card.getCardNumber())
                        .totalAmount(card.getTotalAmount())
                        .cardType(card.getCardType())
                        .expiryDate(card.getExpiryDate())
                        .build();

                cardResponseDtos.add(cardResponseDto);
            }
        }

        return cardResponseDtos;
    }

    // 올해 내 소비 조회
    @Override
    public YearlyConsumeResponseDto getYearlyConsume(Member member) {
        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();

        // 월별 소비 합계 조회
        List<Object[]> rawResults = consumeRepository.findMonthlyAmountByMemberAndYear(member, currentYear);

        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>(Collections.nCopies(currentMonth, 0L));

        for (int month = 1; month <= currentMonth; month++) {
            labels.add(month + "월");
        }

        for (Object[] row : rawResults) {
            Integer month = (Integer) row[0];
            Long totalAmount = (Long) row[1];
            if (month >= 1 && month <= currentMonth) {
                data.set(month - 1, totalAmount); // 인덱스 보정
            }
        }

        return YearlyConsumeResponseDto.builder()
                .labels(labels)
                .data(data)
                .build();
    }

    // 소비 그래프 조회
    @Override
    public ConsumeGraphResponseDto getConsumeGraph(Member member) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int today = now.getDayOfMonth();

        List<Long> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>(Collections.nCopies(today, 0L)); // 0원으로 초기화

        for (int i = 1; i <= today; i++) {
            labels.add((long) i);
        }

        List<Object[]> results = consumeRepository.findDailyAmountByMemberAndYearAndMonth(member, year, month);

        for (Object[] row : results) {
            Integer day = (Integer) row[0];
            Long totalAmount = (Long) row[1];
            if (day >= 1 && day <= today) {
                data.set(day - 1, totalAmount);
            }
        }

        return ConsumeGraphResponseDto.builder()
                .labels(labels)
                .data(data)
                .build();
    }

    // 이번달 내 소비 조회
    @Override
    public CurrentMonthExpenseResponseDto getCurrentMonthExpense(Member member) {
        LocalDate today = LocalDate.now();
        int dayOfMonth = today.getDayOfMonth();

        // 이번달 1일부터 오늘까지
        LocalDate thisMonthStart = today.withDayOfMonth(1);
        LocalDate thisMonthEnd = today;

        // 지난달 전 달의 1일부터 오늘 날짜의 일까지
        LocalDate lastMonth = today.minusMonths(1);
        LocalDate lastMonthStart = lastMonth.withDayOfMonth(1);
        LocalDate lastMonthEnd = lastMonth.withDayOfMonth(Math.min(dayOfMonth, lastMonth.lengthOfMonth()));

        Long thisMonthExpense = consumeRepository.getTotalAmountBetweenDates(member, thisMonthStart, thisMonthEnd);
        Long lastMonthExpense = consumeRepository.getTotalAmountBetweenDates(member, lastMonthStart, lastMonthEnd);

        return CurrentMonthExpenseResponseDto.builder()
                .thisMonthExpense(thisMonthExpense)
                .lastMonthExpense(lastMonthExpense)
                .build();
    }

    // 이번달 내 수익 조회
    @Override
    public CurrentMonthIncomeResponseDto getCurrentMonthIncome(Member member) {
        LocalDate today = LocalDate.now();
        int dayOfMonth = today.getDayOfMonth();

        // 이번달
        LocalDate thisMonthStart = today.withDayOfMonth(1);
        LocalDate thisMonthEnd = today;

        // 지난달
        LocalDate lastMonth = today.minusMonths(1);
        LocalDate lastMonthStart = lastMonth.withDayOfMonth(1);
        LocalDate lastMonthEnd = lastMonth.withDayOfMonth(Math.min(dayOfMonth, lastMonth.lengthOfMonth()));

        Double thisMonthIncome = incomeRepository.getTotalIncomeBetweenDates(member, thisMonthStart, thisMonthEnd);
        Double lastMonthIncome = incomeRepository.getTotalIncomeBetweenDates(member, lastMonthStart, lastMonthEnd);

        return CurrentMonthIncomeResponseDto.builder()
                .thisMonthIncome(thisMonthIncome.longValue())
                .lastMonthIncome(lastMonthIncome.longValue())
                .build();
    }

    // 한달 소비 내역 조회
    @Override
    public List<MonthlyConsumeResponseDto> getMonthlyConsume(Member member) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        Pageable limit4 = PageRequest.of(0, 4); // 상위 4개만
        List<Consume> consumes = consumeRepository
                .findTop4ByMemberAndYearAndMonthOrderByDateDesc(member, year, month, limit4);

        return consumes.stream()
                .map(consume -> MonthlyConsumeResponseDto.builder()
                        .name(consume.getName())
                        .price(consume.getAmount())
                        .cardType(consume.getCard().getCardType())
                        .date(consume.getDate())
                        .category(consume.getCategory())
                        .build())
                .toList();
    }

    // 카테고리별 소비 조회
    @Override
    public CategoryConsumeResponseDto getCategoryConsume(Member member) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        List<Object[]> rawResults = consumeRepository.findCategoryAmountThisMonth(member, year, month);

        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();
        long totalAmount = 0L;

        // 전체 소비 합계 계산
        for (Object[] row : rawResults) {
            Long amount = (Long) row[1];
            totalAmount += amount;
        }

        long otherTotal = 0L;

        for (int i = 0; i < rawResults.size(); i++) {
            String categoryName = (String) rawResults.get(i)[0];
            Long amount = (Long) rawResults.get(i)[1];

            if (i < 3) {
                labels.add(categoryName);
                long percent = Math.round((amount * 100.0) / totalAmount);
                data.add(percent);
            } else {
                otherTotal += amount;
            }
        }

        // 기타 항목 추가
        if (otherTotal > 0) {
            labels.add("기타");
            long otherPercent = Math.round((otherTotal * 100.0) / totalAmount);
            data.add(otherPercent);
        }

        return CategoryConsumeResponseDto.builder()
                .labels(labels)
                .data(data)
                .totalMount(totalAmount)
                .build();
    }
}
