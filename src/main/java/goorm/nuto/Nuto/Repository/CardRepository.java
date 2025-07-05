package goorm.nuto.Nuto.Repository;

import goorm.nuto.Nuto.Entity.Card;
import goorm.nuto.Nuto.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<List<Card>> findByMember(Member member);
    Optional<Card> findByCardNumber(String cardNumber);
}
