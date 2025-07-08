package goorm.nuto.Nuto.Dto;

import lombok.Data;

import java.util.List;

@Data
public class DeleteRequestDto {
    private List<Long> ids;
}
