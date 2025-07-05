package goorm.nuto.Nuto.Dto;

import goorm.nuto.Nuto.Entity.CardType;
import lombok.Data;

import java.time.LocalDate;
import java.time.YearMonth;

@Data
public class CardDto {

    private Long id;

    private String cardNumber;

    private Long totalAmount;

    private String cardType;

    private YearMonth expiryDate;


}
