package goorm.nuto.Nuto.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponseDto<T> {
    private List<T> content;       // 실제 데이터 리스트
    private int totalPages;        // 전체 페이지 수
    private long totalElements;    // 전체 데이터 수
    private int currentPage;       // 현재 페이지 번호 (0부터 시작)
    private int pageSize;          // 한 페이지에 포함된 데이터 수
    private boolean first;         // 첫 페이지 여부
    private boolean last;          // 마지막 페이지 여부
    private boolean empty;         // 데이터 비었는지 여부

    public static <T> PageResponseDto<T> of(Page<T> page) {
        return PageResponseDto.<T>builder()
                .content(page.getContent())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .build();
    }
}
