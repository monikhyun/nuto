package goorm.nuto.Nuto.Service;

import goorm.nuto.Nuto.Dto.CardDto;
import goorm.nuto.Nuto.Dto.CardResponseDto;

import java.util.List;

public interface CardService {

    void saveCard(Long memberId, CardDto dto);

    CardResponseDto getCard(Long memberId,Long cardId);

    List<CardResponseDto> getCardList(Long memberId);

    void modifyCard(Long memberId,CardDto dto);

    void deleteCard(Long memberId,Long cardId);
}
