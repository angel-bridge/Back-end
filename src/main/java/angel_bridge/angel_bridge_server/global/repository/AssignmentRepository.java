package angel_bridge.angel_bridge_server.global.repository;

import angel_bridge.angel_bridge_server.domain.assignment.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    boolean existsByEducationIdAndAssignmentRoundAndDeletedAtIsNull(Long educationId, Integer round);

    Optional<Assignment> findByIdAndDeletedAtIsNull(Long assignmentId);
}
