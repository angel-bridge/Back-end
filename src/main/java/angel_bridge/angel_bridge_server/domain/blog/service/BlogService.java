package angel_bridge.angel_bridge_server.domain.blog.service;

import angel_bridge.angel_bridge_server.domain.blog.dto.request.AdminBlogRequestDto;
import angel_bridge.angel_bridge_server.domain.blog.dto.response.AdminBlogResponseDto;
import angel_bridge.angel_bridge_server.domain.blog.entity.Blog;
import angel_bridge.angel_bridge_server.global.exception.ApplicationException;
import angel_bridge.angel_bridge_server.global.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.NOT_FOUND_BLOG_ID;
import angel_bridge.angel_bridge_server.domain.blog.dto.response.BlogResponseDto;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    public Blog findBlogById(Long blogId) {
        return blogRepository.findByIdAndDeletedAtIsNull(blogId).orElseThrow(() -> new ApplicationException(NOT_FOUND_BLOG_ID));
    }

    // [POST] 어드민 블로그 썸네일 생성
    @Transactional
    public AdminBlogResponseDto createBlog(AdminBlogRequestDto request) {

        Blog saveBlog = blogRepository.save(request.toEntity());

        return AdminBlogResponseDto.from(saveBlog);
    }

    // [PUT] 어드민 블로그 썸네일 수정
    @Transactional
    public AdminBlogResponseDto updateBlog(AdminBlogRequestDto request, Long blogId) {

        Blog blog = findBlogById(blogId);

        blog.update(request);
        Blog updateBlog = blogRepository.save(blog);

        return AdminBlogResponseDto.from(updateBlog);
    }

    // [DELETE] 어드민 블로그 썸네일 삭제
    @Transactional
    public void deleteBlog(Long blogId) {

        Blog blog = findBlogById(blogId);

        blogRepository.delete(blog);
    }

    // [GET] 일반 사용자 최신 블로그 글 2개 조회
    public List<BlogResponseDto> getNewArticle() {

        Pageable pageable = PageRequest.of(0, 2);
        List<Blog> latestBlogs = blogRepository.findLatestBlogs(pageable);

        return latestBlogs.stream()
                .map(BlogResponseDto::from)
                .collect(Collectors.toList());
    }
}
