package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.coupon.UseCouponInfo;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;

import java.util.List;

public class OrderCommand {
	public record Create(

		List<OrderItem> orderItems,
		UseCouponInfo couponInfo
	) {
		public static Create of(List<OrderItem> orderItems, UseCouponInfo couponInfo) {
			return new Create(orderItems, couponInfo);
		}
	}
	public record Save(
		Order order,
		List<Coupon> coupons
	){
		public static Save of(Order order, List<Coupon> coupons) {
			return new Save(order, coupons);
		}
	}
}
