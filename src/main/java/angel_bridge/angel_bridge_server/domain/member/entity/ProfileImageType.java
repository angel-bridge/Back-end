package angel_bridge.angel_bridge_server.domain.member.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ProfileImageType {
    KAKAO("KAKAO"),
    ANGEL("ANGEL");

    private final String description;

    ProfileImageType(String description) {
        this.description = description;
    }
}
