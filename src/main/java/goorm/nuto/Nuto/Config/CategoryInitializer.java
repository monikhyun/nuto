package goorm.nuto.Nuto.Config;

import goorm.nuto.Nuto.Entity.Category;
import goorm.nuto.Nuto.Entity.CategoryType;
import goorm.nuto.Nuto.Repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryInitializer {

    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void initCategories() {
        save("의류/화장품");
        save("교통비");
        save("식비");
        save("생활비");
        save("의료비");
        save("여가비");
    }

    private void save(String name) {
        categoryRepository.save(Category.builder()
                .name(name)
                .type(CategoryType.EXPENSE)
                .build());
    }
}