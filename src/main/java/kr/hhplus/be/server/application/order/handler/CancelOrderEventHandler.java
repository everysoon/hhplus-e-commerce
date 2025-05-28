package kr.hhplus.be.server.application.order.handler;

import kr.hhplus.be.server.application.coupon.CouponCommand;
import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.point.PointCommand;
import kr.hhplus.be.server.application.point.PointService;
import kr.hhplus.be.server.application.product.ProductCommand;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.domain.order.event.CancelOrderEvent;
import kr.hhplus.be.server.domain.order.event.CancelOrderPaidEvent;
import kr.hhplus.be.server.infra.kafka.publisher.OrderEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CancelOrderEventHandler {
	private final PointService pointService;
	private final CouponService couponService;
	private final ProductService productService;

	private final OrderEventPublisher orderEventPublisher;

	@EventListener
	public void handle(CancelOrderEvent event) {
		// 포인트 환불
		pointService.refund(PointCommand.Refund.of(event.userId(), event.totalPrice()));
		// 쿠폰 상태 복원 (쿠폰 사용했으면)
		couponService.restore(CouponCommand.Restore.of(event.userId(), event.couponIds()));
		// 재고 복원
		productService.increaseStock(ProductCommand.Refund.of(event.orderItems(), event.orderId()));
		// 결제 취소
		orderEventPublisher.sendOrderCancelPaidEvent(new CancelOrderPaidEvent(event.orderId(), event.totalPrice()));
	}
}
