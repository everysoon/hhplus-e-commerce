package kr.hhplus.be.server.infra.payment.repository;

import kr.hhplus.be.server.infra.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
}
