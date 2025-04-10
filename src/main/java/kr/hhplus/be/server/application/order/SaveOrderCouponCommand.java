package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.order.Order;

import java.util.List;

public record SaveOrderCouponCommand (
	Order order,
	List<Coupon> couponList
){
	public static SaveOrderCouponCommand of(Order order, List<Coupon> couponList){
		return new SaveOrderCouponCommand(order,couponList);
	}
}
