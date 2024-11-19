package angel_bridge.angel_bridge_server.domain.member.entity;

import angel_bridge.angel_bridge_server.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String nickname;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(length = 320)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = false, length = 8)
    private LoginType loginType = LoginType.KAKAO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private MemberStatus status = MemberStatus.ACTIVE;

    @Column(name = "inactive_date")
    private LocalDateTime inactiveDate;

}
