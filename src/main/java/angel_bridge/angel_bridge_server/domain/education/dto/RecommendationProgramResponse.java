package angel_bridge.angel_bridge_server.domain.education.dto;

import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationProgramResponse {

    private Long id;
    private String preImage;
    private String title;
    private String description;
    private String recruitmentStatus;

    public static RecommendationProgramResponse of(Education education) {

        return RecommendationProgramResponse.builder()
                .id(education.getId())
                .preImage(education.getEducationPreImage())
                .title(education.getEducationTitle())
                .description(education.getEducationDescription())
                .recruitmentStatus(education.getRecruitmentStatus().getDescription())
                .build();
    }
}
