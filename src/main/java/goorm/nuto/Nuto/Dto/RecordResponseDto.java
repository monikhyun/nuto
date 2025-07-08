package goorm.nuto.Nuto.Dto;


import goorm.nuto.Nuto.Entity.Card;
import goorm.nuto.Nuto.Entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordResponseDto {
    private Long id;
    private String shopName;
    private String fileName;
    private Long amount;
    private LocalDate date;
    private CardDto card;
    private CategoryDto category;
    private String image;
}
