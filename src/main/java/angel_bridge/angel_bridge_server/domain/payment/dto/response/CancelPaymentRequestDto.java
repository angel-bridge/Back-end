package angel_bridge.angel_bridge_server.domain.payment.dto.response;

import lombok.Builder;

@Builder
public record CancelPaymentRequestDto(
        String cancelReason
) {
}
