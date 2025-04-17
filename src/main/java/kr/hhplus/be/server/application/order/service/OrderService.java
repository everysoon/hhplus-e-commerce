package kr.hhplus.be.server.application.order.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import kr.hhplus.be.server.application.order.CreateOrderCommand;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderHistory;
import kr.hhplus.be.server.domain.order.OrderItem;
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
		BigDecimal totalPrice = command.orderItems().stream().map(
			OrderItem::getUnitPrice
		).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal totalDiscount = command.coupons().stream().map(
			c -> c.getDiscountAmount(totalPrice)
		).reduce(BigDecimal.ZERO, BigDecimal::add);

		return new Order(
			null,
			command.userId(),
			command.coupons().stream().map(Coupon::getId).toList(),
			command.orderItems(),
			totalPrice,
			totalDiscount,
			LocalDateTime.now()
		);

	}

	public Order save(Order order) {
		OrderHistory history = OrderHistory.of(order);
		orderHistoryRepository.save(history);
		return orderRepository.save(order);
	}
}
