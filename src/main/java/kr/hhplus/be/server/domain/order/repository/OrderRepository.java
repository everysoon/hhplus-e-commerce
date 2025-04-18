package kr.hhplus.be.server.domain.order.repository;

import kr.hhplus.be.server.domain.order.Order;

import java.util.List;

public interface OrderRepository {
	Order save(Order order);
	Order findById(Long orderId);
	Order findByIdAndUserId(Long orderId, Long userId);
	List<Order> findByUserId(Long userId);
	List<Order> findAll();
}
