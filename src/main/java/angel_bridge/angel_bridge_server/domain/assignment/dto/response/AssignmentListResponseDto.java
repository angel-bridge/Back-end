package angel_bridge.angel_bridge_server.domain.assignment.dto.response;

import angel_bridge.angel_bridge_server.domain.assignment.entity.Assignment;
import angel_bridge.angel_bridge_server.domain.assignment.entity.AssignmentStatus;
import angel_bridge.angel_bridge_server.domain.submission.entity.AttendanceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AssignmentListResponseDto(
        Long assignmentId,

        int round,

        LocalDateTime startTime,

        LocalDateTime endTime,

        @Schema(example = "과거회차 | 오늘회차 | 공개예정회차")
        String assignmentStatus,

        @Schema(example = "출석 | 지각 | 결석 | 미제출")
        String attendanceStatus
) {
    public static AssignmentListResponseDto from(Assignment assignment, AssignmentStatus assignmentStatus, AttendanceStatus attendanceStatus) {

        return AssignmentListResponseDto.builder()
                .assignmentId(assignment.getId())
                .round(assignment.getAssignmentRound())
                .startTime(assignment.getAssignmentStartTime())
                .endTime(assignment.getAssignmentEndTime())
                .assignmentStatus(assignmentStatus.getDescription())
                .attendanceStatus(attendanceStatus.getDescription())
                .build();
    }
}
