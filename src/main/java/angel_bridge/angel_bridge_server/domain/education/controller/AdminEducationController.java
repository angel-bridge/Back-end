package angel_bridge.angel_bridge_server.domain.education.controller;

import angel_bridge.angel_bridge_server.domain.blog.dto.request.AdminBlogRequestDto;
import angel_bridge.angel_bridge_server.domain.blog.dto.response.AdminBlogResponseDto;
import angel_bridge.angel_bridge_server.domain.education.dto.request.AdminEducationRequestDto;
import angel_bridge.angel_bridge_server.domain.education.dto.response.AdminEducationResponseDto;
import angel_bridge.angel_bridge_server.domain.education.service.EducationService;
import angel_bridge.angel_bridge_server.global.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin/education")
@RequiredArgsConstructor
@Tag(name = "ADMIN_Education", description = "ADMIN 교육 프로그램 관련 API")
public class AdminEducationController {

    private final EducationService educationService;

    @Operation(summary = "교육프로그램 생성", description = "하나의 교육프로그램을 생성하는 API")
    @PostMapping
    public CommonResponse<AdminEducationResponseDto> createEducation(@Valid @RequestPart(value = "data") AdminEducationRequestDto request, @RequestPart(value = "preImage") MultipartFile preImage, @RequestPart(value = "detailImage") MultipartFile detailImage) {

        return new CommonResponse<>(educationService.createEducation(request, preImage, detailImage), "하나의 교육프로그램 생성에 성공하였습니다.");
    }


}
