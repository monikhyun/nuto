package goorm.nuto.Nuto.Service;

import goorm.nuto.Nuto.Dto.ConsumeGraphResponseDto;
import goorm.nuto.Nuto.Entity.Member;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GraphService {
    ConsumeGraphResponseDto getGraph(Member member, int year, int month);
    List<String> getAvailableMonths(Long memberId);
}
