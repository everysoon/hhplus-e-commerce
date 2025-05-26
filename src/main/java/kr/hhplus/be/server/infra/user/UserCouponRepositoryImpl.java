package kr.hhplus.be.server.infra.user;

import kr.hhplus.be.server.application.coupon.CouponCommand;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.user.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserCouponRepositoryImpl implements UserCouponRepository {
	private final UserCouponJpaRepository userCouponJpaRepository;

	@Override
	public List<UserCoupon> findAll() {
		return userCouponJpaRepository.findAll().stream()
			.toList();
	}

	@Override
	public List<UserCoupon> findByUserId(Long userId) {
		return userCouponJpaRepository.findByUserId(userId).stream()
			.toList();
	}

	@Override
	public UserCoupon save(CouponCommand.UnitCouponValid command) {
		return userCouponJpaRepository.save(new UserCoupon(command.userId(),command.couponId()));
	}

	@Override
	public List<UserCoupon> findByUserIdAndCouponIds(Long userId, List<String> couponIds) {
		return userCouponJpaRepository.findByUserIdAndCouponIds(userId, couponIds).stream().toList();
	}

	@Override
	public void updateAll(List<UserCoupon> userCoupons) {
		List<UserCoupon> userCouponEntities = userCoupons.stream().toList();
		userCouponJpaRepository.saveAll(userCouponEntities);
	}

	@Override
	public List<UserCoupon> saveAll(List<UserCoupon> userCoupons) {
		List<UserCoupon> userCouponEntities = userCoupons.stream().toList();
		return userCouponJpaRepository.saveAll(userCouponEntities);
	}

	@Override
	public List<UserCoupon> updateExpiredCoupons(List<String> expiredCouponIds) {
		return userCouponJpaRepository.updateExpiredAll(expiredCouponIds).stream().toList();
	}

	@Override
	public List<UserCoupon> findByCouponIds(List<String> expiredCouponIds) {
		return userCouponJpaRepository.findByCouponIds(expiredCouponIds).stream().toList();
	}
}
