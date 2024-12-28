package angel_bridge.angel_bridge_server.domain.member.dto.response;

import angel_bridge.angel_bridge_server.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record MemberResponseDto(

        Long memberId,

        String nickname,

        String email,

        String profileImageUrl,

        @Schema(description = "프로필 이미지 타입", example = "KAKAO | ANGEL")
        String profileImageType,

        String phoneNumber,

        @Schema(description = "사용자 역할", example = "ROLE_USER | ROLE_ADMIN")
        String role,

        @Schema(description = "선택 동의 여부")
        Boolean isSelect,

        @Schema(description = "엔젤브릿지 가입 여부")
        Boolean isRegistered
) {
    public static MemberResponseDto from(Member member) {

        return MemberResponseDto.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileImageUrl(member.getProfileImage())
                .profileImageType(member.getImageType().getDescription())
                .phoneNumber(member.getPhoneNumber())
                .role(member.getRole())
                .isSelect(member.getIsSelect())
                .isRegistered(member.getIsRegistered())
                .build();
    }

    public static MemberResponseDto from(Member member, String imageUrl) {

        return MemberResponseDto.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileImageUrl(imageUrl != null ? imageUrl : member.getProfileImage())
                .profileImageType(member.getImageType().getDescription())
                .phoneNumber(member.getPhoneNumber())
                .role(member.getRole())
                .isSelect(member.getIsSelect())
                .isRegistered(member.getIsRegistered())
                .build();
    }
}
