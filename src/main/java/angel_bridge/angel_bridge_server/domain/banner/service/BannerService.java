package angel_bridge.angel_bridge_server.domain.banner.service;

import angel_bridge.angel_bridge_server.domain.banner.dto.request.BannerRequestDto;
import angel_bridge.angel_bridge_server.domain.banner.dto.response.AdminBannerResponseDto;
import angel_bridge.angel_bridge_server.domain.banner.entity.Banner;
import angel_bridge.angel_bridge_server.global.exception.ApplicationException;
import angel_bridge.angel_bridge_server.global.repository.BannerRepository;
import angel_bridge.angel_bridge_server.global.s3.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.NOT_FOUND_BANNER_ID;

@Service
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
    public AdminBannerResponseDto registerBanner(BannerRequestDto request, MultipartFile file) {

        String image = null;

        try {
            image = imageService.uploadImage(file);
        } catch (IOException e){
            throw new RuntimeException("이미지 저장에 실패하였습니다.", e);
        }

        Banner saveBanner = bannerRepository.save(request.toEntity(image));

        return AdminBannerResponseDto.from(saveBanner);
    }

    // [PUT] 어드민 배너 정보 수정
    @Transactional
    public AdminBannerResponseDto updateBanner(BannerRequestDto request, MultipartFile file, Long bannerId) {

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
            throw new RuntimeException("이미지 저장에 실패하였습니다.", e);
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
}
