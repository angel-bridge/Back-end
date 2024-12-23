package angel_bridge.angel_bridge_server.global.repository;

import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import angel_bridge.angel_bridge_server.domain.education.entity.RecruitmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EducationRepository extends JpaRepository<Education, Long> {

    Optional<Education> findByIdAndDeletedAtIsNull(Long educationId);

    @Query("SELECT e FROM Education e WHERE e.recruitmentStatus = :status AND e.deletedAt IS NULL ORDER BY e.recruitmentEndDate ASC")
    List<Education> findEducationsByStatus(RecruitmentStatus status);
}
