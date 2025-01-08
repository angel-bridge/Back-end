package angel_bridge.angel_bridge_server.domain.payment.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CanceledPaymentResponseDto (
        String imageUrl,
        String educationName,
        String price,
        LocalDateTime canceledAt
){
}
