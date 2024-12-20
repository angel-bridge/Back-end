package angel_bridge.angel_bridge_server.domain.education.service;

import angel_bridge.angel_bridge_server.domain.education.dto.request.AdminEducationRequestDto;
import angel_bridge.angel_bridge_server.domain.education.dto.response.AdminEducationResponseDto;
import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import angel_bridge.angel_bridge_server.global.exception.ApplicationException;
import angel_bridge.angel_bridge_server.global.repository.EducationRepository;
import angel_bridge.angel_bridge_server.global.s3.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.NOT_FOUND_EDUCATION_ID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EducationService {

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
            throw new RuntimeException("이미지 저장에 실패하였습니다.", e);
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
            throw new RuntimeException("이미지 저장에 실패하였습니다.", e);
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
