package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.product.Product;

import java.util.List;

public record OrderDetailResult (
	Long orderId,
	List<Product> productList,
	List<Coupon> coupons
){
	public OrderDetailResult from(Long orderId, List<Product> productList, List<Coupon> coupons) {
		return new OrderDetailResult(
			orderId,
			productList,
			coupons
		);
	}
}
