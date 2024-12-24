package angel_bridge.angel_bridge_server.domain.education.dto.response;

import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import lombok.Builder;

@Builder
public record ProgramResponseDto(
        Long educationId,

        String preImage,

        String description,

        String title,
        String recruitmentStatus
) {

    public static ProgramResponseDto from(Education education) {
        return ProgramResponseDto.builder()
                .educationId(education.getId())
                .preImage(education.getEducationPreImage())
                .description(education.getEducationDescription())
                .title(education.getEducationTitle())
                .recruitmentStatus(education.getRecruitmentStatus().getDescription())
                .build();
    }
}
