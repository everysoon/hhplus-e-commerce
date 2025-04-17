package kr.hhplus.be.server.infra.payment.repository;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import kr.hhplus.be.server.infra.payment.entity.PaymentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {
	private final PaymentJpaRepository paymentJpaRepository;
	@Override
	public Payment save(Payment payment) {
		return paymentJpaRepository.save(PaymentEntity.from(payment)).toDomain();
	}
}
