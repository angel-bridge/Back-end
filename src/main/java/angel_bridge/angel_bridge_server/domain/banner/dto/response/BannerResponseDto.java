package angel_bridge.angel_bridge_server.domain.banner.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record BannerResponseDto(

        @Schema(description = "배너 이미지 url")
        String imageFile
) {

    public static BannerResponseDto from(String imageUrl) {
        return BannerResponseDto.builder()
                .imageFile(imageUrl)
                .build();
    }
}
