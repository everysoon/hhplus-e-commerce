package kr.hhplus.be.server.application.order.service;

import kr.hhplus.be.server.application.order.CreateOrderCommand;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderHistory;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.repository.OrderHistoryRepository;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderHistoryRepository orderHistoryRepository;
	private final Logger logger = LoggerFactory.getLogger(OrderService.class);

	public Order create(CreateOrderCommand command) {
		logger.info("### create : {}", command);
		BigDecimal totalPrice = command.orderItems()
			.stream().map(
			OrderItem::getUnitPrice
		).reduce(BigDecimal.ZERO, BigDecimal::add);

		return new Order(
			null,
			command.couponInfo().userId(),
			command.couponIdsToString(),
			command.orderItems(),
			totalPrice,
			command.getDiscountAmount(totalPrice),
			LocalDateTime.now()
		);
	}

	public List<OrderHistory> findHistoryByUserId(Long userId) {
		return orderHistoryRepository.findByUserId(userId);
	}
	public List<Order> findOrderByUserId(Long userId){
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

	public void cancel(Long orderId) {
		OrderHistory history = OrderHistory.of(orderId, "CANCEL");
		orderHistoryRepository.save(history);
	}
}
