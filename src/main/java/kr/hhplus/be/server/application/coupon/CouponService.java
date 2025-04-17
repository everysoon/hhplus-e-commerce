package kr.hhplus.be.server.application.coupon;

import java.util.List;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.user.repository.UserCouponRepository;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponService {
	private final CouponRepository couponRepository;
	private final UserCouponRepository userCouponRepository;

	public List<UserCoupon> findMineByUserId(Long userId) {
		return userCouponRepository.findByUserId(userId);
	}

	public Boolean validIsMine(CouponValidCommand command) {
		return userCouponRepository.existsByUserIdAndCouponId(command);
	}
	public UserCoupon saveUserCoupon(UserCoupon coupon) {
		return userCouponRepository.save(coupon);
	}
	public List<UserCoupon> use(UseCouponCommand command) {
		return command.getUserCoupons()
			.stream()
			.peek(UserCoupon::use)
			.toList();
	}
	public UserCoupon issueByUser(UserCoupon coupon) {
		return userCouponRepository.save(coupon);
	}
	public long countCouponByUserId(IssueCouponCommand command) {
		return userCouponRepository.countCouponByUserId(command);
	}
	public Coupon findCouponById(UUID id) {
		return couponRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.NOT_EXIST_COUPON));
	}
	public Coupon issue(Coupon coupon) {
		return couponRepository.issue(coupon);
	}
}
