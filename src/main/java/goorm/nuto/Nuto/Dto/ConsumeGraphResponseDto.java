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
public class ConsumeGraphResponseDto {
    private List<Long> labels;
    private List<Long> data;
}
