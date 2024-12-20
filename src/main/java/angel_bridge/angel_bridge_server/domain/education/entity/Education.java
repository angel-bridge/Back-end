package angel_bridge.angel_bridge_server.domain.education.entity;

import angel_bridge.angel_bridge_server.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Enumerated(EnumType.STRING)
    @Column(name = "recruitment_status")
    private RecruitmentStatus recruitmentStatus;

    @Builder
    public Education(String educationPreImage, String educationDetailImage, String educationDescription, String educationTitle, LocalDate educationStartDate, LocalDate educationEndDate, LocalDate recruitmentStartDate, LocalDate recruitmentEndDate, String price, RecruitmentStatus recruitmentStatus) {
        this.educationPreImage = educationPreImage;
        this.educationDetailImage = educationDetailImage;
        this.educationDescription = educationDescription;
        this.educationTitle = educationTitle;
        this.educationStartDate = educationStartDate;
        this.educationEndDate = educationEndDate;
        this.recruitmentStartDate = recruitmentStartDate;
        this.recruitmentEndDate = recruitmentEndDate;
        this.price = price;
        this.recruitmentStatus = RecruitmentStatus.UPCOMING;
    }
}

