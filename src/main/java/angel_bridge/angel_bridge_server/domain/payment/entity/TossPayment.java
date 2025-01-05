package angel_bridge.angel_bridge_server.domain.payment.entity;

import angel_bridge.angel_bridge_server.domain.enrollment.entity.Enrollment;
import angel_bridge.angel_bridge_server.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TossPayment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "enrollment_id")
    private Enrollment enrollment;

    @Column(name = "toss_order_id")
    private String tossOrderId;

    @Column(name = "toss_payment_key", nullable = false, unique = true)
    private String tossPaymentKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "toss_payment_method")
    private PaymentMethod tossPaymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "toss_payment_status")
    private PaymentStatus tossPaymentStatus;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "request_at")
    private LocalDateTime requestAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Builder
    public TossPayment(Enrollment enrollment,
                       String tossOrderId,
                       String tossPaymentKey,
                       PaymentMethod tossPaymentMethod,
                       PaymentStatus tossPaymentStatus,
                       BigDecimal totalAmount,
                       LocalDateTime requestAt,
                       LocalDateTime approvedAt) {

        this.enrollment = enrollment;
        this.tossOrderId = tossOrderId;
        this.tossPaymentKey = tossPaymentKey;
        this.tossPaymentMethod = tossPaymentMethod;
        this.tossPaymentStatus = tossPaymentStatus;
        this.totalAmount = totalAmount;
        this.requestAt = requestAt;
        this.approvedAt = approvedAt;
    }
}
