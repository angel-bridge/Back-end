package angel_bridge.angel_bridge_server.domain.education.service;

import angel_bridge.angel_bridge_server.domain.education.dto.response.EducationDetailResponseDto;
import angel_bridge.angel_bridge_server.domain.education.dto.request.AdminEducationRequestDto;
import angel_bridge.angel_bridge_server.domain.education.dto.response.AdminEducationResponseDto;
import angel_bridge.angel_bridge_server.domain.education.dto.response.ProgramResponseDto;
import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import angel_bridge.angel_bridge_server.domain.education.entity.RecruitmentStatus;
import angel_bridge.angel_bridge_server.global.exception.ApplicationException;
import angel_bridge.angel_bridge_server.global.repository.EducationRepository;
import angel_bridge.angel_bridge_server.global.s3.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.IMAGE_UPLOAD_ERROR;
import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.NOT_FOUND_EDUCATION_ID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EducationService {

    private final ImageService imageService;
    private final EducationRepository educationRepository;

    // [GET] 일반 사용자 추천 교육 프로그램 조회
    public List<ProgramResponseDto> getRecommendationProgram() {

        // 모집 중인 프로그램 (마감 기간이 가까운 순으로 정렬)
        List<Education> ongoingEducations
                = educationRepository.findEducationsByStatus(RecruitmentStatus.ONGOING);

        // 모집 예정인 프로그램 (마감 기간이 가까운 순으로 정렬)
        List<Education> upcomingEducations
                = educationRepository.findEducationsByStatus(RecruitmentStatus.UPCOMING);

        // 모집 중인 프로그램에서 최대 3개 선택
        List<ProgramResponseDto> responses = ongoingEducations.stream()
                .limit(3)
                .map(education -> ProgramResponseDto.from(
                        education, imageService.getImageUrl(education.getEducationPreImage())
                ))
                .collect(Collectors.toList());

        // 모집 중인 프로그램에서 부족한 경우, 모집 예정인 프로그램에서 추가 선택
        int remaining = 3 - responses.size();
        if (remaining > 0) {
            List<ProgramResponseDto> upcomingResponses = upcomingEducations.stream()
                    .limit(remaining)
                    .map(education -> ProgramResponseDto.from(
                            education, imageService.getImageUrl(education.getEducationPreImage())
                    ))
                    .toList();
            responses.addAll(upcomingResponses);
        }
        return responses;
    }

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

    // [GET] 일반 사용자 전체 프로그램 조회
    public List<ProgramResponseDto> getAllProgram(int page) {

        Pageable pageable = PageRequest.of(page - 1, 12);

        return educationRepository.findAll(pageable)
                .map(education -> ProgramResponseDto.from(
                                education, imageService.getImageUrl(education.getEducationPreImage())))
                .stream().toList();
    }

    // [GET] 일반 사용자 모집 중인 전체 프로그램 조회
    public List<ProgramResponseDto> getAllOngoingProgram(int page) {

        Pageable pageable = PageRequest.of(page - 1, 12);
        return educationRepository.findByRecruitmentStatusAndDeletedAtIsNull(RecruitmentStatus.ONGOING, pageable)
                .map(education -> ProgramResponseDto.from(
                        education, imageService.getImageUrl(education.getEducationPreImage())))
                .stream().toList();
    }

    // [GET] 일반 사용자 모집 예정인 전체 프로그램 조회
    public List<ProgramResponseDto> getAllUpcomingProgram(int page) {

        Pageable pageable = PageRequest.of(page - 1, 12);
        return educationRepository.findByRecruitmentStatusAndDeletedAtIsNull(RecruitmentStatus.UPCOMING, pageable)
                .map(education -> ProgramResponseDto.from(
                        education, imageService.getImageUrl(education.getEducationPreImage())))
                .stream().toList();
    }

    // [GET] 일반 사용자 프로그램 상세 페이지 조회
    public EducationDetailResponseDto getProgramDetail(Long educationId) {

        Education education = findEducationById(educationId);
        return EducationDetailResponseDto.from(education, education.getEducationPreImage(), education.getEducationDetailImage());
    }
}
