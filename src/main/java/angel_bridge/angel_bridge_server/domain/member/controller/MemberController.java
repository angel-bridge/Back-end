package angel_bridge.angel_bridge_server.domain.member.controller;

import angel_bridge.angel_bridge_server.domain.member.dto.response.MemberResponseDto;
import angel_bridge.angel_bridge_server.domain.member.service.MemberService;
import angel_bridge.angel_bridge_server.global.common.response.CommonResponse;
import angel_bridge.angel_bridge_server.global.oauth2.dto.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @Operation(summary = "회원 기본 정보 조회", description = "회원의 기본 정보를 조회하는 API")
    public CommonResponse<MemberResponseDto> getMemberInfo(@AuthenticationPrincipal CustomOAuth2User userDetails) {

        Long memberId = userDetails.getMemberId();

        return new CommonResponse<>(memberService.getMemberInfo(memberId), "회원 기본 정보 조회를 성공하였습니다");
    }
}
