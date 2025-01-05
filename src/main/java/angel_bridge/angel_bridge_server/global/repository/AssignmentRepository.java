package angel_bridge.angel_bridge_server.global.repository;

import angel_bridge.angel_bridge_server.domain.assignment.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    boolean existsByEducationIdAndAssignmentRoundAndDeletedAtIsNull(Long educationId, Integer round);

    Optional<Assignment> findByIdAndDeletedAtIsNull(Long assignmentId);

    @Query("SELECT MIN(a.assignmentStartTime) FROM Assignment a " +
            "WHERE a.education.id = :educationId " +
            "AND a.assignmentRound > :round " +
            "AND a.deletedAt IS NULL")
    LocalDateTime findEarliestStartDateForNextRounds(@Param("educationId") Long educationId, @Param("round") Integer round);

    @Query("SELECT MAX(a.assignmentEndTime) FROM Assignment a " +
            "WHERE a.education.id = :educationId " +
            "AND a.assignmentRound < :round " +
            "AND a.deletedAt IS NULL")
    LocalDateTime findLatestEndTimeForBeforeRounds(@Param("educationId") Long educationId, @Param("round") Integer round);
}
