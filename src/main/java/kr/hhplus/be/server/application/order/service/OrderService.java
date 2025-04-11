package kr.hhplus.be.server.application.order.service;

import kr.hhplus.be.server.application.order.CreateOrderCommand;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderHistory;
import kr.hhplus.be.server.domain.order.repository.OrderHistoryRepository;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderHistoryRepository orderHistoryRepository;

	public Order create(CreateOrderCommand command) {
		Order order = Order.create(command.userId());
		order.calculateTotalPrice(command.orderItems());
		command.coupons().forEach(order::applyCoupon);
		return order;
	}

	public Order save(Order order) {
		OrderHistory history = OrderHistory.of(order,"SAVE");
		orderHistoryRepository.save(history);
		return orderRepository.save(order);
	}
}
