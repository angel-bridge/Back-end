package angel_bridge.angel_bridge_server.global.repository;

import angel_bridge.angel_bridge_server.domain.assignment.entity.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    boolean existsByEducationIdAndAssignmentRoundAndDeletedAtIsNull(Long educationId, int round);

    Optional<Assignment> findByIdAndDeletedAtIsNull(Long assignmentId);

    @Query("SELECT MIN(a.assignmentStartTime) FROM Assignment a " +
            "WHERE a.education.id = :educationId " +
            "AND a.assignmentRound > :round " +
            "AND a.deletedAt IS NULL")
    LocalDateTime findEarliestStartDateForNextRounds(@Param("educationId") Long educationId, @Param("round") int round);

    @Query("SELECT MAX(a.assignmentEndTime) FROM Assignment a " +
            "WHERE a.education.id = :educationId " +
            "AND a.assignmentRound < :round " +
            "AND a.deletedAt IS NULL")
    LocalDateTime findLatestEndTimeForBeforeRounds(@Param("educationId") Long educationId, @Param("round") int round);

    @Query("SELECT a FROM Assignment a " +
            "WHERE a.education.id = :educationId " +
            "AND a.deletedAt IS NULL " +
            "AND a.assignmentStartTime <= :now " +
            "AND a.assignmentEndTime >= :now")
    Optional<Assignment> findCurrentAssignmentByEducationId(@Param("educationId") Long educationId, @Param("now") LocalDateTime now);

    Page<Assignment> findByEducationIdAndDeletedAtIsNull(Long educationId, Pageable pageable);
}
