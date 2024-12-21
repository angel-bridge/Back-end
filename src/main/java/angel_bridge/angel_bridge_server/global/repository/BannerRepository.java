package angel_bridge.angel_bridge_server.global.repository;

import angel_bridge.angel_bridge_server.domain.banner.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    Optional<Banner> findByIdAndDeletedAtIsNull(Long bannerId);

    @Query("SELECT b FROM Banner b WHERE b.deletedAt IS NULL AND b.isPost = true ORDER BY b.priority ASC")
    List<Banner> findAllActiveBannersSortedByPriority();
}
