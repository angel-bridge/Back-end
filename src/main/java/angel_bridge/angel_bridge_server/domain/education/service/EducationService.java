package angel_bridge.angel_bridge_server.domain.education.service;

import angel_bridge.angel_bridge_server.domain.education.dto.response.EducationDetailResponseDto;
import angel_bridge.angel_bridge_server.domain.education.dto.request.AdminEducationRequestDto;
import angel_bridge.angel_bridge_server.domain.education.dto.response.AdminEducationResponseDto;
import angel_bridge.angel_bridge_server.domain.education.dto.response.EducationResponseDto;
import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import angel_bridge.angel_bridge_server.domain.education.entity.RecruitmentStatus;
import angel_bridge.angel_bridge_server.global.common.response.PagedResponseDto;
import angel_bridge.angel_bridge_server.global.exception.ApplicationException;
import angel_bridge.angel_bridge_server.global.repository.EducationRepository;
import angel_bridge.angel_bridge_server.global.s3.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EducationService {

    private final ImageService imageService;
    private final EducationRepository educationRepository;

    // 자정에 recruitment Status 업데이트
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateRecruitmentStatus() {

        LocalDate today = LocalDate.now();

        // 모집 예정 상태 업데이트
        educationRepository.findAllByRecruitmentStartDateAfter(today)
                .forEach(education -> education.setRecruitmentStatus(RecruitmentStatus.UPCOMING));

        // 모집 중 상태 업데이트
        educationRepository.findAllByRecruitmentStartDateBeforeAndRecruitmentEndDateAfter(today, today)
                .forEach(education -> education.setRecruitmentStatus(RecruitmentStatus.ONGOING));

        // 모집 종료 상태 업데이트
        educationRepository.findAllByRecruitmentEndDateBefore(today)
                .forEach(education -> education.setRecruitmentStatus(RecruitmentStatus.CLOSED));
    }

    // [GET] 일반 사용자 추천 교육 프로그램 조회
    public List<EducationResponseDto> getRecommendationProgram() {

        // 모집 중인 프로그램 (마감 기간이 가까운 순으로 정렬)
        List<Education> ongoingEducations
                = educationRepository.findEducationsByStatus(RecruitmentStatus.ONGOING);

        // 모집 예정인 프로그램 (마감 기간이 가까운 순으로 정렬)
        List<Education> upcomingEducations
                = educationRepository.findEducationsByStatus(RecruitmentStatus.UPCOMING);

        // 모집 중인 프로그램에서 최대 3개 선택
        List<EducationResponseDto> responses = ongoingEducations.stream()
                .limit(3)
                .map(education -> EducationResponseDto.from(
                        education, imageService.getImageUrl(education.getEducationPreImage())
                ))
                .collect(Collectors.toList());

        // 모집 중인 프로그램에서 부족한 경우, 모집 예정인 프로그램에서 추가 선택
        int remaining = 3 - responses.size();
        if (remaining > 0) {
            List<EducationResponseDto> upcomingResponses = upcomingEducations.stream()
                    .limit(remaining)
                    .map(education -> EducationResponseDto.from(
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
//                    imageService.deleteImage(preFile);
                }

                preFile = imageService.uploadImage(preImage);
            }
            if (detailImage != null && !detailImage.isEmpty()) {
                if (detailFile != null && !detailFile.isEmpty()) {
//                    imageService.deleteImage(detailFile);
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
    public PagedResponseDto<EducationResponseDto> getAllProgram(int page) {

        if (page == 0)
            throw new ApplicationException(BAD_REQUEST_ERROR);
        Pageable pageable = PageRequest.of(page - 1, 12, Sort.by(Sort.Direction.ASC, "recruitmentEndDate"));

        // 페이지 조회
        Page<Education> educationPage = educationRepository.findAllActive(pageable);

        // Education 객체를 EducationResponseDto로 매핑
        List<EducationResponseDto> content = educationPage.getContent().stream()
                .map(education -> EducationResponseDto.from(
                        education, imageService.getImageUrl(education.getEducationPreImage())))
                .toList();

        // PagedResponseDto로 변환하여 반환
        return PagedResponseDto.from(
                new PageImpl<>(content, pageable, educationPage.getTotalElements())
        );
    }

    // [GET] 일반 사용자 모집 중인 전체 프로그램 조회
    public PagedResponseDto<EducationResponseDto> getAllOngoingProgram(int page) {

        if (page == 0) {
            throw new ApplicationException(BAD_REQUEST_ERROR);
        }

        Pageable pageable = PageRequest.of(page - 1, 12, Sort.by(Sort.Direction.ASC, "recruitmentEndDate"));

        // 페이지 조회
        Page<Education> educationPage = educationRepository.findByRecruitmentStatusAndDeletedAtIsNull(RecruitmentStatus.ONGOING, pageable);

        // Education 객체를 EducationResponseDto로 매핑
        List<EducationResponseDto> content = educationPage.getContent().stream()
                .map(education -> EducationResponseDto.from(
                        education, imageService.getImageUrl(education.getEducationPreImage())))
                .toList();

        // PagedResponseDto로 변환하여 반환
        return PagedResponseDto.from(
                new PageImpl<>(content, pageable, educationPage.getTotalElements())
        );
    }

    // [GET] 일반 사용자 모집 예정인 전체 프로그램 조회
    public PagedResponseDto<EducationResponseDto> getAllUpcomingProgram(int page) {

        if (page == 0) {
            throw new ApplicationException(BAD_REQUEST_ERROR);
        }

        Pageable pageable = PageRequest.of(page - 1, 12, Sort.by(Sort.Direction.ASC, "recruitmentEndDate"));

        // 페이지 조회
        Page<Education> educationPage = educationRepository.findByRecruitmentStatusAndDeletedAtIsNull(RecruitmentStatus.UPCOMING, pageable);

        // Education 객체를 EducationResponseDto로 매핑
        List<EducationResponseDto> content = educationPage.getContent().stream()
                .map(education -> EducationResponseDto.from(
                        education, imageService.getImageUrl(education.getEducationPreImage())))
                .toList();

        // PagedResponseDto로 변환하여 반환
        return PagedResponseDto.from(
                new PageImpl<>(content, pageable, educationPage.getTotalElements())
        );
    }

    // [GET] 일반 사용자 프로그램 상세 페이지 조회
    public EducationDetailResponseDto getEducationDetail(Long educationId) {

        Education education = findEducationById(educationId);
        return EducationDetailResponseDto.from(education, imageService.getImageUrl(education.getEducationPreImage()), imageService.getImageUrl(education.getEducationDetailImage()));
    }

    // [GET] 프로그램 검색 조회
    public List<EducationResponseDto> searchEducationByTitle(String keyword, int page, String status) {

        if (page == 0)
            throw new ApplicationException(BAD_REQUEST_ERROR);

        Pageable pageable = PageRequest.of(page - 1, 12);

        // 1. 전체 리스트에서 검색하는 경우
        if (status.equals("ALL")) {
            return educationRepository.findByTitle(keyword, pageable)
                    .map(education -> EducationResponseDto.from(
                            education, imageService.getImageUrl(education.getEducationPreImage())))
                    .stream().toList();

            // 2. 모집중인 리스트에서 검색하는 경우
        } else if (status.equals("ONGOING")) {
            return educationRepository.findByTitleAndStatus(keyword, RecruitmentStatus.ONGOING, pageable)
                    .map(education -> EducationResponseDto.from(
                            education, imageService.getImageUrl(education.getEducationPreImage())))
                    .stream().toList();

            // 3. 모집예정인 리스트에서 검색하는 경우
        } else if (status.equals("UPCOMING")) {
            return educationRepository.findByTitleAndStatus(keyword, RecruitmentStatus.UPCOMING, pageable)
                    .map(education -> EducationResponseDto.from(
                            education, imageService.getImageUrl(education.getEducationPreImage())))
                    .stream().toList();

        } else {
            throw new ApplicationException(BAD_REQUEST_ERROR);
        }
    }
}
