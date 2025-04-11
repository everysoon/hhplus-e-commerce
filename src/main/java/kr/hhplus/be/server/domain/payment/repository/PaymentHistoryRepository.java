package kr.hhplus.be.server.domain.payment.repository;

import kr.hhplus.be.server.domain.payment.PaymentHistory;

import java.util.List;

public interface PaymentHistoryRepository {
	PaymentHistory save(PaymentHistory paymentHistory);
	List<PaymentHistory> findByUserId(Long userId);
	PaymentHistory findByTransactionId(Long transactionId);
}
