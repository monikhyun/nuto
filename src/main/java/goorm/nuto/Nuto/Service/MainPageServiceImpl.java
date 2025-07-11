package goorm.nuto.Nuto.Service;

import goorm.nuto.Nuto.Dto.CategoryDto;
import goorm.nuto.Nuto.Dto.DailyConsumeDto;
import goorm.nuto.Nuto.Entity.Category;
import goorm.nuto.Nuto.Entity.Member;
import goorm.nuto.Nuto.Repository.CategoryRepository;
import goorm.nuto.Nuto.Repository.ConsumeRepository;
import goorm.nuto.Nuto.Repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MainPageServiceImpl implements MainPageService {

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final ConsumeRepository consumeRepository;

    // 소비 상위 카테고리 3개 조회
    @Override
    public List<CategoryDto> getCategory(Member member) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        List<Object[]> results = consumeRepository.findTop3CategoriesByMemberAndMonth(member, year, month);

        return results.stream()
                .limit(3)
                .map(result -> {
                    Category category = (Category) result[0];
                    return CategoryDto.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .type(category.getType().name())
                            .build();
                })
                .toList();
    }


    // 날짜별 금액 및 카테고리 조회
    @Override
    public List<DailyConsumeDto> getDailyConsume(Member member) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        return consumeRepository.findDailySummaryByMemberAndMonth(member, year, month);
    }


}
