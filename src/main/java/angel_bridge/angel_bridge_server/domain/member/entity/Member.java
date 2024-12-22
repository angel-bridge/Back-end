package angel_bridge.angel_bridge_server.domain.member.entity;

import angel_bridge.angel_bridge_server.domain.member.dto.request.AuthRequestDto;
import angel_bridge.angel_bridge_server.domain.member.dto.request.MemberRequestDto;
import angel_bridge.angel_bridge_server.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE Member SET deleted_at = NOW() where member_id = ?")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long id;

    // 사용자 실명
    @Column(length = 20)
    private String name;

    // 카카오에서 넘어오는 이름
    @Column(length = 50, nullable = false)
    private String nickname;

    @Column(length = 320)
    private String email;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = false, length = 8)
    private LoginType loginType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MemberStatus status;

    @Column
    private String role;

    @Column(name = "is_select")
    private Boolean isSelect;

    // 카카오에서 가져온 사용자 특정할 일종의 아이디 요소
    @Column(name = "oauth_name")
    private String oauthname;

    @Column(name = "is_registered")
    private Boolean isRegistered;

    public void update(String nickname) {
        this.nickname = nickname;
    }

    public void update(AuthRequestDto request) {
        this.email = request.email();
        this.phoneNumber = request.phoneNumber();
        this.isSelect = request.isSelect();
        this.isRegistered = true;
    }

    public void update(MemberRequestDto request) {
        this.nickname = request.nickname();
        this.email = request.email();
        this.phoneNumber = request.phoneNumber();
    }

    @Builder
    public Member(String name, String nickname, String email, String profileImage, String phoneNumber, LoginType loginType, MemberStatus status, String role, Boolean isSelect, String oauthname, Boolean isRegistered) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
        this.phoneNumber = phoneNumber;
        this.loginType = (loginType == null ? LoginType.KAKAO : loginType);
        this.status = (status == null ? MemberStatus.ACTIVE : status);
        this.role = role;
        this.isSelect = isSelect;
        this.oauthname = oauthname;
        this.isRegistered = (isRegistered == null ? false : isRegistered);
    }
}
