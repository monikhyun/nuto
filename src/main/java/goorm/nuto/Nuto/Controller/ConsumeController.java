package goorm.nuto.Nuto.Controller;

import com.cloudinary.Api;
import goorm.nuto.Nuto.Dto.*;
import goorm.nuto.Nuto.Entity.CategoryType;
import goorm.nuto.Nuto.Service.ConsumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/consumption")
@RequiredArgsConstructor
public class ConsumeController {
    private final ConsumeService consumeService;

    //영수증 등록
    @PostMapping("/receipt/record")
    @Operation(summary = "영수증 등록", description = "영수증 등록 api입니다.")
    public ResponseEntity<ApiResponse<String>> createReceipt(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute ConsumeRequestDto dto) {

        Long memberId = userDetails.getMember().getId();
        consumeService.saveReceipt(memberId, dto);
        return ResponseEntity.ok(ApiResponse.success("영수증 등록 성공"));
    }

    // 전체 데이터 조회
    @GetMapping("/")
    @Operation(summary = "전체 데이터 조회", description = "소비 및 지출 기록 내역을 모두 조회합니다.")
    public ResponseEntity<ApiResponse<List<RecordResponseDto>>> getConsumeList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                               @RequestParam("category") CategoryType categoryType) {
        return ResponseEntity.ok(ApiResponse.success(consumeService.getReceipts(userDetails.getMember(), categoryType)));
    }
    // 기록 삭제
    @DeleteMapping("/remove")
    @Operation(summary = "영수증 기록 삭제", description = "선택한 영수증 기록을 삭제합니다.")
    public ResponseEntity<ApiResponse<String>> removeConsume(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @RequestBody RecordRemoveRequestDto recordRemoveRequestDto) {
        consumeService.deleteReceipt(userDetails.getMember(), recordRemoveRequestDto);
        return ResponseEntity.ok(ApiResponse.success("영수증 기록 삭제 완료"));
    }
    // 소비 기록 시 카드 목록 조회
    @GetMapping("/receipt/record/cards")
    @Operation(summary = "영수증 기록 화면 내 등록카드 목록", description = "사용자가 등록한 카드 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<CardNameResponseDto>>> getReceiptCards(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(ApiResponse.success(consumeService.getCardNames(userDetails.getMember())));
    }
    // 월별 데이터 조회
    @GetMapping("/{category}/{date}")
    @Operation(summary = "해당 월 기록 데이터 조회", description = "선택한 월에 해당하는 모든 기록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<RecordResponseDto>>> getMonthlyReceipts(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                   @PathVariable String category,
                                                                                   @PathVariable YearMonth date) {
        CategoryType categoryType;
        try {
            categoryType = CategoryType.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("유효하지 않은 category 값입니다. (EXPENSE 또는 INCOME)"));
        }
        return ResponseEntity.ok(ApiResponse.success(consumeService.getMonthlyReceipts(userDetails.getMember(), date, categoryType)));
    }
    // 기록 수정
    @PutMapping("/receipt/update")
    @Operation(summary = "영수증 기록 수정", description = "영수증 기록 수정 기능")
    public ResponseEntity<ApiResponse<String>> updateReceipt(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @ModelAttribute RecordRequestDto recordRequestDto) {
        consumeService.updateReceipt(userDetails.getMember(), recordRequestDto);
        return ResponseEntity.ok(ApiResponse.success("수정을 완료했습니다."));
    }
}
