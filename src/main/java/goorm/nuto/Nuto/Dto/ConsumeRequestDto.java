package goorm.nuto.Nuto.Dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class ConsumeRequestDto {
    private String name;
    private Long amount;
    private LocalDate date;
    private Long cardId;
    private Long categoryId;
    private MultipartFile image;
}
