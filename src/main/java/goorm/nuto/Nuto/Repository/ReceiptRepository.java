package goorm.nuto.Nuto.Repository;

import goorm.nuto.Nuto.Entity.CategoryType;
import goorm.nuto.Nuto.Entity.Member;
import goorm.nuto.Nuto.Entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    List<Receipt> findByMemberAndCategory_Type(Member member, CategoryType categoryType);
    Optional<Receipt> findByMemberAndId(Member member, Long id);
    List<Receipt> findByMemberAndCategory_TypeAndDateBetween(Member member, CategoryType type, LocalDate start, LocalDate end);
}
