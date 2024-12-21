package angel_bridge.angel_bridge_server.domain.education.dto.request;

import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import angel_bridge.angel_bridge_server.domain.education.entity.RecruitmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record AdminEducationRequestDto(

        @Schema(description = "썸네일 한줄 소개", example = "프로그램 한 줄 소개입니다.")
        String description,

        @Schema(description = "제목", example = "예비 창업 패키지 2주 완성")
        String title,

        @Schema(description = "시작날짜", example = "2024-12-16")
        LocalDate educationStartDate,

        @Schema(description = "종료날짜", example = "2025-01-05")
        LocalDate educationEndDate,

        @Schema(description = "모집 시작일", example = "2024-12-20")
        LocalDate recruitmentStartDate,

        @Schema(description = "모집 마감일", example = "2025-01-05")
        LocalDate recruitmentEndDate,

        @Schema(description = "가격", example = "119,000원")
        String price
) {

    public Education toEntity(String preFile, String detailFile) {

        return Education.builder()
                .educationPreImage(preFile)
                .educationDescription(this.description)
                .educationTitle(this.title)
                .educationStartDate(this.educationStartDate)
                .educationEndDate(this.educationEndDate)
                .recruitmentStartDate(this.recruitmentStartDate)
                .recruitmentEndDate(this.recruitmentEndDate)
                .price(this.price)
                .educationDetailImage(detailFile)
                .recruitmentStatus(this.recruitmentStartDate.isAfter(LocalDate.now()) ? RecruitmentStatus.UPCOMING : RecruitmentStatus.ONGOING)
                .build();

    }
}
