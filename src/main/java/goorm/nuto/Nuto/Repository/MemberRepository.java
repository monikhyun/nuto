package goorm.nuto.Nuto.Repository;

import goorm.nuto.Nuto.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserid(String userid);
    Optional<Member> findByName(String name);
}
