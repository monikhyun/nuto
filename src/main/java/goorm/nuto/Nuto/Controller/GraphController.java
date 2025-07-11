package goorm.nuto.Nuto.Controller;

import goorm.nuto.Nuto.Dto.ApiResponse;
import goorm.nuto.Nuto.Dto.ConsumeGraphResponseDto;
import goorm.nuto.Nuto.Dto.CustomUserDetails;
import goorm.nuto.Nuto.Entity.Member;
import goorm.nuto.Nuto.Service.GraphService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/graph")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "GraphController", description = "그래프 관련 API")
public class GraphController {
    private final GraphService graphService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<ConsumeGraphResponseDto>> getConsumeGraph(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @AuthenticationPrincipal CustomUserDetails user) {

        // 현재 날짜 기준으로 기본값 설정
        LocalDate now = LocalDate.now();
        int targetYear = (year == null) ? now.getYear() : year;
        int targetMonth = (month == null) ? now.getMonthValue() : month;

        Member member = user.getMember();
        ConsumeGraphResponseDto consumeGraphResponseDto = graphService.getGraph(member, targetYear, targetMonth);
        return ResponseEntity.ok(ApiResponse.success(consumeGraphResponseDto));
    }

    //회원가입일 ~ 현재 날짜
    @GetMapping("/available-months")
    public ResponseEntity<ApiResponse<List<String>>> getAvailableMonths(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getId();
        List<String> months = graphService.getAvailableMonths(memberId);
        return ResponseEntity.ok(ApiResponse.success(months));
    }
}
