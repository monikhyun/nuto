package goorm.nuto.Nuto.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

    @Schema(description = "이메일 또는 로그인 ID", example = "user1@naver.com", required = true)
    private String userId;

    @Schema(description = "회원 이름", example = "홍길동", required = true)
    private String name;

    @Schema(description = "비밀번호", example = "1234", required = true)
    private String password;

    @Schema(description = "나이", example = "25", required = true)
    private Long age;

    @Schema(description = "직업", example = "학생, 개발자 등", required = true)
    private String job;
}
