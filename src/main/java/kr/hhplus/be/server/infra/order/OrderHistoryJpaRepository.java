package kr.hhplus.be.server.infra.order;

import kr.hhplus.be.server.domain.order.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderHistoryJpaRepository extends JpaRepository<OrderHistory, Long> {
	@Query("""
		SELECT oh FROM OrderHistory oh
		LEFT JOIN Order o ON o.id = oh.orderId
		WHERE o.userId =:userId
		""")
	List<OrderHistory> findByUserId(Long userId);
}
