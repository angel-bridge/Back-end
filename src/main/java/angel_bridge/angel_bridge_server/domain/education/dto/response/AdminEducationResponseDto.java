package angel_bridge.angel_bridge_server.domain.education.dto.response;

import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import angel_bridge.angel_bridge_server.domain.education.entity.RecruitmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Builder
public record AdminEducationResponseDto(

        Long educationId,

        String preFile,

        String description,

        String title,

        LocalDate educationStartDate,

        LocalDate educationEndDate,

        LocalDate recruitmentStartDate,

        LocalDate recruitmentEndDate,

        String price,

        String assignmentNoticeLink,

        String assignmentMethodLink,

        String detailFile,

        String recruitmentStatus
) {

    public static AdminEducationResponseDto from(Education education) {

        return AdminEducationResponseDto.builder()
                .educationId(education.getId())
                .preFile(education.getEducationPreImage())
                .description(education.getEducationDescription())
                .title(education.getEducationTitle())
                .educationStartDate(education.getEducationStartDate())
                .educationEndDate(education.getEducationEndDate())
                .recruitmentStartDate(education.getRecruitmentStartDate())
                .recruitmentEndDate(education.getRecruitmentEndDate())
                .price(education.getPrice())
                .assignmentNoticeLink(education.getNoticeLink())
                .assignmentMethodLink(education.getMethodLink())
                .detailFile(education.getEducationDetailImage())
                .recruitmentStatus(education.getRecruitmentStatus().getDescription())
                .build();
    }
}
