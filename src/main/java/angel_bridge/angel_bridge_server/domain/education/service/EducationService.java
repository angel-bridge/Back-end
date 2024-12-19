package angel_bridge.angel_bridge_server.domain.education.service;

import angel_bridge.angel_bridge_server.domain.education.dto.RecommendationProgramResponse;
import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import angel_bridge.angel_bridge_server.domain.education.entity.RecruitmentStatus;
import angel_bridge.angel_bridge_server.domain.education.repository.EducationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EducationService {

    private final EducationRepository educationRepository;

    /**
     * 프로그램 추천 메서드
     *
     * 모집 중인 프로그램 중에서 마감 기간이 가까운 순으로 우선 추천합니다.
     */
    public List<RecommendationProgramResponse> getRecommendationProgram() {

        List<RecommendationProgramResponse> responses = new ArrayList<>();

        // 모집 중인 프로그램 (마감 기간이 가까운 순으로 정렬)
        List<Education> ongoingEducations
                = educationRepository.findByRecruitmentStatusOrderByRecruitmentEndDateAsc(RecruitmentStatus.ONGOING);

        // 모집 예정인 프로그램 (마감 기간이 가까운 순으로 정렬)
        List<Education> upcomingEducations
                = educationRepository.findByRecruitmentStatusOrderByRecruitmentEndDateAsc(RecruitmentStatus.UPCOMING);

        for (int i = 0; i < Math.min(3, ongoingEducations.size()); i++) {
            responses.add(RecommendationProgramResponse.of(ongoingEducations.get(i)));
        }
        if (responses.size() < 3) {
            for (int i = 0; i < Math.min(3 - responses.size(), upcomingEducations.size()); i++) {
                responses.add(RecommendationProgramResponse.of(upcomingEducations.get(i)));
            }
        }

        return responses;
    }
}
