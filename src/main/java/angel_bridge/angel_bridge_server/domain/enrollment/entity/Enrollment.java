package angel_bridge.angel_bridge_server.domain.enrollment.entity;

import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import angel_bridge.angel_bridge_server.domain.member.entity.Member;
import angel_bridge.angel_bridge_server.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Enrollment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "education_id")
    private Education education;

    @Column(name = "is_paid")
    private boolean isPaid;

    @Enumerated(EnumType.STRING)
    @Column(name = "enrollment_status")
    private EnrollmentStatus enrollmentStatus;

    @Builder
    public Enrollment(Member member, Education education, boolean isPaid, EnrollmentStatus enrollmentStatus) {
        this.member = member;
        this.education = education;
        this.isPaid = isPaid;
        this.enrollmentStatus = enrollmentStatus;
    }

}
