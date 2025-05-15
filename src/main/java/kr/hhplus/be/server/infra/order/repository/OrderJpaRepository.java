package kr.hhplus.be.server.infra.order.repository;

import kr.hhplus.be.server.infra.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
	OrderEntity findByIdAndUserId(Long orderId, Long userId);
	List<OrderEntity> findByUserId(Long userId);
}
