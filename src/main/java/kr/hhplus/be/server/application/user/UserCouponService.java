package kr.hhplus.be.server.application.user;

import java.util.List;
import kr.hhplus.be.server.application.coupon.CouponValidCommand;
import kr.hhplus.be.server.application.coupon.IssueCouponCommand;
import kr.hhplus.be.server.application.coupon.UseCouponCommand;
import kr.hhplus.be.server.domain.user.UserCoupon;
import kr.hhplus.be.server.domain.user.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCouponService {

	private final UserCouponRepository userCouponRepository;

	public List<UserCoupon> findByUserId(Long userId) {
		return userCouponRepository.findByUserId(userId);
	}

	public Boolean existsByUserIdAndCouponId(CouponValidCommand command) {
		return userCouponRepository.existsByUserIdAndCouponId(command);
	}

	public List<UserCoupon> use(UseCouponCommand command) {
		return command.getUserCoupons()
			.stream().peek(UserCoupon::use)
			.toList();
	}
	public UserCoupon save(UserCoupon coupon) {
		return userCouponRepository.save(coupon);
	}
	public long countCouponByUserId(IssueCouponCommand command) {
		return userCouponRepository.countCouponByUserId(command);
	}
}
