package angel_bridge.angel_bridge_server.domain.banner.dto.request;

import angel_bridge.angel_bridge_server.domain.banner.entity.Banner;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AdminBannerRequestDto(

        @NotNull
        @Schema(description = "배너 제목", example = "엔젤브릿지 메인 배너")
        String name,

        @Schema(description = "배너 게시 순서", example = "1")
        Integer priority
) {
    public Banner toEntity(String file) {

        return Banner.builder()
                .bannerImage(file)
                .name(this.name)
                .priority(this.priority)
                .build();
    }
}
