package kr.hhplus.be.server.application.coupon.handler;

import kr.hhplus.be.server.domain.coupon.event.CouponIssueDLQEvent;
import kr.hhplus.be.server.application.coupon.CouponIssueDLQPublisher;
import kr.hhplus.be.server.application.coupon.CouponIssueProcessor;
import kr.hhplus.be.server.domain.coupon.event.CouponIssuedEvent;
import kr.hhplus.be.server.support.utils.RetryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Component
@RequiredArgsConstructor
@Slf4j
public class CouponIssueHandler {

	private final CouponIssueProcessor processor;
	private final RetryHandler retryHandler;

	private final CouponIssueDLQPublisher couponIssueDLQPublisher;

	@TransactionalEventListener(phase = AFTER_COMMIT)
	public void handle(CouponIssuedEvent event) {
		log.info("CouponIssueHandler event: {}", event);
		retryHandler.runWithRetry(() -> {
			Long userId = event.userId();
			String couponId = event.couponId();

			processor.process(userId, couponId); // DB 반영
			log.info("쿠폰 발급 처리 성공: userId={}, couponId={}", userId, couponId);
			return null;
		}, e -> couponIssueDLQPublisher.publishEvent(new CouponIssueDLQEvent(
			event.userId(), event.couponId(), e
		)), 3);
	}
}
