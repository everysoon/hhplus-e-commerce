package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.user.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponIssueProcessor {
	private final CouponRepository couponRepository;
	private final UserCouponRepository userCouponRepository;

	@Transactional
	public void process(Long userId, String couponId) {
		log.info("couponIssueProcessor : userId = {}, couponId = {}", userId, couponId);
		Coupon coupon = couponRepository.issue(couponId);
		userCouponRepository.save(CouponCommand.UnitCouponValid.of(userId,coupon));
	}
}
