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

import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.ALREADY_EXIST_ASSIGNMENT_ROUND_EXCEPTION;
import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.NOT_FOUND_EDUCATION_ID;

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

    // [POST] 회차별 미션 생성
    @Transactional
    public AssignmentResponseDto createAssignments(Long educationId, AssignmentRequestDto request) {

        Education education = findEducationById(educationId);

        // 동일한 회차를 입력할 경우 예외처리
        if (assignmentRepository.existsByEducationIdAndAssignmentRoundAndDeletedAtIsNull(educationId, request.round())) {
            throw new ApplicationException(ALREADY_EXIST_ASSIGNMENT_ROUND_EXCEPTION);
        }
        Assignment saveAssignment = assignmentRepository.save(request.toEntity(education));

        return AssignmentResponseDto.from(saveAssignment);
    }
}
