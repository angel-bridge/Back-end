package angel_bridge.angel_bridge_server.domain.enrollment.service;

import angel_bridge.angel_bridge_server.domain.enrollment.dto.response.EnrollmentResponseDto;
import angel_bridge.angel_bridge_server.domain.enrollment.entity.Enrollment;
import angel_bridge.angel_bridge_server.domain.enrollment.entity.EnrollmentStatus;
import angel_bridge.angel_bridge_server.domain.member.entity.Member;
import angel_bridge.angel_bridge_server.global.common.response.PagedResponseDto;
import angel_bridge.angel_bridge_server.global.exception.ApplicationException;
import angel_bridge.angel_bridge_server.global.repository.EnrollmentRepository;
import angel_bridge.angel_bridge_server.global.repository.MemberRepository;
import angel_bridge.angel_bridge_server.global.s3.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.BAD_REQUEST_ERROR;
import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.NOT_FOUND_USER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final ImageService imageService;
    private final MemberRepository memberRepository;

    // 자정에 enrollment Status 업데이트
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateEnrollmentStatus() {

        enrollmentRepository.updateEnrollmentStatusToInProgress();
        enrollmentRepository.updateEnrollmentStatusToCompleted();
    }

    // [GET] 수강 중인 프로그램 조회
    public PagedResponseDto<EnrollmentResponseDto> getInProgressProgram(int page, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));

        if (page == 0) {
            throw new ApplicationException(BAD_REQUEST_ERROR);
        }

        Pageable pageable = PageRequest.of(page - 1, 4, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Enrollment> enrollmentPage = enrollmentRepository.findByMemberAndEnrollmentStatusAndDeletedAtIsNull(member, EnrollmentStatus.IN_PROGRESS, pageable);

        List<EnrollmentResponseDto> content = enrollmentPage.getContent().stream()
                .map(enrollment -> EnrollmentResponseDto.from(
                        enrollment, imageService.getImageUrl(enrollment.getEducation().getEducationPreImage())))
                .toList();

        return PagedResponseDto.from(
                new PageImpl<>(content, pageable, enrollmentPage.getTotalElements())
        );
    }

    // [GET] 수강 예정인 프로그램 조회
    public PagedResponseDto<EnrollmentResponseDto> getScheduledProgram(int page, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));

        if (page == 0) {
            throw new ApplicationException(BAD_REQUEST_ERROR);
        }

        Pageable pageable = PageRequest.of(page - 1, 4, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Enrollment> enrollmentPage = enrollmentRepository.findByMemberAndEnrollmentStatusAndDeletedAtIsNull(member, EnrollmentStatus.SCHEDULED, pageable);

        List<EnrollmentResponseDto> content = enrollmentPage.getContent().stream()
                .map(enrollment -> EnrollmentResponseDto.from(
                        enrollment, imageService.getImageUrl(enrollment.getEducation().getEducationPreImage())))
                .toList();

        return PagedResponseDto.from(
                new PageImpl<>(content, pageable, enrollmentPage.getTotalElements())
        );
    }

    // [GET] 수강 완료인 프로그램 조회
    public PagedResponseDto<EnrollmentResponseDto> getCompletedProgram(int page, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));

        if (page == 0) {
            throw new ApplicationException(BAD_REQUEST_ERROR);
        }

        Pageable pageable = PageRequest.of(page - 1, 4, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Enrollment> enrollmentPage = enrollmentRepository.findByMemberAndEnrollmentStatusAndDeletedAtIsNull(member, EnrollmentStatus.COMPLETED, pageable);

        List<EnrollmentResponseDto> content = enrollmentPage.getContent().stream()
                .map(enrollment -> EnrollmentResponseDto.from(
                        enrollment, imageService.getImageUrl(enrollment.getEducation().getEducationPreImage())))
                .toList();

        return PagedResponseDto.from(
                new PageImpl<>(content, pageable, enrollmentPage.getTotalElements())
        );
    }
}
