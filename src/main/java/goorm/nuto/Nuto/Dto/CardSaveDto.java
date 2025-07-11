package goorm.nuto.Nuto.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardSaveDto {

    private Long id;

    private String cardNumber;

    private Long totalAmount;

    private String cardType;

    private String expiryDate;


}
