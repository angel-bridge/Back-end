package angel_bridge.angel_bridge_server.domain.member.controller;

import angel_bridge.angel_bridge_server.domain.member.dto.request.AuthRequestDto;
import angel_bridge.angel_bridge_server.domain.member.dto.request.TokenReissueRequestDto;
import angel_bridge.angel_bridge_server.domain.member.dto.response.MemberResponseDto;
import angel_bridge.angel_bridge_server.domain.member.service.AuthService;
import angel_bridge.angel_bridge_server.global.common.response.CommonResponse;
import angel_bridge.angel_bridge_server.global.oauth2.dto.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급", description = "토큰 재발급 요청 API")
    public CommonResponse<Void> reissue(@RequestBody TokenReissueRequestDto requestDto, HttpServletResponse response) {

//        String refreshToken = authService.extractRefreshToken(request);
        String refreshToken = requestDto.refreshToken();
        authService.validateRefreshToken(refreshToken);

        String newAccessToken = authService.reissueAccessToken(refreshToken);
        Cookie RefreshTokenCookie = authService.createRefreshTokenCookie(refreshToken);

        authService.setNewTokens(response, newAccessToken, RefreshTokenCookie);

        return new CommonResponse<>("토큰 재발급을 성공하였습니다");
    }

    @GetMapping("/checkToken")
    public CommonResponse<String> checkToken(HttpServletRequest request) {

        String refreshToken = Optional.ofNullable(request.getCookies())
                .map(Arrays::stream) // 쿠키 배열을 스트림으로 변환
                .orElseGet(Stream::empty) // null인 경우 빈 스트림 반환
                .filter(cookie -> "refreshToken".equals(cookie.getName())) // refreshToken 이름 필터링
                .map(Cookie::getValue) // refreshToken 값 추출
                .findFirst() // 첫 번째 값 반환
                .orElse(null); // 없으면 null 반환

        // refreshToken 값 반환
        if (refreshToken != null) {
            return new CommonResponse<>(refreshToken, "Refresh token found");
        } else {
            return new CommonResponse<>(null, "No refresh token found");
        }
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입 정보 입력", description = "회원가입 시 초기 정보 입력 받는 API")
    public CommonResponse<MemberResponseDto> saveMemberInfo(@AuthenticationPrincipal CustomOAuth2User userDetails, @Valid @RequestBody AuthRequestDto request) {

        Long memberId = userDetails.getMemberId();

        return new CommonResponse<>(authService.saveMemberInfo(request, memberId), "사용자 추가 정보 입력을 성공하였습니다.");
    }
}
