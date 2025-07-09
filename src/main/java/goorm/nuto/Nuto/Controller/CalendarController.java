package goorm.nuto.Nuto.Controller;

import goorm.nuto.Nuto.Dto.*;
import goorm.nuto.Nuto.Entity.Member;
import goorm.nuto.Nuto.Service.CalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
@Slf4j
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<CalendarResponseDto>>> getConsumeGraph(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @AuthenticationPrincipal CustomUserDetails user) {

        // 현재 날짜 기준으로 기본값 설정
        LocalDate now = LocalDate.now();
        int targetYear = (year == null) ? now.getYear() : year;
        int targetMonth = (month == null) ? now.getMonthValue() : month;

        Member member = user.getMember();
        List<CalendarResponseDto> dto = calendarService.getDailyConsumes(member, targetYear, targetMonth);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @GetMapping("/calendar-summary")
    public ResponseEntity<ApiResponse<CalendarSummaryDto>> getCalendarSummary(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getId();

        LocalDate now = LocalDate.now();
        int targetYear = (year == null) ? now.getYear() : year;
        int targetMonth = (month == null) ? now.getMonthValue() : month;

        CalendarSummaryDto summary = calendarService.getCalendarSummary(memberId, targetYear, targetMonth);
        return ResponseEntity.ok(ApiResponse.success(summary));
    }
}
