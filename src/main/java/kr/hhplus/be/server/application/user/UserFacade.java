package kr.hhplus.be.server.application.user;

import kr.hhplus.be.server.application.order.service.OrderHistoryService;
import kr.hhplus.be.server.application.order.service.OrderItemService;
import kr.hhplus.be.server.domain.order.OrderDetail;
import kr.hhplus.be.server.domain.order.OrderHistory;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.user.UserCoupon;
import kr.hhplus.be.server.interfaces.dto.UserCouponDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserFacade {
	private final UserService userService;
	private final OrderHistoryService orderHistoryService;
	private final OrderItemService orderItemService;
	private final UserCouponService userCouponService;

	public OrderDetail getOrders(Long userId) {
		List<OrderHistory> histories = orderHistoryService.findByUserId(userId);
		List<OrderItem> orderItems = histories.stream()
			.map(OrderHistory::getOrderId)
			.map(orderItemService::findByOrderId)
			.flatMap(List::stream)
			.toList();

		return OrderDetail.from(userId, orderItems, histories);
	}

	public UserCouponDTO.Response issueCoupon(Long userId) {
		return null;
	}

	public List<UserCoupon> getUserCoupons(Long userId) {
		return userCouponService.findByUserId(userId);
	}
}
