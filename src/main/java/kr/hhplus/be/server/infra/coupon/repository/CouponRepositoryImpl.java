package kr.hhplus.be.server.infra.coupon.repository;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.infra.coupon.entity.CouponEntity;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {
	private final CouponJpaRepository couponJpaRepository;

	@Override
	public Coupon findById(String id) {
		return couponJpaRepository.findById(id)
			.orElseThrow(()->new CustomException(ErrorCode.NOT_EXIST_COUPON))
			.toDomain();
	}

	@Override
	public Coupon issue(Coupon coupon) {
		return couponJpaRepository.save(CouponEntity.from(coupon)).toDomain();
	}

	@Override
	public void updateAll(List<Coupon> coupons) {
		List<CouponEntity> couponEntities = coupons.stream().map(CouponEntity::update).toList();
		couponJpaRepository.saveAll(couponEntities);
	}

	@Override
	public List<Coupon> findAll() {
		return couponJpaRepository.findAll().stream().map(CouponEntity::toDomain).toList();
	}

	@Override
	public List<Coupon> findAllByIdIn(List<String> couponIds) {
		return couponJpaRepository.findAllByIdIn(couponIds).stream().map(CouponEntity::toDomain).toList();
	}
}
