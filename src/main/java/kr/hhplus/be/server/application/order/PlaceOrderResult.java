package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.product.Product;

import java.util.List;

public record PlaceOrderResult(
	Long userId,
	List<Product> products,
	Payment payment,
	Order order,
	List<Coupon> coupons
) {
	public static PlaceOrderResult of(Long userId, List<Product> products, Payment payment, Order order,List<Coupon> coupons) {
		return new PlaceOrderResult(
			userId,
			products,
			payment,
			order,
			coupons
		);
	}
}
