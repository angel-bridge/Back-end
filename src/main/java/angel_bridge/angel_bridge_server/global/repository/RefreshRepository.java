package angel_bridge.angel_bridge_server.global.repository;

import angel_bridge.angel_bridge_server.domain.member.entity.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository extends JpaRepository<Refresh, String> {

    Boolean existsByRefresh(String refresh);

    @Transactional
    void deleteByOauthname(String oauthname);

    @Transactional
    void deleteByRefresh(String refresh);
}
