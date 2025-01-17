package angel_bridge.angel_bridge_server.domain.banner.service;

import angel_bridge.angel_bridge_server.domain.banner.dto.request.AdminBannerRequestDto;
import angel_bridge.angel_bridge_server.domain.banner.dto.response.AdminBannerResponseDto;
import angel_bridge.angel_bridge_server.domain.banner.dto.response.BannerResponseDto;
import angel_bridge.angel_bridge_server.domain.banner.entity.Banner;
import angel_bridge.angel_bridge_server.global.exception.ApplicationException;
import angel_bridge.angel_bridge_server.global.repository.BannerRepository;
import angel_bridge.angel_bridge_server.global.s3.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.IMAGE_UPLOAD_ERROR;
import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.NOT_FOUND_BANNER_ID;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;
    private final ImageService imageService;

    public Banner findBannerById(Long bannerId) {
        return bannerRepository.findByIdAndDeletedAtIsNull(bannerId).orElseThrow(() -> new ApplicationException(NOT_FOUND_BANNER_ID));
    }

    // [POST] 어드민 배너 이미지 등록
    @Transactional
    public AdminBannerResponseDto registerBanner(AdminBannerRequestDto request, MultipartFile file) {

        String image = null;

        try {
            image = imageService.uploadImage(file);
        } catch (IOException e){

            log.error("이미지 업로드 중 오류 발생: {}", e.getMessage(), e);
            throw new ApplicationException(IMAGE_UPLOAD_ERROR);
        }

        Banner saveBanner = bannerRepository.save(request.toEntity(image));

        return AdminBannerResponseDto.from(saveBanner);
    }

    // [PUT] 어드민 배너 정보 수정
    @Transactional
    public AdminBannerResponseDto updateBanner(AdminBannerRequestDto request, MultipartFile file, Long bannerId) {

        Banner banner = findBannerById(bannerId);

        String image = banner.getBannerImage();

        try {
            // 기존 이미지 삭제 후 새로운 이미지 저장
            if (file != null && !file.isEmpty()) {
                if (image != null && !image.isEmpty()) {
                    imageService.deleteImage(image);
                }

                image = imageService.uploadImage(file);
            }
        } catch (IOException e) {

            log.error("이미지 업로드 중 오류 발생: {}", e.getMessage(), e);
            throw new ApplicationException(IMAGE_UPLOAD_ERROR);
        }

        banner.update(request, image);
        Banner updateBanner = bannerRepository.save(banner);

        return AdminBannerResponseDto.from(updateBanner);
    }

    // [PATCH] 어드민 배너 게시 여부 변경
    @Transactional
    public AdminBannerResponseDto changeIsPost(Long bannerId) {

        Banner banner = findBannerById(bannerId);

        banner.changeIsPost();
        Banner updateBanner = bannerRepository.save(banner);

        return AdminBannerResponseDto.from(updateBanner);
    }

    // [DELETE] 어드민 배너 삭제
    @Transactional
    public void deleteBanner(Long bannerId) {

        Banner banner = findBannerById(bannerId);

        String image = banner.getBannerImage();

        if (image != null && !image.isEmpty()) {
            imageService.deleteImage(image);
        }

        bannerRepository.delete(banner);
    }

    // [GET] 일반 사용자 메인 페이지 배너 조회
    public List<BannerResponseDto> getBanner() {

        List<Banner> banners = bannerRepository.findAllActiveBannersSortedByPriority();

        return banners.stream()
                .filter(banner -> banner.getPriority() >= 1 && banner.getPriority() <= 3)
                .limit(3)
                .map(banner -> BannerResponseDto.from(imageService.getImageUrl(banner.getBannerImage())))
                .toList();
    }

    // [GET] 일반 사용자 교육 페이지 배너 조회
    public BannerResponseDto getEducationBanner() {

        return bannerRepository.findByPriorityAndDeletedAtIsNull(4)
                .map(banner -> BannerResponseDto.from(imageService.getImageUrl(banner.getBannerImage())))
                .orElse(null);
    }
}
