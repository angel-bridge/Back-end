package angel_bridge.angel_bridge_server.global.oauth2;

import angel_bridge.angel_bridge_server.domain.member.entity.Refresh;
import angel_bridge.angel_bridge_server.global.jwt.JWTUtil;
import angel_bridge.angel_bridge_server.global.oauth2.dto.CustomOAuth2User;
import angel_bridge.angel_bridge_server.global.repository.RefreshRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getOAuthname();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 토큰 생성
        String access = jwtUtil.createJwt("access", username, role, 1000L * 60 * 60 * 2);   // 2시간

        // 테스트용 (삭제 예정)
        System.out.println("access = " + access);

        String refresh = jwtUtil.createJwt("refresh", username, role, 1000L * 60 * 60 * 24 * 14);   // 2주

        // 테스트용 (삭제 예정)
        System.out.println("refresh = " + refresh);

        // Refresh 토큰 저장
        addRefreshEntity(username, refresh, 86400000L);

        //응답 설정
        response.setHeader("Authorization", "Bearer " + access);
        response.addCookie(createCookie("refreshToken", refresh));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"result\": \"로그인이 성공적으로 완료되었습니다.\"}");

        // TODO: 프론트 배포 시 수정 예정
        // 로그인 성공 시 이동 경로 지정
        response.sendRedirect("http://localhost:3000/");
    }

    // 프론트에 전달할 방식 : 쿠키 방식
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(1000 * 60 * 60 * 24 * 14);

        // TODO: https로 배포 시 추가 예정
        //cookie.setSecure(true); // https 통신에서만 가능하게 함

        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefreshEntity(String oauthname, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        Refresh refreshEntity = Refresh.builder()
                .oauthname(oauthname)
                .refresh(refresh)
                .expiration(date.toString())
                .build();

        refreshRepository.save(refreshEntity);
    }
}
