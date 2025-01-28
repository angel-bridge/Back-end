package angel_bridge.angel_bridge_server.domain.assignment.service;

import angel_bridge.angel_bridge_server.domain.assignment.dto.request.AdminAssignmentRequestDto;
import angel_bridge.angel_bridge_server.domain.assignment.dto.response.*;
import angel_bridge.angel_bridge_server.domain.assignment.entity.Assignment;
import angel_bridge.angel_bridge_server.domain.assignment.entity.AssignmentStatus;
import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import angel_bridge.angel_bridge_server.domain.member.entity.Member;
import angel_bridge.angel_bridge_server.domain.submission.dto.request.SubmissionRequestDto;
import angel_bridge.angel_bridge_server.domain.submission.entity.AttendanceStatus;
import angel_bridge.angel_bridge_server.domain.submission.entity.Submission;
import angel_bridge.angel_bridge_server.global.common.response.PagedResponseDto;
import angel_bridge.angel_bridge_server.global.exception.ApplicationException;
import angel_bridge.angel_bridge_server.global.repository.AssignmentRepository;
import angel_bridge.angel_bridge_server.global.repository.EducationRepository;
import angel_bridge.angel_bridge_server.global.repository.MemberRepository;
import angel_bridge.angel_bridge_server.global.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final SubmissionRepository submissionRepository;
    private final MemberRepository memberRepository;

    public Education findEducationById(Long educationId) {
        return educationRepository.findByIdAndDeletedAtIsNull(educationId).orElseThrow(() -> new ApplicationException(NOT_FOUND_EDUCATION_ID));
    }

    public Assignment findAssignmentById(Long assignmentId, Long educationId) {
        return assignmentRepository.findByIdAndEducationIdAndDeletedAtIsNull(assignmentId, educationId).orElseThrow(() -> new ApplicationException(NOT_FOUND_ASSIGNMENT_ID));
    }

    public Member findMemberById(Long memberId) {
        return memberRepository.findByIdAndDeletedAtIsNull(memberId).orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));
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
     * 미션 수행도 계산 로직
     */
    private int calculatePerformanceRate(Long educationId, Long memberId) {

        // 출석 제출 개수
        int onTimeCount = submissionRepository.countByEducationIdAndAttendanceStatus(educationId, memberId, AttendanceStatus.ON_TIME);

        // 지각 제출 개수
        int lateCount = submissionRepository.countByEducationIdAndAttendanceStatus(educationId, memberId, AttendanceStatus.LATE);

        // 전체 미션 개수
        int totalAssignments = assignmentRepository.countByEducationIdAndDeletedAtIsNull(educationId);

        if (totalAssignments == 0) {
            return 0;
        }

        double rate = ((onTimeCount + (lateCount * 0.5)) / totalAssignments) * 100;
        return (int) Math.round(rate);
    }

    /**
     * [과제 미제출] AttendanceStatus 계산 메서드
     */
    private AttendanceStatus calculateAttendanceStatus(Assignment assignment) {
        LocalDateTime now = LocalDateTime.now();

        // 현재 시간이 endTime 이후면 ABSENT
        if (now.isAfter(assignment.getAssignmentEndTime())) {
            return AttendanceStatus.ABSENT;
        }

        return AttendanceStatus.PENDING;
    }

    /**
     * [과제 제출] AttendanceStatus 계산 메서드
     */
    private AttendanceStatus calculateSubmitStatus(Assignment assignment) {
        LocalDateTime now = LocalDateTime.now();

        // 현재 시간이 endTime 이후면 LATE
        if (now.isAfter(assignment.getAssignmentEndTime())) {
            return AttendanceStatus.LATE;
        }

        return AttendanceStatus.ON_TIME;
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

        Assignment assignment = findAssignmentById(assignmentId, educationId);

        validateDuplicateRound(educationId, assignment, request.round());
        validateDateRange(educationId, request.round(), request.startTime(), request.endTime());

        assignment.update(request);

        AssignmentStatus status = assignment.getStatus(LocalDateTime.now());
        return AdminAssignmentResponseDto.from(assignment, status);
    }

    // [DELETE] 어드민 회차별 미션 삭제
    @Transactional
    public void deleteAssignment(Long educationId, Long assignmentId) {

        Assignment assignment = findAssignmentById(assignmentId, educationId);

        assignmentRepository.delete(assignment);
    }

    // [GET] 미션 수행 현황 상위 박스 정보 조회
    public AssignmentResponseDto getAssignmentBox(Long educationId, Long memberId) {

        Education education = findEducationById(educationId);

        Assignment currentAssignment = assignmentRepository.findCurrentAssignmentByEducationId(educationId, LocalDateTime.now())
                .orElse(null);

        int performanceRate = calculatePerformanceRate(educationId, memberId);

        if (currentAssignment == null) {
            return AssignmentResponseDto.fromClosed(education, performanceRate);
        }

        return AssignmentResponseDto.fromOngoing(currentAssignment, performanceRate);
    }

    // [GET] 미션 수행 현황 전체 과제 리스트 조회
    public PagedResponseDto<AssignmentListResponseDto> getAllAssignments(Long educationId, int page, Long memberId) {

        if (page == 0)
            throw new ApplicationException(BAD_REQUEST_ERROR);
        Pageable pageable = PageRequest.of(page - 1, 7, Sort.by(Sort.Direction.ASC, "assignmentRound"));

        Page<Assignment> assignmentPage = assignmentRepository.findByEducationIdAndDeletedAtIsNull(educationId, pageable);

        List<AssignmentListResponseDto> assignments = assignmentPage.getContent().stream()
                .map(assignment -> {
                    AttendanceStatus attendanceStatus = submissionRepository.findAttendanceStatusByAssignmentIdAndMemberId(assignment.getId(), memberId)
                            .orElseGet(() -> calculateAttendanceStatus(assignment));
                    AssignmentStatus assignmentStatus = assignment.getStatus(LocalDateTime.now());
                    return AssignmentListResponseDto.from(assignment, assignmentStatus, attendanceStatus);
                })
                .toList();

        return new PagedResponseDto<>(
                assignmentPage.getTotalElements(),
                assignmentPage.getNumber() + 1,
                assignmentPage.getTotalPages(),
                assignments
        );
    }

    // [GET] 개별 미션 조회
    public AssignmentDetailResponseDto getAssignmentInfo(Long educationId, Long assignmentId, Long memberId, String status) {

        Assignment assignment = findAssignmentById(assignmentId, educationId);

        Submission submission = null;
        if (status.equals("ONTIME") || status.equals("LATE")) {
            submission = submissionRepository.findByAssignmentIdAndMemberId(assignmentId, memberId)
                    .orElseThrow(() -> new ApplicationException(SUBMISSION_NOT_FOUND));
        }

        return AssignmentDetailResponseDto.from(assignment, submission);
    }

    // [PATCH] 과제 제출하기
    @Transactional
    public AssignmentDetailResponseDto submitAssignment(Long educationId, Long assignmentId, Long memberId, SubmissionRequestDto request) {

        Member member = findMemberById(memberId);
        Assignment assignment = findAssignmentById(assignmentId, educationId);

        if (submissionRepository.existsByAssignmentIdAndMemberId(assignmentId, memberId)) {
            throw new ApplicationException(ALREADY_EXIST_EXCEPTION);
        }

        AttendanceStatus attendanceStatus = calculateSubmitStatus(assignment);

        Submission submission = request.toEntity(assignment, member, attendanceStatus);
        submissionRepository.save(submission);

        return AssignmentDetailResponseDto.from(assignment, submission);
    }
}
