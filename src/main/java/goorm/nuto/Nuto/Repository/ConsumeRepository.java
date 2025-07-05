package goorm.nuto.Nuto.Repository;

import goorm.nuto.Nuto.Entity.Consume;
import goorm.nuto.Nuto.Entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

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
}
