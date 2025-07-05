package goorm.nuto.Nuto.Dto;

import goorm.nuto.Nuto.Entity.CardType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CardDto {

    private Long id;

    private String cardNumber;

    private Long totalAmount;

    private String cardType;

    private LocalDate expiryDate;


}
