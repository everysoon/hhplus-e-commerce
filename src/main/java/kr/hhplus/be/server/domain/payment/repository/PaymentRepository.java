package kr.hhplus.be.server.domain.payment.repository;

import java.util.List;
import kr.hhplus.be.server.domain.payment.Payment;

public interface PaymentRepository {
	Payment save(Payment payment);
	List<Payment> saveAll(List<Payment> payment);
	Payment findByOrderId(Long orderId);
}
