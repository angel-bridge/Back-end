package angel_bridge.angel_bridge_server.domain.member.controller;

import angel_bridge.angel_bridge_server.domain.member.service.AuthService;
import angel_bridge.angel_bridge_server.global.common.response.CommonResponse;
import angel_bridge.angel_bridge_server.global.jwt.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "카카오 소셜로그인 관련 API")
public class AuthController {

    private final AuthService authService;
    private final JWTUtil jwtUtil;

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급", description = "토큰 재발급 요청 API")
    public CommonResponse<Void> reissue(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = authService.extractRefreshToken(request);
        authService.validateRefreshToken(refreshToken);

        String newAccessToken = authService.reissueAccessToken(refreshToken);
        Cookie RefreshTokenCookie = authService.createRefreshTokenCookie(refreshToken);

        authService.setNewTokens(response, newAccessToken, RefreshTokenCookie);

        return new CommonResponse<>("토근 재발급을 성공하였습니다");
    }
}
