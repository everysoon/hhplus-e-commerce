package kr.hhplus.be.server.application.user;

import kr.hhplus.be.server.application.coupon.CouponValidCommand;
import kr.hhplus.be.server.application.coupon.UseCouponCommand;
import kr.hhplus.be.server.domain.user.UserCoupon;
import kr.hhplus.be.server.domain.user.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCouponService {
	private final UserCouponRepository userCouponRepository;

	public List<UserCoupon> findByUserId(Long userId) {
		return userCouponRepository.findByUserId(userId);
	}
	public Boolean existsByUserIdAndCouponId(CouponValidCommand command) {
		return userCouponRepository.existsByUserIdAndCouponId(command.userId(),command.couponIds());
	}
	public void use(UseCouponCommand command) {

	}
}
