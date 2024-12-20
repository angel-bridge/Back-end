package angel_bridge.angel_bridge_server.global.repository;

import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationRepository extends JpaRepository<Education, Long> {
}
