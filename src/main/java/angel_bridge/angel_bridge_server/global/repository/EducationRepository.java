package angel_bridge.angel_bridge_server.global.repository;

import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import angel_bridge.angel_bridge_server.domain.education.entity.RecruitmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EducationRepository extends JpaRepository<Education, Long> {

    @Query("SELECT e FROM Education e WHERE e.deletedAt IS NULL")
    Page<Education> findAllActive(Pageable pageable);

    Optional<Education> findByIdAndDeletedAtIsNull(Long educationId);

    @Query("SELECT e FROM Education e WHERE e.recruitmentStatus = :status AND e.deletedAt IS NULL ORDER BY e.recruitmentEndDate ASC")
    List<Education> findEducationsByStatus(RecruitmentStatus status);

    Page<Education> findByRecruitmentStatusAndDeletedAtIsNull(RecruitmentStatus status, Pageable pageable);

    @Query("SELECT e FROM Education e WHERE e.deletedAt IS NULL AND LOWER(e.educationTitle) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Education> findByTitle(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT e FROM Education e WHERE e.deletedAt IS NULL AND e.recruitmentStatus = :status AND LOWER(e.educationTitle) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Education> findByTitleAndStatus(@Param("keyword") String keyword, @Param("status") RecruitmentStatus status, Pageable pageable);
}
