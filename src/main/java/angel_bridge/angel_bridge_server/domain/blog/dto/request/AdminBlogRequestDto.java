package angel_bridge.angel_bridge_server.domain.blog.dto.request;

import angel_bridge.angel_bridge_server.domain.blog.entity.Blog;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AdminBlogRequestDto(

        @NotNull
        @Schema(description = "블로그 제목", example = "2025 스타트업 지원 사업 총정리")
        String title,

        @NotNull
        @Schema(description = "블로그 링크", example = "https://velog.io/@angelbridge/link")
        String blogLink,

        @NotNull
        @Schema(description = "블로그 첫 시작 문구", example = "2025 스타트업 지원 사업들에 대해 저희 엔젤브릿지가 설명해드리겠습니다.")
        String text,

        @NotNull
        @Schema(description = "게시 날짜", example = "2024-12-07")
        LocalDate blogDate
) {
        public Blog toEntity() {

                return Blog.builder()
                        .title(this.title)
                        .blogLink(this.blogLink)
                        .startText(this.text)
                        .blogDate(this.blogDate)
                        .build();
        }
}
