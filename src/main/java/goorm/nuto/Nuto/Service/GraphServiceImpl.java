package goorm.nuto.Nuto.Service;

import goorm.nuto.Nuto.Dto.ConsumeGraphResponseDto;
import goorm.nuto.Nuto.Entity.Member;
import goorm.nuto.Nuto.Exception.NotFoundMemberException;
import goorm.nuto.Nuto.Repository.ConsumeRepository;
import goorm.nuto.Nuto.Repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GraphServiceImpl implements GraphService{
    private final ConsumeRepository consumeRepository;
    private final MemberRepository memberRepository;

    @Override
    public ConsumeGraphResponseDto getGraph(Member member, int year, int month) {
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();

        List<Long> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>(Collections.nCopies(daysInMonth, 0L));

        for (int i = 1; i <= daysInMonth; i++) {
            labels.add((long) i);
        }

        List<Object[]> results = consumeRepository.findDailyAmountByMemberAndYearAndMonth(member, year, month);


        for (Object[] row : results) {
            Integer day = (Integer) row[0];
            Long totalAmount = (Long) row[1];
            if (day >= 1 && day <= daysInMonth) {
                data.set(day - 1, totalAmount);
            }
        }

        return ConsumeGraphResponseDto.builder()
                .labels(labels)
                .data(data)
                .build();
    }

    @Override
    public List<String> getAvailableMonths(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        LocalDate joinDate = member.getCreatedDate().atStartOfDay().toLocalDate();
        LocalDate today = LocalDate.now();

        List<String> result = new ArrayList<>();

        YearMonth start = YearMonth.from(joinDate);
        YearMonth end = YearMonth.from(today);

        while (!start.isAfter(end)) {
            result.add(start.toString()); // "2024-11"
            start = start.plusMonths(1);
        }

        return result;
    }
}
