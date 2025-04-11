package kr.hhplus.be.server.infra.payment.repository;

import kr.hhplus.be.server.domain.payment.PaymentHistory;
import kr.hhplus.be.server.domain.payment.repository.PaymentHistoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentHistoryRepositoryImpl implements PaymentHistoryRepository {

	@Override
	public PaymentHistory save(PaymentHistory paymentHistory) {
		return null;
	}

	@Override
	public List<PaymentHistory> findByUserId(Long userId) {
		return List.of();
	}

	@Override
	public PaymentHistory findByTransactionId(Long transactionId) {
		return null;
	}
}
