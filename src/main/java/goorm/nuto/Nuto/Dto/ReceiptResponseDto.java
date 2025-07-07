package goorm.nuto.Nuto.Dto;

import goorm.nuto.Nuto.Entity.CategoryType;
import goorm.nuto.Nuto.Entity.Consume;
import goorm.nuto.Nuto.Entity.Receipt;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReceiptResponseDto {
    private String name;
    private String shop_name;
    private Long price;
    private LocalDate date;
    private Long cardId;
    private Long categoryId;
    //private CategoryType categoryType;

    public ReceiptResponseDto(Receipt receipt) {
        this.name = receipt.getName();
        this.shop_name = receipt.getShop_name();
        this.price = receipt.getPrice();
        this.date = receipt.getDate();
        this.cardId = receipt.getCard().getId();
        this.categoryId = receipt.getCategory().getId();
        //this.categoryType = receipt.getCategory().getType();
    }
}
