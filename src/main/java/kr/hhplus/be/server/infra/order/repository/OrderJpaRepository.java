package kr.hhplus.be.server.infra.order.repository;

import kr.hhplus.be.server.infra.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
}
