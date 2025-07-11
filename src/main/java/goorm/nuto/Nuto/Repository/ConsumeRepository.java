package goorm.nuto.Nuto.Repository;

import goorm.nuto.Nuto.Entity.Card;
import goorm.nuto.Nuto.Entity.Consume;
import goorm.nuto.Nuto.Entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ConsumeRepository extends JpaRepository<Consume, Long> {
    @Query("SELECT c.month, SUM(c.amount) " +
            "FROM Consume c " +
            "WHERE c.member = :member AND c.year = :year " +
            "GROUP BY c.month " +
            "ORDER BY c.month ASC")
    List<Object[]> findMonthlyAmountByMemberAndYear(@Param("member") Member member,
                                                    @Param("year") int year);

    @Query("SELECT c.day, SUM(c.amount) " +
            "FROM Consume c " +
            "WHERE c.member = :member AND c.year = :year AND c.month = :month " +
            "GROUP BY c.day " +
            "ORDER BY c.day ASC")
    List<Object[]> findDailyAmountByMemberAndYearAndMonth(@Param("member") Member member,
                                                          @Param("year") int year,
                                                          @Param("month") int month);

    @Query("SELECT c.category.name, SUM(c.amount) " +
            "FROM Consume c " +
            "WHERE c.member = :member AND c.year = :year AND c.month = :month " +
            "GROUP BY c.category.name " +
            "ORDER BY SUM(c.amount) DESC")
    List<Object[]> findCategoryAmountThisMonth(@Param("member") Member member,
                                               @Param("year") int year,
                                               @Param("month") int month);

    @Query("SELECT c FROM Consume c " +
            "WHERE c.member = :member AND c.year = :year AND c.month = :month " +
            "ORDER BY c.date DESC, c.id DESC")
    List<Consume> findTop4ByMemberAndYearAndMonthOrderByDateDesc(@Param("member") Member member,
                                                                 @Param("year") int year,
                                                                 @Param("month") int month,
                                                                 Pageable pageable);

    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM Consume c " +
            "WHERE c.member = :member AND c.date BETWEEN :startDate AND :endDate")
    Long getTotalAmountBetweenDates(@Param("member") Member member,
                                    @Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate);

    Optional<Consume> findConsumeByReceiptId(Long receiptId);
    void deleteByReceiptId(Long receiptId);
    Page<Consume> findAllByMember(Member member, Pageable pageable);

    Page<Consume> findByMemberAndCardId(Member member, Long cardId, Pageable pageable);
    Page<Consume> findByMemberAndCategoryName(Member member, String categoryName, Pageable pageable);
    Page<Consume> findByMemberAndYearAndMonth(Member member, int year, int month, Pageable pageable);

    @Query("SELECT c FROM Consume c WHERE c.member = :member AND c.category.id = :categoryId")
    Page<Consume> findByMemberAndCategoryId(@Param("member") Member member, @Param("categoryId") Long categoryId, Pageable pageable);


    @Query("SELECT SUM(c.amount) FROM Consume c WHERE c.member.id = :memberId AND YEAR(c.date) = :year AND MONTH(c.date) = :month")
    Long findTotalAmountByMonth(@Param("memberId") Long memberId,
                                @Param("year") int year,
                                @Param("month") int month);

    @Query(value = """
    SELECT MAX(total) FROM (
        SELECT SUM(amount) AS total
        FROM consume
        WHERE member_id = :memberId AND YEAR(date) = :year AND MONTH(date) = :month
        GROUP BY date
    ) AS sub
    """, nativeQuery = true)
    Long findMaxDailyConsumeAmount(Long memberId, int year, int month);
    @Query("SELECT c.date FROM Consume c WHERE c.member.id = :memberId AND YEAR(c.date) = :year AND MONTH(c.date) = :month GROUP BY c.date HAVING SUM(c.amount) = :targetAmount")
    List<LocalDate> findTopSpendDaysByAmount(@Param("memberId") Long memberId,
                                             @Param("year") int year,
                                             @Param("month") int month,
                                             @Param("targetAmount") Long targetAmount);

    @Query(value = """
    SELECT MAX(total) FROM (
        SELECT SUM(amount) AS total
        FROM consume
        WHERE member_id = :memberId
          AND YEAR(date) = :year
          AND MONTH(date) = :month
        GROUP BY category_id
    ) AS category_totals
    """, nativeQuery = true)
    Long findMaxDailyConsumeCategory(@Param("memberId") Long memberId,
                                     @Param("year") int year,
                                     @Param("month") int month);

    @Query("SELECT c.category.name FROM Consume c WHERE c.member.id = :memberId AND YEAR(c.date) = :year AND MONTH(c.date) = :month GROUP BY c.category HAVING SUM(c.amount) = :totalAmount")
    List<String> findTopSpendDaysByCategory(@Param("memberId") Long memberId,
                                             @Param("year") int year,
                                             @Param("month") int month,
                                             @Param("totalAmount") Long category);
}
