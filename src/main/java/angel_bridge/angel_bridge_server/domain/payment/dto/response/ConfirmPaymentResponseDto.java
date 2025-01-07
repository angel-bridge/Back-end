package angel_bridge.angel_bridge_server.domain.payment.dto.response;

import angel_bridge.angel_bridge_server.domain.payment.entity.PaymentMethod;
import angel_bridge.angel_bridge_server.domain.payment.entity.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConfirmPaymentResponseDto(
        @JsonProperty("paymentKey") String paymentKey,
        @JsonProperty("orderId") String orderId,
        @JsonProperty("totalAmount") Long totalAmount,
        @JsonProperty("requestedAt") String requestedAt,
        @JsonProperty("approvedAt") String approvedAt,
        @JsonProperty("method") String method,
        @JsonProperty("status") String status
) {

    public OffsetDateTime getRequestedAt() {
        return OffsetDateTime.parse(requestedAt);
    }

    public OffsetDateTime getApprovedAt() {
        return OffsetDateTime.parse(approvedAt);
    }

    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.fromDescription(method);
    }

    public PaymentStatus getPaymentStatus() {
        return PaymentStatus.valueOf(status.toUpperCase());
    }
}
