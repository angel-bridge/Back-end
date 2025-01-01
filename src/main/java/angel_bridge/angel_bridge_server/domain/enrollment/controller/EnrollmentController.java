package angel_bridge.angel_bridge_server.domain.enrollment.controller;

import angel_bridge.angel_bridge_server.domain.enrollment.dto.response.EnrollmentResponseDto;
import angel_bridge.angel_bridge_server.domain.enrollment.service.EnrollmentService;
import angel_bridge.angel_bridge_server.global.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/enrollment")
@Tag(name = "USER_Enrollment", description = "USER 프로그램 수강 관련 API")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Operation(summary = "수강예정 프로그램 조회", description = "수강 예정인 프로그램 조회하는 API")
    @GetMapping("/scheduled")
    public CommonResponse<List<EnrollmentResponseDto>> getScheduledProgram(@RequestParam(defaultValue = "1") int page) {

        return new CommonResponse<>(enrollmentService.getScheduledProgram(page), "수강 예정인 프로그램 조회에 성공하였습니다.");
    }

    @Operation(summary = "수강중 프로그램 조회", description = "수강 중인 프로그램 조회하는 API")
    @GetMapping("/inprogress")
    public CommonResponse<List<EnrollmentResponseDto>> getInProgressProgram(@RequestParam(defaultValue = "1") int page) {

        return new CommonResponse<>(enrollmentService.getInProgressProgram(page), "수강 중인 프로그램 조회에 성공하였습니다.");
    }

    @Operation(summary = "수강완료 프로그램 조회", description = "수강 완료인 프로그램 조회하는 API")
    @GetMapping("/completed")
    public CommonResponse<List<EnrollmentResponseDto>> getCompletedProgram(@RequestParam(defaultValue = "1") int page) {

        return new CommonResponse<>(enrollmentService.getCompletedProgram(page), "수강 완료인 프로그램 조회에 성공하였습니다.");
    }
}
