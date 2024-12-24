package angel_bridge.angel_bridge_server.domain.member.service;

import angel_bridge.angel_bridge_server.domain.member.dto.request.AuthRequestDto;
import angel_bridge.angel_bridge_server.domain.member.dto.request.MemberRequestDto;
import angel_bridge.angel_bridge_server.domain.member.dto.response.MemberResponseDto;
import angel_bridge.angel_bridge_server.domain.member.entity.Member;
import angel_bridge.angel_bridge_server.domain.member.entity.ProfileImageType;
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

        String newImage;

        try {
            newImage = updateProfileImage(profileImage, member);
        } catch (IOException e) {
            log.error("이미지 처리 중 오류 발생: {}", e.getMessage(), e);
            throw new ApplicationException(IMAGE_UPLOAD_ERROR);
        }

        member.update(request, newImage);
        Member updateMember = memberRepository.save(member);

        return MemberResponseDto.from(updateMember);
    }

    /**
     * 프로필 이미지 처리 로직 (3가지 경우의 수)
     */
    private String updateProfileImage(MultipartFile profileImage, Member member) throws IOException {

        // 경우 1 ) 프로필 이미지 아예 삭제하는 경우
        if (profileImage == null) {

            // 1-1. 카카오 프로필 삭제하는 경우
            if (member.getImageType().equals(ProfileImageType.KAKAO) || member.getProfileImage() == null) {
                return null;
            }

            // 1-2.기존 프로필 사진 삭제하는 경우
            deleteOldImage(member);
            return null;
        }

        // 경우 2 ) 프로필 이미지 수정하는 경우
        if (!profileImage.isEmpty()) {
            if (member.getImageType().equals(ProfileImageType.KAKAO) || member.getProfileImage() == null) {

                // 이미지 타입을 ANGEL 로 변환
                member.updateImageType();
                memberRepository.save(member);

                // 카카오 이미지 타입은 업로드만 처리
                return imageService.uploadImage(profileImage);
            }

            // 기존 이미지를 삭제한 뒤 새 이미지 업로드
            deleteOldImage(member);
            return imageService.uploadImage(profileImage);
        }

        // 경우 3 ) 프로필 이미지 수정하지 않는 경우
        return member.getProfileImage();
    }

    /**
     * 기존 이미지 삭제하는 로직
     */
    private void deleteOldImage(Member member) {

        String image = member.getProfileImage();
        if (image != null && !image.isEmpty()) {
            imageService.deleteImage(image);
        }
    }
}
