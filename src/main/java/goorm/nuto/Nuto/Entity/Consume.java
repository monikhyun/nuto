package goorm.nuto.Nuto.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Consume {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consume_id", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int month;

    @Column(nullable = false)
    private int day;

    @Column(nullable = false)
    private int weekOfYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToOne
    @JoinColumn(name = "receipt_id", unique = true, nullable = false)
    private Receipt receipt;

    public static Consume create(String name, Long amount, LocalDate date, Card card, Category category, Member member,Receipt receipt) {
        return Consume.builder()
                .name(name)
                .amount(amount)
                .date(date)
                .year(date.getYear())
                .month(date.getMonthValue())
                .day(date.getDayOfMonth())
                .weekOfYear(date.get(java.time.temporal.WeekFields.ISO.weekOfYear()))
                .card(card)
                .category(category)
                .member(member)
                .receipt(receipt)
                .build();
    }


}
