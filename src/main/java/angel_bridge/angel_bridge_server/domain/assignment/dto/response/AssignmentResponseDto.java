package angel_bridge.angel_bridge_server.domain.assignment.dto.response;

import angel_bridge.angel_bridge_server.domain.assignment.entity.Assignment;
import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record AssignmentResponseDto(
        Long educationId,

        Long assignmentId,

        int assignmentRound,

        String educationTitle,

        String assignmentTitle,

        String description,

        String assignmentLink,

        int performanceRate,

        @Schema(example = "공지사항 링크")
        String noticeLink,

        @Schema(example = "인증방법 링크")
        String methodLink,

        @Schema(example = "교육 프로그램 진행 여부")
        boolean isFinished
) {
    public static AssignmentResponseDto fromOngoing(Assignment assignment) {
        return AssignmentResponseDto.builder()
                .assignmentId(assignment.getId())
                .educationId(assignment.getEducation().getId())
                .assignmentRound(assignment.getAssignmentRound())
                .educationTitle(assignment.getEducation().getEducationTitle())
                .assignmentTitle(assignment.getAssignmentTitle())
                .description(assignment.getAssignmentDescription())
                .assignmentLink(assignment.getAssignmentLink())
                .noticeLink(assignment.getEducation().getNoticeLink())
                .methodLink(assignment.getEducation().getMethodLink())
                .isFinished(false)
                .build();
    }

    public static AssignmentResponseDto fromClosed(Education education) {
        return AssignmentResponseDto.builder()
                .educationId(education.getId())
                .educationTitle(education.getEducationTitle())
                .noticeLink(education.getNoticeLink())
                .methodLink(education.getMethodLink())
                .isFinished(true)
                .build();
    }
}
