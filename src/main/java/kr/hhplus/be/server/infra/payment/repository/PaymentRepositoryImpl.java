package kr.hhplus.be.server.infra.payment.repository;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import kr.hhplus.be.server.infra.payment.entity.PaymentEntity;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {
	private final PaymentJpaRepository paymentJpaRepository;
	@Override
	public Payment save(Payment payment) {
		return paymentJpaRepository.saveAndFlush(PaymentEntity.from(payment)).toDomain();
	}

	@Override
	public Payment findByOrderId(Long orderId) {
		return paymentJpaRepository.findByOrderId(orderId)
			.orElseThrow(()->new CustomException(ErrorCode.NOT_EXIST_PAYMENT_BY_ORDER))
			.toDomain();
	}
}
