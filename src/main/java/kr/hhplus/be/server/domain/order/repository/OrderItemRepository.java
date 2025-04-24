package kr.hhplus.be.server.domain.order.repository;

import kr.hhplus.be.server.domain.order.OrderItem;

import java.util.List;

public interface OrderItemRepository {
	List<OrderItem> saveAll(List<OrderItem> orderItems);
	List<OrderItem> findByOrderId(Long orderId);
}
