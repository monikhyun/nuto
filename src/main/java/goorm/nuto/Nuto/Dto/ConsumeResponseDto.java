package goorm.nuto.Nuto.Dto;

import goorm.nuto.Nuto.Entity.Consume;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ConsumeResponseDto {
    private String name;
    private Long amount;
    private LocalDate date;
    private Long cardId;
    private Long categoryId;

    public ConsumeResponseDto(Consume consume) {
        this.name = consume.getName();
        this.amount = consume.getAmount();
        this.date = consume.getDate();
        this.cardId = consume.getCard().getId();
        this.categoryId = consume.getCategory().getId();
    }
}
