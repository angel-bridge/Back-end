package angel_bridge.angel_bridge_server.domain.member.service;

import angel_bridge.angel_bridge_server.domain.member.entity.Refresh;
import angel_bridge.angel_bridge_server.global.exception.ApplicationException;
import angel_bridge.angel_bridge_server.global.jwt.JWTUtil;
import angel_bridge.angel_bridge_server.global.repository.MemberRepository;
import angel_bridge.angel_bridge_server.global.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    /**
     * Refresh Token 추출
     * **/
    public String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    return cookie.getValue();
                }
            }
        }
        throw new ApplicationException(NOT_FOUND_REFRESH_TOKEN);
    }

    /**
     * Refresh Token 검증
     * **/
    public void validateRefreshToken(String refreshToken) {

        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new ApplicationException(EXPIRED_PERIOD_REFRESH_TOKEN);
        }

        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals("refresh")) {
            throw new ApplicationException(INVALID_REFRESH_TOKEN);
        }

        Boolean isExist = refreshRepository.existsByRefresh(refreshToken);
        if (!isExist) {
            throw new ApplicationException(INVALID_REFRESH_TOKEN);
        }
    }

    /**
     * Access Token 재발급
     * **/
    public String reissueAccessToken(String refreshToken) {

        String oauthname = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);
        return jwtUtil.createJwt("access", oauthname, role, 1000L * 60 * 60 * 2);
    }

    /**
     * 새로운 Refresh Token 생성
     * **/
    @Transactional
    public Cookie createRefreshTokenCookie(String refreshToken) {

        String oauthname = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);
        String newRefresh = jwtUtil.createJwt("refresh", oauthname, role, 1000L * 60 * 60 * 24 * 14);

        if (oauthname == null) {
            throw new ApplicationException(WRONG_TOKEN_EXCEPTION);
        }

        deleteAndSaveNewRefreshToken(oauthname, newRefresh, 1000L * 60 * 60 * 24 * 14);

        return createCookie("refreshToken", newRefresh);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(1000 * 60 * 60 * 24 * 14);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        // cookie.setSecure(true);

        return cookie;
    }

    /**
     * 기존의 Refresh Token 삭제 후 새 Refresh Token 저장
     **/
    @Transactional
    public void deleteAndSaveNewRefreshToken(String oauthname, String newRefresh, Long expiredMs) {

        refreshRepository.deleteByRefresh(newRefresh);

        addRefreshEntity(oauthname, newRefresh, expiredMs);
    }

    /**
     * 새로운 Refresh Token 저장하는 메서드
     **/
    @Transactional
    public void addRefreshEntity(String oauthname, String refresh, Long expiredMs) {

        Date expirationDate = new Date(System.currentTimeMillis() + expiredMs);

        Refresh refreshEntity = Refresh.builder()
                .oauthname(oauthname)
                .refresh(refresh)
                .expiration(expirationDate.toString())
                .build();

        refreshRepository.save(refreshEntity);
    }

    /**
     * 응답 헤더 및 쿠키 설정
     * **/
    public void setNewTokens(HttpServletResponse response, String newAccessToken, Cookie refreshCookie) {

        response.setHeader("Authorization", "Bearer " + newAccessToken);

        response.addCookie(refreshCookie);
    }
}
