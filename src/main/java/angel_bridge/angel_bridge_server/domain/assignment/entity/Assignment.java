package angel_bridge.angel_bridge_server.domain.assignment.entity;

import angel_bridge.angel_bridge_server.domain.assignment.dto.request.AdminAssignmentRequestDto;
import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import angel_bridge.angel_bridge_server.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE Assignment SET deleted_at = NOW() where assignment_id = ?")
public class Assignment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id", nullable = false)
    private Long id;

    @Column(name = "submission_start_time", nullable = false)
    private LocalDateTime assignmentStartTime;

    @Column(name = "submission_end_time", nullable = false)
    private LocalDateTime assignmentEndTime;

    @Column(name = "assignment_title")
    private String assignmentTitle;

    @Column(name = "assignment_description")
    private String assignmentDescription;

    @Column(name = "assignment_link", nullable = false)
    private String assignmentLink;

    @Column(name = "assignment_round")
    private Integer assignmentRound;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "education_id")
    private Education education;

    public boolean isSubmissionValid(LocalDateTime submissionTime) {
        return !submissionTime.isBefore(assignmentStartTime) && !submissionTime.isAfter(assignmentEndTime);
    }

    public void update(AdminAssignmentRequestDto request) {
        this.assignmentStartTime = request.startTime();
        this.assignmentEndTime = request.endTime();
        this.assignmentRound = request.round();
        this.assignmentTitle = request.title();
        this.assignmentDescription = request.description();
        this.assignmentLink = request.assignmentLink();
    }

    @Builder
    public Assignment(LocalDateTime assignmentStartTime, LocalDateTime assignmentEndTime, String assignmentTitle, String assignmentDescription, String assignmentLink, Integer assignmentRound, Education education) {
        this.assignmentStartTime = assignmentStartTime;
        this.assignmentEndTime = assignmentEndTime;
        this.assignmentTitle = assignmentTitle;
        this.assignmentDescription = assignmentDescription;
        this.assignmentLink = assignmentLink;
        this.assignmentRound = assignmentRound;
        this.education = education;
    }
}
