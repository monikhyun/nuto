package goorm.nuto.Nuto.Repository;

import goorm.nuto.Nuto.Entity.Consume;
import goorm.nuto.Nuto.Entity.Income;
import goorm.nuto.Nuto.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM Income i " +
            "WHERE i.member = :member AND i.date BETWEEN :startDate AND :endDate")
    Double getTotalIncomeBetweenDates(@Param("member") Member member,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);

    Optional<Income> findIncomeByReceiptId(Long receiptId);
    void deleteByReceiptId(Long receiptId);
}
