
package goorm.nuto.Nuto.Config;

import goorm.nuto.Nuto.Entity.Category;
import goorm.nuto.Nuto.Entity.CategoryType;
import goorm.nuto.Nuto.Repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryInitializer {

    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void initCategories() {
        // 식비
        Category 식비 = saveParent("식비");
        saveChildren(식비, List.of("외식", "카페/디저트", "식재료/마트"));

        // 주거
        Category 주거 = saveParent("주거");
        saveChildren(주거, List.of("월세/전세", "관리비", "수도/전기/가스"));

        // 교통
        Category 교통 = saveParent("교통");
        saveChildren(교통, List.of("대중교통", "택시/모빌리티", "자가용 유지비 (주유, 보험 등)"));

        // 통신
        Category 통신 = saveParent("통신");
        saveChildren(통신, List.of("휴대폰 요금", "인터넷"));

        // 의류/미용
        Category 의류 = saveParent("의류/미용");
        saveChildren(의류, List.of("의류 구매", "미용실/네일/화장품"));

        // 의료/건강
        Category 의료 = saveParent("의료/건강");
        saveChildren(의료, List.of("병원/약국", "헬스장/운동", "건강식품"));

        // 교육
        Category 교육 = saveParent("교육");
        saveChildren(교육, List.of("학원/강의", "도서/수강료"));

        // 생활용품
        Category 생활 = saveParent("생활용품");
        saveChildren(생활, List.of("생필품", "청소/세탁"));

        // 취미/여가
        Category 취미 = saveParent("취미/여가");
        saveChildren(취미, List.of("영화/공연", "게임/콘텐츠 구독", "여행/레저"));

        // 금융
        Category 금융 = saveParent("금융");
        saveChildren(금융, List.of("보험료", "적금/예금", "대출 상환"));

        // 선물/경조사
        Category 선물 = saveParent("선물/경조사");
        saveChildren(선물, List.of("생일/기념일", "경조사비"));

        // 반려동물
        Category 반려 = saveParent("반려동물");
        saveChildren(반려, List.of("사료/용품", "병원"));

        // 기타
        Category 기타 = saveParent("기타");
        saveChildren(기타, List.of("분류되지 않은 지출", "현금 인출 등"));
    }

    private Category saveParent(String name) {
        return categoryRepository.save(Category.builder()
                .name(name)
                .type(CategoryType.EXPENSE)
                .build());
    }

    private void saveChildren(Category parent, List<String> names) {
        for (String name : names) {
            categoryRepository.save(Category.builder()
                    .name(name)
                    .type(CategoryType.EXPENSE)
                    .parent(parent)
                    .build());
        }
    }
}
