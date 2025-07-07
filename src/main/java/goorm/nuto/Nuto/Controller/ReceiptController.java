package goorm.nuto.Nuto.Controller;

import goorm.nuto.Nuto.Dto.ApiResponse;
import goorm.nuto.Nuto.Dto.ReceiptRequestDto;
import goorm.nuto.Nuto.Dto.CustomUserDetails;
import goorm.nuto.Nuto.Service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
public class ReceiptController {
    private final ReceiptService receiptService;

    //영수증 등록
    @PostMapping("/")
    public ResponseEntity<ApiResponse<String>> createReceipt(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ReceiptRequestDto dto) {

        Long memberId = userDetails.getMember().getId();

        receiptService.saveReceipt(memberId, dto);

        return ResponseEntity.ok(ApiResponse.success("영수증 등록 성공"));
    }
}
