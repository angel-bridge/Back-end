package angel_bridge.angel_bridge_server.domain.blog.dto.response;

import angel_bridge.angel_bridge_server.domain.blog.entity.Blog;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record AdminBlogResponseDto(

        Long blogId,

        String title,

        String blogLink,

        String text,

        LocalDate blogDate
) {
    public static AdminBlogResponseDto from(Blog blog) {

        return AdminBlogResponseDto.builder()
                .blogId(blog.getId())
                .title(blog.getTitle())
                .blogLink(blog.getBlogLink())
                .text(blog.getStartText())
                .blogDate(blog.getBlogDate())
                .build();
    }
}
