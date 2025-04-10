package kr.hhplus.be.server.application.order.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kr.hhplus.be.server.application.order.command.OrderCommand;
import kr.hhplus.be.server.application.order.dto.OrderRequestDTO;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderHistory;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.repository.OrderHistoryRepository;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import kr.hhplus.be.server.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderHistoryRepository orderHistoryRepository;
	private final OrderRepository orderRepository;

	public Order create(OrderCommand command) {
		BigDecimal totalPrice = command.orderItems().stream()
			.map(OrderItem::getProduct)
			.map(Product::getPrice)
			.reduce(BigDecimal.ZERO, BigDecimal::add);

		return new Order(
			null,                   // id (미지정 상태)
			command.userId(),
			null,                   // paymentId (결제 전이므로 null)
			totalPrice,
			BigDecimal.ZERO,        // totalDiscount 초기값
			LocalDateTime.now()     // orderedAt
		);
	}

}
