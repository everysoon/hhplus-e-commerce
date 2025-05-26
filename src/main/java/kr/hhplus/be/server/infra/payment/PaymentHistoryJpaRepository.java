package kr.hhplus.be.server.infra.payment;

import kr.hhplus.be.server.domain.payment.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentHistoryJpaRepository extends JpaRepository<PaymentHistory, Long> {
	@Query("SELECT ph FROM PaymentHistory ph JOIN Order o ON o.id = ph.orderId WHERE o.userId =:userId")
	List<PaymentHistory> findByUserId(@Param("userId") Long userId);
	Optional<PaymentHistory> findByTransactionId(String transactionId);
}
