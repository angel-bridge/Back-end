package angel_bridge.angel_bridge_server.domain.member.controller;

import angel_bridge.angel_bridge_server.domain.member.dto.request.MemberRequestDto;
import angel_bridge.angel_bridge_server.domain.member.dto.response.MemberResponseDto;
import angel_bridge.angel_bridge_server.domain.member.service.AuthService;
import angel_bridge.angel_bridge_server.global.common.response.CommonResponse;
import angel_bridge.angel_bridge_server.global.jwt.JWTUtil;
import angel_bridge.angel_bridge_server.global.oauth2.dto.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급", description = "토큰 재발급 요청 API")
    public CommonResponse<Void> reissue(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = authService.extractRefreshToken(request);
        authService.validateRefreshToken(refreshToken);

        String newAccessToken = authService.reissueAccessToken(refreshToken);
        Cookie RefreshTokenCookie = authService.createRefreshTokenCookie(refreshToken);

        authService.setNewTokens(response, newAccessToken, RefreshTokenCookie);

        return new CommonResponse<>("토큰 재발급을 성공하였습니다");
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입 정보 입력", description = "회원가입 시 초기 정보 입력 받는 API")
    public CommonResponse<MemberResponseDto> saveMemberInfo(@AuthenticationPrincipal CustomOAuth2User userDetails, @Valid @RequestBody MemberRequestDto request) {

        Long memberId = userDetails.getMemberId();

        return new CommonResponse<>(authService.saveMemberInfo(request, memberId), "사용자 추가 정보 입력을 성공하였습니다.");
    }
}
