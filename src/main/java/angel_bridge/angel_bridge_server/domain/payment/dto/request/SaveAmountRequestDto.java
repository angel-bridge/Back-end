package angel_bridge.angel_bridge_server.domain.payment.dto.request;

public record SaveAmountRequestDto(

        String orderId,
        Long amount
) {
}
