package angel_bridge.angel_bridge_server.global.repository;

import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import angel_bridge.angel_bridge_server.domain.education.entity.RecruitmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EducationRepository extends JpaRepository<Education, Long> {

    Optional<Education> findByIdAndDeletedAtIsNull(Long educationId);
    List<Education> findByRecruitmentStatusAndDeletedAtIsNullOrderByRecruitmentEndDateAsc(RecruitmentStatus status);
}
