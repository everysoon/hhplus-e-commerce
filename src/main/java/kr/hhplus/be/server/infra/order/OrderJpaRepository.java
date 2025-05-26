package kr.hhplus.be.server.infra.order;

import kr.hhplus.be.server.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
	Optional<Order> findByIdAndUserId(Long orderId, Long userId);
	List<Order> findByUserId(Long userId);
	@Query("SELECT o FROM Order o LEFT JOIN OrderItem  oi ON o.id = oi.order.id WHERE o.userId =:userId AND oi.productId IN :productIds")
    Order existsOrder(@Param("userId") Long userId, @Param("productIds") List<Long> productIds);
}
