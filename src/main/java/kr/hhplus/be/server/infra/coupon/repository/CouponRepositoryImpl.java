package kr.hhplus.be.server.infra.coupon.repository;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.infra.coupon.entity.CouponEntity;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {
	private final CouponJpaRepository couponJpaRepository;

	@Override
	public Optional<Coupon> findById(UUID id) {
		return Optional.of(couponJpaRepository.findById(id)
			.orElseThrow(()->new CustomException(ErrorCode.NOT_EXIST_COUPON))
			.toDomain()
		);
	}

	@Override
	public Coupon issue(Coupon coupon) {
		return couponJpaRepository.save(CouponEntity.from(coupon)).toDomain();
	}

	@Override
	public List<Coupon> validateCoupons(List<UUID> couponIds) {
		List<Coupon> coupons = couponIds.stream()
			.map(couponJpaRepository::findById)
			.flatMap(Optional::stream)
			.map(CouponEntity::toDomain)
			.toList();

		coupons.forEach(Coupon::isValid);
		return coupons;
	}
}
