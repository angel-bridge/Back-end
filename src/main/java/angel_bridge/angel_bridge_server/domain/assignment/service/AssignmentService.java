package angel_bridge.angel_bridge_server.domain.assignment.service;

import angel_bridge.angel_bridge_server.domain.assignment.dto.request.AssignmentRequestDto;
import angel_bridge.angel_bridge_server.domain.assignment.dto.response.AssignmentResponseDto;
import angel_bridge.angel_bridge_server.domain.assignment.entity.Assignment;
import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import angel_bridge.angel_bridge_server.global.exception.ApplicationException;
import angel_bridge.angel_bridge_server.global.repository.AssignmentRepository;
import angel_bridge.angel_bridge_server.global.repository.EducationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // [POST] 어드민 회차별 미션 생성
    @Transactional
    public AssignmentResponseDto createAssignment(Long educationId, AssignmentRequestDto request) {

        Education education = findEducationById(educationId);

        // 동일한 회차를 입력할 경우 예외처리
        if (assignmentRepository.existsByEducationIdAndAssignmentRoundAndDeletedAtIsNull(educationId, request.round())) {
            throw new ApplicationException(ALREADY_EXIST_ASSIGNMENT_ROUND_EXCEPTION);
        }

        Assignment saveAssignment = assignmentRepository.save(request.toEntity(education));

        return AssignmentResponseDto.from(saveAssignment);
    }

    // [PUT] 어드민 회차별 미션 수정
    @Transactional
    public AssignmentResponseDto updateAssignment(Long educationId, Long assignmentId, AssignmentRequestDto request) {

        Assignment assignment = findAssignmentById(assignmentId);

        // 올바르지 않는 educationId를 입력할 경우 예외처리
        if (!assignment.getEducation().getId().equals(educationId)) {
            throw new ApplicationException(INVALID_EDUCATION_ID_ASSIGNMENT);
        }

        // 동일한 회차를 입력할 경우 예외처리
        boolean isDuplicateRound = assignmentRepository.existsByEducationIdAndAssignmentRoundAndDeletedAtIsNull(educationId, request.round())
                && !assignment.getAssignmentRound().equals(request.round());
        if (isDuplicateRound) {
            throw new ApplicationException(ALREADY_EXIST_ASSIGNMENT_ROUND_EXCEPTION);
        }

        assignment.update(request);

        return AssignmentResponseDto.from(assignment);

    }
}
