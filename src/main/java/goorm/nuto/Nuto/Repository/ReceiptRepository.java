package goorm.nuto.Nuto.Repository;

import goorm.nuto.Nuto.Entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
