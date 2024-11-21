package angel_bridge.angel_bridge_server.domain.member.entity;

import angel_bridge.angel_bridge_server.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE member SET deleted_at = NOW() where id = ?")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long id;

    @Column(length = 20)
    private String name;

    // 카카오에서 넘어오는 이름
    @Column(length = 50, nullable = false)
    private String nickname;

    @Column(length = 320)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = false, length = 8)
    private LoginType loginType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private MemberStatus status;

    @Column
    private String role;

    // 카카오에서 가져온 사용자 특정할 일종의 아이디 요소
    @Column(name = "oauth_name")
    private String oauthname;

    @Column(name = "inactive_date")
    private LocalDateTime inactiveDate;

    @Builder
    public Member(String name, String nickname, String email, String phoneNumber, String loginType, String status, String role, String oauthname, LocalDateTime inactiveDate) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.loginType = (loginType == null || loginType.isEmpty()) ? LoginType.KAKAO : LoginType.valueOf(loginType);
        this.status = (status == null || status.isEmpty()) ? MemberStatus.ACTIVE : MemberStatus.valueOf(status);
        this.role = role;
        this.oauthname = oauthname;
        this.inactiveDate = inactiveDate;
    }
}
