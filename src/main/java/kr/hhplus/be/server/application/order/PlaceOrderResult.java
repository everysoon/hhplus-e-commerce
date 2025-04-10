package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.payment.Payment;

import java.util.List;

public record PlaceOrderResult(
	Long userId,
	List<OrderItem> orderItems,
	Payment payment,
	Order order,
	List<Coupon> coupons
) {
	public static PlaceOrderResult of(Long userId, List<OrderItem> orderItems, Payment payment, Order order,List<Coupon> coupons) {
		return new PlaceOrderResult(
			userId,
			orderItems,
			payment,
			order,
			coupons
		);
	}
}
