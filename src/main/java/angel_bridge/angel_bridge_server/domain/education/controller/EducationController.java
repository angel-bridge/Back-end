package angel_bridge.angel_bridge_server.domain.education.controller;

import angel_bridge.angel_bridge_server.domain.education.dto.response.EducationDetailResponseDto;
import angel_bridge.angel_bridge_server.domain.education.dto.response.EducationResponseDto;
import angel_bridge.angel_bridge_server.domain.education.service.EducationService;
import angel_bridge.angel_bridge_server.global.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/education")
@Tag(name = "USER_Education", description = "USER 교육 프로그램 관련 API")
public class EducationController {

    private EducationService educationService;

    @Operation(summary = "추천 프로그램 조회", description = "3개의 추천 프로그램 조회하는 API")
    @GetMapping("/recommendations")
    public CommonResponse<List<EducationResponseDto>> getRecommendationProgram() {

        return new CommonResponse<>(educationService.getRecommendationProgram(), "추천 프로그램 조회에 성공하였습니다.");
    }

    @Operation(summary = "전체 프로그램 조회", description = "전체 프로그램 조회하는 API")
    @GetMapping
    public CommonResponse<List<EducationResponseDto>> getAllProgram(@RequestParam(defaultValue = "0") int page) {

        return new CommonResponse<>(educationService.getAllProgram(page), "전체 프로그램 조회에 성공하였습니다.");
    }

    @Operation(summary = "모집 중인 전체 프로그램 조회", description = "모집 중인 전체 프로그램 조회하는 API")
    @GetMapping("/ongoing")
    public CommonResponse<List<EducationResponseDto>> getAllOngoingProgram(@RequestParam(defaultValue = "0") int page) {

        return new CommonResponse<>(educationService.getAllOngoingProgram(page), "모집 중인 전체 프로그램 조회에 성공하였습니다.");
    }

    @Operation(summary = "모집 예정인 전체 프로그램 조회", description = "모집 예정인 전체 프로그램 조회하는 API")
    @GetMapping("/upcoming")
    public CommonResponse<List<EducationResponseDto>> getAllUpcomingProgram(@RequestParam(defaultValue = "0") int page) {

        return new CommonResponse<>(educationService.getAllUpcomingProgram(page), "모집 예정인 전체 프로그램 조회에 성공하였습니다.");
    }

    @Operation(summary = "프로그램 상세 페이지 조회", description = "프로그램 상세 페이지를 조회하는 API")
    @GetMapping("/{educationId}")
    public CommonResponse<EducationDetailResponseDto> getEducationDetail(@PathVariable Long educationId) {

        return new CommonResponse<>(educationService.getEducationDetail(educationId), "프로그램 상세 페이지 조회에 성공하였습니다.");
    }
}
