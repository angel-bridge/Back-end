package angel_bridge.angel_bridge_server.domain.assignment.dto.response;

import angel_bridge.angel_bridge_server.domain.assignment.entity.Assignment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AssignmentResponseDto(

        Long assignmentId,

        Long educationId,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime startTime,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime endTime,

        Integer round,

        String title,

        String description,

        String assignmentLink
) {
    public static AssignmentResponseDto from(Assignment assignment) {

        return AssignmentResponseDto.builder()
                .assignmentId(assignment.getId())
                .educationId(assignment.getEducation().getId())
                .startTime(assignment.getAssignmentStartTime())
                .endTime(assignment.getAssignmentEndTime())
                .round(assignment.getAssignmentRound())
                .title(assignment.getAssignmentTitle())
                .description(assignment.getAssignmentDescription())
                .assignmentLink(assignment.getAssignmentLink())
                .build();
    }
}
