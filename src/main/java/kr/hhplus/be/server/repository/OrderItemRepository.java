package kr.hhplus.be.server.repository;

import kr.hhplus.be.server.entity.Order;
import kr.hhplus.be.server.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
