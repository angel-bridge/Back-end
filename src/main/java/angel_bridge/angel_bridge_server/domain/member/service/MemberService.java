package angel_bridge.angel_bridge_server.domain.member.service;

import angel_bridge.angel_bridge_server.domain.member.dto.request.AuthRequestDto;
import angel_bridge.angel_bridge_server.domain.member.dto.request.MemberRequestDto;
import angel_bridge.angel_bridge_server.domain.member.dto.response.MemberResponseDto;
import angel_bridge.angel_bridge_server.domain.member.entity.Member;
import angel_bridge.angel_bridge_server.global.exception.ApplicationException;
import angel_bridge.angel_bridge_server.global.repository.MemberRepository;
import angel_bridge.angel_bridge_server.global.s3.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.IMAGE_UPLOAD_ERROR;
import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.NOT_FOUND_USER;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ImageService imageService;

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
    public MemberResponseDto updateMemberInfo(MemberRequestDto request, MultipartFile profileImage, Long memberId) {

        Member member = findMemberById(memberId);

        String imageUrl = member.getProfileImage();

        try {
            if (profileImage != null && !profileImage.isEmpty()) {
                imageUrl = imageService.uploadImage(profileImage);
            }
        } catch (IOException e) {
            log.error("이미지 업로드 중 오류 발생: {}", e.getMessage(), e);
            throw new ApplicationException(IMAGE_UPLOAD_ERROR);
        }

        member.update(request, imageUrl);
        Member updateMember = memberRepository.save(member);

        return MemberResponseDto.from(updateMember);
    }
}
