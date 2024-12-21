package angel_bridge.angel_bridge_server.domain.banner.controller;

import angel_bridge.angel_bridge_server.domain.banner.dto.response.BannerResponseDto;
import angel_bridge.angel_bridge_server.domain.banner.service.BannerService;
import angel_bridge.angel_bridge_server.global.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banner")
@RequiredArgsConstructor
@Tag(name = "USER_Banner", description = "USER 배너 관련 API")
public class BannerController {

    private final BannerService bannerService;

    @Operation(summary = "배너 이미지 조회", description = "3개의 배너 이미지를 조회하는 API")
    @GetMapping
    public CommonResponse<List<BannerResponseDto>> registerBanner() {

        return new CommonResponse<>(bannerService.getBanner(), "배너 이미지 조회에 성공하였습니다.");
    }
}
