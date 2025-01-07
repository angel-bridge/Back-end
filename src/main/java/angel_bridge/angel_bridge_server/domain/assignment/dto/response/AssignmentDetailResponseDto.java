package angel_bridge.angel_bridge_server.domain.assignment.dto.response;

import angel_bridge.angel_bridge_server.domain.assignment.entity.Assignment;
import angel_bridge.angel_bridge_server.domain.submission.entity.Submission;
import lombok.Builder;

@Builder
public record AssignmentDetailResponseDto(

        Long assignmentId,

        int round,

        String title,

        String description,

        String submissionLink,

        String status
) {
    public static AssignmentDetailResponseDto from(Assignment assignment, Submission submission) {
        return AssignmentDetailResponseDto.builder()
                .assignmentId(assignment.getId())
                .round(assignment.getAssignmentRound())
                .title(assignment.getAssignmentTitle())
                .description(assignment.getAssignmentDescription())
                .submissionLink(submission != null ? submission.getSubmissionLink() : null)
                .status(submission != null ? submission.getAttendanceStatus().getDescription() : null)
                .build();
    }
}
