package angel_bridge.angel_bridge_server.domain.enrollment.dto.response;

import angel_bridge.angel_bridge_server.domain.enrollment.entity.Enrollment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EnrollmentResponseDto(

        @Schema(description = "프로그램 id", example = "1")
        Long educationId,

        @Schema(description = "프리뷰 이미지", example = "프리뷰 이미지 url")
        String preImage,

        @Schema(description = "프로그램 소개", example = "21일 간 9개의 미션 수행 및 피드백")
        String description,

        @Schema(description = "제목", example = "예비 창업 패키지 2주 완성")
        String title,

        @Schema(description = "프로그램 시작 날짜", example = "2024-12-16")
        LocalDate educationStartDate,

        @Schema(description = "프로그램 종료 날짜", example = "2025-01-05")
        LocalDate educationEndDate,

        @Schema(description = "수강 여부", example = "수강예정 | 수강중 | 수강완료")
        String enrollmentStatus
) {

    public static EnrollmentResponseDto from(Enrollment enrollment, String preImage) {

        return EnrollmentResponseDto.builder()
                .educationId(enrollment.getEducation().getId())
                .preImage(preImage)
                .description(enrollment.getEducation().getEducationDescription())
                .title(enrollment.getEducation().getEducationTitle())
                .educationStartDate(enrollment.getEducation().getEducationStartDate())
                .educationEndDate(enrollment.getEducation().getEducationEndDate())
                .enrollmentStatus(enrollment.getEnrollmentStatus().getDescription())
                .build();
    }
}
