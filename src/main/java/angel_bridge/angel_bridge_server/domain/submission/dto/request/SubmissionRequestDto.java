package angel_bridge.angel_bridge_server.domain.submission.dto.request;

import angel_bridge.angel_bridge_server.domain.assignment.entity.Assignment;
import angel_bridge.angel_bridge_server.domain.member.entity.Member;
import angel_bridge.angel_bridge_server.domain.submission.entity.AttendanceStatus;
import angel_bridge.angel_bridge_server.domain.submission.entity.Submission;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record SubmissionRequestDto(

        @NotBlank
        @Schema(description = "과제 제출 링크", example = "https://notion.com/submission")
        String submissionLink
) {
    public Submission toEntity(Assignment assignment, Member member, AttendanceStatus attendanceStatus) {
        return Submission.builder()
                .submissionLink(submissionLink)
                .attendanceStatus(attendanceStatus)
                .assignment(assignment)
                .member(member)
                .build();
    }
}
