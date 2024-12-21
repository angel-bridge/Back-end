package angel_bridge.angel_bridge_server.domain.blog.service;

import angel_bridge.angel_bridge_server.domain.blog.dto.BlogResponseDto;
import angel_bridge.angel_bridge_server.domain.blog.entity.Blog;
import angel_bridge.angel_bridge_server.domain.blog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    // [GET] 일반 사용자 최신 블로그 글 2개 조회
    public List<BlogResponseDto> getNewArticle() {

        Pageable pageable = PageRequest.of(0, 2);
        List<Blog> latestBlogs = blogRepository.findLatestBlogs(pageable);

        return latestBlogs.stream()
                .map(BlogResponseDto::from)
                .collect(Collectors.toList());
    }
}
