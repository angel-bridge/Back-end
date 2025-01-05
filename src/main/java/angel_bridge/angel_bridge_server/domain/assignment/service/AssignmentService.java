package angel_bridge.angel_bridge_server.domain.assignment.service;

import angel_bridge.angel_bridge_server.domain.assignment.dto.request.AdminAssignmentRequestDto;
import angel_bridge.angel_bridge_server.domain.assignment.dto.response.AdminAssignmentResponseDto;
import angel_bridge.angel_bridge_server.domain.assignment.dto.response.AssignmentResponseDto;
import angel_bridge.angel_bridge_server.domain.assignment.entity.Assignment;
import angel_bridge.angel_bridge_server.domain.assignment.entity.AssignmentStatus;
import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import angel_bridge.angel_bridge_server.global.exception.ApplicationException;
import angel_bridge.angel_bridge_server.global.repository.AssignmentRepository;
import angel_bridge.angel_bridge_server.global.repository.EducationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.*;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final EducationRepository educationRepository;

    public Education findEducationById(Long educationId) {
        return educationRepository.findByIdAndDeletedAtIsNull(educationId).orElseThrow(() -> new ApplicationException(NOT_FOUND_EDUCATION_ID));
    }

    public Assignment findAssignmentById(Long assignmentId) {
        return assignmentRepository.findByIdAndDeletedAtIsNull(assignmentId).orElseThrow(() -> new ApplicationException(NOT_FOUND_ASSIGNMENT_ID));
    }

    /**
     * 미션 기간 유효성 검사
     */
    private void validateDateRange(Long educationId, int round, LocalDateTime startTime, LocalDateTime endTime) {

        if (!startTime.isBefore(endTime)) {
            throw new ApplicationException(INVALID_DATE_RANGE_EXCEPTION);
        }

        // 다음 회차들과의 미션 기간 유효성 검사
        LocalDateTime nextRoundDate = assignmentRepository.findEarliestStartDateForNextRounds(educationId, round);
        if (nextRoundDate != null) {
            if (!startTime.isBefore(nextRoundDate) || !endTime.isBefore(nextRoundDate)) {
                throw new ApplicationException(INVALID_DATE_RANGE_EXCEPTION);
            }
        }

        // 이전 회차들과의 미션 기간 유효성 검사
        LocalDateTime beforeRoundEndTime = assignmentRepository.findLatestEndTimeForBeforeRounds(educationId, round);
        if (beforeRoundEndTime != null && !startTime.isAfter(beforeRoundEndTime)) {
            throw new ApplicationException(INVALID_DATE_RANGE_EXCEPTION);
        }
    }

    /**
     * 미션 회차 유효성 검사
     */
    private void validateDuplicateRound(Long educationId, Assignment assignment, int round) {
        boolean isDuplicateRound = assignmentRepository.existsByEducationIdAndAssignmentRoundAndDeletedAtIsNull(educationId, round)
                && (assignment == null || assignment.getAssignmentRound() != round);
        if (isDuplicateRound) {
            throw new ApplicationException(ALREADY_EXIST_ASSIGNMENT_ROUND_EXCEPTION);
        }
    }

    /**
     * 수정하고자 하는 미션의 교육 프로그램 ID 부합 검사
     */
    private void validateEducationId(Assignment assignment, Long educationId) {
        if (!assignment.getEducation().getId().equals(educationId)) {
            throw new ApplicationException(INVALID_EDUCATION_ID_ASSIGNMENT);
        }
    }

    // [POST] 어드민 회차별 미션 생성
    @Transactional
    public AdminAssignmentResponseDto createAssignment(Long educationId, AdminAssignmentRequestDto request) {

        Education education = findEducationById(educationId);

        validateDuplicateRound(educationId, null, request.round());
        validateDateRange(educationId, request.round(), request.startTime(), request.endTime());

        Assignment saveAssignment = assignmentRepository.save(request.toEntity(education));

        AssignmentStatus status = saveAssignment.getStatus(LocalDateTime.now());
        return AdminAssignmentResponseDto.from(saveAssignment, status);
    }

    // [PUT] 어드민 회차별 미션 수정
    @Transactional
    public AdminAssignmentResponseDto updateAssignment(Long educationId, Long assignmentId, AdminAssignmentRequestDto request) {

        Assignment assignment = findAssignmentById(assignmentId);

        validateEducationId(assignment, educationId);
        validateDuplicateRound(educationId, assignment, request.round());
        validateDateRange(educationId, request.round(), request.startTime(), request.endTime());

        assignment.update(request);

        AssignmentStatus status = assignment.getStatus(LocalDateTime.now());
        return AdminAssignmentResponseDto.from(assignment, status);
    }

    // [DELETE] 어드민 회차별 미션 삭제
    @Transactional
    public void deleteAssignment(Long educationId, Long assignmentId) {

        Assignment assignment = findAssignmentById(assignmentId);

        validateEducationId(assignment, educationId);

        assignmentRepository.delete(assignment);
    }

    // [GET] 미션 수행 현황 상위 박스 정보 조회
    public AssignmentResponseDto getAssignmentBox(Long educationId) {

        Education education = findEducationById(educationId);

        Assignment currentAssignment = assignmentRepository.findCurrentAssignmentByEducationId(educationId, LocalDateTime.now())
                .orElse(null);

        if (currentAssignment == null) {
            return AssignmentResponseDto.fromClosed(education);
        }

        return AssignmentResponseDto.fromOngoing(currentAssignment);
    }
}
