package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.support.utils.RetryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CouponIssueStreamListener implements StreamListener<String, MapRecord<String, String, String>> {

	private final CouponIssueProcessor processor;
	private final RetryHandler retryHandler;

	private final CouponIssueDLQPublisher dlqMessagePublisher;

	@Override
	public void onMessage(MapRecord<String, String, String> message) {
		log.info("onMessage: {}", message);
		retryHandler.runWithRetry(()->{
			Long userId = Long.parseLong(message.getValue().get("userId"));
			String couponId = message.getValue().get("couponId");

			processor.process(userId, couponId); // DB 반영
			log.info("쿠폰 발급 처리 성공: userId={}, couponId={}", userId, couponId);
			return null;
		},e->dlqMessagePublisher.publish(message,e),3);
	}
}
