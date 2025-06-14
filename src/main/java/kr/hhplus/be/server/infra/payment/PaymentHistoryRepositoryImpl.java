package kr.hhplus.be.server.infra.payment;

import kr.hhplus.be.server.domain.payment.PaymentHistory;
import kr.hhplus.be.server.domain.payment.repository.PaymentHistoryRepository;
import kr.hhplus.be.server.support.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.hhplus.be.server.support.config.swagger.ErrorCode.NOT_EXIST_PAYMENT_BY_TX_ID;

@Repository
@RequiredArgsConstructor
public class PaymentHistoryRepositoryImpl implements PaymentHistoryRepository {
	private final PaymentHistoryJpaRepository paymentHistoryJpaRepository;
	@Override
	public PaymentHistory save(PaymentHistory paymentHistory) {
		return paymentHistoryJpaRepository.save(paymentHistory);
	}

	@Override
	public List<PaymentHistory> findByUserId(Long userId) {
		return paymentHistoryJpaRepository.findByUserId(userId);
	}

	@Override
	public PaymentHistory findByTransactionId(String transactionId) {
		return paymentHistoryJpaRepository.findByTransactionId(transactionId)
			.orElseThrow(()->new CustomException(NOT_EXIST_PAYMENT_BY_TX_ID));
	}
}
