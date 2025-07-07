package goorm.nuto.Nuto.Dto;

import goorm.nuto.Nuto.Entity.CategoryType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReceiptRequestDto {
    private String name;
    private String shop_name;
    private Long price;
    private LocalDate date;
    private Long cardId;
    private Long categoryId;
    //private CategoryType categoryType;
}
