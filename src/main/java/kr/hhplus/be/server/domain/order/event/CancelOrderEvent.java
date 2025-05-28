package kr.hhplus.be.server.domain.order.event;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public record CancelOrderEvent(
	Long userId,
	Long orderId,
	List<OrderItem> orderItems,
	List<String> couponIds,
	BigDecimal totalPrice
) {
	public static CancelOrderEvent of(Long userId, Order order){
		return new CancelOrderEvent(
			userId,
			order.getId(),
			order.getOrderItems(),
			order.getUsedUserCouponIds(),
			order.getTotalPrice()
		);
	}
	public String getIdempotencyKey(){
		return "order:canceled".concat(orderId.toString());
	}
}
