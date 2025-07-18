package goorm.nuto.Nuto.Repository;

import goorm.nuto.Nuto.Entity.Card;
import goorm.nuto.Nuto.Entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<List<Card>> findByMember(Member member);
    Page<Card> findByMemberOrderByIdDesc(Member member, Pageable pageable);
    Optional<Card> findByCardNumber(String cardNumber);
}
