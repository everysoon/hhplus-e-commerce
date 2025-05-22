package kr.hhplus.be.server.domain.payment.repository;

import kr.hhplus.be.server.domain.payment.Payment;

import java.util.List;

public interface PaymentRepository {
	Payment save(Payment payment);
	List<Payment> saveAll(List<Payment> payment);
	Payment findByOrderId(Long orderId);
}
