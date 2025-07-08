package goorm.nuto.Nuto.Dto;

import goorm.nuto.Nuto.Entity.CardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDto {

    private Long id;

    private String cardNumber;

    private Long totalAmount;

    private String cardType;

    private YearMonth expiryDate;


}
