package kr.hhplus.be.server.application.order.facade;

import java.util.List;
import java.util.Optional;
import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.order.command.OrderCommand;
import kr.hhplus.be.server.application.order.dto.OrderRequestDTO;
import kr.hhplus.be.server.application.order.dto.OrderResponseDTO;
import kr.hhplus.be.server.application.order.service.OrderService;
import kr.hhplus.be.server.application.payment.PaymentService;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.application.user.UserService;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class OrderFacade {
	private final OrderService orderService;
	private final PaymentService paymentService;
	private final ProductService productService;
	private final CouponService couponService;
	private final UserService userService;
	public OrderResponseDTO order(OrderCommand command) {
		// 유저 포인트 조회
		User user = userService.get(command.userId());
		// 상품 조회
		List<OrderItem> orderItems = command.orderItems().stream()
			.map(itemCommand -> {
				Product product = productService.findById(itemCommand.productId());
				return OrderItem.create(product, itemCommand.quantity());
			})
			.toList();
		// 쿠폰 유효성 확인
		// 만료 되진 않았는지, 쿠폰이 유저 소유가 맞는지
		List<Coupon> coupons = Optional.ofNullable(command.couponIds())
			.orElse(List.of())
			.stream()
			.map(couponService::findById)
			.toList();
		Order order = orderService.create(command.userId(), orderItems, coupons);
		if(order.getTotalPrice().compareTo(user.getPoint()) <=0){
			return null;
		}
		// 쿠폰 적용

		// 결제 시도
		// Order에 결제 추가
		// 주문 저장
		return null;
	}

}
