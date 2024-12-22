package angel_bridge.angel_bridge_server.domain.member.service;

import angel_bridge.angel_bridge_server.domain.member.dto.request.AuthRequestDto;
import angel_bridge.angel_bridge_server.domain.member.dto.request.MemberRequestDto;
import angel_bridge.angel_bridge_server.domain.member.dto.response.MemberResponseDto;
import angel_bridge.angel_bridge_server.domain.member.entity.Member;
import angel_bridge.angel_bridge_server.global.exception.ApplicationException;
import angel_bridge.angel_bridge_server.global.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.NOT_FOUND_USER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findMemberById(Long memberId) {
        return memberRepository.findByIdAndDeletedAtIsNull(memberId).orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));
    }

    // [GET] 회원 기본 정보 조회
    @Transactional
    public MemberResponseDto getMemberInfo(Long memberId) {

        Member member = findMemberById(memberId);

        return MemberResponseDto.from(member);
    }

    // [PUT] 회원 정보 수정
    @Transactional
    public MemberResponseDto updateMemberInfo(MemberRequestDto request, Long memberId) {

        Member member = findMemberById(memberId);

        member.update(request);
        Member saveMember = memberRepository.save(member);

        return MemberResponseDto.from(saveMember);
    }
}
