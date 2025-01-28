package angel_bridge.angel_bridge_server.domain.education.entity;

import angel_bridge.angel_bridge_server.domain.education.dto.request.AdminEducationRequestDto;
import angel_bridge.angel_bridge_server.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE Education SET deleted_at = NOW() where education_id = ?")
public class Education extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "education_id", nullable = false)
    private Long id;

    @Column(name = "education_preview_image")
    private String educationPreImage;

    @Column(name = "education_detail_image")
    private String educationDetailImage;

    @Column(name = "education_description")
    private String educationDescription;

    @Column(name = "education_title")
    private String educationTitle;

    @Column(name = "education_start_date")
    private LocalDate educationStartDate;

    @Column(name = "education_end_date")
    private LocalDate educationEndDate;

    @Column(name = "recruitment_start_date")
    private LocalDate recruitmentStartDate;

    @Column(name = "recruitment_end_date")
    private LocalDate recruitmentEndDate;

    @Column(name = "price")
    private String price;

    @Column(name = "notice_link")
    private String noticeLink;

    @Column(name = "method_link")
    private String methodLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "recruitment_status")
    private RecruitmentStatus recruitmentStatus;

    public void setRecruitmentStatus(RecruitmentStatus status) {
        this.recruitmentStatus = status;
    }

    public void update(AdminEducationRequestDto request, String preFile, String detailFile) {
        this.educationPreImage = preFile;
        this.educationDetailImage = detailFile;
        this.educationDescription = request.description();
        this.educationTitle = request.title();
        this.educationStartDate = request.educationStartDate();
        this.educationEndDate = request.educationEndDate();
        this.recruitmentStartDate = request.recruitmentStartDate();
        this.recruitmentEndDate = request.recruitmentEndDate();
        this.price = request.price();
        this.recruitmentStatus = this.recruitmentStartDate.isAfter(LocalDate.now()) ? RecruitmentStatus.UPCOMING : RecruitmentStatus.ONGOING;
    }

    @Builder
    public Education(String educationPreImage, String educationDetailImage, String educationDescription, String educationTitle, LocalDate educationStartDate, LocalDate educationEndDate, LocalDate recruitmentStartDate, LocalDate recruitmentEndDate, String price, String noticeLink, String methodLink, RecruitmentStatus recruitmentStatus) {
        this.educationPreImage = educationPreImage;
        this.educationDetailImage = educationDetailImage;
        this.educationDescription = educationDescription;
        this.educationTitle = educationTitle;
        this.educationStartDate = educationStartDate;
        this.educationEndDate = educationEndDate;
        this.recruitmentStartDate = recruitmentStartDate;
        this.recruitmentEndDate = recruitmentEndDate;
        this.price = price;
        this.noticeLink = noticeLink;
        this.methodLink = methodLink;
        this.recruitmentStatus = recruitmentStatus;
    }
}

