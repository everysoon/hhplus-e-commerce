package kr.hhplus.be.server.infra.kafka.consumer;

import kr.hhplus.be.server.application.coupon.CouponCommand;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.event.CouponIssuedEvent;
import kr.hhplus.be.server.domain.user.UserCouponRepository;
import kr.hhplus.be.server.support.aop.event.idempotent.Idempotent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponIssuedEventConsumer {
	private final CouponRepository couponRepository;
	private final UserCouponRepository userCouponRepository;

	@KafkaListener(topics = "coupon.issued", groupId = "coupon-issue-consumer")
	@Idempotent(key = "#event.getIdempotentKey()")
	public void consume(CouponIssuedEvent event) {
		Long userId = event.userId();
		String couponId = event.couponId();

		log.info("Kafka 메시지 소비: userId={}, couponId={}", userId, couponId);

		Coupon coupon = couponRepository.issue(couponId);
		userCouponRepository.save(CouponCommand.UnitCouponValid.of(userId, coupon.getId()));

		log.info("쿠폰 발급 DB 처리 완료: userId={}, couponId={}", userId, couponId);
	}
}
