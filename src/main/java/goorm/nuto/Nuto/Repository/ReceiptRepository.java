package goorm.nuto.Nuto.Repository;

import goorm.nuto.Nuto.Dto.MonthlyReceiptDto;
import goorm.nuto.Nuto.Entity.Member;
import goorm.nuto.Nuto.Entity.Receipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    Page<Receipt> findReceiptsByMemberOrderByIdDesc(Member member, Pageable pageable);
    List<Receipt> findByCardId(Long cardId);

    @Query("SELECT r FROM Receipt r WHERE r.member = :member AND r.category.type = 'EXPENSE' ORDER BY r.id DESC")
    Page<Receipt> findExpenseReceiptsByMember(@Param("member") Member member, Pageable pageable);
    @Query("SELECT r FROM Receipt r WHERE r.member = :member AND r.category.type = 'INCOME' ORDER BY r.id DESC")
    Page<Receipt> findIncomeReceiptsByMember(@Param("member") Member member, Pageable pageable);

    @Query("""
    SELECT r FROM Receipt r
    WHERE r.member = :member
      AND r.category.type = 'EXPENSE'
      AND FUNCTION('YEAR', r.date) = :year
      AND FUNCTION('MONTH', r.date) = :month
    ORDER BY r.id DESC
    """)
    Page<Receipt> findExpenseReceiptsByMemberAndYearMonth(
            @Param("member") Member member,
            @Param("year") int year,
            @Param("month") int month,
            Pageable pageable
    );

    @Query("SELECT new goorm.nuto.Nuto.Dto.MonthlyReceiptDto(YEAR(r.date), MONTH(r.date), COUNT(r)) " +
            "FROM Receipt r WHERE r.member = :member GROUP BY YEAR(r.date), MONTH(r.date) ORDER BY YEAR(r.date) DESC, MONTH(r.date) DESC")
    Page<MonthlyReceiptDto> findMonthlyReceiptByMemberId(@Param("member") Member member, Pageable pageable);
}
