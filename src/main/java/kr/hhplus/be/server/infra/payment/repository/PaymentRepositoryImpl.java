package kr.hhplus.be.server.infra.payment.repository;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

	@Override
	public Payment save(Payment payment) {
		return null;
	}
}
