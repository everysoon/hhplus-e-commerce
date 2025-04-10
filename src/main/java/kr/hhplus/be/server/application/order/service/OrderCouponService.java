package kr.hhplus.be.server.application.order.service;

import kr.hhplus.be.server.application.order.SaveOrderCouponCommand;
import kr.hhplus.be.server.domain.order.OrderCoupon;
import kr.hhplus.be.server.domain.order.repository.OrderCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderCouponService {
	private final OrderCouponRepository orderCouponRepository;

	public OrderCoupon save(OrderCoupon orderCoupon) {
		return orderCouponRepository.save(orderCoupon);
	}

	public List<OrderCoupon> saveAll(SaveOrderCouponCommand command) {
		List<OrderCoupon> orderCoupons = command.couponList().stream()
			.map(c -> OrderCoupon.of(command.order(), c)).toList();
		orderCoupons.forEach(orderCouponRepository::save);
		return orderCoupons;
	}
}
