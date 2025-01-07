package angel_bridge.angel_bridge_server.global.repository;

import angel_bridge.angel_bridge_server.domain.payment.entity.TossPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TossPaymentRepository extends JpaRepository<TossPayment, Long> {
}
