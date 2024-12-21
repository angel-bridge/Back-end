package angel_bridge.angel_bridge_server.domain.education.service;

import angel_bridge.angel_bridge_server.domain.education.dto.request.AdminEducationRequestDto;
import angel_bridge.angel_bridge_server.domain.education.dto.response.AdminEducationResponseDto;
import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import angel_bridge.angel_bridge_server.global.exception.ApplicationException;
import angel_bridge.angel_bridge_server.global.repository.EducationRepository;
import angel_bridge.angel_bridge_server.global.s3.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.IMAGE_UPLOAD_ERROR;
import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.NOT_FOUND_EDUCATION_ID;

@Service
@Slf4j
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

    private final EducationRepository educationRepository;
    private final ImageService imageService;

    public Education findEducationById(Long educationId) {
        return educationRepository.findByIdAndDeletedAtIsNull(educationId).orElseThrow(() -> new ApplicationException(NOT_FOUND_EDUCATION_ID));
    }

    // [POST] 어드민 교육프로그램 생성
    @Transactional
    public AdminEducationResponseDto createEducation(AdminEducationRequestDto request, MultipartFile preImage, MultipartFile detailImage) {

        String preFile = null;
        String detailFile = null;

        try {
            preFile = imageService.uploadImage(preImage);
            detailFile = imageService.uploadImage(detailImage);
        } catch (IOException e) {

            log.error("이미지 업로드 중 오류 발생: {}", e.getMessage(), e);
            throw new ApplicationException(IMAGE_UPLOAD_ERROR);
        }

        Education saveEducation = educationRepository.save(request.toEntity(preFile, detailFile));

        return AdminEducationResponseDto.from(saveEducation);
    }

    // [PUT] 어드민 교육프로그램 수정
    @Transactional
    public AdminEducationResponseDto updateEducation(AdminEducationRequestDto request, MultipartFile preImage, MultipartFile detailImage, Long educationId) {

        Education education = findEducationById(educationId);

        String preFile = education.getEducationPreImage();
        String detailFile = education.getEducationDetailImage();

        try {
            // 기존 이미지 삭제 후 새로운 이미지 저장
            if (preImage != null && !preImage.isEmpty()) {
                if (preFile != null && !preFile.isEmpty()) {
                    imageService.deleteImage(preFile);
                }

                preFile = imageService.uploadImage(preImage);
            }
            if (detailImage != null && !detailImage.isEmpty()) {
                if (detailFile != null && !detailFile.isEmpty()) {
                    imageService.deleteImage(detailFile);
                }

                detailFile = imageService.uploadImage(detailImage);
            }
        } catch (IOException e) {

            log.error("이미지 업로드 중 오류 발생: {}", e.getMessage(), e);
            throw new ApplicationException(IMAGE_UPLOAD_ERROR);
        }

        education.update(request, preFile, detailFile);
        Education updateEducation = educationRepository.save(education);

        return AdminEducationResponseDto.from(updateEducation);
    }

    // [DELETE] 어드민 교육프로그램 삭제
    @Transactional
    public void deleteEducation(Long educationId) {

        Education education = findEducationById(educationId);

        String preImage = education.getEducationPreImage();
        String detailImage = education.getEducationDetailImage();

        if (preImage != null && !preImage.isEmpty()) {
            imageService.deleteImage(preImage);
        }
        if (detailImage != null && !detailImage.isEmpty()) {
            imageService.deleteImage(detailImage);
        }

        educationRepository.delete(education);
    }
}
