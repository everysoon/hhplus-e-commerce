package kr.hhplus.be.server.application.user;

import java.math.BigDecimal;
import java.util.List;
import kr.hhplus.be.server.application.coupon.dto.CouponResponseDTO;
import kr.hhplus.be.server.application.order.service.OrderHistoryService;
import kr.hhplus.be.server.application.order.service.OrderItemService;
import kr.hhplus.be.server.application.order.service.OrderService;
import kr.hhplus.be.server.application.user.dto.UserCouponResponseDTO;
import kr.hhplus.be.server.application.user.dto.UserOrderResponseDTO;
import kr.hhplus.be.server.application.user.dto.UserResponseDTO;
import kr.hhplus.be.server.domain.order.OrderDetail;
import kr.hhplus.be.server.domain.order.OrderHistory;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.user.PointHistory;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserCoupon;
import kr.hhplus.be.server.domain.user.repository.PointHistoryRepository;
import kr.hhplus.be.server.domain.user.repository.UserCouponRepository;
import kr.hhplus.be.server.domain.user.repository.UserRepository;
import kr.hhplus.be.server.infra.user.entity.PointStatus;
import kr.hhplus.be.server.infra.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class UserFacade {
	private final UserService userService;
	private final OrderHistoryService orderHistoryService;
	private final OrderItemService orderItemService;

	public OrderDetail getOrders(Long userId){
//		User user = userService.get(userId);
		List<OrderHistory> histories = orderHistoryService.findByUserId(userId);
		List<OrderItem> orderItems = histories.stream()
			.map(OrderHistory::getOrderId)
			.map(orderItemService::findByOrderId)
			.flatMap(List::stream)
			.toList();

		return OrderDetail.from(userId,orderItems,histories);
	}
}
