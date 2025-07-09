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
    private final ReceiptRepository receiptRepository;

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

        Member member2 = Member.builder()
                .name("테스트유저2")
                .userid("test2@nuto.com")
                .password(encoder.encode("12345")) // ⬅ 여기서 인코딩!
                .role(Role.USER)
                .age(22L)
                .job("개발자")
                .build();
        memberRepository.save(member2);

        // 2. 카테고리 생성
        Category food = categoryRepository.save(Category.builder().name("식비").type(CategoryType.EXPENSE).build());
        Category transport = categoryRepository.save(Category.builder().name("교통비").type(CategoryType.EXPENSE).build());
        Category leisure = categoryRepository.save(Category.builder().name("여가비").type(CategoryType.EXPENSE).build());
        Category living = categoryRepository.save(Category.builder().name("생활비").type(CategoryType.EXPENSE).build());
        Category clothes = categoryRepository.save(Category.builder().name("의류/화장품").type(CategoryType.EXPENSE).build());
        Category medical = categoryRepository.save(Category.builder().name("의료비").type(CategoryType.EXPENSE).build());
        Category salary = categoryRepository.save(Category.builder().name("월급").type(CategoryType.INCOME).build());

        // 3. 카드 생성
        Card card = cardRepository.save(Card.builder()
                .cardNumber("0000-0000-0000-0000")
                .cardType(CardType.OTHER)
                .member(member)
                .expiryDate(YearMonth.of(2099, 12))
                .totalAmount(0L)
                .build());

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
        addConsume("의류", "test 가게명", 40000L, LocalDate.of(2025, 6, 5), card1, clothes, member);
        addConsume("화장품", "test2 가게명", 30000L, LocalDate.of(2025, 6, 7), card1, clothes, member);
        addConsume("교통비", "test3 가게명", 20000L, LocalDate.of(2025, 6, 10), card2, transport, member);
        addConsume("의료비", "test4 가게명", 25000L, LocalDate.of(2025, 6, 11), card2, medical, member);
        addConsume("생활비", "test5 가게명", 15000L, LocalDate.of(2025, 6, 15), card3, living, member);
        addConsume("식비", "test6 가게명", 50000L, LocalDate.of(2025, 6, 20), card3, food, member);
        addConsume("여가비", "test7 가게명", 10000L, LocalDate.of(2025, 6, 21), card1, leisure, member);

        addConsume("식비", "test6 가게명", 50000L, LocalDate.of(2025, 6, 20), card3, food, member2);
        addConsume("여가비", "test7 가게명", 10000L, LocalDate.of(2025, 6, 21), card1, leisure, member2);

        // 5. 소비 생성 (7월 소비 내역)
        LocalDate today = LocalDate.now();
        addConsume("식비", "가게A", 30000L, today.minusDays(7), card1, food, member);
        addConsume("생활비", "가게B", 20000L, today.minusDays(6), card2, living, member);
        addConsume("교통비", "가게C", 12000L, today.minusDays(5), card3, transport, member);
        addConsume("의료비", "가게D", 17000L, today.minusDays(4), card1, medical, member);
        addConsume("여가비", "가게E", 22000L, today.minusDays(3), card2, leisure, member);
        addConsume("의류", "가게F", 45000L, today.minusDays(2), card3, clothes, member);
        addConsume("식비", "가게G", 25000L, today.minusDays(1), card1, food, member);

        // 6. 최근 소비 (4개)
        for (int i = 0; i < 4; i++) {
            addConsume("최근소비" + i, "최근가게" + i, 10000L + (i * 500), today.minusDays(i), card1, food, member);
        }

        // 7. 수익 생성 (6월, 7월)
        addIncome("월급", "test 가게명", 2500000L, LocalDate.of(2025, 6, 1), card1, salary, member);
        addIncome("월급", "test 가게명", 2500000L, LocalDate.of(2025, 7, 1), card1, salary, member);

    }

    private void addConsume(String name, String shop_name, Long amount,
                            LocalDate date, Card card, Category category, Member member) {
        Receipt receipt = receiptRepository.save(Receipt.create(name, shop_name, amount, date, card, category, member));
        consumeRepository.save(Consume.create(name, amount, date, card, category, member, receipt));
    }

    private void addIncome(String name, String shop_name, Long amount, LocalDate date, Card card, Category category, Member member) {
        Receipt receipt = receiptRepository.save(Receipt.create(name, shop_name, amount, date, card, category, member));
        incomeRepository.save(Income.builder()
                .member(member)
                .category(category)
                .amount(amount.doubleValue())
                .date(date)
                .card(card)
                .receipt(receipt)
                .build());
    }
}