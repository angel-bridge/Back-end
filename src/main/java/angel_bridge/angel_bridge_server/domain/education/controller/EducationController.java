package angel_bridge.angel_bridge_server.domain.education.controller;

import angel_bridge.angel_bridge_server.domain.education.dto.RecommendationProgramResponse;
import angel_bridge.angel_bridge_server.domain.education.service.EducationService;
import angel_bridge.angel_bridge_server.global.common.response.CommonResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/education")
public class EducationController {

    private EducationService educationService;

    @GetMapping("/recommendations")
    public CommonResponse<List<RecommendationProgramResponse>> getRecommendationProgram() {

        List<RecommendationProgramResponse> recommendationPrograms = educationService.getRecommendationProgram();
        return new CommonResponse<>(recommendationPrograms, "추천 프로그램 조회 완료");
    }
}
