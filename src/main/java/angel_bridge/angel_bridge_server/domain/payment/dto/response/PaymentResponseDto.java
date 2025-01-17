package angel_bridge.angel_bridge_server.domain.payment.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PaymentResponseDto(
        Long enrollmentId,
        String imageUrl,
        String educationName,
        String price,
        LocalDateTime date,
        String status
) {
}
