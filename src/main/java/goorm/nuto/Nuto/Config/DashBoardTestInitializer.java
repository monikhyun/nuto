package goorm.nuto.Nuto.Config;

import goorm.nuto.Nuto.Entity.*;
import goorm.nuto.Nuto.Repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
                .password(encoder.encode("1234"))
                .role(Role.USER)
                .age(25L)
                .job("학생")
                .build();
        memberRepository.save(member);

        Member member2 = Member.builder()
                .name("테스트유저2")
                .userid("test2@nuto.com")
                .password(encoder.encode("12345"))
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

        Map<String, Category> categoryMap = new HashMap<>();
        categoryMap.put("식비", food);
        categoryMap.put("교통비", transport);
        categoryMap.put("여가비", leisure);
        categoryMap.put("생활비", living);
        categoryMap.put("의류/화장품", clothes);
        categoryMap.put("의료비", medical);

        // 3. 카드 생성
        Card card = cardRepository.save(Card.builder()
                .cardNumber("0000-0000-0000-0000")
                .cardType(CardType.OTHER)
                .member(member)
                .name("기타")
                .expiryDate(YearMonth.of(2099, 12))
                .totalAmount(0L)
                .build());

        Card card1 = cardRepository.save(Card.builder()
                .cardNumber("1234-5678-9012-3456")
                .cardType(CardType.KAKAOBANK)
                .name("카카오페이 카드")
                .member(member)
                .expiryDate(YearMonth.of(2026, 12))
                .totalAmount(500000L)
                .build());

        Card card2 = cardRepository.save(Card.builder()
                .cardNumber("1111-2222-3333-4444")
                .cardType(CardType.HYUNDAI)
                .member(member)
                .name("현대 카드")
                .expiryDate(YearMonth.of(2025, 11))
                .totalAmount(300000L)
                .build());

        Card card3 = cardRepository.save(Card.builder()
                .cardNumber("5555-6666-7777-8888")
                .cardType(CardType.KB_KOOKMIN)
                .name("국민 나라사랑 카드")
                .member(member)
                .expiryDate(YearMonth.of(2027, 1))
                .totalAmount(400000L)
                .build());

        // 4. 수익 생성 (6월, 7월)
        addIncome("월급", "test 가게명", 2500000L, LocalDate.of(2025, 6, 1), card1, salary, member);
        addIncome("월급", "test 가게명", 2500000L, LocalDate.of(2025, 7, 1), card1, salary, member);
        List<Card> cardList = List.of(card, card1, card2, card3);
        insertDailyConsumesFromCSV(member, cardList, categoryMap);
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

    private void insertDailyConsumesFromCSV(Member member, List<Card> cardList, Map<String, Category> categoryMap) {
        String csvPath = "nuto/src/main/resources/DailyExpenseTestData.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            boolean skip = true;
            Random random = new Random();
            while ((line = br.readLine()) != null) {
                if (skip) { skip = false; continue; }
                String[] tokens = line.split(",");

                String name = tokens[0].trim();
                String shop = tokens[1].trim();
                Long amount = Long.parseLong(tokens[2].trim());
                LocalDate date = LocalDate.parse(tokens[3].trim());
                String categoryName = tokens[4].trim();

                Category category = categoryMap.get(categoryName);
                if (category == null) continue;

                // ✅ 카드 랜덤 선택
                Card randomCard = cardList.get(random.nextInt(cardList.size()));

                Receipt receipt = receiptRepository.save(
                        Receipt.create(name, shop, amount, date, randomCard, category, member)
                );

                consumeRepository.save(
                        Consume.create(name, amount, date, randomCard, category, member, receipt)
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}