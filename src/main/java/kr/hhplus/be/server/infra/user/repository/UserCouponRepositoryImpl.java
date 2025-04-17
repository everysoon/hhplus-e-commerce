package kr.hhplus.be.server.infra.user.repository;

import kr.hhplus.be.server.application.coupon.CouponValidCommand;
import kr.hhplus.be.server.application.coupon.IssueCouponCommand;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.user.repository.UserCouponRepository;
import kr.hhplus.be.server.infra.coupon.entity.UserCouponEntity;
import kr.hhplus.be.server.support.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static kr.hhplus.be.server.support.config.swagger.ErrorCode.*;

@Repository
@RequiredArgsConstructor
public class UserCouponRepositoryImpl implements UserCouponRepository {
	private final UserCouponJpaRepository userCouponJpaRepository;
	@Override
	public List<UserCoupon> findByUserId(Long userId) {
		return userCouponJpaRepository.findByUserId(userId).stream().map(UserCouponEntity::toDomain).toList();
	}

	@Override
	public void validateUserCoupons(CouponValidCommand command) {
		List<UserCoupon> userCoupons = findByUserIdAndCouponIds(command.userId(), command.couponIds());
		if(userCoupons.size() != command.couponIds().size()) {
			throw new CustomException(INVALID_USER_COUPON);
		}
		userCoupons.forEach(userCoupon -> {
			if(!userCoupon.isValid()){
				throw new  CustomException(USED_COUPON);
			}
		});
	}

	@Override
	public void validateDuplicateIssued(IssueCouponCommand command) {
		List<UserCouponEntity> userCoupons= userCouponJpaRepository.findByUserIdAndCouponIds(command.userId(),List.of(command.couponId()));
		if(!userCoupons.isEmpty()){
			throw new CustomException(DUPLICATE_COUPON_CLAIM);
		}
	}

	@Override
	public UserCoupon save(UserCoupon coupon) {
		return userCouponJpaRepository.save(UserCouponEntity.from(coupon)).toDomain();
	}

	@Override
	public List<UserCoupon> findByUserIdAndCouponIds(Long userId, List<UUID> couponIds) {
		return userCouponJpaRepository.findByUserIdAndCouponIds(userId,couponIds).stream().map(UserCouponEntity::toDomain).toList();
	}
}
