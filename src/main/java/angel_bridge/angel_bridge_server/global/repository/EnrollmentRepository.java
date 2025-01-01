package angel_bridge.angel_bridge_server.global.repository;

import angel_bridge.angel_bridge_server.domain.enrollment.entity.Enrollment;
import angel_bridge.angel_bridge_server.domain.enrollment.entity.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    Page<Enrollment> findByEnrollmentStatusAndDeletedAtIsNull(EnrollmentStatus status, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Enrollment e SET e.enrollmentStatus = 'IN_PROGRESS' WHERE e.education.educationStartDate <= CURRENT_DATE AND e.enrollmentStatus = 'SCHEDULED'")
    void updateEnrollmentStatusToInProgress();

    @Modifying
    @Transactional
    @Query("UPDATE Enrollment e SET e.enrollmentStatus = 'COMPLETED' WHERE e.education.educationEndDate <= CURRENT_DATE AND e.enrollmentStatus = 'IN_PROGRESS'")
    void updateEnrollmentStatusToCompleted();
}
