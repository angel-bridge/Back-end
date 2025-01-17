package angel_bridge.angel_bridge_server.global.common.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record PagedResponseDto<T> (
        long total,
        int pageNum,
        int totalPages,
        List<T> content
) {
    public static <T> PagedResponseDto<T> from(Page<T> page) {
        return new PagedResponseDto<>(
                page.getTotalElements(),
                page.getNumber() + 1,
                page.getTotalPages(),
                page.getContent()
        );
    }
}
