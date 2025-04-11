package kr.hhplus.be.server.infra.order.repository;

import kr.hhplus.be.server.infra.order.entity.OrderHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHistoryJpaRepository extends JpaRepository<OrderHistoryEntity, Long> {
}
