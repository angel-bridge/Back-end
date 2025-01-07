package angel_bridge.angel_bridge_server.domain.submission.entity;

import angel_bridge.angel_bridge_server.domain.assignment.entity.Assignment;
import angel_bridge.angel_bridge_server.domain.member.entity.Member;
import angel_bridge.angel_bridge_server.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Submission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_id", nullable = false)
    private Long id;

    @Column(name = "submission_link")
    private String submissionLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_status", length = 10, nullable = false)
    private AttendanceStatus attendanceStatus = AttendanceStatus.ON_TIME;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @Builder
    public Submission(String submissionLink, AttendanceStatus attendanceStatus, Member member, Assignment assignment) {
        this.submissionLink = submissionLink;
        this.attendanceStatus = attendanceStatus;
        this.member = member;
        this.assignment = assignment;
    }
}
