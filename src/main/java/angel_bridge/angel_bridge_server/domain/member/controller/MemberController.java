package angel_bridge.angel_bridge_server.domain.member.controller;

import angel_bridge.angel_bridge_server.domain.member.dto.request.AuthRequestDto;
import angel_bridge.angel_bridge_server.domain.member.dto.request.MemberRequestDto;
import angel_bridge.angel_bridge_server.domain.member.dto.response.MemberResponseDto;
import angel_bridge.angel_bridge_server.domain.member.service.MemberService;
import angel_bridge.angel_bridge_server.global.common.response.CommonResponse;
import angel_bridge.angel_bridge_server.global.oauth2.dto.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 기본 정보 조회", description = "회원의 기본 정보를 조회하는 API")
    @GetMapping
    public CommonResponse<MemberResponseDto> getMemberInfo(@AuthenticationPrincipal CustomOAuth2User userDetails) {

        Long memberId = userDetails.getMemberId();

        return new CommonResponse<>(memberService.getMemberInfo(memberId), "회원 기본 정보 조회를 성공하였습니다.");
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보 수정하는 API")
    @PutMapping
    public CommonResponse<MemberResponseDto> updateMemberInfo(@AuthenticationPrincipal CustomOAuth2User userDetails, MemberRequestDto request) {

        Long memberId = userDetails.getMemberId();

        return new CommonResponse<>(memberService.updateMemberInfo(request, memberId), "회원 정보 수정을 성공하였습니다.");
    }
}
