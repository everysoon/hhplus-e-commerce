package kr.hhplus.be.server.application.user;

import kr.hhplus.be.server.domain.user.UserCoupon;
import kr.hhplus.be.server.domain.user.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserCouponService {
	private final UserCouponRepository userCouponRepository;

	public List<UserCoupon> findByUserId(Long userId) {
		return userCouponRepository.findByUserId(userId);
	}
	public Boolean existsByUserIdAndCouponId(Long userId, List<UUID> couponIds) {
		return userCouponRepository.existsByUserIdAndCouponId(userId,couponIds);
	}
}
