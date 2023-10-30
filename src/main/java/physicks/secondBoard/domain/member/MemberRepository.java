package physicks.secondBoard.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import physicks.secondBoard.domain.user.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Optional<Member> findMemberById(Long id);

}
