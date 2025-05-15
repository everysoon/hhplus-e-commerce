package kr.hhplus.be.server.infra.order.repository;

import kr.hhplus.be.server.infra.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
	OrderEntity findByIdAndUserId(Long orderId, Long userId);
	List<OrderEntity> findByUserId(Long userId);
	@Query("SELECT o FROM OrderEntity o LEFT JOIN OrderItemEntity  oi ON o.id = oi.order.id WHERE o.userId =:userId AND oi.productId IN :productIds")
	OrderEntity existsOrder(@Param("userId") Long userId,@Param("productIds") List<Long> productIds);
}
