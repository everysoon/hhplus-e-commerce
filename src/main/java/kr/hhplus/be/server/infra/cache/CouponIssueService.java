package kr.hhplus.be.server.infra.cache;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RSet;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponIssueService {
	private final CouponIssueRepository couponIssueRepository;
	public RSet<Long> issueCoupon(Long userId, String couponId) {
		return couponIssueRepository.issueCoupon(userId, couponId);
	}

	public RAtomicLong initCouponStock(String couponId, int initialQuantity){
		return couponIssueRepository.initCouponStock(couponId, initialQuantity);
	}
}
