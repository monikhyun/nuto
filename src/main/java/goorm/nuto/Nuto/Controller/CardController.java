package goorm.nuto.Nuto.Controller;


import goorm.nuto.Nuto.Dto.ApiResponse;
import goorm.nuto.Nuto.Dto.CardDto;
import goorm.nuto.Nuto.Dto.CardResponseDto;
import goorm.nuto.Nuto.Dto.CustomUserDetails;
import goorm.nuto.Nuto.Service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    //카드 등록
    @PostMapping("/")
    public ResponseEntity<ApiResponse<String>> createCard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CardDto dto) {

        Long memberId = userDetails.getMember().getId();

        cardService.saveCard(memberId, dto);

        return ResponseEntity.ok(ApiResponse.success("카드 등록 성공"));
    }

    //카드 목록 화면
    @GetMapping("/")
    public  ResponseEntity<ApiResponse<List<CardResponseDto>>> getCardList(
            @AuthenticationPrincipal CustomUserDetails userDetails){
        Long memberId = userDetails.getMember().getId();

        List<CardResponseDto> data = cardService.getCardList(memberId);
        return ResponseEntity.ok(ApiResponse.success("카드 목록 조회 성공", data));
    }

    //카드 조회
    @GetMapping("/{cardId}")
    public ResponseEntity<ApiResponse<CardResponseDto>> getCard(
            @PathVariable("cardId") Long cardId,@AuthenticationPrincipal CustomUserDetails userDetails){
        Long memberId = userDetails.getMember().getId();

        CardResponseDto data = cardService.getCard(memberId, cardId);
        return ResponseEntity.ok(ApiResponse.success("카드 조회 성공", data));
    }

    @PostMapping("/modify/{cardId}")
    public ResponseEntity<ApiResponse<String>> modifyCard(
            @PathVariable("cardId") Long cardId, @RequestBody CardDto dto,@AuthenticationPrincipal CustomUserDetails userDetails){

        Long memberId = userDetails.getMember().getId();

        dto.setId(cardId);

        cardService.modifyCard(memberId, dto);

        return ResponseEntity.ok(ApiResponse.success("카드 수정 성공"));
    }

    @DeleteMapping("/delete/{cardId}")
    public ResponseEntity<ApiResponse<String>> deleteCard(
            @PathVariable("cardId") Long cardId,@AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getId();
        cardService.deleteCard(memberId,cardId);
        return ResponseEntity.ok(ApiResponse.success("카드 삭제 성공"));
    }

}
