package angel_bridge.angel_bridge_server.domain.payment.dto.response;

import lombok.Builder;
import java.util.List;

@Builder
public record PaymentResponseDto(
        List<CompletePaymentResponseDto> complete,
        List<CanceledPaymentResponseDto> canceled
) {
}
