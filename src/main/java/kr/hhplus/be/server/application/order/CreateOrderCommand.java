package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.order.OrderItem;

import java.util.List;

public record CreateOrderCommand (
	Long userId,
	List<OrderItem> orderItems,
	List<Coupon> coupons
){
	public static CreateOrderCommand of(Long userId,List<OrderItem> orderItems, List<Coupon> coupons){
		return new CreateOrderCommand(userId, orderItems, coupons);
	}
}
