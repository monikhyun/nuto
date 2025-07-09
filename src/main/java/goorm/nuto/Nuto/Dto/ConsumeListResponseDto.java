package goorm.nuto.Nuto.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class ConsumeListResponseDto {
    private String name;
    private Long amount;
    private String cardName;
    private LocalDate date;
    private String categoryName;
}