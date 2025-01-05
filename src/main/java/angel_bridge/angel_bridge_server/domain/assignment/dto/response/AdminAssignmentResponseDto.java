package angel_bridge.angel_bridge_server.domain.assignment.dto.response;

import angel_bridge.angel_bridge_server.domain.assignment.entity.Assignment;
import angel_bridge.angel_bridge_server.domain.assignment.entity.AssignmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AdminAssignmentResponseDto(

        Long assignmentId,

        Long educationId,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime startTime,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime endTime,

        int round,

        String title,

        String description,

        String assignmentLink,

        String assignmentStatus
) {
    public static AdminAssignmentResponseDto from(Assignment assignment, AssignmentStatus status) {

        return AdminAssignmentResponseDto.builder()
                .assignmentId(assignment.getId())
                .educationId(assignment.getEducation().getId())
                .startTime(assignment.getAssignmentStartTime())
                .endTime(assignment.getAssignmentEndTime())
                .round(assignment.getAssignmentRound())
                .title(assignment.getAssignmentTitle())
                .description(assignment.getAssignmentDescription())
                .assignmentLink(assignment.getAssignmentLink())
                .assignmentStatus(status.getDescription())
                .build();
    }
}
