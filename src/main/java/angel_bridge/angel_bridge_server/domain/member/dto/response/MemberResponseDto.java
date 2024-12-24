package angel_bridge.angel_bridge_server.domain.member.dto.response;

import angel_bridge.angel_bridge_server.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
public record MemberResponseDto(

        Long memberId,

        String nickname,

        String email,

        String profileImageUrl,

        String phoneNumber,

        String role,

        Boolean isSelect,

        Boolean isRegistered
) {
    public static MemberResponseDto from(Member member) {

        return MemberResponseDto.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileImageUrl(member.getProfileImage())
                .phoneNumber(member.getPhoneNumber())
                .role(member.getRole())
                .isSelect(member.getIsSelect())
                .isRegistered(member.getIsRegistered())
                .build();
    }
}
