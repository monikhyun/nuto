package goorm.nuto.Nuto.Dto;

import goorm.nuto.Nuto.Entity.Category;
import goorm.nuto.Nuto.Entity.CategoryType;
import goorm.nuto.Nuto.Entity.Consume;
import goorm.nuto.Nuto.Entity.Receipt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceiptResponseDto {
    private String name;
    private String shop_name;
    private Long price;
    private LocalDate date;
    private String categoryName;
    //private CategoryType categoryType;

    public ReceiptResponseDto(Receipt receipt) {
        this.name = receipt.getName();
        this.shop_name = receipt.getShop_name();
        this.price = receipt.getPrice();
        this.date = receipt.getDate();
        this.categoryName = receipt.getCategory().getName();
        //this.categoryType = receipt.getCategory().getType();
    }
}
