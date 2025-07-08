package goorm.nuto.Nuto.Service;

import goorm.nuto.Nuto.Dto.CardDto;
import goorm.nuto.Nuto.Dto.CardResponseDto;
import goorm.nuto.Nuto.Entity.*;
import goorm.nuto.Nuto.Exception.NotAuthorizedCardAccessException;
import goorm.nuto.Nuto.Exception.NotFoundCardException;
import goorm.nuto.Nuto.Exception.DuplicateCardNumberException;
import goorm.nuto.Nuto.Exception.NotFoundMemberException;
import goorm.nuto.Nuto.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final MemberRepository memberRepository;
    private final ReceiptRepository receiptRepository;
    private final ConsumeRepository consumeRepository;
    private final IncomeRepository incomeRepository;

    @Override
    public void saveCard(Long memberId, CardDto dto) {
        // 1) memberId 로 Member 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        if (cardRepository.findByMemberAndCardNumber(member, dto.getCardNumber()).isPresent()) {
            throw new DuplicateCardNumberException();
        }

        // 2) Card 엔티티 생성 & 저장
        Card card = Card.builder()
                .cardNumber(dto.getCardNumber())
                .cardType(CardType.valueOf(dto.getCardType()))
                .totalAmount(dto.getTotalAmount())
                .expiryDate(dto.getExpiryDate())
                .member(member)
                .build();

        cardRepository.save(card);

    }

    @Override
    public CardResponseDto getCard(Long memberId,Long cardId) {

        Card card = cardRepository.findById(cardId)
                .orElseThrow(NotFoundCardException::new);

        if (!card.getMember().getId().equals(memberId)) {
            throw new NotAuthorizedCardAccessException("해당 사용자의 카드가 아닙니다.");
        }

        CardResponseDto dto = CardResponseDto.builder()
                .cardNumber(card.getCardNumber())
                .totalAmount(card.getTotalAmount())
                .cardType(card.getCardType())
                .expiryDate(card.getExpiryDate())
                .build();

        return dto;

    }


    @Override
    public List<CardResponseDto> getCardList(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        List<Card> cards = cardRepository.findByMember(member)
                .orElseThrow(NotFoundCardException::new);

        return cards.stream()
                .map(card -> CardResponseDto.builder()
                        .cardNumber(card.getCardNumber())
                        .totalAmount(card.getTotalAmount())
                        .cardType(card.getCardType())
                        .expiryDate(card.getExpiryDate())
                        .build())
                .toList();

    }

    @Override
    public Page<CardResponseDto> getCardListPage(int page, int size, Long memberId) {

        Pageable pageable = PageRequest.of(page, size);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        Page<Card> cards = cardRepository.findByMemberOrderByIdDesc(member, pageable);

        return cards.map(CardResponseDto::new);

    }

    @Override
    public void modifyCard(Long memberId,CardDto dto){
        Card card = cardRepository.findById(dto.getId())
                .orElseThrow(NotFoundCardException::new);

        if (!card.getMember().getId().equals(memberId)) {
            throw new NotAuthorizedCardAccessException("해당 사용자의 카드가 아닙니다.");
        }

        if (dto.getCardNumber() != null) {
            card.setCardNumber(dto.getCardNumber());
        }

        if (dto.getCardType() != null) {
            card.setCardType(CardType.valueOf(dto.getCardType()));
        }

        if (dto.getExpiryDate() != null) {
            card.setExpiryDate(dto.getExpiryDate());
        }

        cardRepository.save(card);
    }

    @Override
    public void deleteCards(Long memberId, List<Long> cardIds) {
        // 기타 카드 조회
        Card otherCard = cardRepository.findByCardTypeAndMemberId(CardType.OTHER, memberId)
                .orElseThrow(() -> new RuntimeException("기타 카드가 존재하지 않습니다."));

        for(Long cardId : cardIds) {
            Card card = cardRepository.findById(cardId)
                    .orElseThrow(NotFoundCardException::new);

            if (!card.getMember().getId().equals(memberId)) {
                throw new NotAuthorizedCardAccessException("해당 사용자의 카드가 아닙니다.");
            }

            //card - receipt - consume/income 삭제
            List<Receipt> receipts = receiptRepository.findByCardId(cardId);

            for (Receipt receipt : receipts) {

                // 기타 카드로 교체
                receipt.setCard(otherCard);

                if (receipt.getCategory().getType() == CategoryType.EXPENSE) {
                    consumeRepository.findConsumeByReceiptId(receipt.getId())
                            .ifPresent(consume -> consume.setCard(otherCard));
                } else {
                    incomeRepository.findIncomeByReceiptId(receipt.getId())
                            .ifPresent(income -> income.setCard(otherCard));
                }
            }

            receiptRepository.saveAll(receipts);
            cardRepository.delete(card);
        }
    }
}
