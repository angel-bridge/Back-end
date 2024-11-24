package angel_bridge.angel_bridge_server.global.oauth2.dto;

import angel_bridge.angel_bridge_server.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record OAuthUserDTO (
    
        Member member,
        String role,
        String nickname,
        String oauthname
) {
}
