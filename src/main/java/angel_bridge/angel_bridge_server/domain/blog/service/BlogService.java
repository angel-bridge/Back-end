package angel_bridge.angel_bridge_server.domain.blog.service;

import angel_bridge.angel_bridge_server.domain.blog.dto.request.AdminBlogRequestDto;
import angel_bridge.angel_bridge_server.domain.blog.dto.response.AdminBlogResponseDto;
import angel_bridge.angel_bridge_server.domain.blog.entity.Blog;
import angel_bridge.angel_bridge_server.global.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    // [POST] 어드민 블로그 썸네일 생성
    @Transactional
    public AdminBlogResponseDto createBlog(AdminBlogRequestDto request) {

        Blog saveBlog = blogRepository.save(request.toEntity());

        return AdminBlogResponseDto.from(saveBlog);
    }
}
