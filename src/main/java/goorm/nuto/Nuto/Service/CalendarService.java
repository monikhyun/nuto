package goorm.nuto.Nuto.Service;

import goorm.nuto.Nuto.Dto.CalendarResponseDto;
import goorm.nuto.Nuto.Dto.CalendarSummaryDto;
import goorm.nuto.Nuto.Entity.Member;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CalendarService {
    List<CalendarResponseDto> getDailyConsumes(Member member, int year, int month);
    CalendarSummaryDto getCalendarSummary(Long memberId, int targetYear, int targetMonth);
}
