package angel_bridge.angel_bridge_server.domain.education.repository;

import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import angel_bridge.angel_bridge_server.domain.education.entity.RecruitmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducationRepository extends JpaRepository<Long, Education> {

    List<Education> findByRecruitmentStatusOrderByRecruitmentEndDateAsc(RecruitmentStatus recruitmentStatus);
}
