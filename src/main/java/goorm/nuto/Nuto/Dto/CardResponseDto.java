package goorm.nuto.Nuto.Dto;

import goorm.nuto.Nuto.Entity.Card;
import goorm.nuto.Nuto.Entity.CardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardResponseDto {

    private String cardNumber;

    private Long totalAmount;

    private CardType cardType;

    private YearMonth expiryDate;

    public CardResponseDto(Card card) {
        this.cardNumber = card.getCardNumber();
        this.totalAmount = card.getTotalAmount();
        this.cardType = card.getCardType();
        this.expiryDate = card.getExpiryDate();
    }


}
