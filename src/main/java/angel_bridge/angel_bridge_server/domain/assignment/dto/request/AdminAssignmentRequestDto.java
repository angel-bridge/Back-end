package angel_bridge.angel_bridge_server.domain.assignment.dto.request;

import angel_bridge.angel_bridge_server.domain.assignment.entity.Assignment;
import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AdminAssignmentRequestDto(
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        @Schema(description = "미션 시작일", example = "2025-01-01T00:00")
        LocalDateTime startTime,

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        @Schema(description = "미션 마감일", example = "2025-01-10T00:00")
        LocalDateTime endTime,

        @NotNull
        @Schema(description = "미션 회차", example = "1")
        Integer round,

        @Schema(description = "미션 제목", example = "창업아이템 배경 및 필요성")
        String title,

        @Schema(description = "미션 설명", example = "창업 아이템의 배경과 필요성을 명확히 하며 시작해봅시다!")
        String description,

        @Schema(description = "미션 자세히보기 링크", example = "https://angelbridge.notion.site/1-1")
        String assignmentLink
) {
        public Assignment toEntity(Education education) {
                return Assignment.builder()
                        .assignmentStartTime(startTime)
                        .assignmentEndTime(endTime)
                        .assignmentTitle(title)
                        .assignmentDescription(description)
                        .assignmentLink(assignmentLink)
                        .assignmentRound(round)
                        .education(education)
                        .build();
        }
}
