package kr.hhplus.be.server.infra.payment;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {
	private final PaymentJpaRepository paymentJpaRepository;
	@Override
	public Payment save(Payment payment) {
		return paymentJpaRepository.saveAndFlush(payment);
	}

	@Override
	public List<Payment> saveAll(List<Payment> payments) {
		return paymentJpaRepository.saveAll(payments);
	}

	@Override
	public Payment findByOrderId(Long orderId) {
		return paymentJpaRepository.findByOrderId(orderId)
			.orElseThrow(()->new CustomException(ErrorCode.NOT_EXIST_PAYMENT_BY_ORDER));
	}
}
