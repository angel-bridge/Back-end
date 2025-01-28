package angel_bridge.angel_bridge_server.domain.banner.controller;

import angel_bridge.angel_bridge_server.domain.banner.dto.request.AdminBannerRequestDto;
import angel_bridge.angel_bridge_server.domain.banner.dto.response.AdminBannerResponseDto;
import angel_bridge.angel_bridge_server.domain.banner.service.BannerService;
import angel_bridge.angel_bridge_server.global.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin/banner")
@RequiredArgsConstructor
@Tag(name = "ADMIN_Banner", description = "ADMIN 배너 관련 API")
public class AdminBannerController {

    private final BannerService bannerService;

    @Operation(summary = "배너 이미지 등록", description = "하나의 배너 이미지를 등록하는 API")
    @PostMapping
    public CommonResponse<AdminBannerResponseDto> registerBanner(@Valid @RequestPart(value = "data") AdminBannerRequestDto request, @RequestPart(value = "image") MultipartFile file) {

        return new CommonResponse<>(bannerService.registerBanner(request, file), "하나의 배너 이미지 등록에 성공하였습니다.");
    }

    @Operation(summary = "배너 이미지 정보 수정", description = "해당 배너 이미지 정보를 수정하는 API")
    @PutMapping("/{bannerId}")
    public CommonResponse<AdminBannerResponseDto> updateBanner(@Valid @RequestPart(value = "data") AdminBannerRequestDto request, @RequestPart(value = "image", required = false) MultipartFile file, @PathVariable Long bannerId) {

        return new CommonResponse<>(bannerService.updateBanner(request, file, bannerId), "해당 배너 이미지 수정에 성공하였습니다.");
    }

    @Operation(summary = "배너 삭제", description = "하나의 배너를 삭제하는 API")
    @DeleteMapping("/{bannerId}")
    public CommonResponse<Void> deleteBanner(@PathVariable Long bannerId) {

        bannerService.deleteBanner(bannerId);
        return new CommonResponse<>("해당 배너 삭제에 성공하였습니다.");
    }
}
