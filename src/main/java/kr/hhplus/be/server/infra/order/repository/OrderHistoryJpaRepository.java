package kr.hhplus.be.server.infra.order.repository;

import kr.hhplus.be.server.infra.order.entity.OrderHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderHistoryJpaRepository extends JpaRepository<OrderHistoryEntity, Long> {
	@Query("""
		SELECT oh FROM OrderHistoryEntity oh
		LEFT JOIN OrderEntity o ON o.id = oh.orderId
		WHERE o.userId =:userId
		""")
	List<OrderHistoryEntity> findByUserId(Long userId);
}
