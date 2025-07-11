package goorm.nuto.Nuto.Service;

import goorm.nuto.Nuto.Dto.CategoryDto;
import goorm.nuto.Nuto.Dto.DailyConsumeDto;
import goorm.nuto.Nuto.Entity.Member;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MainPageService {
    // 소비 상위 카테고리 3개 조회
    List<CategoryDto> getCategory(Member member);
    // 날짜별 금액 및 카테고리 조회
    List<DailyConsumeDto> getDailyConsume(Member member);
}
