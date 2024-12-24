package angel_bridge.angel_bridge_server.domain.education.dto.response;

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

    public static RecommendationProgramResponse from(Education education, String imageFile) {

        return RecommendationProgramResponse.builder()
                .id(education.getId())
                .preImage(imageFile)
                .title(education.getEducationTitle())
                .description(education.getEducationDescription())
                .recruitmentStatus(education.getRecruitmentStatus().getDescription())
                .build();
    }
}
