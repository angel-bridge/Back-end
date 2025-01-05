package angel_bridge.angel_bridge_server.global.repository;

import angel_bridge.angel_bridge_server.domain.submission.entity.AttendanceStatus;
import angel_bridge.angel_bridge_server.domain.submission.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    @Query("SELECT s.attendanceStatus FROM Submission s WHERE s.assignment.id = :assignmentId AND s.member.id = :memberId")
    Optional<AttendanceStatus> findAttendanceStatusByAssignmentIdAndMemberId(Long assignmentId, Long memberId);

    @Query("SELECT COUNT(s) FROM Submission s " +
            "WHERE s.assignment.education.id = :educationId " +
            "AND s.member.id = :memberId " +
            "AND s.attendanceStatus = :status")
    int countByEducationIdAndAttendanceStatus(Long educationId, Long memberId, AttendanceStatus status);
}
