package angel_bridge.angel_bridge_server.global.oauth2.service;

import angel_bridge.angel_bridge_server.domain.member.entity.Member;
import angel_bridge.angel_bridge_server.global.oauth2.dto.CustomOAuth2User;
import angel_bridge.angel_bridge_server.global.oauth2.dto.KakaoResponse;
import angel_bridge.angel_bridge_server.global.oauth2.dto.OAuth2Response;
import angel_bridge.angel_bridge_server.global.oauth2.dto.OAuthUserDTO;
import angel_bridge.angel_bridge_server.global.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 테스트 용
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("kakao")) {

            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());

        } else {
            System.out.println("해당하지 않는 registrationId: " + registrationId);
            return null;
        }

        // 리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String oauthname = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();

        Member existData = memberRepository.findByOauthname(oauthname);

        if (existData == null) {

            Member member = Member.builder()
                    .nickname(oAuth2Response.getName())
                    .role("ROLE_USER")
                    .build();

            memberRepository.save(member);

            OAuthUserDTO oAuthUserDTO = OAuthUserDTO.builder()
                    .oauthname(oauthname)
                    .nickname(oAuth2Response.getName())
                    .role("ROLE_USER")
                    .build();

            return new CustomOAuth2User(oAuthUserDTO);
        }
        else {

            existData.update(oAuth2Response.getName());

            memberRepository.save(existData);

            OAuthUserDTO oAuthUserDTO = OAuthUserDTO.builder()
                    .oauthname(existData.getOauthname())
                    .nickname(oAuth2Response.getName())
                    .role(existData.getRole())
                    .build();

            return new CustomOAuth2User(oAuthUserDTO);
        }
    }
}
