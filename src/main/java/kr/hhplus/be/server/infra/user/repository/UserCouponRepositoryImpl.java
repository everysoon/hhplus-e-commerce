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

import static kr.hhplus.be.server.support.config.swagger.ErrorCode.*;

@Repository
@RequiredArgsConstructor
public class UserCouponRepositoryImpl implements UserCouponRepository {
	private final UserCouponJpaRepository userCouponJpaRepository;

	@Override
	public List<UserCoupon> findAll() {
		return userCouponJpaRepository.findAll().stream().map(UserCouponEntity::toDomain).toList();
	}

	@Override
	public List<UserCoupon> findByUserId(Long userId) {
		return userCouponJpaRepository.findByUserId(userId).stream()
			.map(UserCouponEntity::toDomain)
			.toList();
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
	public UserCoupon save(IssueCouponCommand command) {
		UserCoupon userCoupon = UserCoupon.of(command.userId(), command.couponId());
		return userCouponJpaRepository.save(UserCouponEntity.from(userCoupon)).toDomain();
	}

	@Override
	public List<UserCoupon> findByUserIdAndCouponIds(Long userId, List<String> couponIds) {
		return userCouponJpaRepository.findByUserIdAndCouponIds(userId,couponIds).stream().map(UserCouponEntity::toDomain).toList();
	}

	@Override
	public void updateAll(List<UserCoupon> userCoupons) {
		List<UserCouponEntity> userCouponEntities = userCoupons.stream().map(UserCouponEntity::update).toList();
		userCouponJpaRepository.saveAll(userCouponEntities);
	}

	@Override
	public void saveAll(List<UserCoupon> userCoupons) {
		List<UserCouponEntity> userCouponEntities = userCoupons.stream().map(UserCouponEntity::from).toList();
		userCouponJpaRepository.saveAll(userCouponEntities);
	}
}
