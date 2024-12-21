package angel_bridge.angel_bridge_server.domain.education.controller;

import angel_bridge.angel_bridge_server.domain.education.dto.RecommendationProgramResponse;
import angel_bridge.angel_bridge_server.domain.education.service.EducationService;
import angel_bridge.angel_bridge_server.global.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/education")
@Tag(name = "USER_Education", description = "USER 교육 프로그램 관련 API")
public class EducationController {

    private EducationService educationService;

    @Operation(summary = "추천 프로그램 조회", description = "3개의 추천 프로그램 조회하는 API")
    @GetMapping("/recommendations")
    public CommonResponse<List<RecommendationProgramResponse>> getRecommendationProgram() {

        return new CommonResponse<>(educationService.getRecommendationProgram(), "추천 프로그램 조회에 성공하였습니다.");
    }
}
