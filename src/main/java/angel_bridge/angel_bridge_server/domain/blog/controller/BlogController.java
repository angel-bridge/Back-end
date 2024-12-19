package angel_bridge.angel_bridge_server.domain.blog.controller;

import angel_bridge.angel_bridge_server.domain.blog.dto.request.AdminBlogRequestDto;
import angel_bridge.angel_bridge_server.domain.blog.dto.response.AdminBlogResponseDto;
import angel_bridge.angel_bridge_server.domain.blog.service.BlogService;
import angel_bridge.angel_bridge_server.global.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/blog")
@RequiredArgsConstructor
@Tag(name = "ADMIN_Blog", description = "ADMIN 블로그 관련 API")
public class BlogController {

    public final BlogService blogService;

    @Operation(summary = "블로그 썸네일 생성", description = "하나의 블로그 썸네일을 생성하는 API")
    @PostMapping
    public CommonResponse<AdminBlogResponseDto> createBlog(@Valid @RequestBody AdminBlogRequestDto request) {

        return new CommonResponse<>(blogService.createBlog(request), "하나의 블로그 썸네일 생성에 성공하였습니다.");
    }

    @Operation(summary = "블로그 썸네일 수정", description = "하나의 블로그 썸네일을 수정하는 API")
    @PutMapping("/{blogId}")
    public CommonResponse<AdminBlogResponseDto> updateBlog(@Valid @RequestBody AdminBlogRequestDto request, @PathVariable Long blogId) {

        return new CommonResponse<>(blogService.updateBlog(request, blogId), "해당 블로그 썸네일 수정에 성공하였습니다.");
    }

    @Operation(summary = "블로그 썸네일 삭제", description = "하나의 블로그 썸네일을 삭제하는 API")
    @DeleteMapping("/{blogId}")
    public CommonResponse<Void> deleteBlog(@PathVariable Long blogId) {

        blogService.deleteBlog(blogId);
        return new CommonResponse<>("해당 블로그 썸네일 삭제에 성공하였습니다.");
    }
}
