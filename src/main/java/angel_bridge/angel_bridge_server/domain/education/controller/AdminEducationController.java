package angel_bridge.angel_bridge_server.domain.education.controller;

import angel_bridge.angel_bridge_server.domain.assignment.dto.request.AdminAssignmentRequestDto;
import angel_bridge.angel_bridge_server.domain.assignment.dto.response.AdminAssignmentResponseDto;
import angel_bridge.angel_bridge_server.domain.assignment.service.AssignmentService;
import angel_bridge.angel_bridge_server.domain.education.dto.request.AdminEducationRequestDto;
import angel_bridge.angel_bridge_server.domain.education.dto.response.AdminEducationResponseDto;
import angel_bridge.angel_bridge_server.domain.education.service.EducationService;
import angel_bridge.angel_bridge_server.global.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin/education")
@RequiredArgsConstructor
@Tag(name = "ADMIN_Education", description = "ADMIN 교육 프로그램 관련 API")
public class AdminEducationController {

    private final EducationService educationService;
    private final AssignmentService assignmentService;

    @Operation(summary = "교육프로그램 생성", description = "하나의 교육프로그램을 생성하는 API")
    @PostMapping
    public CommonResponse<AdminEducationResponseDto> createEducation(@Valid @RequestPart(value = "data") AdminEducationRequestDto request, @RequestPart(value = "preImage") MultipartFile preImage, @RequestPart(value = "detailImage") MultipartFile detailImage) {

        return new CommonResponse<>(educationService.createEducation(request, preImage, detailImage), "하나의 교육프로그램 생성에 성공하였습니다.");
    }

    @Operation(summary = "교육프로그램 정보 수정", description = "하나의 교육프로그램 정보를 수정하는 API")
    @PutMapping("/{educationId}")
    public CommonResponse<AdminEducationResponseDto> updateEducation(@Valid @RequestPart(value = "data") AdminEducationRequestDto request, @RequestPart(value = "preImage", required = false) MultipartFile preImage, @RequestPart(value = "detailImage", required = false) MultipartFile detailImage, @PathVariable Long educationId) {

        return new CommonResponse<>(educationService.updateEducation(request, preImage, detailImage, educationId), "해당 교육프로그램 수정에 성공하였습니다.");
    }

    @Operation(summary = "교육프로그램 삭제", description = "하나의 교육프로그램을 삭제하는 API")
    @DeleteMapping("/{educationId}")
    public CommonResponse<Void> deleteEducation(@PathVariable Long educationId) {

        educationService.deleteEducation(educationId);
        return new CommonResponse<>("해당 교육프로그램 삭제에 성공하였습니다.");
    }

    @Operation(summary = "교육프로그램 미션 생성", description = "특정 교육프로그램 미션을 생성하는 API")
    @PostMapping("/{educationId}/assignment")
    public CommonResponse<AdminAssignmentResponseDto> createAssignment(@PathVariable Long educationId, @Valid @RequestBody AdminAssignmentRequestDto request) {

        return new CommonResponse<>(assignmentService.createAssignment(educationId, request), "하나의 미션 생성에 성공하였습니다.");
    }

    @Operation(summary = "교육프로그램 미션 수정", description = "특정 교육프로그램 미션을 수정하는 API")
    @PutMapping("/{educationId}/assignment/{assignmentId}")
    public CommonResponse<AdminAssignmentResponseDto> updateAssignment(@PathVariable Long educationId, @PathVariable Long assignmentId, @Valid @RequestBody AdminAssignmentRequestDto request) {

        return new CommonResponse<>(assignmentService.updateAssignment(educationId, assignmentId, request), "해당 미션 수정에 성공하였습니다.");
    }

    @Operation(summary = "교육프로그램 미션 삭제", description = "특정 교육프로그램 미션을 삭제하는 API")
    @DeleteMapping("/{educationId}/assignment/{assignmentId}")
    public CommonResponse<Void> deleteAssignment(@PathVariable Long educationId, @PathVariable Long assignmentId) {

        assignmentService.deleteAssignment(educationId, assignmentId);
        return new CommonResponse<>("해당 교육프로그램의 미션 삭제에 성공하였습니다.");
    }
}
