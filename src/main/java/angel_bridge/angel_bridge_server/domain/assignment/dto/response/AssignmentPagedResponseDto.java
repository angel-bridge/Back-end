package angel_bridge.angel_bridge_server.domain.assignment.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record AssignmentPagedResponseDto<T> (
        long total,
        int pageNum,
        int totalPages,
        List<T> assignments
) {
    public static <T> AssignmentPagedResponseDto<T> from(Page<T> page) {
        return new AssignmentPagedResponseDto<>(
                page.getTotalElements(),
                page.getNumber() + 1,
                page.getTotalPages(),
                page.getContent()
        );
    }
}
