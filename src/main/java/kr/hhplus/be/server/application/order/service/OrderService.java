package kr.hhplus.be.server.application.order.service;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

import java.util.List;
import kr.hhplus.be.server.application.order.OrderCommand;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderHistory;
import kr.hhplus.be.server.domain.order.repository.OrderHistoryRepository;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import kr.hhplus.be.server.infra.order.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderHistoryRepository orderHistoryRepository;
	private final Logger logger = LoggerFactory.getLogger(OrderService.class);

	public boolean isExistingOrder(OrderCommand.Exist command) {
		Order order = orderRepository.existsOrder(command.userId(),
			command.productIds());
		return order != null;
	}

	public Order create(OrderCommand.Create command) {

		logger.info("### create : {}", command);
		return new Order(
			command.couponInfo().userId(),
			command.couponInfo().coupons(),
			command.orderItems()
		);
	}

	public List<OrderHistory> findHistoryByUserId(Long userId) {
		return orderHistoryRepository.findByUserId(userId);
	}

	public List<Order> findOrderByUserId(Long userId) {
		return orderRepository.findByUserId(userId);
	}

	public Order findById(Long orderId) {
		return orderRepository.findById(orderId);
	}

	public Order findByIdAndUserId(Long orderId, Long userId) {
		return orderRepository.findByIdAndUserId(orderId, userId);
	}

	@Transactional
	public Order save(Order order) {
		Order save = orderRepository.save(order);
		OrderHistory history = OrderHistory.of(save.getId());
		orderHistoryRepository.save(history);
		return save;
	}

	@Transactional(propagation = MANDATORY)
	public void cancel(Order order) {
		OrderHistory history = OrderHistory.of(order.getId(), OrderStatus.CANCELED.toString());
		orderHistoryRepository.save(history);
	}
}
