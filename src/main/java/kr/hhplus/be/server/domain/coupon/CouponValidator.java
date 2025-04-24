package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.repository.UserCouponRepository;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CouponValidator {
	private final UserCouponRepository userCouponRepository;
	public void isCouponIdValidUuid(String id) {
		try {
			UUID.fromString(id);
		} catch (IllegalArgumentException e) {
			throw new CustomException(ErrorCode.INVALID_COUPON_ID);
		}
	}
	public void isValidCoupon(Coupon coupon) {
		isCouponIdValidUuid(coupon.getId());
		coupon.validExpired();
		coupon.validateStock();
	}
	public void duplicateIssued(Long userId, String couponId) {
		List<UserCoupon> userCoupons = userCouponRepository.findByUserIdAndCouponIds(userId, List.of(couponId));
		if(!userCoupons.isEmpty()){
			throw new CustomException(ErrorCode.DUPLICATE_COUPON_CLAIM);
		}
	}
}
