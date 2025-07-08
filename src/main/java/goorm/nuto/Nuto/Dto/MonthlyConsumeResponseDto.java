package goorm.nuto.Nuto.Dto;

import goorm.nuto.Nuto.Entity.CardType;
import goorm.nuto.Nuto.Entity.Category;
import goorm.nuto.Nuto.Entity.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyConsumeResponseDto {
    private String name;
    private Long price;
    private CardType cardType;
    private LocalDate date;
    private Category category;
}
