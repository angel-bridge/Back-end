package angel_bridge.angel_bridge_server.domain.enrollment.service;

import angel_bridge.angel_bridge_server.domain.enrollment.dto.response.EnrollmentResponseDto;
import angel_bridge.angel_bridge_server.domain.enrollment.entity.EnrollmentStatus;
import angel_bridge.angel_bridge_server.global.exception.ApplicationException;
import angel_bridge.angel_bridge_server.global.repository.EnrollmentRepository;
import angel_bridge.angel_bridge_server.global.s3.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.BAD_REQUEST_ERROR;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final ImageService imageService;

    // 자정에 enrollment Status 업데이트
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateEnrollmentStatus() {

        enrollmentRepository.updateEnrollmentStatusToInProgress();
        enrollmentRepository.updateEnrollmentStatusToCompleted();
    }

    // [GET] 수강 중인 프로그램 조회
    public List<EnrollmentResponseDto> getInProgressProgram(int page) {

        if (page == 0)
            throw new ApplicationException(BAD_REQUEST_ERROR);
        Pageable pageable = PageRequest.of(page - 1, 4);

        return enrollmentRepository.findByEnrollmentStatusAndDeletedAtIsNull(EnrollmentStatus.IN_PROGRESS, pageable)
                .map(enrollment -> EnrollmentResponseDto.from(
                        enrollment, imageService.getImageUrl(enrollment.getEducation().getEducationPreImage())))
                .stream().toList();
    }

    // [GET] 수강 예정인 프로그램 조회
    public List<EnrollmentResponseDto> getScheduledProgram(int page) {

        if (page == 0)
            throw new ApplicationException(BAD_REQUEST_ERROR);
        Pageable pageable = PageRequest.of(page - 1, 4);

        return enrollmentRepository.findByEnrollmentStatusAndDeletedAtIsNull(EnrollmentStatus.SCHEDULED, pageable)
                .map(enrollment -> EnrollmentResponseDto.from(
                        enrollment, imageService.getImageUrl(enrollment.getEducation().getEducationPreImage())))
                .stream().toList();
    }

    // [GET] 수강 완료인 프로그램 조회
    public List<EnrollmentResponseDto> getCompletedProgram(int page) {

        if (page == 0)
            throw new ApplicationException(BAD_REQUEST_ERROR);
        Pageable pageable = PageRequest.of(page - 1, 4);

        return enrollmentRepository.findByEnrollmentStatusAndDeletedAtIsNull(EnrollmentStatus.COMPLETED, pageable)
                .map(enrollment -> EnrollmentResponseDto.from(
                        enrollment, imageService.getImageUrl(enrollment.getEducation().getEducationPreImage())))
                .stream().toList();
    }
}
