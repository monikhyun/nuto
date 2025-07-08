package goorm.nuto.Nuto.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class MonthlyReceiptDto {
    private int year;
    private int month;
    private Long count;
}
