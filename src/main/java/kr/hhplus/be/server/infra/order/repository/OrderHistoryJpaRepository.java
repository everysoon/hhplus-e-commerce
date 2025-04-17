package kr.hhplus.be.server.infra.order.repository;

import kr.hhplus.be.server.infra.order.entity.OrderHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderHistoryJpaRepository extends JpaRepository<OrderHistoryEntity, Long> {
	List<OrderHistoryEntity> findByOrderUserId(Long userId);
}
