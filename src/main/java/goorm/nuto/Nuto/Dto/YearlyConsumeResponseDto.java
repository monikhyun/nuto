package goorm.nuto.Nuto.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YearlyConsumeResponseDto {
    private List<String> labels;
    private List<Long> data;
}
