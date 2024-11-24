package angel_bridge.angel_bridge_server.global.oauth2.dto;

import java.util.Map;

public class KakaoResponse implements OAuth2Response{

    private final Map<String, Object> attribute;

    public KakaoResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {

        return "kakao";
    }

    @Override
    public String getProviderId() {

        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {

        Map<String, Object> account = (Map<String, Object>) attribute.get("kakao_account");

        return "이메일 개인정보를 제공하지 않습니다.";
    }

    @Override
    public String getName() {

        Map<String, Object> account = (Map<String, Object>) attribute.get("kakao_account");

        if (account == null) {
            return null;
        }

        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        if (profile == null) {
            return null;
        }

        return (String) profile.get("nickname");
    }
}
