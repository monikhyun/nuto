package goorm.nuto.Nuto.Config;

import goorm.nuto.Nuto.Entity.*;
import goorm.nuto.Nuto.Repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;

@Component
@RequiredArgsConstructor
public class DashBoardTestInitializer {

    private final MemberRepository memberRepository;
    private final CardRepository cardRepository;
    private final CategoryRepository categoryRepository;
    private final ConsumeRepository consumeRepository;
    private final IncomeRepository incomeRepository;

    @PostConstruct
    public void init() {
        // 1. 멤버 생성
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Member member = Member.builder()
                .name("테스트유저")
                .userid("test@nuto.com")
                .password(encoder.encode("1234")) // ⬅ 여기서 인코딩!
                .role(Role.USER)
                .age(25L)
                .job("학생")
                .build();
        memberRepository.save(member);

        // 2. 카테고리 생성
        Category food = categoryRepository.save(Category.builder().name("식비").type(CategoryType.EXPENSE).build());
        Category transport = categoryRepository.save(Category.builder().name("교통비").type(CategoryType.EXPENSE).build());
        Category leisure = categoryRepository.save(Category.builder().name("여가비").type(CategoryType.EXPENSE).build());
        Category living = categoryRepository.save(Category.builder().name("생활비").type(CategoryType.EXPENSE).build());
        Category clothes = categoryRepository.save(Category.builder().name("의류/화장품").type(CategoryType.EXPENSE).build());
        Category medical = categoryRepository.save(Category.builder().name("의료비").type(CategoryType.EXPENSE).build());
        Category salary = categoryRepository.save(Category.builder().name("월급").type(CategoryType.INCOME).build());

        // 3. 카드 생성
        Card card1 = cardRepository.save(Card.builder()
                .cardNumber("1234-5678-9012-3456")
                .cardType(CardType.KAKAOBANK)
                .member(member)
                .expiryDate(YearMonth.of(2026, 12))
                .totalAmount(500000L)
                .build());

        Card card2 = cardRepository.save(Card.builder()
                .cardNumber("1111-2222-3333-4444")
                .cardType(CardType.HYUNDAI)
                .member(member)
                .expiryDate(YearMonth.of(2025, 11))
                .totalAmount(300000L)
                .build());

        Card card3 = cardRepository.save(Card.builder()
                .cardNumber("5555-6666-7777-8888")
                .cardType(CardType.KB_KOOKMIN)
                .member(member)
                .expiryDate(YearMonth.of(2027, 1))
                .totalAmount(400000L)
                .build());

        // 4. 소비 생성 (6월 소비 내역)
        addConsume("의류", 40000L, LocalDate.of(2025, 6, 5), card1, clothes, member);
        addConsume("화장품", 30000L, LocalDate.of(2025, 6, 7), card1, clothes, member);
        addConsume("교통비", 20000L, LocalDate.of(2025, 6, 10), card2, transport, member);
        addConsume("의료비", 25000L, LocalDate.of(2025, 6, 11), card2, medical, member);
        addConsume("생활비", 15000L, LocalDate.of(2025, 6, 15), card3, living, member);
        addConsume("식비", 50000L, LocalDate.of(2025, 6, 20), card3, food, member);
        addConsume("여가비", 10000L, LocalDate.of(2025, 6, 21), card1, leisure, member);

        // 5. 소비 생성 (7월 소비 내역)
        LocalDate today = LocalDate.now();
        addConsume("식비", 30000L, today.minusDays(7), card1, food, member);
        addConsume("생활비", 20000L, today.minusDays(6), card2, living, member);
        addConsume("교통비", 12000L, today.minusDays(5), card3, transport, member);
        addConsume("의료비", 17000L, today.minusDays(4), card1, medical, member);
        addConsume("여가비", 22000L, today.minusDays(3), card2, leisure, member);
        addConsume("의류", 45000L, today.minusDays(2), card3, clothes, member);
        addConsume("식비", 25000L, today.minusDays(1), card1, food, member);

        // 6. 최근 소비 (4개)
        for (int i = 0; i < 4; i++) {
            addConsume("최근소비" + i, 10000L + (i * 500), today.minusDays(i), card1, food, member);
        }

        // 7. 수익 생성 (6월, 7월)
        incomeRepository.save(Income.builder()
                .member(member)
                .category(salary)
                .amount(2500000.0)
                .date(LocalDate.of(2025, 6, 1))
                .build());

        incomeRepository.save(Income.builder()
                .member(member)
                .category(salary)
                .amount(2500000.0)
                .date(LocalDate.of(2025, 7, 1))
                .build());
    }

    private void addConsume(String name, Long amount, LocalDate date, Card card, Category category, Member member) {
        consumeRepository.save(Consume.create(name, amount, date, card, category, member));
    }
}