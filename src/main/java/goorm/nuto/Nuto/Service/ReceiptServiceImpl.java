package goorm.nuto.Nuto.Service;

import goorm.nuto.Nuto.Dto.ReceiptRequestDto;
import goorm.nuto.Nuto.Entity.*;
import goorm.nuto.Nuto.Exception.NotFoundCardException;
import goorm.nuto.Nuto.Exception.NotFoundCategoryException;
import goorm.nuto.Nuto.Exception.NotFoundMemberException;
import goorm.nuto.Nuto.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReceiptServiceImpl implements ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final MemberRepository memberRepository;
    private final CardRepository cardRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public void saveReceipt(Long memberId, ReceiptRequestDto dto) {
        // 1) memberId 로 Member 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        Card card = cardRepository.findById(dto.getCardId())
                .orElseThrow(NotFoundCardException::new);

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(NotFoundCategoryException::new);

        // 2) Receipt 엔티티 생성 & 저장
        Receipt receipt = Receipt.builder()
                .name(dto.getName())
                .shop_name(dto.getShop_name())
                .date(dto.getDate())
                .price(dto.getPrice())
                .card(card)
                .category(category)
                .member(member)
                .build();

        receiptRepository.save(receipt);

    }
}
