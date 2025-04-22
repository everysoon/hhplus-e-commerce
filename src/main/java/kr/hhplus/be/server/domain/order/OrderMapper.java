package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.infra.order.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {
	private final CouponRepository couponRepository;
	private final OrderItemMapper orderItemMapper;

	public Order toDomain(OrderEntity entity) {
		List<Coupon> coupons = entity.getUsedUserCouponIds() == null ? null : entity.getUsedUserCouponIds().stream().map(couponRepository::findById).toList();
		List<OrderItem> orderItems = entity.getOrderItems().stream().map(orderItemMapper::toDomain).toList();
		return new Order(
			entity.getId(),
			entity.getUserId(),
			coupons,
			orderItems,
			entity.getTotalPrice(),
			entity.getTotalDiscount(),
			entity.getOrderedAt()
		);
	}

	public OrderEntity toEntity(Order domain) {
		return new OrderEntity(
			domain.getId(),
			domain.getUserId(),
			domain.getCoupons() == null ? null : domain.getCoupons().stream().map(Coupon::getId).toList(),
			domain.getOrderItems().stream().map(orderItemMapper::toEntity).toList(),
			domain.getTotalPrice(),
			domain.getTotalDiscount(),
			domain.getOrderedAt()
		);
	}
}
