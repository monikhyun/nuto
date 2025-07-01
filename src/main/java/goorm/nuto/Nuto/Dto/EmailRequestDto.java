package goorm.nuto.Nuto.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequestDto {
    @Valid
    @Schema(description = "사용자 이메일 주소", example = "user@example.com", required = true)
    private String email;
}