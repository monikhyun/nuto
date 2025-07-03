package goorm.nuto.Nuto.Service;

import goorm.nuto.Nuto.Dto.CardDto;
import goorm.nuto.Nuto.Dto.CardResponseDto;
import goorm.nuto.Nuto.Entity.Card;
import goorm.nuto.Nuto.Entity.CardType;
import goorm.nuto.Nuto.Entity.Member;
import goorm.nuto.Nuto.Exception.NotAuthorizedCardAccessException;
import goorm.nuto.Nuto.Exception.NotFoundCardException;
import goorm.nuto.Nuto.Exception.DuplicateCardNumberException;
import goorm.nuto.Nuto.Exception.NotFoundMemberException;
import goorm.nuto.Nuto.Repository.CardRepository;
import goorm.nuto.Nuto.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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

    @Override
    public void saveCard(Long memberId, CardDto dto) {
        // 1) memberId 로 Member 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        if (cardRepository.findByCardNumber(dto.getCardNumber()).isPresent()) {
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

        CardResponseDto dto = new CardResponseDto(card);

        return dto;

    }


    @Override
    public List<CardResponseDto> getCardList(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        List<Card> cards = cardRepository.findByMember(member)
                .orElseThrow(NotFoundCardException::new);

        return cards.stream()
                .map(CardResponseDto::new)
                .toList();

//        List<Card> cards = cardRepository.findAll();
//
//        List<CardResponseDto> cardResponseDtos = new ArrayList<>();
//        for (Card card : cards) {
//            cardResponseDtos.add(new CardResponseDto(card));
//        }
//        return cardResponseDtos;
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
    public void deleteCard(Long memberId,Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(NotFoundCardException::new);

        if (!card.getMember().getId().equals(memberId)) {
            throw new NotAuthorizedCardAccessException("해당 사용자의 카드가 아닙니다.");
        }

        cardRepository.delete(card);
    }
}
