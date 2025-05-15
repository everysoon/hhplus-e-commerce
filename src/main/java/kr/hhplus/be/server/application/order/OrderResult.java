package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.product.Product;

import java.util.List;

public class OrderResult {
	public record Cancel(
		Order order,
		Payment payment
	) {
		public static Cancel of(Order order, Payment payment) {
			return new Cancel(order, payment);
		}
	}
	public record DetailByOrder(
		Long orderId,
		List<Product> productList,
		List<Coupon> coupons
	){
		public static DetailByOrder from(Order order) {
			return new DetailByOrder(
				order.getId(),
				order.getOrderItems().stream().map(OrderItem::getProduct).toList(),
				order.getCoupons()
			);
		}
	}
	public record InfoByUser (
		Long userId,
		List<OrderResult.DetailByOrder> results
	){

		public static InfoByUser from(Long userId, List<OrderResult.DetailByOrder> results) {
			return new InfoByUser(
				userId,
				results
			);
		}
	}
	public record Place(
		Long userId,
		List<Product> products,
		Payment payment,
		Order order
	) {
		public static Place of(Long userId, List<Product> products, Payment payment, Order order) {
			return new Place(
				userId,
				products,
				payment,
				order
			);
		}
	}

}
