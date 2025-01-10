package angel_bridge.angel_bridge_server.domain.payment.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Builder
public record CompletePaymentResponseDto(
        Long enrollmentId,
        String imageUrl,
        String educationName,
        String price,
        LocalDateTime approvedAt
) {
}
