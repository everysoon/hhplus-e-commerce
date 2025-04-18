package kr.hhplus.be.server.infra.payment.repository;

import kr.hhplus.be.server.infra.payment.entity.PaymentHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentHistoryJpaRepository extends JpaRepository<PaymentHistoryEntity, Long> {
	@Query("SELECT ph FROM PaymentHistoryEntity ph JOIN OrderEntity o ON o.id = ph.orderId WHERE o.userId =:userId")
	List<PaymentHistoryEntity> findByUserId(@Param("userId") Long userId);
	Optional<PaymentHistoryEntity> findByTransactionId(String transactionId);
}
