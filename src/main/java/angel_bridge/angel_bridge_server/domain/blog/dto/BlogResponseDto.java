package angel_bridge.angel_bridge_server.domain.blog.dto;

import angel_bridge.angel_bridge_server.domain.blog.entity.Blog;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record BlogResponseDto(

        @NotNull
        @Schema(description = "블로그 글 제목", example = "2025 스타트업 지원 사업 총정리")
        String title,
        @NotNull
        @Schema(description = "블로그 글 시작 문구", example = "2025 스타트업 지원 사업들에 대해 저희 엔젤브릿지가 설명해드리겠습니다.")
        String content,
        @NotNull
        @Schema(description = "블로그 글 저자", example = "angelbridge")
        String author,
        @NotNull
        @Schema(description = "블로그 글 포스팅 날짜", example = "2024-12-21")
        LocalDate postingDate,
        @NotNull
        @Schema(description = "블로그 글 링크", example = "https://velog.io/@angelbridge/link")
        String link
) {
        public static BlogResponseDto from(Blog blog) {
            return BlogResponseDto.builder()
                    .title(blog.getTitle())
                    .content(blog.getStartText())
                    .author("angelbridge")
                    .postingDate(blog.getBlogDate())
                    .link(blog.getBlogLink())
                    .build();
        }
}
