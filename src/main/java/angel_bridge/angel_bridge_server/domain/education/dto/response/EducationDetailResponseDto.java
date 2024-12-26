package angel_bridge.angel_bridge_server.domain.education.dto.response;

import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EducationDetailResponseDto(

        @Schema(description = "제목", example = "예비 창업 패키지 2주 완성")
        String title,
        @Schema(description = "프로그램 소개", example = "21일 간 9개의 미션 수행 및 피드백")
        String description,

        @Schema(description = "시작날짜", example = "2024-12-16")
        LocalDate educationStartDate,

        @Schema(description = "종료날짜", example = "2025-01-05")
        LocalDate educationEndDate,

        @Schema(description = "모집 시작일", example = "2024-12-20")
        LocalDate recruitmentStartDate,

        @Schema(description = "모집 마감일", example = "2025-01-05")
        LocalDate recruitmentEndDate,

        @Schema(description = "가격", example = "119,000원")
        String price,

        @Schema(description = "프리뷰 이미지", example = "프리뷰 이미지 url")
        String preFile,

        @Schema(description = "디테일 이미지", example = "디테일 이미지 url")
        String detailFile

) {

        public static EducationDetailResponseDto from(Education education, String preImage, String detailImage) {

                return EducationDetailResponseDto.builder()
                        .title(education.getEducationTitle())
                        .description(education.getEducationDescription())
                        .educationStartDate(education.getEducationStartDate())
                        .educationEndDate(education.getEducationEndDate())
                        .recruitmentStartDate(education.getRecruitmentStartDate())
                        .recruitmentEndDate(education.getRecruitmentEndDate())
                        .price(education.getPrice())
                        .preFile(preImage)
                        .detailFile(detailImage)
                        .build();
        }
}
