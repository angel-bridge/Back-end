package angel_bridge.angel_bridge_server.domain.banner.controller;

import angel_bridge.angel_bridge_server.domain.banner.dto.request.BannerRequestDto;
import angel_bridge.angel_bridge_server.domain.banner.dto.response.AdminBannerResponseDto;
import angel_bridge.angel_bridge_server.domain.banner.service.BannerService;
import angel_bridge.angel_bridge_server.domain.education.dto.request.AdminEducationRequestDto;
import angel_bridge.angel_bridge_server.domain.education.dto.response.AdminEducationResponseDto;
import angel_bridge.angel_bridge_server.global.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin/banner")
@RequiredArgsConstructor
@Tag(name = "ADMIN_Banner", description = "ADMIN 배너 관련 API")
public class BannerController {

    private final BannerService bannerService;

    @Operation(summary = "배너 이미지 등록", description = "하나의 배너 이미지를 등록하는 API")
    @PostMapping
    public CommonResponse<AdminBannerResponseDto> registerBanner(@Valid @RequestPart(value = "data") BannerRequestDto request, @RequestPart(value = "image") MultipartFile file) {

        return new CommonResponse<>(bannerService.registerBanner(request, file), "하나의 배너 이미지 등록에 성공하였습니다.");
    }
}
