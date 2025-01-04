package angel_bridge.angel_bridge_server.global.repository;

import angel_bridge.angel_bridge_server.domain.assignment.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    boolean existsByEducationIdAndAssignmentRound(Long educationId, Integer round);
}
