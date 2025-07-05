package goorm.nuto.Nuto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import goorm.nuto.Nuto.Dto.*;
import goorm.nuto.Nuto.Entity.Member;
import goorm.nuto.Nuto.Repository.MemberRepository;
import goorm.nuto.Nuto.Service.DashBoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DashBoardServiceImplTest {

    @Autowired
    private DashBoardService dashBoardService;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        member = memberRepository.findByUserid("test@nuto.com").orElseThrow();

        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())     // 등록
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // 읽기 편한 문자열로 출력
                .enable(SerializationFeature.INDENT_OUTPUT); // 보기 좋게 들여쓰기
    }

    @Test
    void 카드정보_조회() throws Exception {
        List<CardResponseDto> cards = dashBoardService.getCards(member);
        assertThat(cards).isNotEmpty();

        // JSON 출력
        System.out.println("📦 카드정보 JSON:");
        System.out.println(objectMapper.writeValueAsString(cards));
    }

    @Test
    void 올해_소비_조회() throws Exception {
        YearlyConsumeResponseDto result = dashBoardService.getYearlyConsume(member);
        assertThat(result.getLabels()).isNotEmpty();
        assertThat(result.getData()).hasSize(result.getLabels().size());

        System.out.println("📈 올해 소비 JSON:");
        System.out.println(objectMapper.writeValueAsString(result));
    }

    @Test
    void 소비그래프_조회() throws Exception {
        ConsumeGraphResponseDto graph = dashBoardService.getConsumeGraph(member);
        assertThat(graph.getLabels()).isNotEmpty();
        assertThat(graph.getData()).hasSize(graph.getLabels().size());

        System.out.println("📊 소비 그래프 JSON:");
        System.out.println(objectMapper.writeValueAsString(graph));
    }

    @Test
    void 소비카테고리_비율_조회() throws Exception {
        CategoryConsumeResponseDto result = dashBoardService.getCategoryConsume(member);
        assertThat(result.getLabels()).contains("기타");
        assertThat(result.getData().stream().mapToLong(Long::longValue).sum()).isBetween(99L, 101L);
        assertThat(result.getTotalMount()).isGreaterThan(0L);

        System.out.println("📂 소비 카테고리 비율 JSON:");
        System.out.println(objectMapper.writeValueAsString(result));
    }

    @Test
    void 최근_4개_소비내역_조회() throws Exception {
        List<MonthlyConsumeResponseDto> list = dashBoardService.getMonthlyConsume(member);
        assertThat(list).hasSizeLessThanOrEqualTo(4);

        System.out.println("🧾 최근 4개 소비내역 JSON:");
        System.out.println(objectMapper.writeValueAsString(list));
    }
}