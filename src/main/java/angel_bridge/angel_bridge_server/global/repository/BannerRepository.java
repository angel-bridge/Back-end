package angel_bridge.angel_bridge_server.global.repository;

import angel_bridge.angel_bridge_server.domain.banner.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    Optional<Banner> findByIdAndDeletedAtIsNull(Long bannerId);
    List<Banner> findByDeletedAtIsNull();
}
