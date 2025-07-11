package goorm.nuto.Nuto.Controller;

import goorm.nuto.Nuto.Dto.CategoryDto;
import goorm.nuto.Nuto.Dto.CustomUserDetails;
import goorm.nuto.Nuto.Dto.DailyConsumeDto;
import goorm.nuto.Nuto.Entity.Member;
import goorm.nuto.Nuto.Service.MainPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "MainPageController", description = "메인 페이지 관련 API")
public class MainPageController {

    private final MainPageService mainPageService;

    @Operation(summary = "카테고리별 월 소비 TOP3", description = "이번 달 소비가 많은 상위 3개 카테고리를 조회합니다.")
    @GetMapping("/top-categories")
    public List<CategoryDto> getTopCategories(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Member member = userDetails.getMember();
        return mainPageService.getCategory(member);
    }

    @Operation(summary = "날짜별 소비 내역", description = "날짜별 총 소비 금액과 카테고리명을 조회합니다.")
    @GetMapping("/daily-consumes")
    public List<DailyConsumeDto> getDailyConsumes(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Member member = userDetails.getMember();
        return mainPageService.getDailyConsume(member);
    }
}
