package goorm.nuto.Nuto.Controller;

import goorm.nuto.Nuto.Dto.*;
import goorm.nuto.Nuto.Service.CloudinaryService;
import goorm.nuto.Nuto.Service.ReceiptService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
@Tag(name = "ReceiptController", description = "영수증 관련 API")
public class ReceiptController {
    private final ReceiptService receiptService;
    private final CloudinaryService cloudinaryService;

    //영수증 등록
    @PostMapping(value = "/", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<String>> createReceipt(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("dto") ReceiptRequestDto dto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {

        Long memberId = userDetails.getMember().getId();

        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = cloudinaryService.uploadImage(imageFile);
            dto.setImageUrl(imageUrl);
        }

        receiptService.saveReceipt(memberId, dto);

        return ResponseEntity.ok(ApiResponse.success("영수증 등록 성공"));
    }

    //[수입+지출]영수증 목록 화면 (페이지네이션)
    @GetMapping("/monthly")
    public  ResponseEntity<ApiResponse<List<MonthlyReceiptDto>>> getMonthlyReceiptPage(
            @RequestParam(defaultValue = "0") int page, // 현재 페이지
            @AuthenticationPrincipal CustomUserDetails userDetails){

        int size = 4; // 크기

        Long memberId = userDetails.getMember().getId();

        Page<MonthlyReceiptDto> pagedata = receiptService.getMonthlyReceiptPage(page, size, memberId);

        List<MonthlyReceiptDto> data = pagedata.getContent();

        return ResponseEntity.ok(ApiResponse.success("영수증 목록 조회 성공", data));
    }


    //[지출]영수증 목록 화면 (페이지네이션)
    @GetMapping("/monthly/consume")
    public  ResponseEntity<ApiResponse<List<ReceiptResponseDto>>> getConsumeList(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(defaultValue = "0") int page, // 현재 페이지
            @AuthenticationPrincipal CustomUserDetails userDetails){

        int size = 6; // 크기

        Long memberId = userDetails.getMember().getId();

        Page<ReceiptResponseDto> pagedata = receiptService.getConsumeListPage(page, size, memberId, year, month);

        List<ReceiptResponseDto> data = pagedata.getContent();

        return ResponseEntity.ok(ApiResponse.success("영수증 목록 조회 성공", data));
    }

    //[수입]영수증 목록 화면 (페이지네이션)
    @GetMapping("/monthly/income")
    public  ResponseEntity<ApiResponse<List<ReceiptResponseDto>>> getIncomeList(
            @RequestParam(defaultValue = "0") int page, // 현재 페이지
            @AuthenticationPrincipal CustomUserDetails userDetails){

        int size = 6; // 크기

        Long memberId = userDetails.getMember().getId();

        Page<ReceiptResponseDto> pagedata = receiptService.getIncomeListPage(page, size, memberId);

        List<ReceiptResponseDto> data = pagedata.getContent();

        return ResponseEntity.ok(ApiResponse.success("영수증 목록 조회 성공", data));
    }

    //영수증 조회
    @GetMapping("/{receiptId}")
    public ResponseEntity<ApiResponse<ReceiptResponseDto>> getReceipt(
            @PathVariable("receiptId") Long receiptId,@AuthenticationPrincipal CustomUserDetails userDetails){
        Long memberId = userDetails.getMember().getId();

        ReceiptResponseDto data = receiptService.getReceipt(memberId, receiptId);
        return ResponseEntity.ok(ApiResponse.success("영수증 조회 성공", data));
    }

    @PostMapping("/modify/{receiptId}")
    public ResponseEntity<ApiResponse<String>> modifyReceipt(
            @PathVariable("receiptId") Long receiptId, @RequestBody ReceiptRequestDto dto, @AuthenticationPrincipal CustomUserDetails userDetails){

        Long memberId = userDetails.getMember().getId();

        dto.setId(receiptId);

        receiptService.modifyReceipt(memberId, dto);

        return ResponseEntity.ok(ApiResponse.success("영수증 수정 성공"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteReceipts(
            @RequestBody DeleteRequestDto dto,@AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getId();
        receiptService.deleteReceipts(memberId,dto.getIds());
        return ResponseEntity.ok(ApiResponse.success("영수증 삭제 성공"));
    }
}
