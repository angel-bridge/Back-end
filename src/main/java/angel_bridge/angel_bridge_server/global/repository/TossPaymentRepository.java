package angel_bridge.angel_bridge_server.global.repository;

import angel_bridge.angel_bridge_server.domain.enrollment.entity.Enrollment;
import angel_bridge.angel_bridge_server.domain.payment.entity.TossPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TossPaymentRepository extends JpaRepository<TossPayment, Long> {

    TossPayment findByEnrollment(Enrollment enrollment);
}
