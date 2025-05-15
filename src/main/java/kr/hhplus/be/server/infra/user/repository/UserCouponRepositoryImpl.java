package kr.hhplus.be.server.infra.user.repository;

import kr.hhplus.be.server.application.coupon.CouponCommand;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponMapper;
import kr.hhplus.be.server.domain.user.repository.UserCouponRepository;
import kr.hhplus.be.server.infra.coupon.entity.UserCouponEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserCouponRepositoryImpl implements UserCouponRepository {
	private final UserCouponJpaRepository userCouponJpaRepository;
	private final UserCouponMapper mapper;
	@Override
	public List<UserCoupon> findAll() {
		return userCouponJpaRepository.findAll().stream()
			.map(mapper::toDomain)
			.toList();
	}

	@Override
	public List<UserCoupon> findByUserId(Long userId) {
		return userCouponJpaRepository.findByUserId(userId).stream()
			.map(mapper::toDomain)
			.toList();
	}

	@Override
	public UserCoupon save(CouponCommand.UnitCouponValid command) {
		UserCoupon userCoupon = UserCoupon.of(command.userId(), command.coupon());
		UserCouponEntity entity = userCouponJpaRepository.save(mapper.toEntity(userCoupon));
		return mapper.toDomain(entity);
	}

	@Override
	public List<UserCoupon> findByUserIdAndCouponIds(Long userId, List<String> couponIds) {
		return userCouponJpaRepository.findByUserIdAndCouponIds(userId,couponIds).stream()
			.map(mapper::toDomain).toList();
	}

	@Override
	public void updateAll(List<UserCoupon> userCoupons) {
		List<UserCouponEntity> userCouponEntities = userCoupons.stream().map(UserCouponEntity::update).toList();
		userCouponJpaRepository.saveAll(userCouponEntities);
	}

	@Override
	public List<UserCoupon> saveAll(List<UserCoupon> userCoupons) {
		List<UserCouponEntity> userCouponEntities = userCoupons.stream().map(mapper::toEntity).toList();
		return userCouponJpaRepository.saveAll(userCouponEntities).stream().map(mapper::toDomain).toList();
	}
}
