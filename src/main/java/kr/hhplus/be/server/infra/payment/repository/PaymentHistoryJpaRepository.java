package kr.hhplus.be.server.infra.payment.repository;

import kr.hhplus.be.server.infra.payment.entity.PaymentHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryJpaRepository extends JpaRepository<PaymentHistoryEntity, Long> {
}
