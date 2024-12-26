package angel_bridge.angel_bridge_server.domain.education.dto.response;

import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record EducationResponseDto(

        @Schema(description = "프로그램 id", example = "1")
        Long educationId,

        @Schema(description = "프리뷰 이미지", example = "프리뷰 이미지 url")
        String preImage,

        @Schema(description = "프로그램 소개", example = "21일 간 9개의 미션 수행 및 피드백")
        String description,

        @Schema(description = "제목", example = "예비 창업 패키지 2주 완성")
        String title,

        @Schema(description = "모집 여부", example = "모집중")
        String recruitmentStatus
) {

    public static EducationResponseDto from(Education education, String preImage) {
        return EducationResponseDto.builder()
                .educationId(education.getId())
                .preImage(preImage)
                .description(education.getEducationDescription())
                .title(education.getEducationTitle())
                .recruitmentStatus(education.getRecruitmentStatus().getDescription())
                .build();
    }
}
