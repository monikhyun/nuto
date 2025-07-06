package goorm.nuto.Nuto.Service;

import goorm.nuto.Nuto.Dto.*;
import goorm.nuto.Nuto.Entity.Member;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DashBoardService {
    // 카드 정보 조회
    List<CardResponseDto> getCards(Member member);

    // 올해 내 소비 조회
    YearlyConsumeResponseDto getYearlyConsume(Member member);

    // 소비 그래프 조회
    ConsumeGraphResponseDto getConsumeGraph(Member member);

    // 이번달 내 소비 조회
    CurrentMonthExpenseResponseDto getCurrentMonthExpense(Member member);

    // 이번달 내 수익 조회
    CurrentMonthIncomeResponseDto getCurrentMonthIncome(Member member);
    // 지금 내 소비 조회

    // 한달 소비 내역 조회
    List<MonthlyConsumeResponseDto> getMonthlyConsume(Member member);
    // 카테고리별 소비 조회
    CategoryConsumeResponseDto getCategoryConsume(Member member);
}
