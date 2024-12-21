package angel_bridge.angel_bridge_server.domain.education.entity;

import angel_bridge.angel_bridge_server.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
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

    @Column(name = "education_description")
    private String educationDescription;

    @Column(name = "education_title")
    private String educationTitle;

    @Column(name = "education_start_date")
    private LocalDate educationStartDate;

    @Column(name = "education_end_date")
    private LocalDate educationEndDate;

    @Column(name = "recruitment_end_date")
    private LocalDate recruitmentEndDate;

    @Column(name = "price")
    private String price;

    @Column(name = "recruitment_status")
    private RecruitmentStatus recruitmentStatus;

}
