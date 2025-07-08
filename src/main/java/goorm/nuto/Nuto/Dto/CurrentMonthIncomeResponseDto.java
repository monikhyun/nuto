package goorm.nuto.Nuto.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentMonthIncomeResponseDto {
    private Long lastMonthIncome;
    private Long thisMonthIncome;
}
