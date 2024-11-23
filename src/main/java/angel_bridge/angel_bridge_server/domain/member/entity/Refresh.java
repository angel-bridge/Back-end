package angel_bridge.angel_bridge_server.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Refresh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_id", nullable = false)
    private Long id;

    @Column(length = 20, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String refresh;

    @Column(nullable = false)
    private String expiration;

    @Builder
    public Refresh(String nickname, String refresh, String expiration) {
        this.nickname = nickname;
        this.refresh = refresh;
        this.expiration = expiration;
    }
}
