package goorm.nuto.Nuto.Dto;

import goorm.nuto.Nuto.Entity.Card;
import goorm.nuto.Nuto.Entity.Category;
import goorm.nuto.Nuto.Entity.CategoryType;
import jakarta.annotation.Nullable;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordRequestDto {
    private Long id;
    private String name;
    private String fileName;
    private Long amount;
    private LocalDate date;
    private Long cardId;
    private Long categoryId;

    @Nullable
    private MultipartFile image;
}
