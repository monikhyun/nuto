package goorm.nuto.Nuto.Controller;

import goorm.nuto.Nuto.Dto.*;
import goorm.nuto.Nuto.Entity.Member;
import goorm.nuto.Nuto.Service.DashBoardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashBoardController {
    private final DashBoardService dashBoardService;

    // 카드 정보 조회
    @GetMapping("/cards")
    @Operation(summary = "카드 정보 조회", description = "등록된 카드 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<List<CardResponseDto>>> getCards(@AuthenticationPrincipal CustomUserDetails user) {
        Member member = user.getMember();
        List<CardResponseDto> cards = dashBoardService.getCards(member);
        return ResponseEntity.ok(ApiResponse.success(cards));
    }
    // 올해 내 소비 조회
    @GetMapping("/year")
    @Operation(summary = "올해 내 소비 조회", description = "올해의 내 월별 소비를 조회합니다.")
    public ResponseEntity<ApiResponse<YearlyConsumeResponseDto>> getYear(@AuthenticationPrincipal CustomUserDetails user) {
        Member member = user.getMember();
        YearlyConsumeResponseDto yearlyConsumeResponseDto = dashBoardService.getYearlyConsume(member);
        return ResponseEntity.ok(ApiResponse.success(yearlyConsumeResponseDto));
    }
    // 소비 그래프 조회
    @GetMapping("/consume/graph")
    @Operation(summary = "소비 그래프 조회", description = "소비 그래프 데이터를 조회합니다.")
    public ResponseEntity<ApiResponse<ConsumeGraphResponseDto>> getConsumeGraph(@AuthenticationPrincipal CustomUserDetails user) {
        Member member = user.getMember();
        ConsumeGraphResponseDto consumeGraphResponseDto = dashBoardService.getConsumeGraph(member);
        return ResponseEntity.ok(ApiResponse.success(consumeGraphResponseDto));
    }
    // 이번달 내 소비 조회
    @GetMapping("/consume/month")
    @Operation(summary = "이번달 내 소비 조회", description = "저번 달과 이번 달의 내 소비 금액을 조회합니다.")
    public ResponseEntity<ApiResponse<CurrentMonthExpenseResponseDto>> getCurrentMonthExpense(@AuthenticationPrincipal CustomUserDetails user) {
        Member member = user.getMember();
        CurrentMonthExpenseResponseDto currentMonthExpenseResponseDto = dashBoardService.getCurrentMonthExpense(member);
        return ResponseEntity.ok(ApiResponse.success(currentMonthExpenseResponseDto));
    }
    // 이번달 내 수익 조회
    @GetMapping("/income/month")
    @Operation(summary = "이번달 내 수익 조회", description = "저번 달과 이번 달의 내 수익 금액을 조회합니다.")
    public ResponseEntity<ApiResponse<CurrentMonthIncomeResponseDto>> getCurrentMonthIncome(@AuthenticationPrincipal CustomUserDetails user) {
        Member member = user.getMember();
        CurrentMonthIncomeResponseDto currentMonthIncomeResponseDto = dashBoardService.getCurrentMonthIncome(member);
        return ResponseEntity.ok(ApiResponse.success(currentMonthIncomeResponseDto));
    }
    // 한달 소비 내역 조회
    @GetMapping("/monthly")
    @Operation(summary = "한달 소비 내역 조회", description = "이번달 최근 4개의 소비 내역을 조회합니다.")
    public ResponseEntity<ApiResponse<List<MonthlyConsumeResponseDto>>> getMonthlyConsume(@AuthenticationPrincipal CustomUserDetails user) {
        Member member = user.getMember();
        List<MonthlyConsumeResponseDto> monthlyConsumeResponseDtos = dashBoardService.getMonthlyConsume(member);
        return ResponseEntity.ok(ApiResponse.success(monthlyConsumeResponseDtos));
    }
    // 카테고리별 소비 조회
    @GetMapping("/consume/category")
    @Operation(summary = "카테고리별 내 소비 조회", description = "이번 달 상위 3개의 카테고리별 내 소비를 조회합니다.")
    public ResponseEntity<ApiResponse<CategoryConsumeResponseDto>> getCategoryConsume(@AuthenticationPrincipal CustomUserDetails user) {
        Member member = user.getMember();
        CategoryConsumeResponseDto categoryConsumeResponseDto = dashBoardService.getCategoryConsume(member);
        return ResponseEntity.ok(ApiResponse.success(categoryConsumeResponseDto));
    }

}
