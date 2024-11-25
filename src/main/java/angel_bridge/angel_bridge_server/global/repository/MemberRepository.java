package angel_bridge.angel_bridge_server.global.repository;

import angel_bridge.angel_bridge_server.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByOauthname(String name);


}
