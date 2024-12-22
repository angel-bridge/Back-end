package angel_bridge.angel_bridge_server.domain.blog.controller;

import angel_bridge.angel_bridge_server.domain.blog.dto.response.BlogResponseDto;
import angel_bridge.angel_bridge_server.domain.blog.service.BlogService;
import angel_bridge.angel_bridge_server.global.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/blog")
@Tag(name = "USER_Blog", description = "USER 블로그 관련 API")
public class BlogController {

    private final BlogService blogService;

    @Operation(summary = "블로그 글 조회", description = "2개의 최신 블로그 글 조회하는 API")
    @GetMapping("/latest")
    public CommonResponse<List<BlogResponseDto>> getNewArticle() {

        return new CommonResponse<>(blogService.getNewArticle(), "최신 블로그 글 조회 성공");
    }
}
