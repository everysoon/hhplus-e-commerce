package kr.hhplus.be.server.infra.cache;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RAtomicLong;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponIssueService {
	private final CouponIssueRepository couponIssueRepository;
	public boolean issueCoupon(Long userId, String couponId) {
		return couponIssueRepository.issueCouponV2(userId, couponId);
	}

	public RAtomicLong initCouponStock(String couponId, int initialQuantity){
		return couponIssueRepository.initCouponStock(couponId, initialQuantity);
	}
}
