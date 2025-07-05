package goorm.nuto.Nuto.Dto;

import goorm.nuto.Nuto.Entity.Card;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CardResponseDto {

    private String cardNumber;

    private Long totalAmount;

    private String cardType;

    private LocalDate expiryDate;

    public CardResponseDto(Card card) {
        this.cardNumber = card.getCardNumber();
        this.totalAmount = card.getTotalAmount();
        this.cardType = card.getCardType().name();
        this.expiryDate = card.getExpiryDate();
    }

}
