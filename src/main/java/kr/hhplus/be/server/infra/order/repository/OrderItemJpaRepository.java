package kr.hhplus.be.server.infra.order.repository;

import kr.hhplus.be.server.infra.order.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemJpaRepository extends JpaRepository<OrderItemEntity, Long> {
}
