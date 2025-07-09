package goorm.nuto.Nuto.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarSummaryDto {
    private Long thisMonthTotal; // 이번 달 소비
    private Long lastMonthTotal; // 지난 달 소비
    private List<LocalDate> topSpendDay; // 가장 많이 소비한 날
    private List<String> topCategory; // 가장 많이 소비한 카테고리
}
