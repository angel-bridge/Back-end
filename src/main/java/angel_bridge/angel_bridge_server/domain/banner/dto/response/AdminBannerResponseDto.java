package angel_bridge.angel_bridge_server.domain.banner.dto.response;

import angel_bridge.angel_bridge_server.domain.banner.entity.Banner;
import lombok.Builder;

@Builder
public record AdminBannerResponseDto(

        Long bannerId,

        String imageFile,

        String name,

        Integer priority
) {
    public static AdminBannerResponseDto from(Banner banner) {

        return AdminBannerResponseDto.builder()
                .bannerId(banner.getId())
                .imageFile(banner.getBannerImage())
                .name(banner.getName())
                .priority(banner.getPriority())
                .build();
    }
}
